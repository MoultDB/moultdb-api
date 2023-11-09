package org.moultdb.api.repository.mysql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.repository.dao.DAO;
import org.moultdb.api.repository.dao.SampleSetDAO;
import org.moultdb.api.repository.dto.CollectionLocationTO;
import org.moultdb.api.repository.dto.EnvironmentTO;
import org.moultdb.api.repository.dto.FossilPreservationTypeTO;
import org.moultdb.api.repository.dto.GeologicalAgeTO;
import org.moultdb.api.repository.dto.GeologicalFormationTO;
import org.moultdb.api.repository.dto.NamedEntityTO;
import org.moultdb.api.repository.dto.SampleSetTO;
import org.moultdb.api.repository.dto.SpecimenTypeTO;
import org.moultdb.api.repository.dto.TransfertObject;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
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
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
@Repository
public class MySQLSampleSetDAO implements SampleSetDAO {
    
    private final static Logger logger = LogManager.getLogger(MySQLSampleSetDAO.class.getName());
    
    NamedParameterJdbcTemplate template;
    
    private static final String SELECT_STATEMENT = "SELECT s.*, gaf.*, gat.*, sl.*, cl.*, fpt.*, e.*, gf.*, st.* " +
            "FROM sample_set s " +
            "LEFT JOIN geological_age gaf ON (gaf.notation = s.from_geological_age_notation) " +
            "LEFT JOIN geological_age gat ON (gat.notation = s.to_geological_age_notation) " +
            "LEFT JOIN sample_set_storage_location sssl ON (sssl.sample_set_id = s.id) " +
            "LEFT JOIN storage_location sl ON (sl.id = sssl.storage_location_id) " +
            "LEFT JOIN sample_set_collection_location sscl ON (sscl.sample_set_id = s.id) " +
            "LEFT JOIN collection_location cl ON (cl.id = sscl.collection_location_id) " +
            "LEFT JOIN sample_set_fossil_preservation_type ssfpt ON (ssfpt.sample_set_id = s.id) " +
            "LEFT JOIN fossil_preservation_type fpt ON (fpt.id = ssfpt.fossil_preservation_type_id) " +
            "LEFT JOIN sample_set_environment sse ON (sse.sample_set_id = s.id) " +
            "LEFT JOIN environment e ON (e.id = sse.environment_id) " +
            "LEFT JOIN sample_set_geological_formation ssgf ON (ssgf.sample_set_id = s.id) " +
            "LEFT JOIN geological_formation gf ON (e.id = ssgf.geological_formation_id) " +
            "LEFT JOIN sample_set_specimen_type ssst ON (ssst.sample_set_id = s.id) " +
            "LEFT JOIN specimen_type st ON (st.id = ssst.specimen_type_id) ";
    
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
    
    @Override
    public List<SampleSetTO> find(GeologicalAgeTO fromGeoAgeTO, GeologicalAgeTO toGeoAgeTO, String location) {
        String sql = "SELECT s.*, gaf.*, gat.*, cl.* " +
                "FROM sample_set s " +
                "INNER JOIN geological_age gaf ON (gaf.notation = s.from_geological_age_notation) " +
                "INNER JOIN geological_age gat ON (gat.notation = s.to_geological_age_notation) " +
                "INNER JOIN sample_set_collection_location sscl ON (sscl.sample_set_id = s.id) " +
                "INNER JOIN collection_location cl ON (cl.id = sscl.collection_location_id) " +
                "WHERE gaf.notation = ':gaf' " +
                "AND gat.notation = ':got' " +
                "AND cl.name = ':location' ";
        MapSqlParameterSource source = new MapSqlParameterSource()
                .addValue("gaf", fromGeoAgeTO.getNotation())
                .addValue("gat", toGeoAgeTO.getNotation())
                .addValue("location", location);
        return template.query(sql, source, new SampleSetResultSetExtractor());
    }
    
    @Override
    public int insert(SampleSetTO sampleSetTO) {
        int[] ints = batchUpdate(Collections.singleton(sampleSetTO));
        return ints[0];
    }
    
