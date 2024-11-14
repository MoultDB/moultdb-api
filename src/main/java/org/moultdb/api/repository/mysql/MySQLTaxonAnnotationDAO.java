package org.moultdb.api.repository.mysql;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.repository.dao.DAO;
import org.moultdb.api.repository.dao.TaxonAnnotationDAO;
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
 * @since 2021-11-22
 */
@Repository
public class MySQLTaxonAnnotationDAO implements TaxonAnnotationDAO {
    
    private final static Logger logger = LogManager.getLogger(MySQLTaxonAnnotationDAO.class.getName());
    
    NamedParameterJdbcTemplate template;
    
    private static final String SELECT_STATEMENT = "SELECT ta.*, t.*, tdx.*, dx1.*, ds1.*, c.*, ds.*, ae.*, a.*, dx2.*, ds2.*, " +
            " i.*, eco.*, cio.*, v.*, uc.*, um.* " +
            "FROM taxon_annotation ta " +
            "LEFT JOIN taxon t ON t.path = ta.taxon_path " +
            "LEFT JOIN taxon_db_xref tdx ON t.path = tdx.taxon_path " +
            "LEFT JOIN db_xref dx1 ON tdx.db_xref_id = dx1.id " +
            "LEFT JOIN data_source ds1 ON dx1.data_source_id = ds1.id " +
            "LEFT JOIN cond c ON c.id = ta.condition_id " +
            "LEFT JOIN developmental_stage ds ON ds.id = c.dev_stage_id " +
            "LEFT JOIN anatomical_entity ae ON ae.id = c.anatomical_entity_id " +
            "LEFT JOIN article a ON a.id = ta.article_id " +
            "LEFT JOIN article_db_xref adx ON a.id = adx.article_id " +
            "LEFT JOIN db_xref dx2 ON adx.db_xref_id = dx2.id " +
            "LEFT JOIN data_source ds2 ON dx2.data_source_id = ds2.id " +
            "LEFT JOIN image i ON i.id = ta.image_id " +
            "LEFT JOIN eco ON eco.id = ta.eco_id " +
            "LEFT JOIN cio ON cio.id = ta.cio_id " +
            "LEFT JOIN version v ON ta.version_id = v.id " +
            "LEFT JOIN user uc ON v.creation_user_id = uc.id " +
            "LEFT JOIN user um ON v.last_update_user_id = um.id ";
    
    private static final String ORDER_BY_STATEMENT = "ORDER BY v.creation_date DESC";
    
    public MySQLTaxonAnnotationDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public List<TaxonAnnotationTO> findAll() {
        return template.query(SELECT_STATEMENT + ORDER_BY_STATEMENT, new TaxonAnnotationResultSetExtractor());
    }
    
    @Override
    public List<TaxonAnnotationTO> findLastCreated(int limit) {
        return template.query(SELECT_STATEMENT + " ORDER BY v.creation_date DESC LIMIT " + limit, new TaxonAnnotationResultSetExtractor());
    }
    
    @Override
    public List<TaxonAnnotationTO> findLastUpdated(int limit) {
        return template.query(SELECT_STATEMENT + " ORDER BY v.last_update_date DESC LIMIT " + limit, new TaxonAnnotationResultSetExtractor());
    }
    
    @Override
    public List<TaxonAnnotationTO> findByUser(String email, Integer limit) {
        String limitSql = limit != null ? " LIMIT " + limit : "";
        return template.query(SELECT_STATEMENT + "WHERE uc.email = :email ORDER BY v.creation_date DESC " + limitSql,
                new MapSqlParameterSource().addValue("email", email), new TaxonAnnotationResultSetExtractor());
    }
    
    @Override
    public List<TaxonAnnotationTO> findByTaxonPath(String taxonPath) {
        return template.query(SELECT_STATEMENT + "WHERE t.path like :taxonPath " + ORDER_BY_STATEMENT,
                new MapSqlParameterSource().addValue("taxonPath", taxonPath + "%"), new TaxonAnnotationResultSetExtractor());
    }
    
