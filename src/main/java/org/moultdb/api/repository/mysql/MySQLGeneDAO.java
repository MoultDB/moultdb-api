package org.moultdb.api.repository.mysql;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.exception.MoultDBException;
import org.moultdb.api.repository.dao.DAO;
import org.moultdb.api.repository.dao.GeneDAO;
import org.moultdb.api.repository.dto.*;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author Valentine Rech de Laval
 * @since 2024-03-22
 */
@Repository
public class MySQLGeneDAO implements GeneDAO {
    
    private final static Logger logger = LogManager.getLogger(MySQLGeneDAO.class.getName());
    
    NamedParameterJdbcTemplate template;
    
    private static final String SELECT_STATEMENT = "SELECT * FROM gene g " +
            "LEFT JOIN genome gm ON gm.genbank_acc = g.genome_acc " +
            "LEFT JOIN taxon t ON t.path = gm.taxon_path " +
            "LEFT JOIN data_source ds ON ds.id = g.data_source_id " +
            "LEFT JOIN pathway p ON p.id = g.pathway_id " +
            "LEFT JOIN orthogroup og ON og.id = g.orthogroup_id ";
    
    public MySQLGeneDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public List<GeneTO> findAll() {
        return template.query(SELECT_STATEMENT, new GeneResultSetExtractor());
    }
    
    @Override
    public GeneTO findById(String id) {
        return TransfertObject.getOneTO(findByIds(Collections.singleton(id)));
    }
    
    @Override
    public GeneTO findByGeneId(String geneId) {
        if (StringUtils.isBlank(geneId)) {
            throw new IllegalArgumentException("An ID can not be null");
        }
        return TransfertObject.getOneTO(template.query(SELECT_STATEMENT + "WHERE g.gene_id = :geneId",
                new MapSqlParameterSource().addValue("geneId", geneId), new GeneResultSetExtractor()));
    }
    
    @Override
    public GeneTO findByLocusTag(String locusTag) {
        if (StringUtils.isBlank(locusTag)) {
            throw new IllegalArgumentException("A locus tag can not be null");
        }
        return TransfertObject.getOneTO(template.query(SELECT_STATEMENT + "WHERE g.locus_tag = :locusTag",
                new MapSqlParameterSource().addValue("locusTag", locusTag), new GeneResultSetExtractor()));
    }
    
    @Override
    public List<GeneTO> findByIds(Set<String> ids) {
        if (ids == null || ids.isEmpty() || ids.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("An ID can not be null");
        }
        return template.query(SELECT_STATEMENT + "WHERE g.id IN (:ids)",
                new MapSqlParameterSource().addValue("ids", ids), new GeneResultSetExtractor());
    }
    
    @Override
    public List<GeneTO> findByPathwayId(String pathwayId) {
        return template.query(SELECT_STATEMENT + "WHERE LOWER(p.id) = LOWER(:pathwayId)",
                new MapSqlParameterSource().addValue("pathwayId", pathwayId), new GeneResultSetExtractor());
    }
    
    @Override
    public List<GeneTO> findByDomainId(String domainId) {
        return template.query(SELECT_STATEMENT +
                        "LEFT JOIN gene_domain gd ON gd.gene_id = g.id " +
                        "LEFT JOIN domain d ON d.id = gd.domain_id " +
                        "WHERE LOWER(d.id) = LOWER(:domainId)",
                new MapSqlParameterSource().addValue("domainId", domainId), new GeneResultSetExtractor());
    }
    
    @Override
    public List<GeneTO> findByOrthogroupId(Integer orthogroupId) {
        return template.query(SELECT_STATEMENT +
                        "WHERE og.id = (:orthogroupId)",
                new MapSqlParameterSource().addValue("orthogroupId", orthogroupId), new GeneResultSetExtractor());
    }
    
    @Override
    public GeneTO findByProteinId(String proteinId) {
        return TransfertObject.getOneTO(findByProteinIds(Collections.singleton(proteinId)));
    }
    
    @Override
    public List<GeneTO> findByProteinIds(Set<String> proteinIds) {
        if (proteinIds == null || proteinIds.isEmpty() || proteinIds.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("An protein ID can not be null");
        }
        return template.query(SELECT_STATEMENT + "WHERE g.protein_id IN (:protein_ids)",
                new MapSqlParameterSource().addValue("protein_ids", proteinIds), new GeneResultSetExtractor());
    }
    
    @Override
    public List<GeneTO> findByTaxon(String taxonPath, Boolean inAMoultingPathway) {
        String pathwayCondition;
        if (inAMoultingPathway == null) {
            pathwayCondition = "";
        } else if (inAMoultingPathway) {
            pathwayCondition = "AND g.pathway_id IS NOT NULL ";
        } else {
            pathwayCondition = "AND g.pathway_id IS NULL ";
        }
        
        return template.query(SELECT_STATEMENT + "WHERE t.path like :taxonPath " + pathwayCondition,
                new MapSqlParameterSource().addValue("taxonPath", taxonPath + "%"), new GeneResultSetExtractor());
    }
    
