package org.moultdb.api.repository.mysql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.repository.dao.CIOStatementDAO;
import org.moultdb.api.repository.dto.CIOStatementTO;
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
public class MySQLCIOStatementDAO implements CIOStatementDAO {
    
    private final static Logger logger = LogManager.getLogger(MySQLCIOStatementDAO.class.getName());
    
    NamedParameterJdbcTemplate template;
    
    private static final String SELECT_STATEMENT = "SELECT * FROM cio ";
    
    public MySQLCIOStatementDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public List<CIOStatementTO> findAll() {
        return template.query(SELECT_STATEMENT, new CIOStatementRowMapper());
    }
    
    @Override
    public CIOStatementTO findById(String id) {
        String sql = SELECT_STATEMENT + "WHERE id = :cioId ";
        MapSqlParameterSource source = new MapSqlParameterSource().addValue("cioId", id);
        return TransfertObject.getOneTO(template.query(sql, source, new CIOStatementRowMapper()));
    }
    
    private static class CIOStatementRowMapper implements RowMapper<CIOStatementTO> {
        @Override
        public CIOStatementTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new CIOStatementTO(rs.getString("id"), rs.getString("name"), rs.getString("description"));
        }
    }
}
