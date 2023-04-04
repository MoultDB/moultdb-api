package org.moultdb.api.repository.mysql;

import org.moultdb.api.repository.dao.DataSourceDAO;
import org.moultdb.api.repository.dto.DataSourceTO;
import org.moultdb.api.repository.dto.TransfertObject;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Valentine Rech de Laval
 * @since 2022-06-27
 */
@Repository
public class MySQLDataSourceDAO implements DataSourceDAO {
    
    NamedParameterJdbcTemplate template;
    
    private static final String SELECT_STATEMENT = "SELECT * from data_source ";
    
    public MySQLDataSourceDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @Override
    public List<DataSourceTO> findAll() {
        return template.query(SELECT_STATEMENT, new DataSourceRowMapper());
    }
    
    @Override
    public DataSourceTO findByName(String name) {
        return TransfertObject.getOneTO(template.query(SELECT_STATEMENT + "WHERE id = :name",
                new MapSqlParameterSource().addValue("name", name), new DataSourceRowMapper()));
    }
    
    private static class DataSourceRowMapper implements RowMapper<DataSourceTO> {
        @Override
        public DataSourceTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new DataSourceTO(rs.getInt("id"), rs.getString("name"), rs.getString("description"),
                    rs.getString("base_url"), rs.getDate("last_import_date"), rs.getString("release_version"));
        }
    }
}
