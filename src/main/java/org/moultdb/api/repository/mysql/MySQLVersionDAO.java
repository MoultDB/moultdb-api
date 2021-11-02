package org.moultdb.api.repository.mysql;

import org.moultdb.api.repository.dao.VersionDAO;
import org.moultdb.api.repository.dto.TransfertObject;
import org.moultdb.api.repository.dto.UserTO;
import org.moultdb.api.repository.dto.VersionTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
@Repository
public class MySQLVersionDAO implements VersionDAO {
    
    NamedParameterJdbcTemplate template;
    
    public MySQLVersionDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    private static final String SELECT_STATEMENT =
            "SELECT v.*, u1.name, u2.name " +
            "FROM version v " +
            "INNER JOIN user u1 ON (u1.id = v.creation_user_id) " +
            "INNER JOIN user u2 ON (u2.id = v.last_update_user_id) ";
    
    @Override
    public List<VersionTO> findAll() {
        return template.query(SELECT_STATEMENT, new VersionRowMapper());
    }
    
    @Override
    public VersionTO findById(int id) {
        return TransfertObject.getOneTO(findByIds(Collections.singleton(id)));
    }
    
    @Override
    public List<VersionTO> findByIds(Set<Integer> ids) {
        return template.query(SELECT_STATEMENT + "WHERE v.id IN (:ids)",
                new MapSqlParameterSource().addValue("ids", ids), new VersionRowMapper());
    }
    
    private static class VersionRowMapper implements RowMapper<VersionTO> {
        @Override
        public VersionTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new VersionTO(rs.getInt("id"),
                    new UserTO(rs.getInt("creation_user_id"), rs.getString("u1.name")),
                    rs.getTimestamp("creation_date"),
                    new UserTO(rs.getInt("last_update_user_id"), rs.getString("u2.name")),
                    rs.getTimestamp("last_update_date"), rs.getInt("version_number"));
        }
    }
}
