package org.moultdb.api.repository.mysql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.repository.dao.AnatEntityDAO;
import org.moultdb.api.repository.dto.AnatEntityTO;
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
 * @since 2023-07-03
 */
@Repository
public class MySQLAnatEntityDAO implements AnatEntityDAO {

    private final static Logger logger = LogManager.getLogger(MySQLAnatEntityDAO.class.getName());

    NamedParameterJdbcTemplate template;

    private static final String SELECT_STATEMENT = "SELECT * FROM anatomical_entity ";

    public MySQLAnatEntityDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public List<AnatEntityTO> findAll() {
        return template.query(SELECT_STATEMENT, new MySQLAnatEntityDAO.AnatEntityRowMapper());
    }

    @Override
    public AnatEntityTO findById(String id) {
        String sql = SELECT_STATEMENT + "WHERE id = :aeId ";
        MapSqlParameterSource source = new MapSqlParameterSource().addValue("aeId", id);
        return TransfertObject.getOneTO(template.query(sql, source, new MySQLAnatEntityDAO.AnatEntityRowMapper()));
    }

    @Override
    public Integer getLastId() {
        String sql = "SELECT id FROM anatomical_entity ORDER BY id DESC LIMIT 1";
        try {
            return template.queryForObject(sql, new MapSqlParameterSource(), Integer.class);
        } catch (EmptyResultDataAccessException e) {
            logger.debug("No record found in 'cond' table");
            return 0;
        }
    }
    
    private static class AnatEntityRowMapper implements RowMapper<AnatEntityTO> {
        @Override
        public AnatEntityTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new AnatEntityTO(rs.getString("id"), rs.getString("name"), rs.getString("description"));
        }
    }
}
