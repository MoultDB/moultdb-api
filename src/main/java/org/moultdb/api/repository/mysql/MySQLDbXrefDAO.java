package org.moultdb.api.repository.mysql;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.repository.dao.DbXrefDAO;
import org.moultdb.api.repository.dto.DataSourceTO;
import org.moultdb.api.repository.dto.DbXrefTO;
import org.moultdb.api.repository.dto.TransfertObject;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2021-11-01
 */
@Repository
public class MySQLDbXrefDAO implements DbXrefDAO {
    
    private final static Logger logger = LogManager.getLogger(MySQLDbXrefDAO.class.getName());
    
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
    public DbXrefTO findByAccessionAndDatasource(String accession, Integer dataSourceId) {
        if (StringUtils.isBlank(accession) || dataSourceId == null) {
            throw new IllegalArgumentException("An accession and/or dataSourceId can not be null");
        }
        return  TransfertObject.getOneTO(template.query(SELECT_STATEMENT +
                        "WHERE x.accession = :accession AND data_source_id = :data_source_id ",
                new MapSqlParameterSource().addValue("accession", accession)
                                           .addValue("data_source_id", dataSourceId),
                new DbXrefRowMapper()));
    }
    
    @Override
    public int insert(DbXrefTO dbXrefTO) {
        int[] ints = batchUpdate(Collections.singleton(dbXrefTO));
        return ints[0];
    }
    
    @Transactional
    @Override
    public int[] batchUpdate(Set<DbXrefTO> dbXrefTOs) {
        String insertStmt = "INSERT INTO db_xref (id, accession, data_source_id) " +
                "VALUES (:id, :accession, :data_source_id) " +
                "AS new " +
                "ON DUPLICATE KEY UPDATE accession = new.accession, data_source_id = new.data_source_id";
        List<MapSqlParameterSource> params = new ArrayList<>();
        for (DbXrefTO dbXrefTO : dbXrefTOs) {
            MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue("id", dbXrefTO.getId());
            source.addValue("accession", dbXrefTO.getAccession());
            source.addValue("data_source_id", dbXrefTO.getDataSourceTO().getId());
            params.add(source);
        }
        int[] ints = template.batchUpdate(insertStmt, params.toArray(MapSqlParameterSource[]::new));
        logger.info(Arrays.stream(ints).sum()+ " new row(s) in 'db_xref' table.");
        return ints;
    }
    
    @Override
    public Integer getLastId() {
        String sql = "SELECT id FROM db_xref ORDER BY id DESC LIMIT 1";
        try {
            return template.queryForObject(sql, new MapSqlParameterSource(), Integer.class);
        } catch (EmptyResultDataAccessException e) {
            logger.debug("No record found in 'db_xref' table");
            return 0;
        }
    }
    
    protected static class DbXrefRowMapper implements RowMapper<DbXrefTO> {
        @Override
        public DbXrefTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new DbXrefTO(rs.getInt("x.id"), rs.getString("x.accession"),
                    new DataSourceTO(rs.getInt("ds.id"), rs.getString("ds.name"), rs.getString("description"),
                            rs.getString("ds.base_url"), rs.getDate("ds.last_import_date"), rs.getString("ds.release_version")));
        }
    }
    
    private static class ArticleDbXrefRowMapper implements RowMapper<Map.Entry<Integer, DbXrefTO>> {
        @Override
        public Map.Entry<Integer, DbXrefTO> mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new AbstractMap.SimpleEntry<>(rs.getInt("adx.article_id"),
                    new DbXrefTO(rs.getInt("x.id"), rs.getString("x.accession"),
                            new DataSourceTO(rs.getInt("ds.id"), rs.getString("ds.name"), rs.getString("description"),
                                    rs.getString("ds.base_url"), rs.getDate("ds.last_import_date"), rs.getString("ds.release_version"))));
        }
    }
}
