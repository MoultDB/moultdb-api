package org.moultdb.api.repository.mysql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.repository.dao.DomainDAO;
import org.moultdb.api.repository.dto.DomainTO;
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
public class MySQLDomainDAO implements DomainDAO {
    
    private final static Logger logger = LogManager.getLogger(MySQLDomainDAO.class.getName());
    
    NamedParameterJdbcTemplate template;
    
    private static final String SELECT_STATEMENT = "SELECT * FROM domain d ";
    
    public MySQLDomainDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public List<DomainTO> findAll() {
        return template.query(SELECT_STATEMENT, new DomainRowMapper());
    }
    
    @Override
    public DomainTO findById(String id) {
        return TransfertObject.getOneTO(findByIds(Collections.singleton(id)));
    }
    
    @Override
    public List<DomainTO> findByIds(Collection<String> ids) {
        if (ids == null || ids.isEmpty() || ids.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("An ID can not be null");
        }
        return template.query(SELECT_STATEMENT + "WHERE LOWER(id) IN (:ids)",
                new MapSqlParameterSource().addValue("ids", ids), new DomainRowMapper());
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
        logger.info("'domain' table updated");
    }
    
    protected static class DomainRowMapper implements RowMapper<DomainTO> {
        @Override
        public DomainTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new DomainTO(rs.getString("d.id"), rs.getString("d.description"));
        }
    }
}
