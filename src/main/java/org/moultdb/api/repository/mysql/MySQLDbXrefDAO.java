package org.moultdb.api.repository.mysql;

import org.moultdb.api.repository.dao.DbXrefDAO;
import org.moultdb.api.repository.dto.ArticleTO;
import org.moultdb.api.repository.dto.DataSourceTO;
import org.moultdb.api.repository.dto.DbXrefTO;
import org.moultdb.api.repository.dto.TransfertObject;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Valentine Rech de Laval
 * @since 2021-11-01
 */
@Repository
public class MySQLDbXrefDAO implements DbXrefDAO {
    
    NamedParameterJdbcTemplate template;
    
    String SELECT_STATEMENT = "SELECT x.*, ds.* FROM db_xref x " +
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
                    new MapSqlParameterSource().addValue("ids", ids), new DbXrefRowMapper());
    }
    
    @Override
    public Map<Integer, Set<DbXrefTO>> findByArticleIds(Set<Integer> articleIds) {
        if (articleIds == null || articleIds.size() == 0 || articleIds.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("An ID can not be null");
        }
        return template.query(SELECT_STATEMENT +
                        "INNER JOIN article_db_xref adx ON adx.db_xref_id = x.id " +
                        "WHERE adx.article_id IN (:ids)",
                new MapSqlParameterSource().addValue("ids", articleIds), new ArticleDbXrefRowMapper()).stream()
                       .collect(Collectors.toMap(e -> e.getKey(),
                               e -> new HashSet<>(Arrays.asList(e.getValue())),
                               (v1, v2) -> { v1.addAll(v2); return v1; }));
    }
    
    private static class DbXrefRowMapper implements RowMapper<DbXrefTO> {
        @Override
        public DbXrefTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new DbXrefTO(rs.getInt("x.id"), rs.getString("x.accession"),
                    new DataSourceTO(rs.getInt("ds.id"), rs.getString("ds.name"), rs.getString("description"),
                            rs.getString("ds.base_url"), rs.getDate("ds.release_date"), rs.getString("ds.release_version")));
        }
    }
    
    private static class ArticleDbXrefRowMapper implements RowMapper<Map.Entry<Integer, DbXrefTO>> {
        @Override
        public Map.Entry<Integer, DbXrefTO> mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new AbstractMap.SimpleEntry<>(rs.getInt("adx.article_id"),
                    new DbXrefTO(rs.getInt("x.id"), rs.getString("x.accession"),
                            new DataSourceTO(rs.getInt("ds.id"), rs.getString("ds.name"), rs.getString("description"),
                                    rs.getString("ds.base_url"), rs.getDate("ds.release_date"), rs.getString("ds.release_version"))));
        }
    }
    
}