    @Override
    public TaxonAnnotationTO findByImageFilename(String imageFilename) {
        String sql = SELECT_STATEMENT + "WHERE i.file_name LIKE :imageFilename ";
        return TransfertObject.getOneTO(template.query(sql,
                new MapSqlParameterSource().addValue("imageFilename", imageFilename + "%"),
                new TaxonAnnotationResultSetExtractor()));
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
    public int insertImageTaxonAnnotation(TaxonAnnotationTO taxonAnnotationTO) {
        if (StringUtils.isNotBlank(taxonAnnotationTO.getAuthorAnatEntity()) ||
                StringUtils.isNotBlank(taxonAnnotationTO.getAuthorDevStage())) {
            throw new UnsupportedOperationException("New fields not supported");
        }
        String sql = "INSERT INTO taxon_annotation (taxon_path, author_species_name, " +
                "sample_set_id, condition_id, image_id, version_id) " +
                "VALUES (:taxonPath, :authorSpeciesName, " +
                ":sampleSetId, :conditionId, :imageId, :versionId)";
    
        List<MapSqlParameterSource> params = new ArrayList<>();
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("taxonPath", taxonAnnotationTO.getTaxonTO().getPath());
        source.addValue("authorSpeciesName", taxonAnnotationTO.getAuthorSpeciesName());
        source.addValue("sampleSetId", taxonAnnotationTO.getSampleSetId());
        source.addValue("conditionId", taxonAnnotationTO.getConditionTO().getId());
        source.addValue("imageId", taxonAnnotationTO.getImageTO().getId());
        source.addValue("versionId", taxonAnnotationTO.getVersionId());
        params.add(source);
    
        int[] ints = template.batchUpdate(sql, params.toArray(MapSqlParameterSource[]::new));
        logger.info(Arrays.stream(ints).sum() + " updated row(s) in 'taxon_annotation' table");
    
        return ints[0];
    }
    
    @Override
    public int[] batchUpdate(Set<TaxonAnnotationTO> taxonAnnotationTOs) {
        String sql = "INSERT INTO taxon_annotation (taxon_path, author_species_name, determined_by,  " +
                "sample_set_id, condition_id, author_dev_stage, author_anat_entity, article_id, " +
                "moulting_characters_id, eco_id, cio_id, version_id) " +
                "VALUES (:taxonPath, :authorSpeciesName, :determinedBy, :sampleSetId, " +
                ":conditionId, :authorDevStage, :authorAnatEntity, :articleId, :moultingCharactersId, " +
                ":ecoId, :cioId, :versionId)";
        
        List<MapSqlParameterSource> params = new ArrayList<>();
        for (TaxonAnnotationTO taxonAnnotationTO : taxonAnnotationTOs) {
            MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue("taxonPath", taxonAnnotationTO.getTaxonTO().getPath());
            source.addValue("authorSpeciesName", taxonAnnotationTO.getAuthorSpeciesName());
            source.addValue("determinedBy", taxonAnnotationTO.getDeterminedBy());
            source.addValue("sampleSetId", taxonAnnotationTO.getSampleSetId());
            source.addValue("conditionId", taxonAnnotationTO.getConditionTO().getId());
            source.addValue("authorDevStage", taxonAnnotationTO.getAuthorDevStage());
            source.addValue("authorAnatEntity", taxonAnnotationTO.getAuthorAnatEntity());
            source.addValue("articleId", taxonAnnotationTO.getArticleTO().getId());
            source.addValue("moultingCharactersId", taxonAnnotationTO.getMoultingCharactersId());
            source.addValue("ecoId", taxonAnnotationTO.getEcoTermTO() == null? null: taxonAnnotationTO.getEcoTermTO().getId());
            source.addValue("cioId", taxonAnnotationTO.getCioStatementTO() == null? null: taxonAnnotationTO.getCioStatementTO().getId());
            source.addValue("versionId", taxonAnnotationTO.getVersionId());
            params.add(source);
        }
        
        int[] ints = template.batchUpdate(sql, params.toArray(MapSqlParameterSource[]::new));
        logger.debug(Arrays.stream(ints).sum() + " updated row(s) in 'taxon_annotation' table");
        
        return ints;
    }
    
    @Override
    public void deleteByImageFilename(String imageFilename) {
        String sql = "DELETE ta, i " +
                "FROM taxon_annotation ta " +
                "LEFT JOIN image i ON i.id = ta.image_id " +
                "WHERE i.file_name LIKE :imageFilename ";
        template.update(sql, new MapSqlParameterSource().addValue("imageFilename", imageFilename + "%"));
    }
    
    private static class TaxonAnnotationResultSetExtractor implements ResultSetExtractor<List<TaxonAnnotationTO>> {
        
        @Override
        public List<TaxonAnnotationTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Integer, TaxonAnnotationTO> taxonAnnotationTOs = new HashMap<>();
            while(rs.next()) {
                int id = rs.getInt("ta.id");
                TaxonAnnotationTO taxonAnnotationTO = taxonAnnotationTOs.get(id);
                
                TaxonTO taxonTO = null;
                if (taxonAnnotationTO != null) {
                    taxonTO = taxonAnnotationTO.getTaxonTO();
                }
                
                // Build DbXrefs
                // FIXME do some refactoring
                DbXrefTO dbXrefTO = new MySQLDbXrefDAO.DbXrefRowMapper().mapRow(rs, rs.getRow(), "dx1", "ds1");
                Set<DbXrefTO> dbXrefTOs = taxonTO == null? null : new HashSet<>(taxonTO.getDbXrefTOs());
                if (dbXrefTOs == null) {
                    dbXrefTOs = new HashSet<>();
                }
                dbXrefTOs.add(dbXrefTO);
                
                TaxonToDbXrefTO taxonToDbXrefTO = new TaxonToDbXrefTO(rs.getString("tdx.taxon_path"),
                        rs.getInt("tdx.db_xref_id"), rs.getBoolean("tdx.main"));
                Set<TaxonToDbXrefTO> taxonToDbXrefTOs = taxonTO == null ? null: new HashSet<>(taxonTO.getTaxonToDbXrefTOs());
                if (taxonToDbXrefTOs == null) {
                    taxonToDbXrefTOs = new HashSet<>();
                }
                taxonToDbXrefTOs.add(taxonToDbXrefTO);
                
                // Build TaxonTO. Even if it already exists, we create a new one because it's an unmutable object
                taxonTO = new TaxonTO(rs.getString("t.path"), rs.getString("t.scientific_name"), rs.getString("t.common_name"),
                        DAO.getBoolean(rs, "t.extinct"), dbXrefTOs, taxonToDbXrefTOs);
                
                DevStageTO devStageTO = null;
                if (StringUtils.isNotBlank(rs.getString("c.dev_stage_id"))) {
                    devStageTO = new DevStageTO(rs.getString("ds.id"), rs.getString("ds.name"), rs.getString("ds.description"),
                            rs.getString("ds.taxon_path"), DAO.getInteger(rs, "ds.left_bound"), DAO.getInteger(rs, "ds.right_bound"));
                }
                AnatEntityTO anatEntityTO = null;
                if (StringUtils.isNotBlank(rs.getString("c.anatomical_entity_id"))) {
                    anatEntityTO= new AnatEntityTO(rs.getString("ae.id"), rs.getString("ae.name"), rs.getString("ae.description"));
                }
                
                ConditionTO conditionTO = null;
                if (DAO.getInteger(rs, "ta.condition_id") != null) {
                    conditionTO = new ConditionTO(rs.getInt("c.id"), devStageTO, anatEntityTO,
                            rs.getString("c.sex"), rs.getString("c.reproductive_state"), rs.getString("c.moulting_step"));
                }
                
                ArticleTO articleTO = null;
                if (DAO.getInteger(rs, "ta.article_id") != null) {
                    if (taxonAnnotationTO != null) {
                        articleTO = taxonAnnotationTO.getArticleTO();
                    }
                    // Build DbXrefs
                    DbXrefTO articleDbXrefTO = new MySQLDbXrefDAO.DbXrefRowMapper().mapRow(rs, rs.getRow(), "dx2", "ds2");
                    Set<DbXrefTO> articleDbXrefTOs = articleTO == null? new HashSet<>() : new HashSet<>(articleTO.getDbXrefTOs());
                    if (articleDbXrefTO != null && articleDbXrefTO.getAccession() != null) {
                        articleDbXrefTOs.add(articleDbXrefTO);
                    }
                    articleTO = new ArticleTO(rs.getInt("a.id"), rs.getString("a.citation"),
                            rs.getString("a.title"), rs.getString("a.authors"), articleDbXrefTOs);
                }
                
                ImageTO imageTO = null;
                if (DAO.getInteger(rs, "ta.image_id") != null) {
                    imageTO = new ImageTO(rs.getInt("i.id"), rs.getString("i.file_name"), rs.getString("i.description"));
                }
                
                ECOTermTO ecoTO = null;
                if (StringUtils.isNotBlank(rs.getString("ta.eco_id"))) {
                    ecoTO = new ECOTermTO(rs.getString("eco.id"), rs.getString("eco.name"), rs.getString("eco.description"));
                }
                
                CIOStatementTO cioTO = null;
                if (StringUtils.isNotBlank(rs.getString("ta.cio_id"))) {
                    cioTO = new CIOStatementTO(rs.getString("cio.id"), rs.getString("cio.name"), rs.getString("cio.description"));
                }
                
                taxonAnnotationTOs.put(id, new TaxonAnnotationTO(rs.getInt("ta.id"), taxonTO, rs.getString("ta.author_species_name"),
                        rs.getString("ta.determined_by"), rs.getInt("ta.sample_set_id"), rs.getString("ta.specimen_count"),
                        conditionTO, rs.getString("ta.author_dev_stage"), rs.getString("ta.author_anat_entity"),
                        articleTO, imageTO, DAO.getInteger(rs, "ta.moulting_characters_id"),
                        ecoTO, cioTO, rs.getInt("ta.version_id")));
            }
            return new ArrayList<>(taxonAnnotationTOs.values());
        }
    }
}
