package org.moultdb.api.repository.mysql;

import org.moultdb.api.repository.dao.SampleDAO;
import org.moultdb.api.repository.dto.CollectionLocationTO;
import org.moultdb.api.repository.dto.GeologicalAgeTO;
import org.moultdb.api.repository.dto.SampleTO;
import org.moultdb.api.repository.dto.StorageLocationTO;
import org.moultdb.api.repository.dto.TransfertObject;
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
public class MySQLSampleDAO implements SampleDAO {
    
    NamedParameterJdbcTemplate template;
    
    private static final String SELECT_STATEMENT = "SELECT s.*, sl.*, cl.*, ga.* FROM sample s " +
            "INNER JOIN storage_location sl ON (sl.id = s.storage_location_id) " +
            "INNER JOIN collection_location cl ON (cl.id = s.collection_location_id) " +
            "INNER JOIN geological_age ga ON (ga.id = s.geological_age_id) ";
    
    public MySQLSampleDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public List<SampleTO> findAll() {
        return template.query(SELECT_STATEMENT, new SampleRowMapper());
    }
    
    @Override
    public SampleTO findById(Integer id) {
        return TransfertObject.getOneTO(findByIds(Collections.singleton(id)));
    }
    
    @Override
    public List<SampleTO> findByIds(Set<Integer> samplesIds) {
        return template.query(SELECT_STATEMENT + "WHERE s.id IN (:ids)",
                new MapSqlParameterSource().addValue("ids", samplesIds), new SampleRowMapper());
    }
    
    private static class SampleRowMapper implements RowMapper<SampleTO> {
        @Override
        public SampleTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            
            GeologicalAgeTO geologicalAgeTO = new GeologicalAgeTO(rs.getInt("ga.id"), rs.getString("ga.name"),
                    rs.getString("ga.symbol"), rs.getBigDecimal("ga.younger_bound"), rs.getBigDecimal("ga.older_bound"));

            return new SampleTO(rs.getInt("s.id"), geologicalAgeTO, rs.getString("cl.name"), rs.getString("s.storage_accession"),
                    rs.getString("sl.name"), rs.getString("s.collector"), rs.getInt("s.version_id"));
        }
    }
}