    @Override
    public Integer getLastId() {
        String sql = "SELECT id FROM gene ORDER BY id DESC LIMIT 1";
        try {
            return template.queryForObject(sql, new MapSqlParameterSource(), Integer.class);
        } catch (EmptyResultDataAccessException e) {
            logger.debug("No record found in 'gene' table");
            return 0;
        }
    }
    
    @Override
    public void insert(GeneTO geneTO) {
        batchUpdate(Collections.singleton(geneTO));
    }
    
    @Override
    public int batchUpdate(Set<GeneTO> geneTOs) {
        String geneSql = "INSERT INTO gene (id, gene_id, gene_name, locus_tag, genome_acc, " +
                "orthogroup_id, transcript_id, transcript_url_suffix, protein_id, protein_description, protein_length, " +
                "data_source_id, pathway_id) " +
                "VALUES (:id, :gene_id, :gene_name, :locus_tag, :genome_acc, :orthogroup_id, " +
                ":transcript_id, :transcript_url_suffix, :protein_id, :protein_description, :protein_length, " +
                ":data_source_id, :pathway_id) " +
                "AS new " +
                "ON DUPLICATE KEY UPDATE gene_id = new.gene_id, gene_name = new.gene_name, " +
                "locus_tag = new.locus_tag, genome_acc = new.genome_acc, orthogroup_id = new.orthogroup_id, " +
                "transcript_id = new.transcript_id, transcript_url_suffix = new.transcript_url_suffix, " +
                "protein_id = new.protein_id, protein_description = new.protein_description, " +
                "protein_length = new.protein_length, data_source_id = new.data_source_id, " +
                "pathway_id = new.pathway_id";
        
        List<MapSqlParameterSource> geneParams = new ArrayList<>();
        for (GeneTO geneTO : geneTOs) {
            MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue("id", geneTO.getId());
            source.addValue("gene_id", geneTO.getGeneId());
            source.addValue("gene_name", geneTO.getGeneName());
            source.addValue("locus_tag", geneTO.getLocusTag());
            source.addValue("genome_acc", geneTO.getGenomeAcc());
            source.addValue("orthogroup_id", geneTO.getOrthogroupTO() == null ? null : geneTO.getOrthogroupTO().getId());
            source.addValue("transcript_id", geneTO.getTranscriptId());
            source.addValue("transcript_url_suffix", geneTO.getTranscriptUrlSuffix());
            source.addValue("protein_id", geneTO.getProteinId());
            source.addValue("protein_description", geneTO.getProteinDescription());
            source.addValue("protein_length", geneTO.getProteinLength());
            source.addValue("data_source_id", geneTO.getDataSourceTO().getId());
            source.addValue("pathway_id", geneTO.getPathwayTO() == null ? null : geneTO.getPathwayTO().getId());
            geneParams.add(source);
        }
        
        int[] ints;
        try {
            ints = template.batchUpdate(geneSql, geneParams.toArray(MapSqlParameterSource[]::new));
            logger.debug("'gene' table updated");
        } catch (Exception e) {
            throw new MoultDBException("Insertion of gene(s) failed: " + e.getMessage());
        }
        return Arrays.stream(ints).sum();
    }
    
    @Override
    public void deleteByIds(Set<Integer> geneIds) {
        String sql = "DELETE FROM gene WHERE id IN (:ids)";
        template.update(sql, new MapSqlParameterSource().addValue("ids", geneIds));
    }
    
    private static class GeneResultSetExtractor implements ResultSetExtractor<List<GeneTO>> {
        
        @Override
        public List<GeneTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Integer, GeneTO> genes = new HashMap<>();
            while(rs.next()) {
                Integer id = rs.getInt("g.id");
                
                DataSourceTO dataSourceTO = new MySQLDataSourceDAO.DataSourceRowMapper().mapRow(rs, rs.getRow());
                
                PathwayTO pathwayTO = new MySQLPathwayDAO.PathwayRowMapper().mapRow(rs, rs.getRow());
                
                OrthogroupTO orthogroupTO = new MySQLOrthogroupDAO.OrthogroupRowMapper().mapRow(rs, rs.getRow());
                
                // Build GeneTO. Even if it already exists, we create a new one because it's an unmutable object
                genes.put(id, new GeneTO(id, rs.getString("g.gene_id"), rs.getString("g.gene_name"), rs.getString("g.locus_tag"),
                        rs.getString("g.genome_acc"), rs.getString("t.path"),
                        rs.getString("g.transcript_id"), rs.getString("g.transcript_url_suffix"),
                        rs.getString("g.protein_id"), rs.getString("g.protein_description"),
                        DAO.getInteger(rs, "g.protein_length"), dataSourceTO, pathwayTO, orthogroupTO));
            }
            return new ArrayList<>(genes.values());
        }
    }
}
