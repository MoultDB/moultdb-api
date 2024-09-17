package org.moultdb.api.repository.mysql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.repository.dao.ECOTermDAO;
import org.moultdb.api.repository.dto.ECOTermTO;
import org.moultdb.api.repository.dto.TransfertObject;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Valentine Rech de Laval
 * @since 2024-09-17
 */
@Repository
public class MySQLECOTermDAO implements ECOTermDAO {
    
    private final static Logger logger = LogManager.getLogger(MySQLECOTermDAO.class.getName());
    
    NamedParameterJdbcTemplate template;
    
    private static final String SELECT_STATEMENT = "SELECT * FROM eco ";
    
    public MySQLECOTermDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public List<ECOTermTO> findAll() {
        return template.query(SELECT_STATEMENT, new ECOTermRowMapper());
    }
    
    @Override
    public ECOTermTO findById(String id) {
        String sql = SELECT_STATEMENT + "WHERE id = :ecoId ";
        MapSqlParameterSource source = new MapSqlParameterSource().addValue("ecoId", id);
        return TransfertObject.getOneTO(template.query(sql, source, new ECOTermRowMapper()));
    }
    
    private static class ECOTermRowMapper implements RowMapper<ECOTermTO> {
        @Override
        public ECOTermTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ECOTermTO(rs.getString("id"), rs.getString("name"), rs.getString("description"));
        }
    }
}
