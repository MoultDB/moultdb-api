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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author Valentine Rech de Laval
 * @since 2021-11-01
 */
@Repository
public class MySQLDbXrefDAO implements DbXrefDAO {
    
    private final static Logger logger = LogManager.getLogger(MySQLDbXrefDAO.class.getName());
    
    NamedParameterJdbcTemplate template;
    
    String SELECT_STATEMENT = "SELECT x.*, ds.* FROM db_xref x " +
            "LEFT JOIN data_source ds ON (x.data_source_id = ds.id) ";
    
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
            throw new IllegalArgumentException("An accession [" + accession
                    + "] and/or dataSourceId [" + dataSourceId + "] can not be null");
        }
        return  TransfertObject.getOneTO(template.query(SELECT_STATEMENT +
                        "WHERE x.accession = :accession AND x.data_source_id = :data_source_id ",
                new MapSqlParameterSource().addValue("accession", accession)
                                           .addValue("data_source_id", dataSourceId),
                new DbXrefRowMapper()));
    }
    
    @Override
    public DbXrefTO find(String accession, String name, Integer dataSourceId) {
        if (dataSourceId == null) {
            throw new IllegalArgumentException("A datasource ID [" + dataSourceId + "] can not be null");
        }
        if (StringUtils.isBlank(accession) && StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("An accession [" + accession+ "] and name [" + name + "] can not be null");
        }
        
        String sql = SELECT_STATEMENT + "WHERE x.data_source_id = :data_source_id ";
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("data_source_id", dataSourceId);
        
        if (StringUtils.isNotBlank(accession)) {
            sql += "AND x.accession = :accession ";
            mapSqlParameterSource.addValue("accession", accession);
        }
        
        if (StringUtils.isNotBlank(name)) {
            sql += "AND x.name = :name ";
            mapSqlParameterSource.addValue("name", name);
        }
        return  TransfertObject.getOneTO(template.query(sql,
                new MapSqlParameterSource().addValue("accession", accession)
                        .addValue("name", name)
                        .addValue("data_source_id", dataSourceId),
                new DbXrefRowMapper()));
    }
    
    @Override
    public void insert(DbXrefTO dbXrefTO) {
        batchUpdate(Collections.singleton(dbXrefTO));
    }
    
    @Override
    public void batchUpdate(Set<DbXrefTO> dbXrefTOs) {
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
        template.batchUpdate(insertStmt, params.toArray(MapSqlParameterSource[]::new));
        logger.info("'db_xref' table updated");
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
            return mapRow(rs, rowNum, "x", "ds");
        }
        
        public DbXrefTO mapRow(ResultSet rs, int rowNum, String dbXrefAlias, String datasourceAlias) throws SQLException {
            if (rs.getInt(dbXrefAlias + ".id") != 0) {
                return new DbXrefTO(rs.getInt(dbXrefAlias + ".id"), rs.getString(dbXrefAlias + ".accession"),
                    rs.getString(dbXrefAlias + ".name"),
                    new DataSourceTO(rs.getInt(datasourceAlias + ".id"), rs.getString(datasourceAlias + ".name"),
                            rs.getString(datasourceAlias + ".description"), rs.getString(datasourceAlias + ".short_name"),
                            rs.getString(datasourceAlias + ".base_url"), rs.getString(datasourceAlias + ".xref_url"),
                            rs.getDate(datasourceAlias + ".last_import_date"), rs.getString(datasourceAlias + ".release_version"),
                            rs.getInt(datasourceAlias + ".display_order")));
            }
            return null;
        }
    }
}
