package org.moultdb.api.repository.mysql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.repository.dao.ConditionDAO;
import org.moultdb.api.repository.dto.AnatEntityTO;
import org.moultdb.api.repository.dto.ConditionTO;
import org.moultdb.api.repository.dto.DevStageTO;
import org.moultdb.api.repository.dto.TransfertObject;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2022-06-22
 */
@Repository
public class MySQLConditionDAO implements ConditionDAO {
    
    private final static Logger logger = LogManager.getLogger(MySQLConditionDAO.class.getName());
    
    NamedParameterJdbcTemplate template;
    
    private static final String SELECT_STATEMENT = "SELECT c.*, ds.*, ae.* FROM cond c " +
            "LEFT JOIN developmental_stage ds ON ds.id = c.dev_stage_id " +
            "LEFT JOIN anatomical_entity ae ON ae.id = c.anatomical_entity_id ";
    
    public MySQLConditionDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public List<ConditionTO> findAll() {
        return template.query(SELECT_STATEMENT, new MySQLConditionDAO.ConditionRowMapper());
    }
    
    @Override
    public List<ConditionTO> find(String devStageId, String anatEntityId) {
        String sql = SELECT_STATEMENT + "WHERE ds.id = :dsId AND ae.id =:aeId ";
        MapSqlParameterSource source = new MapSqlParameterSource().addValue("dsId", devStageId)
                                                                  .addValue("aeId", anatEntityId);
        return template.query(sql, source, new MySQLConditionDAO.ConditionRowMapper());
    }
    
    @Override
    public ConditionTO find(String devStageId, String anatEntityId, String sex, String reproductiveState) {
        String sql = SELECT_STATEMENT + "WHERE ds.id = :dsId AND ae.id =:aeId AND sex = :sex AND reproductive_state = :rState ";
        MapSqlParameterSource source = new MapSqlParameterSource()
                .addValue("dsId", devStageId)
                .addValue("aeId", anatEntityId)
                .addValue("sex", sex)
                .addValue("rState", reproductiveState);
        return TransfertObject.getOneTO(template.query(sql, source, new MySQLConditionDAO.ConditionRowMapper()));
    }
    
    @Override
    public List<ConditionTO> find(Integer ageInDays, String sex, String moultingStep) {
        String sql = SELECT_STATEMENT + "WHERE c.dev_stage_days = :ageInDays AND c.sex = :sex AND c.moulting_step = :ms ";
        MapSqlParameterSource source = new MapSqlParameterSource().addValue("ageInDays", ageInDays)
                                                                  .addValue("sex", sex)
                                                                  .addValue("ms", moultingStep);
        return template.query(sql, source, new MySQLConditionDAO.ConditionRowMapper());
    }
    
    @Override
    public void insert(ConditionTO conditionTO) {
        batchUpdate(Collections.singleton(conditionTO));
    }
    
    @Override
    public void batchUpdate(Set<ConditionTO> conditionTOs) {
        String insertStmt = "INSERT INTO cond (id, dev_stage_id, dev_stage_days, anatomical_entity_id, sex, moulting_step) " +
                "VALUES (:id, :dev_stage_id, :dev_stage_days, :anatomical_entity_id, :sex, :moulting_step) ";
        List<MapSqlParameterSource> params = new ArrayList<>();
        for (ConditionTO conditionTO : conditionTOs) {
            MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue("id", conditionTO.getId());
            source.addValue("dev_stage_id", conditionTO.getDevStageTO() == null? null : conditionTO.getDevStageTO().getId());
            source.addValue("dev_stage_days", conditionTO.getAgeInDays());
            source.addValue("anatomical_entity_id", conditionTO.getAnatEntityTO() == null? null : conditionTO.getAnatEntityTO().getId());
            source.addValue("sex", conditionTO.getSex());
            source.addValue("moulting_step", conditionTO.getMoultingStep());
            params.add(source);
        }

        template.batchUpdate(insertStmt, params.toArray(MapSqlParameterSource[]::new));
        logger.info("'cond' table updated");
    }
    
    @Override
    public Integer getLastId() {
        String sql = "SELECT id FROM cond ORDER BY id DESC LIMIT 1";
        try {
            return template.queryForObject(sql, new MapSqlParameterSource(), Integer.class);
        } catch (EmptyResultDataAccessException e) {
            logger.debug("No record found in 'cond' table");
            return 0;
        }
    }
    
    private static class ConditionRowMapper implements RowMapper<ConditionTO> {
        @Override
        public ConditionTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ConditionTO(rs.getInt("c.id"),
                    new DevStageTO(rs.getString("ds.id"), rs.getString("ds.name"), rs.getString("ds.description"),
                            rs.getString("ds.taxon_path"), rs.getInt("ds.left_bound"), rs.getInt("ds.right_bound")),
                    new AnatEntityTO(rs.getString("ae.id"), rs.getString("ae.name"), rs.getString("ae.description")),
                    rs.getString("c.sex"), rs.getString("c.reproductive_state"), rs.getString("c.moulting_step"));
        }
    }
}
