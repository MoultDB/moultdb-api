package org.moultdb.api.repository.mysql;

import org.moultdb.api.repository.dao.GeologicalAgeDAO;
import org.moultdb.api.repository.dto.GeologicalAgeTO;
import org.moultdb.api.repository.dto.TransfertObject;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
@Repository
public class MySQLGeologicalAgeDAO implements GeologicalAgeDAO {
    
    NamedParameterJdbcTemplate template;
    
    private final static String SELECT_STATEMENT = "SELECT * FROM geological_age ";
    
    public MySQLGeologicalAgeDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public List<GeologicalAgeTO> findAll() {
        return template.query(SELECT_STATEMENT, new GeologicalAgeRowMapper());
    }
    
    @Override
    public GeologicalAgeTO findById(Integer id) {
        return TransfertObject.getOneTO(findByIds(Collections.singleton(id)));
    }
    
    @Override
    public List<GeologicalAgeTO> findByIds(Set<Integer> ids) {
        if (ids == null || ids.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("An ID can not be null");
        }
        return template.query(SELECT_STATEMENT + "WHERE id IN (:ids)",
                new MapSqlParameterSource().addValue("ids", ids), new GeologicalAgeRowMapper());
    }
    
    protected static class GeologicalAgeRowMapper implements RowMapper<GeologicalAgeTO> {
        @Override
        public GeologicalAgeTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new GeologicalAgeTO(rs.getInt("id"), rs.getString("name"), rs.getString("symbol"),
                    rs.getBigDecimal("younger_bound"), rs.getBigDecimal("older_bound"));
        }
    }
}
