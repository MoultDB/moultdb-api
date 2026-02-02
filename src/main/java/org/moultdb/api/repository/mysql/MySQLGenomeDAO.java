package org.moultdb.api.repository.mysql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.exception.MoultDBException;
import org.moultdb.api.repository.dao.DAO;
import org.moultdb.api.repository.dao.GenomeDAO;
import org.moultdb.api.repository.dto.*;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

/**
 * @author Valentine Rech de Laval
 * @since 2023-11-21
 */
@Repository
public class MySQLGenomeDAO implements GenomeDAO {
    
    private final static Logger logger = LogManager.getLogger(MySQLGenomeDAO.class.getName());
    
    NamedParameterJdbcTemplate template;
    
    private static final String SELECT_STATEMENT = "SELECT * FROM genome g " +
            "LEFT JOIN taxon t on t.path = g.taxon_path " +
            "LEFT JOIN taxon_db_xref tx ON t.path = tx.taxon_path " +
            "LEFT JOIN db_xref x ON tx.db_xref_id = x.id " +
            "LEFT JOIN data_source ds ON (x.data_source_id = ds.id) ";
    
    public MySQLGenomeDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public List<GenomeTO> findAll() {
        return template.query(SELECT_STATEMENT, new GenomeResultSetExtractor());
    }
    
    @Override
    public List<GenomeTO> findByTaxonPath(String taxonPath, boolean withSubspeciesGenomes) {
        String sql = SELECT_STATEMENT + "WHERE t.path = :taxonPath ";
        MapSqlParameterSource source = new MapSqlParameterSource().addValue("taxonPath", taxonPath);
        if (withSubspeciesGenomes) {
            sql = SELECT_STATEMENT + "WHERE t.path = :taxonPath OR t.path LIKE :childrenTaxonPath ";
            source.addValue("childrenTaxonPath", taxonPath + ".%");
        }
        return template.query(sql, source, new GenomeResultSetExtractor());
    }
    
    @Override
    public GenomeTO findByGenbankAcc(String genbankAcc) {
        return TransfertObject.getOneTO(findByGenbankAccs(Collections.singleton(genbankAcc)));
    }
    
    @Override
    public List<GenomeTO> findByGenbankAccs(Set<String> genomeAccs) {
        if (genomeAccs == null || genomeAccs.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("A genome accession can not be null");
        }
        return template.query(SELECT_STATEMENT + "WHERE g.genbank_acc IN (:accs)",
                new MapSqlParameterSource().addValue("accs", genomeAccs), new GenomeResultSetExtractor());
    }
    
    @Override
    public void insert(GenomeTO genomeTO) {
        batchUpdate(Collections.singleton(genomeTO));
    }
    
    @Override
    public void batchUpdate(Set<GenomeTO> genomeTOs) {
        String genomeSql = "INSERT INTO genome (genbank_acc, taxon_path, submission_date, length, scaffolds, " +
                "scaffold_l50, scaffold_n50, annotation_date, total_genes, complete_busco, single_busco, " +
                "duplicated_busco, fragmented_busco, missing_busco) " +
                "VALUES (:genbank_acc, :taxon_path, :submission_date, :length, :scaffolds, :scaffold_l50, :scaffold_n50, " +
                ":annotation_date, :total_genes, :complete_busco, :single_busco, :duplicated_busco, :fragmented_busco, " +
                ":missing_busco) " +
                "AS new " +
                "ON DUPLICATE KEY UPDATE taxon_path = new.taxon_path, submission_date = new.submission_date, " +
                "length = new.length, scaffolds = new.scaffolds, scaffold_l50 = new.scaffold_l50, " +
                "scaffold_n50 = new.scaffold_n50, annotation_date = new.annotation_date, total_genes = new.total_genes, " +
                "complete_busco = new.complete_busco, single_busco = new.single_busco, duplicated_busco = new.duplicated_busco, " +
                "fragmented_busco = new.fragmented_busco, missing_busco = new.missing_busco";
        
        List<MapSqlParameterSource> genomeParams = new ArrayList<>();
        for (GenomeTO genomeTO : genomeTOs) {
            MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue("genbank_acc", genomeTO.getGeneBankAcc());
            source.addValue("taxon_path", genomeTO.getTaxonTO().getPath());
            source.addValue("submission_date", genomeTO.getSubmissionDate());
            source.addValue("length", genomeTO.getLength());
            source.addValue("scaffolds", genomeTO.getScaffolds());
            source.addValue("scaffold_l50", genomeTO.getScaffoldL50());
            source.addValue("scaffold_n50", genomeTO.getScaffoldN50());
            source.addValue("annotation_date", genomeTO.getAnnotationDate());
            source.addValue("total_genes", genomeTO.getTotalGenes());
            source.addValue("complete_busco", genomeTO.getCompleteBusco());
            source.addValue("single_busco", genomeTO.getSingleBusco());
            source.addValue("duplicated_busco", genomeTO.getDuplicatedBusco());
            source.addValue("fragmented_busco", genomeTO.getFragmentedBusco());
            source.addValue("missing_busco", genomeTO.getMissingBusco());
            genomeParams.add(source);
        }
        
        try {
            template.batchUpdate(genomeSql, genomeParams.toArray(MapSqlParameterSource[]::new));
            logger.debug("'genome' table updated");
        } catch (Exception e) {
            throw new MoultDBException("Insertion of genome(s) failed: " + e.getMessage());
        }
    }
    
