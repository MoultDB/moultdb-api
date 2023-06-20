package org.moultdb.api.repository.mysql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.repository.dao.ImageDAO;
import org.moultdb.api.repository.dto.ImageTO;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Valentine Rech de Laval
 * @since 2023-06-20
 */
@Repository
public class MySQLImageDAO implements ImageDAO {
    
    private final static Logger logger = LogManager.getLogger(MySQLImageDAO.class.getName());
    
    NamedParameterJdbcTemplate template;
    
    private static final String SELECT_STATEMENT = "SELECT * FROM image ";
    
    public MySQLImageDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public List<ImageTO> findAll() {
        return template.query(SELECT_STATEMENT, new MySQLImageDAO.ImageRowMapper());
    }
    
    @Override
    public int insert(ImageTO imageTO) {
        String insertStmt = "INSERT INTO image (id, file_name, description) " +
                "VALUES (:id, :fileName, :description) ";
        List<MapSqlParameterSource> params = new ArrayList<>();
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("id", imageTO.getId());
        source.addValue("fileName", imageTO.getFileName());
        source.addValue("description", imageTO.getDescription());
        params.add(source);
        
        int[] ints = template.batchUpdate(insertStmt, params.toArray(MapSqlParameterSource[]::new));
        logger.info(Arrays.stream(ints).sum()+ " new row(s) in 'image' table.");
        
        return ints[0];
    }
    
    @Override
    public Integer getLastId() {
        String sql = "SELECT id FROM image ORDER BY id DESC LIMIT 1";
        try {
            return template.queryForObject(sql, new MapSqlParameterSource(), Integer.class);
        } catch (EmptyResultDataAccessException e) {
            logger.debug("No record found in 'image' table");
            return 0;
        }
    }
    
    private static class ImageRowMapper implements RowMapper<ImageTO> {
        @Override
        public ImageTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ImageTO(rs.getInt("id"), rs.getString("file_name"), rs.getString("description"));
        }
    }
}
