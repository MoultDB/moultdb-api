package org.moultdb.api.repository.mysql;

import org.moultdb.api.exception.MoultDBException;
import org.moultdb.api.model.ReleaseVersion;
import org.moultdb.api.repository.dao.ReleaseDAO;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Repository
public class MySQLReleaseDAO implements ReleaseDAO {
    
    NamedParameterJdbcTemplate template;

    public MySQLReleaseDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @Override
    public int findAnnotationCount() {
        return getCount("select count(id) from taxon_annotation");
    }
    
    @Override
    public int findGenomeCount() {
        return getCount("select count(genbank_acc) from genome");
    }
    
    private int getCount(String sql) {
        int count = 0;
        try {
            Integer result = template.queryForObject(sql, new HashMap<>(), Integer.class);
            if (result != null) {
                count = result;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new MoultDBException("Count failed: " + e.getMessage());
        }
        return count;
    }
    
    @Override
    public ReleaseVersion findReleaseVersion() {
        return template.queryForObject("SELECT * FROM release_version ORDER BY date DESC LIMIT 1",
                new HashMap<>(), new ReleaseVersionRowMapper());
    }

    @Override
    public int insertNewReleaseVersion() {
        // Currently, version name is the date.
        Map<String,Object> map = new HashMap<>();
        map.put("name", "MoultDB " + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));

        return template.update("INSERT INTO release_version(date, name) VALUES (now(), :name)", map);
    }
    
    private static class ReleaseVersionRowMapper implements RowMapper<ReleaseVersion> {
        @Override
        public ReleaseVersion mapRow(ResultSet rs, int i) throws SQLException {
            return new ReleaseVersion(rs.getString("name"), rs.getObject("date", LocalDateTime.class).toLocalDate());
        }
    }
}
