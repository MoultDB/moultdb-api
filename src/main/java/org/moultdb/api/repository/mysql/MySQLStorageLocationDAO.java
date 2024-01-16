package org.moultdb.api.repository.mysql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.repository.dao.StorageLocationDAO;
import org.moultdb.api.repository.dto.StorageLocationTO;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2022-06-14
 */
public class MySQLStorageLocationDAO implements StorageLocationDAO {
    
    private final static Logger logger = LogManager.getLogger(MySQLStorageLocationDAO.class.getName());
    
    private static final String SELECT_STATEMENT = "SELECT * FROM storage_location ";
    
    NamedParameterJdbcTemplate template;
    
    @Override
    public List<StorageLocationTO> findAll() {
        return template.query(SELECT_STATEMENT, new StorageLocationRowMapper());
    }
    
    @Override
    public void insert(StorageLocationTO storageLocationTO) {
        batchUpdate(Collections.singleton(storageLocationTO));
    }
    
    @Override
    public void batchUpdate(Set<StorageLocationTO> storageLocationTOs) {
        String insertStmt = "INSERT INTO storage_location (id, name) " +
                "VALUES (:id, :name) " +
                "AS new " +
                "ON DUPLICATE KEY UPDATE name = new.name ";
        List<MapSqlParameterSource> params = new ArrayList<>();
        for (StorageLocationTO storageLocationTO : storageLocationTOs) {
            MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue("id", storageLocationTO.getId());
            source.addValue("name", storageLocationTO.getName());
            params.add(source);
        }
        template.batchUpdate(insertStmt, params.toArray(MapSqlParameterSource[]::new));
        logger.info("'storage_location' table updated.");
    }
    
    @Override
    public Integer getLastId() {
        String sql = "SELECT id FROM storage_location ORDER BY id DESC LIMIT 1";
        try {
            return template.queryForObject(sql, new MapSqlParameterSource(), Integer.class);
        } catch (EmptyResultDataAccessException e) {
            logger.debug("No record found in 'storage_location' table");
            return 0;
        }
    }
    
    protected static class StorageLocationRowMapper implements RowMapper<StorageLocationTO> {
        @Override
        public StorageLocationTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new StorageLocationTO(rs.getInt("id"), rs.getString("name"));
        }
    }
}
