package org.moultdb.api.repository.mysql;

import org.moultdb.api.repository.dao.UserDAO;
import org.moultdb.api.repository.dto.TransfertObject;
import org.moultdb.api.repository.dto.UserTO;
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
public class MySQLUserDAO implements UserDAO {
    
    NamedParameterJdbcTemplate template;
    
    private final static String SELECT_STATEMENT = "SELECT * FROM user ";
    
    public MySQLUserDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public List<UserTO> findAll() {
        return template.query(SELECT_STATEMENT, new UserRowMapper());
    }
    
    @Override
    public UserTO findById(int id) {
        return TransfertObject.getOneTO(findByIds(Collections.singleton(id)));
    }
    
    @Override
    public List<UserTO> findByIds(Set<Integer> ids) {
        if (ids == null || ids.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("An ID can not be null");
        }
        return template.query(SELECT_STATEMENT + "WHERE id IN (:ids)",
                new MapSqlParameterSource().addValue("ids", ids), new UserRowMapper());
    }
    
    private static class UserRowMapper implements RowMapper<UserTO> {
        @Override
        public UserTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new UserTO(rs.getInt("id"), rs.getString("name"));
        }
    }
}