    @Override
    public int[] batchUpdate(Set<SampleSetTO> sampleSetTOs) {
        String insertStmt = "INSERT INTO sample_set (id, museum_accessions, from_geological_age_notation," +
                " to_geological_age_notation, specimen_count, is_fossil, is_captive, biozone) " +
                "VALUES (:id, :museum_accessions, :from_geological_age_notation, :to_geological_age_notation, " +
                ":specimen_count, :is_fossil, :is_captive, :biozone) " +
                "AS new " +
                "ON DUPLICATE KEY UPDATE museum_accessions = new.museum_accessions, " +
                "from_geological_age_notation = new.from_geological_age_notation, " +
                "to_geological_age_notation = new.to_geological_age_notation, " +
                "specimen_count = new.specimen_count, is_fossil = new.is_fossil, is_captive = new.is_captive, " +
                "biozone = new.biozone";
        List<MapSqlParameterSource> params = new ArrayList<>();
        for (SampleSetTO sampleSetTO : sampleSetTOs) {
            String storageAccessions = null;
            if (sampleSetTO.getStorageAccessions() != null) {
                storageAccessions = String.join(";", sampleSetTO.getStorageAccessions());
            }
            MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue("id", sampleSetTO.getId());
            source.addValue("museum_accessions", storageAccessions);
            source.addValue("from_geological_age_notation", sampleSetTO.getFromGeologicalAgeTO().getNotation());
            source.addValue("to_geological_age_notation", sampleSetTO.getToGeologicalAgeTO().getNotation());
            source.addValue("specimen_count", sampleSetTO.getSpecimenCount());
            source.addValue("is_fossil", sampleSetTO.isFossil());
            source.addValue("is_captive", sampleSetTO.isCaptive());
            source.addValue("biozone", sampleSetTO.getBiozone());
            params.add(source);
        }
        int[] ints = template.batchUpdate(insertStmt, params.toArray(MapSqlParameterSource[]::new));
        logger.info(Arrays.stream(ints).sum()+ " updated row(s) in 'sample_set' table.");
    
        insertInOtherTable(sampleSetTOs, SampleSetTO::getStorageLocationNames,
                "storage_location", "sample_set_storage_location", "storage_location_id",
                new MySQLStorageLocationDAO.StorageLocationRowMapper());
        insertInOtherTable(sampleSetTOs, SampleSetTO::getCollectionLocationNames,
                "collection_location", "sample_set_collection_location", "collection_location_id",
                new CollectionLocationRowMapper());
        insertInOtherTable(sampleSetTOs, SampleSetTO::getGeologicalFormations,
                "geological_formation", "sample_set_geological_formation", "geological_formation_id",
                new GeologicalFormationRowMapper());
    
        insertInAssociationTable(sampleSetTOs, SampleSetTO::getFossilPreservationTypes,
                "fossil_preservation_type", "sample_set_fossil_preservation_type",
                "fossil_preservation_type_id",
                new FossilPreservationTypeRowMapper());
        insertInAssociationTable(sampleSetTOs, SampleSetTO::getEnvironments,
                "environment", "sample_set_environment", "environment_id",
                new EnvironmentRowMapper());
        insertInAssociationTable(sampleSetTOs, SampleSetTO::getSpecimenTypes,
                "specimen_type", "sample_set_specimen_type", "specimen_type_id",
                new SpecimenTypeRowMapper());
    
        return ints;
    }
    
    private <T extends NamedEntityTO> void insertInOtherTable(
            Set<SampleSetTO> sampleSetTOs, Function<SampleSetTO, Set<String>> func,
            String otherTableName, String associationTableName, String associationFieldName,
            RowMapper<T> mapper) {
        
        String stmt = "INSERT INTO " + otherTableName + " (name) VALUES (:name) AS new " +
                "ON DUPLICATE KEY UPDATE name = new.name ";
        
        Set<MapSqlParameterSource> params = new HashSet<>();
        for (SampleSetTO sampleSetTO : sampleSetTOs) {
            Set<String> values = func.apply(sampleSetTO);
            if (values == null || values.isEmpty()) {
                continue;
            }
            for (String name: values) {
                if (name != null) {
                    MapSqlParameterSource source = new MapSqlParameterSource();
                    source.addValue("name", name);
                    params.add(source);
                }
            }
        }
        if (params.isEmpty()) {
            return;
        }
        
        int[] ints = template.batchUpdate(stmt, params.toArray(MapSqlParameterSource[]::new));
        logger.info(Arrays.stream(ints).sum()+ " updated row(s) in '" + otherTableName + "'  table.");
    
        insertInAssociationTable(sampleSetTOs, func, otherTableName, associationTableName,
                associationFieldName, mapper);
    }
    
