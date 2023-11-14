package org.moultdb.api.repository.mysql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.repository.dao.VersionDAO;
import org.moultdb.api.repository.dto.TaxonAnnotationTO;
import org.moultdb.api.repository.dto.TransfertObject;
import org.moultdb.api.repository.dto.UserTO;
import org.moultdb.api.repository.dto.VersionTO;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
@Repository
public class MySQLVersionDAO implements VersionDAO {
    
    private final static Logger logger = LogManager.getLogger(MySQLVersionDAO.class.getName());
    
    NamedParameterJdbcTemplate template;
    
    public MySQLVersionDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    private static final String SELECT_STATEMENT =
            "SELECT v.*, u1.*, u2.* " +
            "FROM version v " +
            "LEFT JOIN user u1 ON (u1.id = v.creation_user_id) " +
            "LEFT JOIN user u2 ON (u2.id = v.last_update_user_id) ";
    
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
    
    @Override
    public int insert(VersionTO versionTO) {
        return batchUpdate(Collections.singleton(versionTO))[0];
    }
    
    @Override
    public Integer getLastId() {
        String sql = "SELECT id FROM version ORDER BY id DESC LIMIT 1";
        try {
            return template.queryForObject(sql, new MapSqlParameterSource(), Integer.class);
        } catch (EmptyResultDataAccessException e) {
            logger.debug("No record found in 'version' table");
            return 0;
        }
    }
    
    @Override
    public int[] batchUpdate(Set<VersionTO> versionTOs) {
        String insertStmt = "INSERT INTO version (id, creation_user_id, creation_date," +
                " last_update_user_id, last_update_date, version_number) " +
                "VALUES (:id, :creation_user_id, :creation_date, " +
                ":last_update_user_id, :last_update_date, :version_number) ";
        
        List<MapSqlParameterSource> params = new ArrayList<>();
        for (VersionTO versionTO : versionTOs) {
            MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue("id", versionTO.getId());
            source.addValue("creation_user_id", versionTO.getCreationUserTO().getId());
            source.addValue("creation_date", versionTO.getCreationDate());
            source.addValue("last_update_user_id", versionTO.getLastUpdateUserTO().getId());
            source.addValue("last_update_date", versionTO.getLastUpdateDate());
            source.addValue("version_number", versionTO.getVersionNumber());
            params.add(source);
        }
        int[] ints = template.batchUpdate(insertStmt, params.toArray(MapSqlParameterSource[]::new));
        logger.info(Arrays.stream(ints).sum()+ " updated row(s) in 'version' table.");
        return ints;
    }
    
    private static class VersionRowMapper implements RowMapper<VersionTO> {
        @Override
        public VersionTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new VersionTO(rs.getInt("id"),
                    new UserTO(rs.getInt("creation_user_id"),
                            rs.getString("u1.username"), rs.getString("u1.email"),
                            rs.getString("u1.pwd"), rs.getString("u1.roles"),
                            rs.getString("u1.orcidId"), rs.getBoolean("u1.verified")),
                    rs.getTimestamp("creation_date"),
                    new UserTO(rs.getInt("last_update_user_id"),
                            rs.getString("u2.username"), rs.getString("u2.email"),
                            rs.getString("u2.pwd"), rs.getString("u2.roles"),
                            rs.getString("u2.orcidId"), rs.getBoolean("u2.verified")),
                    rs.getTimestamp("last_update_date"), rs.getInt("version_number"));
        }
    }
}
