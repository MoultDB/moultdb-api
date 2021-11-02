package org.moultdb.api.repository.mysql;

import org.moultdb.api.repository.dao.SutureLocationDAO;
import org.moultdb.api.repository.dto.SutureLocationTO;
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
public class MySQLSutureDirectionDAO implements SutureLocationDAO {
    
    NamedParameterJdbcTemplate template;
    
    private final static String SELECT_STATEMENT = "SELECT * FROM suture_location ";
    
    public MySQLSutureDirectionDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public List<SutureLocationTO> findAll() {
        return template.query(SELECT_STATEMENT, new SutureLocationRowMapper());
    }
    
    @Override
    public SutureLocationTO findById(int id) {
        return TransfertObject.getOneTO(findByIds(Collections.singleton(id)));
    }
    
    @Override
    public List<SutureLocationTO> findByIds(Set<Integer> ids) {
        if (ids == null || ids.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("An ID can not be null");
        }
        return template.query(SELECT_STATEMENT + "WHERE id IN (:ids)",
                new MapSqlParameterSource().addValue("ids", ids), new SutureLocationRowMapper());
    }
    
    private static class SutureLocationRowMapper implements RowMapper<SutureLocationTO> {
        @Override
        public SutureLocationTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new SutureLocationTO(rs.getInt("id"), rs.getString("name"));
        }
    }
}