    private <T extends NamedEntityTO> void insertInAssociationTable (
            Set<SampleSetTO> sampleSetTOs, Function<SampleSetTO, Set<String>> func,
            String otherTableName, String associationTableName, String associationFieldName,
            RowMapper<T> mapper) {
        
        Set<String> names = new HashSet<>();
        for (SampleSetTO sampleSetTO : sampleSetTOs) {
            Set<String> apply = func.apply(sampleSetTO);
            if (apply != null) {
                names.addAll(apply);
            }
        }
        if (names.isEmpty()) {
            return;
        }
    
        String sql = "SELECT * FROM " + otherTableName +
                " WHERE name IN (" + names.stream().collect(Collectors.joining("', '", "'", "'")) + ")";
        List<T> namedEntityTOs = template.query(sql, mapper);
  
        if (namedEntityTOs.size() != names.size()) {
            throw new IllegalStateException("Not all names were found with query: SELECT * FROM " + otherTableName +
                    " WHERE name IN (" + names.stream().collect(Collectors.joining("', '", "'", "'")) + ")");
        }
        Map<String, NamedEntityTO> namedEntityTOsByName =
                namedEntityTOs.stream()
                              .collect(Collectors.toMap(NamedEntityTO::getName, Function.identity()));
        
        String stmt = "INSERT INTO " + associationTableName + " (sample_set_id, " + associationFieldName + ") " +
                "VALUES (:sample_set_id, :association_id) " +
                "AS new " +
                "ON DUPLICATE KEY UPDATE sample_set_id = new.sample_set_id ";
        
        List<MapSqlParameterSource> params = new ArrayList<>();
        for (SampleSetTO sampleSetTO : sampleSetTOs) {
            for (String name: func.apply(sampleSetTO)) {
                MapSqlParameterSource source = new MapSqlParameterSource();
                source.addValue("sample_set_id", sampleSetTO.getId());
                source.addValue("association_id", namedEntityTOsByName.get(name).getId());
                params.add(source);
            }
        }
        int[] ints = template.batchUpdate(stmt, params.toArray(MapSqlParameterSource[]::new));
        logger.info(Arrays.stream(ints).sum()+ " updated row(s) in '" + associationTableName + "' table.");
    }
    
    @Override
    public Integer getLastId() {
        String sql = "SELECT id FROM sample_set ORDER BY id DESC LIMIT 1";
        try {
            return template.queryForObject(sql, new MapSqlParameterSource(), Integer.class);
        } catch (EmptyResultDataAccessException e) {
            logger.debug("No record found in 'sample_set' table");
            return 0;
        }
    }
    
    private static class SampleSetResultSetExtractor implements ResultSetExtractor<List<SampleSetTO>> {
        
        @Override
        public List<SampleSetTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Integer, SampleSetTO> sampleSets = new HashMap<>();
            while(rs.next()) {
                Integer sampleSetId = rs.getInt("s.id");
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
                        DAO.mapToGeologicalAgeTO(rs, "gat"), rs.getInt("s.specimen_count"),
                        rs.getBoolean("s.is_fossil"), rs.getBoolean("s.is_captive"), museumAccessions,
                        slNames, clNames, fptNames, eNames, gfNames, stNames, rs.getString("s.biozone"));
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
    
    private static class CollectionLocationRowMapper implements RowMapper<CollectionLocationTO> {
        @Override
        public CollectionLocationTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new CollectionLocationTO(rs.getInt("id"), rs.getString("name"));
        }
    }
    
    private static class GeologicalFormationRowMapper implements RowMapper<GeologicalFormationTO> {
        @Override
        public GeologicalFormationTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new GeologicalFormationTO(rs.getInt("id"), rs.getString("name"));
        }
    }
    
    private static class FossilPreservationTypeRowMapper implements RowMapper<FossilPreservationTypeTO> {
        @Override
        public FossilPreservationTypeTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new FossilPreservationTypeTO(rs.getInt("id"), rs.getString("name"));
        }
    }
    
    private static class SpecimenTypeRowMapper implements RowMapper<SpecimenTypeTO> {
        @Override
        public SpecimenTypeTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new SpecimenTypeTO(rs.getInt("id"), rs.getString("name"));
        }
    }
    
    private static class EnvironmentRowMapper implements RowMapper<EnvironmentTO> {
        @Override
        public EnvironmentTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new EnvironmentTO(rs.getInt("id"), rs.getString("name"));
        }
    }
}