    @Override
    public void deleteByIds(Set<String> genomeAccs) {
        String sql = "DELETE FROM genome WHERE genbank_acc IN (:accs)";
        template.update(sql, new MapSqlParameterSource().addValue("accs", genomeAccs));
    }
    
    private static class GenomeResultSetExtractor implements ResultSetExtractor<List<GenomeTO>> {
        
        @Override
        public List<GenomeTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<String, GenomeTO> genomes = new HashMap<>();
            while(rs.next()) {
                String genbankAcc = rs.getString("genbank_acc");
                GenomeTO genomeTO = genomes.get(genbankAcc);
                
                TaxonTO taxonTO = null;
                if (genomeTO != null) {
                    taxonTO = genomeTO.getTaxonTO();
                }
                
                // Build DbXrefs
                Set<DbXrefTO> dbXrefTOs = taxonTO == null? new HashSet<>() : new HashSet<>(taxonTO.getDbXrefTOs());
                DbXrefTO dbXrefTO = new MySQLDbXrefDAO.DbXrefRowMapper().mapRow(rs, rs.getRow());
                if (dbXrefTO != null) {
                    dbXrefTOs.add(dbXrefTO);
                }
                
                TaxonToDbXrefTO taxonToDbXrefTO = new TaxonToDbXrefTO(rs.getString("tx.taxon_path"),
                        rs.getInt("tx.db_xref_id"), rs.getBoolean("tx.main"));
                Set<TaxonToDbXrefTO> taxonToDbXrefTOs = taxonTO == null ? new HashSet<>(): new HashSet<>(taxonTO.getTaxonToDbXrefTOs());
                taxonToDbXrefTOs.add(taxonToDbXrefTO);
                
                // Build TaxonTO. Even if it already exists, we create a new one because it's an unmutable object
                taxonTO = new TaxonTO(rs.getString("t.path"), rs.getString("t.scientific_name"), rs.getString("t.common_name"),
                        rs.getString("t.taxon_rank"), dbXrefTOs, taxonToDbXrefTOs);
                
                java.sql.Date submissionDate = rs.getDate("submission_date");
                LocalDate submissionLocalDate = submissionDate.toLocalDate();
                
                java.sql.Date annotationDate = rs.getDate("annotation_date");
                LocalDate annotationLocalDate = null;
                if (annotationDate != null) {
                    annotationLocalDate = annotationDate.toLocalDate();
                }
                
                genomes.put(genbankAcc, new GenomeTO(rs.getString("genbank_acc"), taxonTO, submissionLocalDate,
                        rs.getLong("length"), rs.getInt("scaffolds"), rs.getInt("scaffold_l50"), rs.getInt("scaffold_n50"),
                        annotationLocalDate, DAO.getInteger(rs, "total_genes"), DAO.getFloat(rs, "complete_busco"),
                        DAO.getFloat(rs, "single_busco"), DAO.getFloat(rs, "duplicated_busco"),
                        DAO.getFloat(rs, "fragmented_busco"), DAO.getFloat(rs, "missing_busco")));
            }
            return new ArrayList<>(genomes.values());
        }
    }
}
