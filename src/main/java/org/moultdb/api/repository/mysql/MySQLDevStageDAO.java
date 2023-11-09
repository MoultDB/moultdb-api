package org.moultdb.api.repository.mysql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.repository.dao.DevStageDAO;
import org.moultdb.api.repository.dto.DevStageTO;
import org.moultdb.api.repository.dto.TransfertObject;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Valentine Rech de Laval
 * @since 2023-11-09
 */
@Repository
public class MySQLDevStageDAO implements DevStageDAO {

    private final static Logger logger = LogManager.getLogger(MySQLDevStageDAO.class.getName());

    NamedParameterJdbcTemplate template;

    private static final String SELECT_STATEMENT = "SELECT * FROM developmental_stage ";

    public MySQLDevStageDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public List<DevStageTO> findAll() {
        return template.query(SELECT_STATEMENT, new MySQLDevStageDAO.DevStageRowMapper());
    }
    
    @Override
    public DevStageTO findById(String id) {
        String sql = SELECT_STATEMENT + "WHERE id = :dsId ";
        MapSqlParameterSource source = new MapSqlParameterSource().addValue("dsId", id);
        return TransfertObject.getOneTO(template.query(sql, source, new MySQLDevStageDAO.DevStageRowMapper()));
    }
    
    @Override
    public Integer getLastId() {
        String sql = "SELECT id FROM developmental_stage ORDER BY id DESC LIMIT 1";
        try {
            return template.queryForObject(sql, new MapSqlParameterSource(), Integer.class);
        } catch (EmptyResultDataAccessException e) {
            logger.debug("No record found in 'developmental_stage' table");
            return 0;
        }
    }
    
    private static class DevStageRowMapper implements RowMapper<DevStageTO> {
        @Override
        public DevStageTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new DevStageTO(rs.getString("id"), rs.getString("name"), rs.getString("description"),
                    rs.getInt("left_bound"), rs.getInt("right_bound"));
        }
    }
}
