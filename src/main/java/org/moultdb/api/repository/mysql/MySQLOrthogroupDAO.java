package org.moultdb.api.repository.mysql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.repository.dao.OrthogroupDAO;
import org.moultdb.api.repository.dto.OrthogroupTO;
import org.moultdb.api.repository.dto.TransfertObject;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2024-05-22
 */
@Repository
public class MySQLOrthogroupDAO implements OrthogroupDAO {
    
    private final static Logger logger = LogManager.getLogger(MySQLOrthogroupDAO.class.getName());
    
    NamedParameterJdbcTemplate template;
    
    private static final String SELECT_STATEMENT = "SELECT * FROM orthogroup og ";
    
    public MySQLOrthogroupDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public List<OrthogroupTO> findAll() {
        return template.query(SELECT_STATEMENT, new OrthogroupRowMapper());
    }
    
    @Override
    public OrthogroupTO findById(String id) {
        return TransfertObject.getOneTO(template.query(SELECT_STATEMENT + "WHERE LOWER(og.id) = LOWER(:id)",
                new MapSqlParameterSource().addValue("id", id), new OrthogroupRowMapper()));
    }
    
    @Override
    public List<OrthogroupTO> findByIds(Set<Integer> ids) {
        if (ids == null || ids.isEmpty() || ids.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("An ID can not be null");
        }
        return template.query(SELECT_STATEMENT + "WHERE og.id IN (:ids)",
                new MapSqlParameterSource().addValue("ids", ids), new OrthogroupRowMapper());
    }
    
    @Override
    public void batchUpdate(Set<OrthogroupTO> orthogroupTOs) {
        String insertStmt = "INSERT INTO orthogroup (id, name) " +
                "VALUES (:id, :name) " +
                "AS new " +
                "ON DUPLICATE KEY UPDATE name = new.name ";
        List<MapSqlParameterSource> params = new ArrayList<>();
        for (OrthogroupTO orthogroupTO : orthogroupTOs) {
            MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue("id", orthogroupTO.getId());
            source.addValue("name", orthogroupTO.getName());
            params.add(source);
        }
        template.batchUpdate(insertStmt, params.toArray(MapSqlParameterSource[]::new));
        logger.debug("'orthogroup' table updated");
        
    }
    
    protected static class OrthogroupRowMapper implements RowMapper<OrthogroupTO> {
        @Override
        public OrthogroupTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            if (rs.getString("og.id") != null) {
                return new OrthogroupTO(rs.getInt("og.id"), rs.getString("og.name"), null);
            }
            return null;
        }
    }
}
