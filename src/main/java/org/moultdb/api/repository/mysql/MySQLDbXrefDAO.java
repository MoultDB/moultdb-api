package org.moultdb.api.repository.mysql;

import org.moultdb.api.repository.dao.DbXrefDAO;
import org.moultdb.api.repository.dto.DataSourceTO;
import org.moultdb.api.repository.dto.DbXrefTO;
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
 * @since 2021-11-01
 */
@Repository
public class MySQLDbXrefDAO implements DbXrefDAO {
    
    NamedParameterJdbcTemplate template;
    
    String SELECT_STATEMENT = "SELECT * FROM db_xref x " +
            "INNER JOIN data_source ds ON (x.data_source_id = ds.id) ";
    
    public MySQLDbXrefDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public List<DbXrefTO> findAll() {
        return template.query(SELECT_STATEMENT, new DbXrefRowMapper());
    }
    
    @Override
    public DbXrefTO findById(Integer id) {
        return TransfertObject.getOneTO(findByIds(Collections.singleton(id)));
    }
    
    @Override
    public List<DbXrefTO> findByIds(Set<Integer> ids) {
        if (ids == null || ids.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("An ID can not be null");
        }
        return template.query(SELECT_STATEMENT + "WHERE x.id IN (:ids)",
                    new MapSqlParameterSource().addValue("ids", ids), new MySQLDbXrefDAO.DbXrefRowMapper());
    }
    
    private static class DbXrefRowMapper implements RowMapper<DbXrefTO> {
        @Override
        public DbXrefTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new DbXrefTO(rs.getInt("x.id"), rs.getString("x.accession"),
                    new DataSourceTO(rs.getInt("ds.id"), rs.getString("ds.name"), rs.getString("description"),
                            rs.getString("ds.base_url"), rs.getDate("ds.release_date"), rs.getString("ds.release_version")));
        }
    }
}
