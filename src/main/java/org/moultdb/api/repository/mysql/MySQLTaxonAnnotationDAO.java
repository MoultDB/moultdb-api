package org.moultdb.api.repository.mysql;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.repository.dao.TaxonAnnotationDAO;
import org.moultdb.api.repository.dto.AnatEntityTO;
import org.moultdb.api.repository.dto.ArticleTO;
import org.moultdb.api.repository.dto.ConditionTO;
import org.moultdb.api.repository.dto.DevStageTO;
import org.moultdb.api.repository.dto.ImageTO;
import org.moultdb.api.repository.dto.TaxonAnnotationTO;
import org.moultdb.api.repository.dto.TaxonTO;
import org.moultdb.api.repository.dto.TermTO;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2021-11-22
 */
@Repository
public class MySQLTaxonAnnotationDAO implements TaxonAnnotationDAO {
    
    private final static Logger logger = LogManager.getLogger(MySQLTaxonAnnotationDAO.class.getName());
    
    NamedParameterJdbcTemplate template;
    
    private static final String SELECT_STATEMENT = "SELECT ta.*, t.*, c.*, ds.*, ae.*, a.*, i.*, eco.*, cio.*, v.*, uc.*, um.* " +
            "FROM taxon_annotation ta " +
            "LEFT JOIN taxon t ON t.path = ta.taxon_path " +
            "LEFT JOIN cond c ON c.id = ta.condition_id " +
            "LEFT JOIN developmental_stage ds ON ds.id = c.dev_stage_id " +
            "LEFT JOIN anatomical_entity ae ON ae.id = c.dev_stage_id " +
            "LEFT JOIN article a ON a.id = ta.article_id " +
            "LEFT JOIN image i ON i.id = ta.image_id " +
            "LEFT JOIN eco ON eco.id = ta.eco_id " +
            "LEFT JOIN cio ON cio.id = ta.cio_id " +
            "LEFT JOIN version v ON ta.version_id = v.id " +
            "LEFT JOIN user uc ON v.creation_user_id = uc.id " +
            "LEFT JOIN user um ON v.last_update_user_id = um.id ";
    
    public MySQLTaxonAnnotationDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public List<TaxonAnnotationTO> findAll() {
        return template.query(SELECT_STATEMENT, new TaxonAnnotationRowMapper());
    }
    
    @Override
    public List<TaxonAnnotationTO> findByUser(String email) {
        return template.query(SELECT_STATEMENT + "WHERE uc.email = :email",
                new MapSqlParameterSource().addValue("email", email), new TaxonAnnotationRowMapper());
    }
    
    @Override
    public Integer getLastId() {
        String sql = "SELECT id FROM taxon_annotation ORDER BY id DESC LIMIT 1";
        try {
            return template.queryForObject(sql, new MapSqlParameterSource(), Integer.class);
        } catch (EmptyResultDataAccessException e) {
            logger.debug("No record found in 'taxon_annotation' table");
            return 0;
        }
    }
    @Override
    public int insert(TaxonAnnotationTO taxonAnnotationTO) {
        return 0;
    }
    
    @Override
    public int insertImageTaxonAnnotation(TaxonAnnotationTO taxonAnnotationTO) {
        String sql = "INSERT INTO taxon_annotation (taxon_path, annotated_species_name, " +
                "sample_set_id, condition_id, image_id, version_id) " +
                "VALUES (:taxonPath, :annotatedSpeciesName, " +
                ":sampleSetId, :conditionId, :imageId, :versionId)";
    
        List<MapSqlParameterSource> params = new ArrayList<>();
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("taxonPath", taxonAnnotationTO.getTaxonTO().getPath());
        source.addValue("annotatedSpeciesName", taxonAnnotationTO.getAnnotatedSpeciesName());
        source.addValue("sampleSetId", taxonAnnotationTO.getSampleSetId());
        source.addValue("conditionId", taxonAnnotationTO.getConditionTO().getId());
        source.addValue("imageId", taxonAnnotationTO.getImageTO().getId());
        source.addValue("versionId", taxonAnnotationTO.getVersionId());
        params.add(source);
    
        int[] ints = template.batchUpdate(sql, params.toArray(MapSqlParameterSource[]::new));
        logger.info(Arrays.stream(ints).sum() + " new row(s) in 'taxon_annotation' table.");
    
        return ints[0];
    }
    
    @Transactional
    @Override
    public int[] batchUpdate(Set<TaxonAnnotationTO> taxonAnnotationTOs) {
        throw new UnsupportedOperationException("Insertion of 'complete' taxon annotations not available");
    }
    
    
    private static class TaxonAnnotationRowMapper implements RowMapper<TaxonAnnotationTO> {
        @Override
        public TaxonAnnotationTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            
            // TODO: add dbXrefTOs
            TaxonTO taxonTO = new TaxonTO(rs.getString("t.path"), rs.getString("t.scientific_name"), rs.getString("t.common_name"),
                    rs.getString("t.parent_taxon_path"), rs.getString("t.taxon_rank"), rs.getBoolean("t.extinct"), null);
            
            DevStageTO devStageTO = null;
            if (StringUtils.isNotBlank(rs.getString("c.dev_stage_id"))) {
                devStageTO = new DevStageTO(rs.getString("ds.id"), rs.getString("ds.name"), rs.getString("ds.description"),
                        rs.getInt("ds.left_bound"), rs.getInt("ds.right_bound"));
            }
            AnatEntityTO anatEntityTO = null;
            if (StringUtils.isNotBlank(rs.getString("c.dev_stage_id"))) {
                anatEntityTO= new AnatEntityTO(rs.getString("ae.id"), rs.getString("ae.name"), rs.getString("ae.description"));
            }
            
            ConditionTO conditionTO = null;
            if (rs.getInt("ta.condition_id") > 1) {
                conditionTO = new ConditionTO(rs.getInt("c.id"), devStageTO, anatEntityTO,
                        rs.getString("c.sex"), rs.getString("c.moulting_step"));
            }
            
            ArticleTO articleTO = null;
            if (rs.getInt("ta.article_id") > 1) {
                // TODO: add dbXrefTOs
                articleTO = new ArticleTO(rs.getInt("a.id"), rs.getString("a.citation"),
                        rs.getString("a.title"), rs.getString("a.authors"), null);
            }
            
            ImageTO imageTO = null;
            if (rs.getInt("ta.image_id") > 1) {
                imageTO = new ImageTO(rs.getInt("i.id"), rs.getString("i.file_name"), rs.getString("i.description"));
            }
            
            TermTO ecoTO = null;
            if (StringUtils.isNotBlank(rs.getString("ta.eco_id"))) {
                ecoTO = new TermTO(rs.getString("ecoTO.id"), rs.getString("ecoTO.name"), rs.getString("ecoTO.description"));
            }
            
            TermTO cioTO = null;
            if (StringUtils.isNotBlank(rs.getString("ta.cio_id"))) {
                cioTO = new TermTO(rs.getString("cioTO.id"), rs.getString("cioTO.name"), rs.getString("cioTO.description"));
            }
            
            return new TaxonAnnotationTO(rs.getInt("ta.id"), taxonTO, rs.getString("ta.annotated_species_name"),
                    rs.getInt("ta.moulting_characters_id"), rs.getInt("ta.sample_set_id"), conditionTO, articleTO,
                    imageTO, ecoTO, cioTO, rs.getInt("ta.version_id"));
        }
    }
}
