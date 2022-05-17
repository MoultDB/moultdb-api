package org.moultdb.api.repository.mysql;

import org.moultdb.api.repository.dao.DAO;
import org.moultdb.api.repository.dao.SampleSetDAO;
import org.moultdb.api.repository.dto.SampleSetTO;
import org.moultdb.api.repository.dto.TransfertObject;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
@Repository
public class MySQLSampleSetDAO implements SampleSetDAO {
    
    NamedParameterJdbcTemplate template;
    
    private static final String SELECT_STATEMENT = "SELECT s.*, gaf.*, gat.*, sl.*, cl.*, fpt.*, e.*, gf.*, st.* " +
            "FROM sample_set s " +
            "INNER JOIN geological_age gaf ON (gaf.id = s.from_geological_age_id) " +
            "INNER JOIN geological_age gat ON (gat.id = s.to_geological_age_id) " +
            "INNER JOIN sample_set_storage_location sssl ON (sssl.sample_set_id = s.id) " +
            "INNER JOIN storage_location sl ON (sl.id = sssl.storage_location_id) " +
            "INNER JOIN sample_set_collection_location sscl ON (sscl.sample_set_id = s.id) " +
            "INNER JOIN collection_location cl ON (cl.id = sscl.collection_location_id) " +
            "INNER JOIN sample_set_fossil_preservation_type ssfpt ON (ssfpt.sample_set_id = s.id) " +
            "INNER JOIN fossil_preservation_type fpt ON (fpt.id = ssfpt.fossil_preservation_type_id) " +
            "INNER JOIN sample_set_environment sse ON (sse.sample_set_id = s.id) " +
            "INNER JOIN environment e ON (e.id = sse.environment_id) " +
            "INNER JOIN sample_set_geological_formation ssgf ON (ssgf.sample_set_id = s.id) " +
            "INNER JOIN geological_formation gf ON (e.id = ssgf.geological_formation_id) " +
            "INNER JOIN sample_set_specimen_type ssst ON (ssst.sample_set_id = s.id) " +
            "INNER JOIN specimen_type st ON (st.id = ssst.specimen_type_id) ";
    
    public MySQLSampleSetDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public List<SampleSetTO> findAll() {
        return template.query(SELECT_STATEMENT, new SampleSetResultSetExtractor());
    }
    
    @Override
    public SampleSetTO findById(Integer id) {
        return TransfertObject.getOneTO(findByIds(Collections.singleton(id)));
    }
    
    @Override
    public List<SampleSetTO> findByIds(Set<Integer> samplesIds) {
        return template.query(SELECT_STATEMENT + "WHERE s.id IN (:ids)",
                new MapSqlParameterSource().addValue("ids", samplesIds), new SampleSetResultSetExtractor());
    }
    
    private static class SampleSetResultSetExtractor implements ResultSetExtractor<List<SampleSetTO>> {
        
        @Override
        public List<SampleSetTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Integer, SampleSetTO> sampleSets = new HashMap<>();
            while(rs.next()) {
                Integer sampleSetId = rs.getInt("a.id");
                SampleSetTO sampleSetTO = sampleSets.get(sampleSetId);
    
                String museumAccessionValue = rs.getString("s.museum_accessions");
                Set<String> museumAccessions = null;
                if (museumAccessionValue != null) {
                    museumAccessions = Arrays.stream(museumAccessionValue.split(";")).collect(Collectors.toSet());
                }
    
                Set<String> slNames = extractNames(rs, "sl.name", sampleSetTO == null ? null : sampleSetTO.getStorageLocationNames());
                Set<String> clNames = extractNames(rs, "cl.name", sampleSetTO == null ? null : sampleSetTO.getCollectionLocationNames());
                Set<String> fptNames = extractNames(rs, "fpt.name", sampleSetTO == null ? null : sampleSetTO.getFossilPreservationTypes());
                Set<String> eNames = extractNames(rs, "e.name", sampleSetTO == null ? null : sampleSetTO.getEnvironments());
                Set<String> gfNames = extractNames(rs, "gf.name", sampleSetTO == null ? null : sampleSetTO.getGeologicalFormations());
                Set<String> stNames = extractNames(rs, "st.name", sampleSetTO == null ? null : sampleSetTO.getSpecimenTypes());
                
                // Build SampleSetTO. Even if it already exists, we create a new one because it's an unmutable object
                sampleSetTO = new SampleSetTO(rs.getInt("s.id"), DAO.mapToGeologicalAgeTO(rs, "gaf"),
                        DAO.mapToGeologicalAgeTO(rs, "gat"),  rs.getInt("s.specimen_count"), museumAccessions,
                        slNames, clNames, fptNames, eNames, gfNames, stNames, null);
                sampleSets.put(sampleSetId, sampleSetTO);
            }
            return new ArrayList<>(sampleSets.values());
        }
    
        private Set<String> extractNames(ResultSet rs, String label, Set<String> previousValues) throws SQLException {
            String value = rs.getString(label);
            Set<String> values = previousValues;
            if (previousValues == null) {
                previousValues = new HashSet<>();
            }
            previousValues.add(value);
            return values;
        }
    }
}
