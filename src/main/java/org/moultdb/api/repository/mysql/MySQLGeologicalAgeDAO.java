package org.moultdb.api.repository.mysql;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.repository.dao.GeologicalAgeDAO;
import org.moultdb.api.repository.dto.GeologicalAgeTO;
import org.moultdb.api.repository.dto.TransfertObject;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
@Repository
public class MySQLGeologicalAgeDAO implements GeologicalAgeDAO {
    
    private final static Logger logger = LogManager.getLogger(MySQLGeologicalAgeDAO.class.getName());
    
    NamedParameterJdbcTemplate template;
    
    private final static String SELECT_STATEMENT = "SELECT ga.*, gas.* FROM geological_age ga " +
            "INNER JOIN geological_age_synonym gas ON ga.notation = gas.geological_age_notation ";
    
    public MySQLGeologicalAgeDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public List<GeologicalAgeTO> findAll() {
        return template.query(SELECT_STATEMENT, new GeologicalAgeResultSetExtractor());
    }
    
    @Override
    public GeologicalAgeTO findByNotation(String id) {
        return TransfertObject.getOneTO(findByNotations(Collections.singleton(id)));
    }
    
    @Override
    public List<GeologicalAgeTO> findByNotations(Set<String> notations) {
        if (notations == null || notations.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("A notation can not be null");
        }
        return template.query(SELECT_STATEMENT + "WHERE ga.notation IN (:notations)",
                new MapSqlParameterSource().addValue("notations", notations), new GeologicalAgeResultSetExtractor());
    }
    
    @Override
    public GeologicalAgeTO findByLabelOrSynonym(String label) {
        return TransfertObject.getOneTO(findByLabelsOrSynonyms(Collections.singleton(label)));
    }
    
    @Override
    public List<GeologicalAgeTO> findByLabelsOrSynonyms(Set<String> labels) {
        if (labels == null || labels.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("A label can not be null");
        }
        return template.query(SELECT_STATEMENT + "WHERE ga.name IN (:labels) OR gas.synonym IN (:labels) ",
                new MapSqlParameterSource().addValue("labels", labels), new GeologicalAgeResultSetExtractor());
    }
    
    @Transactional
    @Override
    public Integer batchUpdate(Collection<GeologicalAgeTO> geoAgeTOs) {
        String geoAgeSql = "INSERT INTO geological_age (notation, name, rank_type, younger_bound, " +
                "younger_bound_imprecision, older_bound, older_bound_imprecision) " +
                "VALUES (:notation, :name, :rank_type, :younger_bound, :younger_bound_imprecision," +
                " :older_bound, :older_bound_imprecision) " +
                "AS new " +
                "ON DUPLICATE KEY UPDATE rank_type = new.rank_type, " +
                "younger_bound = new.younger_bound, younger_bound_imprecision = new.younger_bound_imprecision, " +
                "older_bound = new.older_bound, older_bound_imprecision = new.older_bound_imprecision";
        List<MapSqlParameterSource> params = new ArrayList<>();
        for (GeologicalAgeTO geoAgeTO : geoAgeTOs) {
            MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue("notation", geoAgeTO.getNotation());
            source.addValue("name", geoAgeTO.getName());
            source.addValue("rank_type", geoAgeTO.getRank());
            source.addValue("younger_bound", geoAgeTO.getYoungerBound());
            source.addValue("younger_bound_imprecision", geoAgeTO.getYoungerBoundImprecision());
            source.addValue("older_bound", geoAgeTO.getOlderBound());
            source.addValue("older_bound_imprecision", geoAgeTO.getOlderBoundImprecision());
            params.add(source);
        }
        int[] ints = template.batchUpdate(geoAgeSql, params.toArray(MapSqlParameterSource[]::new));
        int newGeoAgeCount = Arrays.stream(ints).sum();
        logger.info(newGeoAgeCount + " new geo age(s) has been inserted.");
    
        String synonymSql = "INSERT INTO geological_age_synonym (geological_age_notation, synonym) " +
                "VALUES (:geological_age_notation, :synonym) " +
                "AS new " +
                "ON DUPLICATE KEY UPDATE synonym = new.synonym";
        params = new ArrayList<>();
        for (GeologicalAgeTO geoAgeTO : geoAgeTOs) {
            for (String s: geoAgeTO.getSynonyms()) {
                MapSqlParameterSource source = new MapSqlParameterSource();
                source.addValue("geological_age_notation", geoAgeTO.getNotation());
                source.addValue("synonym", s);
                params.add(source);
            }
        }
        ints = template.batchUpdate(synonymSql, params.toArray(MapSqlParameterSource[]::new));
    
        logger.info(Arrays.stream(ints).sum() + " new synonym(s) has been inserted.");
        
        return newGeoAgeCount;
    }
    
    private static class GeologicalAgeResultSetExtractor implements ResultSetExtractor<List<GeologicalAgeTO>> {
        
        @Override
        public List<GeologicalAgeTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<String, GeologicalAgeTO> geologicalAges = new HashMap<>();
            while(rs.next()) {
                String geologicalAgeNotation = rs.getString("ga.notation");
                GeologicalAgeTO geologicalAgeTO = geologicalAges.get(geologicalAgeNotation);
                
                // Build synonyms
                String synonym = rs.getString("gas.synonym");
                Set<String> synonyms = geologicalAgeTO == null ? null: geologicalAgeTO.getSynonyms();
                if (synonyms == null) {
                    synonyms = new HashSet<>();
                }
                synonyms.add(synonym);
    
                // Build GeologicalAgeTO. Even if it already exists, we create a new one because it's an unmutable object
                geologicalAgeTO = new GeologicalAgeTO(rs.getString("ga.notation"), rs.getString("ga.name"), rs.getString("ga.rank_type"),
                        rs.getBigDecimal("ga.younger_bound"), rs.getBigDecimal("ga.younger_bound_imprecision"),
                        rs.getBigDecimal("ga.older_bound"), rs.getBigDecimal("ga.older_bound_imprecision"), synonyms);
                geologicalAges.put(geologicalAgeNotation, geologicalAgeTO);
            }
            return new ArrayList<>(geologicalAges.values());
        }
    }
}
