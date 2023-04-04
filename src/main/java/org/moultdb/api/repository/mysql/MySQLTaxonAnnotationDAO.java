package org.moultdb.api.repository.mysql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.repository.dao.TaxonAnnotationDAO;
import org.moultdb.api.repository.dto.AnatEntityTO;
import org.moultdb.api.repository.dto.ArticleTO;
import org.moultdb.api.repository.dto.ConditionTO;
import org.moultdb.api.repository.dto.DevStageTO;
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
    
    private static final String SELECT_STATEMENT = "SELECT ta.*, t.*, c.*, ds.*, ae.*, a.*, eco.*, cio.* " +
            "FROM taxon_annotation ta " +
            "INNER JOIN taxon t ON t.path = ta.taxon_path " +
            "INNER JOIN cond c ON c.id = ta.condition_id " +
            "INNER JOIN developmental_stage ds ON ds.id = c.dev_stage_id " +
            "INNER JOIN anatomical_entity ae ON ae.id = c.dev_stage_id " +
            "INNER JOIN article a ON a.id = ta.article_id " +
            "INNER JOIN eco ON eco.id = ta.eco_id " +
            "INNER JOIN cio ON cio.id = ta.cio_id ";
    
    public MySQLTaxonAnnotationDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public List<TaxonAnnotationTO> findAll() {
        return template.query(SELECT_STATEMENT, new TaxonAnnotationRowMapper());
    }
    
    @Override
    public List<TaxonAnnotationTO> findByTaxonId(int id) {
        return null;
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
    
    @Transactional
    @Override
    public int[] batchUpdate(Set<TaxonAnnotationTO> taxonAnnotationTOs) {
        return new int[0];
    }
    
    
    private static class TaxonAnnotationRowMapper implements RowMapper<TaxonAnnotationTO> {
        @Override
        public TaxonAnnotationTO mapRow(ResultSet rs, int rowNum) throws SQLException {

            // TODO: add dbXrefTOs?
            TaxonTO taxonTO = new TaxonTO(rs.getString("t.path"), rs.getString("t.scientific_name"), rs.getString("t.common_name"),
                    rs.getString("t.parent_taxon_path"), rs.getString("t.taxon_rank"), rs.getBoolean("t.extinct"), null);
    
            DevStageTO devStageTO = new DevStageTO(rs.getString("ds.id"), rs.getString("ds.name"), rs.getString("ds.description"),
                    rs.getInt("ds.left_bound"), rs.getInt("ds.right_bound"));
            AnatEntityTO anatEntityTO = new AnatEntityTO(rs.getString("ae.id"), rs.getString("ae.name"), rs.getString("ae.description"));
            ConditionTO conditionTO = new ConditionTO(rs.getInt("c.id"), devStageTO, anatEntityTO,
                    rs.getString("c.sex"), rs.getString("c.moulting_step"));
    
            // TODO: add dbXrefTOs?
            ArticleTO articleTO = new ArticleTO(rs.getInt("a.id"), rs.getString("a.citation"),
                    rs.getString("a.title"), rs.getString("a.authors"), null);
    
            TermTO eco = new TermTO(rs.getString("eco.id"), rs.getString("eco.name"), rs.getString("eco.description"));
            TermTO cio = new TermTO(rs.getString("cio.id"), rs.getString("cio.name"), rs.getString("cio.description"));
    
            return new TaxonAnnotationTO(rs.getInt("ta.id"), taxonTO, rs.getString("ta.annotated_species_name"),
                    rs.getInt("ta.moulting_characters_id"), rs.getInt("ta.sample_set_id"), conditionTO, articleTO,
                    eco, cio, rs.getInt("ta.version_id"));
        }
    }
}
