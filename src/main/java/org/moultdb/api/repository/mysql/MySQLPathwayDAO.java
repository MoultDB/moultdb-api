package org.moultdb.api.repository.mysql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.repository.dao.PathwayDAO;
import org.moultdb.api.repository.dto.PathwayTO;
import org.moultdb.api.repository.dto.TransfertObject;
import org.springframework.jdbc.core.RowMapper;
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
public class MySQLPathwayDAO implements PathwayDAO {
    
    private final static Logger logger = LogManager.getLogger(MySQLPathwayDAO.class.getName());
    
    NamedParameterJdbcTemplate template;
    
    private static final String SELECT_STATEMENT = "SELECT * FROM pathway p ";
    
    public MySQLPathwayDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public List<PathwayTO> findAll() {
        return template.query(SELECT_STATEMENT, new PathwayRowMapper());
    }
    
    @Override
    public PathwayTO findById(String id) {
        return TransfertObject.getOneTO(template.query(SELECT_STATEMENT + "WHERE LOWER(p.id) = LOWER(:id)",
                new MapSqlParameterSource().addValue("id", id), new PathwayRowMapper()));
    }
    
    @Override
    public List<PathwayTO> findByIds(Set<String> ids) {
        if (ids == null || ids.isEmpty() || ids.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("An ID can not be null");
        }
        return template.query(SELECT_STATEMENT + "WHERE p.id IN (:ids)",
                new MapSqlParameterSource().addValue("ids", ids), new PathwayRowMapper());
    }
    
    @Override
    public void insert(PathwayTO pathwayTO) {
        batchUpdate(Collections.singleton(pathwayTO));
    }
    
    @Override
    public void batchUpdate(Set<PathwayTO> pathwayTOs) {
        String insertStmt = "INSERT INTO pathway (id, name) " +
                "VALUES (:id, :name) " +
                "AS new " +
                "ON DUPLICATE KEY UPDATE name = new.name ";
        List<MapSqlParameterSource> params = new ArrayList<>();
        for (PathwayTO pathwayTO : pathwayTOs) {
            MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue("id", pathwayTO.getId());
            source.addValue("name", pathwayTO.getName());
            params.add(source);
        }
        template.batchUpdate(insertStmt, params.toArray(MapSqlParameterSource[]::new));
        logger.info("'pathway' table updated.");
    }
    
    protected static class PathwayRowMapper implements RowMapper<PathwayTO> {
        @Override
        public PathwayTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            if (rs.getString("p.id") == null && rs.getString("p.name") == null) {
                return null;
            }
            return new PathwayTO(rs.getString("p.id"), rs.getString("p.name"));
        }
    }
}
