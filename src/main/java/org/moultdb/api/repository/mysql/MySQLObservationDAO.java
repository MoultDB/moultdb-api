package org.moultdb.api.repository.mysql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.repository.dao.ObservationDAO;
import org.moultdb.api.repository.dto.ObservationTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2023-06-20
 */
@Repository
public class MySQLObservationDAO implements ObservationDAO {
    
    private final static Logger logger = LogManager.getLogger(MySQLObservationDAO.class.getName());
    
    NamedParameterJdbcTemplate template;
    
    private static final String SELECT_STATEMENT = "SELECT * FROM observation ";
    
    public MySQLObservationDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public List<ObservationTO> findAll() {
        return template.query(SELECT_STATEMENT, new ObservationRowMapper());
    }
    
    @Override
    public int insert(ObservationTO observationTO) {
        return batchUpdate(Set.of(observationTO));
    }
    
    @Override
    public int batchUpdate(Set<ObservationTO> observationTOs) {
        
        String insertStmt = "INSERT INTO observation (id, url, description) " +
                "VALUES (:id, :url, :description) " +
                "AS new " +
                "ON DUPLICATE KEY UPDATE url = new.url, description = new.description ";
        
        List<MapSqlParameterSource> params = new ArrayList<>();
        for (ObservationTO observationTO : observationTOs) {
            MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue("id", observationTO.getId());
            source.addValue("url", observationTO.getUrl());
            source.addValue("description", observationTO.getDescription());
            params.add(source);
        }
        int[] ints = template.batchUpdate(insertStmt, params.toArray(MapSqlParameterSource[]::new));
        int count = Arrays.stream(ints).sum();
        
        logger.info("{} updated row(s) in 'observation' table", count);
        
        return count;
    }
    
    private static class ObservationRowMapper implements RowMapper<ObservationTO> {
        @Override
        public ObservationTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ObservationTO(rs.getInt("id"), rs.getString("url"), rs.getString("description"));
        }
    }
}
