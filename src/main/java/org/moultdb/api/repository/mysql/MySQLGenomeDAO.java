package org.moultdb.api.repository.mysql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.exception.MoultDBException;
import org.moultdb.api.repository.dao.DAO;
import org.moultdb.api.repository.dao.GenomeDAO;
import org.moultdb.api.repository.dto.GenomeTO;
import org.moultdb.api.repository.dto.TaxonTO;
import org.moultdb.api.repository.dto.TransfertObject;
import org.springframework.jdbc.core.RowMapper;
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
            "LEFT JOIN taxon t on t.path = g.taxon_path ";
    
    public MySQLGenomeDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public List<GenomeTO> findAll() {
        return template.query(SELECT_STATEMENT, new MySQLGenomeDAO.GenomeRowMapper());
    }
    
    @Override
    public List<GenomeTO> findByTaxonPath(String taxonPath, boolean withSubspeciesGenomes) {
        String sql = SELECT_STATEMENT + "WHERE t.path = :taxonPath ";
        MapSqlParameterSource source = new MapSqlParameterSource().addValue("taxonPath", taxonPath);
        if (withSubspeciesGenomes) {
            sql = SELECT_STATEMENT + "WHERE t.path = :taxonPath OR t.path LIKE :childrenTaxonPath ";
            source.addValue("childrenTaxonPath", taxonPath + ".%");
        }
        return template.query(sql, source, new MySQLGenomeDAO.GenomeRowMapper());
    }
    
    @Override
    public GenomeTO findByGenbankAcc(String genbankAcc) {
        String sql = SELECT_STATEMENT + "WHERE g.genbank_acc = :genbank_acc ";
        MapSqlParameterSource source = new MapSqlParameterSource().addValue("genbank_acc", genbankAcc);
        return TransfertObject.getOneTO(template.query(sql, source, new MySQLGenomeDAO.GenomeRowMapper()));
    }
    
    @Override
    public int insert(GenomeTO genomeTO) {
        int[] ints = batchUpdate(Collections.singleton(genomeTO));
        return ints[0];
    }
    
    @Override
    public int[] batchUpdate(Set<GenomeTO> genomeTOs) {
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
        
        int[] genomeRowCounts = null ;
        try {
            genomeRowCounts = template.batchUpdate(genomeSql, genomeParams.toArray(MapSqlParameterSource[]::new));
            logger.debug(Arrays.stream(genomeRowCounts).sum() + " updated row(s) in 'genome' table.");
        } catch (Exception e) {
            throw new MoultDBException("Insertion of genome(s) failed: " + e.getMessage());
        }
        return genomeRowCounts;
    }
    
    private static class GenomeRowMapper implements RowMapper<GenomeTO> {
        @Override
        public GenomeTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            java.sql.Date submissionDate = rs.getDate("submission_date");
            LocalDate submissionLocalDate = submissionDate.toLocalDate();
            
            java.sql.Date annotationDate = rs.getDate("annotation_date");
            LocalDate annotationLocalDate = null;
            if (annotationDate != null) {
                annotationLocalDate = annotationDate.toLocalDate();
            }
            // TODO: add dbXrefTOs
            TaxonTO taxonTO = new TaxonTO(rs.getString("t.path"), rs.getString("t.scientific_name"), rs.getString("t.common_name"),
                    rs.getString("t.parent_taxon_path"), DAO.getBoolean(rs, "t.extinct"), null);
            
            return new GenomeTO(rs.getString("genbank_acc"), taxonTO, submissionLocalDate,
                    rs.getLong("length"), rs.getInt("scaffolds"), rs.getInt("scaffold_l50"), rs.getInt("scaffold_n50"),
                    annotationLocalDate, DAO.getInteger(rs, "total_genes"), DAO.getFloat(rs, "complete_busco"),
                    DAO.getFloat(rs, "single_busco"), DAO.getFloat(rs, "duplicated_busco"),
                    DAO.getFloat(rs, "fragmented_busco"), DAO.getFloat(rs, "missing_busco"));
        }
    }
}