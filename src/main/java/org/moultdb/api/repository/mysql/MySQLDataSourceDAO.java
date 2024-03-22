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
    
    private static final String SELECT_STATEMENT = "SELECT * from data_source ds ";
    
    public MySQLDataSourceDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @Override
    public List<DataSourceTO> findAll() {
        return template.query(SELECT_STATEMENT, new DataSourceRowMapper());
    }
    
    @Override
    public DataSourceTO findByName(String name) {
        return TransfertObject.getOneTO(template.query(SELECT_STATEMENT + "WHERE LOWER(ds.name) = LOWER(:name)",
                new MapSqlParameterSource().addValue("name", name), new DataSourceRowMapper()));
    }
    
    protected static class DataSourceRowMapper implements RowMapper<DataSourceTO> {
        @Override
        public DataSourceTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new DataSourceTO(rs.getInt("ds.id"), rs.getString("ds.name"), rs.getString("ds.description"),
                    rs.getString("ds.short_name"), rs.getString("ds.base_url"), rs.getString("ds.xref_url"),
                    rs.getDate("ds.last_import_date"), rs.getString("ds.release_version"));
        }
    }
}
