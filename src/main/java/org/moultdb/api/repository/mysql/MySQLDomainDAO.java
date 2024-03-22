package org.moultdb.api.repository.mysql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.repository.dao.DomainDAO;
import org.moultdb.api.repository.dto.DomainTO;
import org.moultdb.api.repository.dto.TransfertObject;
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
 * @since 2024-03-22
 */
public class MySQLDomainDAO implements DomainDAO {
    
    private final static Logger logger = LogManager.getLogger(MySQLDomainDAO.class.getName());
    
    NamedParameterJdbcTemplate template;
    
    private static final String SELECT_STATEMENT = "SELECT * FROM domain ";
    
    public MySQLDomainDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public List<DomainTO> findAll() {
        return template.query(SELECT_STATEMENT, new DomainRowMapper());
    }
    
    @Override
    public DomainTO findById(String id) {
        return TransfertObject.getOneTO(template.query(SELECT_STATEMENT + "WHERE LOWER(id) = LOWER(:id)",
                new MapSqlParameterSource().addValue("id", id), new DomainRowMapper()));
    }
    
    @Override
    public void insert(DomainTO domainTO) {
        batchUpdate(Collections.singleton(domainTO));
    }
    
    @Override
    public void batchUpdate(Set<DomainTO> domainTOs) {
        String insertStmt = "INSERT INTO domain (id, description) " +
                "VALUES (:id, :description) " +
                "AS new " +
                "ON DUPLICATE KEY UPDATE description = new.description ";
        List<MapSqlParameterSource> params = new ArrayList<>();
        for (DomainTO domainTO : domainTOs) {
            MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue("id", domainTO.getId());
            source.addValue("description", domainTO.getDescription());
            params.add(source);
        }
        template.batchUpdate(insertStmt, params.toArray(MapSqlParameterSource[]::new));
        logger.info("'domain' table updated.");
    }
    
    protected static class DomainRowMapper implements RowMapper<DomainTO> {
        @Override
        public DomainTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new DomainTO(rs.getString("id"), rs.getString("description"));
        }
    }
}
