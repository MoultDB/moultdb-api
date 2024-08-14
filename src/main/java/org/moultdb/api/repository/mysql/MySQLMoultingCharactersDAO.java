package org.moultdb.api.repository.mysql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.repository.dao.DAO;
import org.moultdb.api.repository.dao.MoultingCharactersDAO;
import org.moultdb.api.repository.dto.*;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
@Repository
public class MySQLMoultingCharactersDAO implements MoultingCharactersDAO {
    
    private final static Logger logger = LogManager.getLogger(MySQLMoultingCharactersDAO.class.getName());
    
    NamedParameterJdbcTemplate template;
    
    private static final String SELECT_STATEMENT = "SELECT * FROM moulting_characters mc " +
            "LEFT JOIN segment_addition_mode sam ON mc.segment_addition_mode_id = sam.id " +
            "LEFT JOIN suture_location sl ON mc.suture_location_id = sl.id " +
            "LEFT JOIN cephalic_suture_location csl ON mc.cephalic_suture_location_id = csl.id " +
            "LEFT JOIN post_cephalic_suture_location pcsl ON mc.post_cephalic_suture_location_id = pcsl.id " +
            "LEFT JOIN resulting_named_moulting_configuration rnmc ON mc.resulting_named_moulting_configuration_id = rnmc.id " +
            "LEFT JOIN other_behaviour ob ON mc.other_behaviour_id = ob.id " +
            "LEFT JOIN fossil_exuviae_quality feq ON mc.fossil_exuviae_quality_id = feq.id ";
    
    public MySQLMoultingCharactersDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public List<MoultingCharactersTO> findAll() {
        return template.query(SELECT_STATEMENT, new MoultingCharactersResultSetExtractor());
    }
    
    @Override
    public MoultingCharactersTO findById(int id) {
        return TransfertObject.getOneTO(findByIds(Collections.singleton(id)));
    }
    
    @Override
    public List<MoultingCharactersTO> findByIds(Set<Integer> ids) {
        return template.query(SELECT_STATEMENT + " WHERE mc.id IN (:ids)",
                new MapSqlParameterSource().addValue("ids", ids), new MoultingCharactersResultSetExtractor());
    }
    
    @Override
    public Integer getLastId() {
        String sql = "SELECT id FROM moulting_characters ORDER BY id DESC LIMIT 1";
        try {
            return template.queryForObject(sql, new MapSqlParameterSource(), Integer.class);
        } catch (EmptyResultDataAccessException e) {
            logger.debug("No record found in 'moulting_characters' table");
            return 0;
        }
    }
    
    @Override
    public void insert(MoultingCharactersTO moultingCharactersTO) {
        batchUpdate(Collections.singleton(moultingCharactersTO));
    }
    
    @Override
    public void batchUpdate(Set<MoultingCharactersTO> moultingCharactersTOs) {
        
        String insertStmt = "INSERT INTO moulting_characters (id, life_history_style, juvenile_moult_count, " +
                "major_morphological_transition_count, terminal_adult_stage, observed_moult_stage_count, " +
                "estimated_moult_count, segment_addition_mode_id, body_segment_count, body_segment_count_in_adults, " +
                "body_length_average, body_length_increase_average, body_mass_increase_average,  " +
                "intermoult_period, premoult_period, postmoult_period, variation_within_cohorts, " +
                "suture_location_id, cephalic_suture_location_id, post_cephalic_suture_location_id, " +
                "resulting_named_moulting_configuration_id, egress_direction, position_exuviae_found_in, moulting_phase, " +
                "moulting_variability, calcification_event, heavy_metal_reinforcement, other_behaviour_id, " +
                "exuviae_consumed, exoskeletal_reabsorption, fossil_exuviae_quality_id, general_comments) " +
                "VALUES (:id, :life_history_style, :juvenile_moult_count, :major_morphological_transition_count, " +
                ":terminal_adult_stage, :observed_moult_stage_count, :estimated_moult_count, " +
                "(SELECT id FROM segment_addition_mode WHERE name = :segment_addition_mode_id), " +
                ":body_segment_count, :body_segment_count_in_adults, " +
                ":body_length_average, :body_length_increase_average, :body_mass_increase_average, " +
                ":intermoult_period, :premoult_period, :postmoult_period, :variation_within_cohorts, " +
                "(SELECT id FROM suture_location WHERE name = :suture_location_id), " +
                "(SELECT id FROM cephalic_suture_location WHERE name = :cephalic_suture_location_id), " +
                "(SELECT id FROM post_cephalic_suture_location WHERE name = :post_cephalic_suture_location_id), " +
                "(SELECT id FROM resulting_named_moulting_configuration WHERE name = :resulting_named_moulting_configuration_id), " +
                ":egress_direction, :position_exuviae_found_in, :moulting_phase, :moulting_variability, " +
                ":calcification_event, :heavy_metal_reinforcement, " +
                "(SELECT id FROM other_behaviour WHERE name = :other_behaviour_id), " +
                ":exuviae_consumed, :exoskeletal_reabsorption, " +
                "(SELECT id FROM fossil_exuviae_quality WHERE name = :fossil_exuviae_quality_id), " +
                ":general_comments) " +
                "AS new " +
                "ON DUPLICATE KEY UPDATE life_history_style = new.life_history_style, " +
                "juvenile_moult_count = new.juvenile_moult_count, " +
                "major_morphological_transition_count = new.major_morphological_transition_count, " +
                "terminal_adult_stage = new.terminal_adult_stage, observed_moult_stage_count = new.observed_moult_stage_count, " +
                "estimated_moult_count = new.estimated_moult_count, segment_addition_mode_id = new.segment_addition_mode_id, " +
                "body_segment_count = new.body_segment_count, body_segment_count_in_adults = new.body_segment_count_in_adults, " +
                "body_length_average = new.body_length_average, body_length_increase_average = new.body_length_increase_average, " +
                "body_mass_increase_average = new.body_mass_increase_average, " +
                "intermoult_period = new.intermoult_period, premoult_period = new.premoult_period, " +
                "postmoult_period = new.postmoult_period, variation_within_cohorts = new.variation_within_cohorts, " +
                "suture_location_id = new.suture_location_id, cephalic_suture_location_id = new.cephalic_suture_location_id, " +
                "post_cephalic_suture_location_id = new.post_cephalic_suture_location_id, " +
                "resulting_named_moulting_configuration_id = new.resulting_named_moulting_configuration_id, " +
                "egress_direction = new.egress_direction, position_exuviae_found_in = new.position_exuviae_found_in, " +
                "moulting_phase = new.moulting_phase, moulting_variability = new.moulting_variability, " +
                "calcification_event = new.calcification_event, heavy_metal_reinforcement = new.heavy_metal_reinforcement, " +
                "other_behaviour_id = new.other_behaviour_id, exuviae_consumed = new.exuviae_consumed, " +
                "exoskeletal_reabsorption = new.exoskeletal_reabsorption, fossil_exuviae_quality_id = new.fossil_exuviae_quality_id," +
                "general_comments = new.general_comments";
        
        List<MapSqlParameterSource> params = new ArrayList<>();
        for (MoultingCharactersTO mcTO : moultingCharactersTOs) {
            MapSqlParameterSource mcSource = new MapSqlParameterSource();
            mcSource.addValue("id", mcTO.getId());
            mcSource.addValue("life_history_style", mcTO.getLifeHistoryStyle());
            mcSource.addValue("juvenile_moult_count", mcTO.getJuvenileMoultCount());
            mcSource.addValue("major_morphological_transition_count", mcTO.getMajorMorphologicalTransitionCount());
            mcSource.addValue("terminal_adult_stage", mcTO.getHasTerminalAdultStage());
            mcSource.addValue("observed_moult_stage_count", mcTO.getObservedMoultStageCount());
            mcSource.addValue("estimated_moult_count", mcTO.getEstimatedMoultStageCount());
            mcSource.addValue("segment_addition_mode_id", mcTO.getSegmentAdditionMode());
            mcSource.addValue("body_segment_count", mcTO.getBodySegmentCount());
            mcSource.addValue("body_segment_count_in_adults", mcTO.getBodySegmentCountInAdults());
            mcSource.addValue("body_length_average", mcTO.getBodyLengthAverage());
            mcSource.addValue("body_length_increase_average", mcTO.getBodyLengthIncreaseAverage());
            mcSource.addValue("body_mass_increase_average", mcTO.getBodyMassIncreaseAverage());
            mcSource.addValue("intermoult_period", mcTO.getIntermoultPeriod());
            mcSource.addValue("premoult_period", mcTO.getPremoultPeriod());
            mcSource.addValue("postmoult_period", mcTO.getPostmoultPeriod());
            mcSource.addValue("variation_within_cohorts", mcTO.getVariationWithinCohorts());
            mcSource.addValue("suture_location_id", mcTO.getSutureLocation());
            mcSource.addValue("cephalic_suture_location_id", mcTO.getCephalicSutureLocation());
            mcSource.addValue("post_cephalic_suture_location_id", mcTO.getPostCephalicSutureLocation());
            mcSource.addValue("resulting_named_moulting_configuration_id", mcTO.getResultingNamedMoultingConfiguration());
            mcSource.addValue("egress_direction", mcTO.getEgressDirection());
            mcSource.addValue("position_exuviae_found_in", mcTO.getPositionExuviaeFoundIn());
            mcSource.addValue("moulting_phase", mcTO.getMoultingPhase());
            mcSource.addValue("moulting_variability", mcTO.getMoultingVariability());
            mcSource.addValue("calcification_event", mcTO.getCalcificationEvent());
            mcSource.addValue("heavy_metal_reinforcement", mcTO.getHeavyMetalReinforcement());
            mcSource.addValue("other_behaviour_id", mcTO.getOtherBehaviour());
            mcSource.addValue("exuviae_consumed", mcTO.getExuviaeConsumed());
            mcSource.addValue("exoskeletal_reabsorption", mcTO.getExoskeletalMaterialReabsorption());
            mcSource.addValue("fossil_exuviae_quality_id", mcTO.getFossilExuviaeQuality());
            mcSource.addValue("general_comments", mcTO.getGeneralComments());
            params.add(mcSource);
        }
        template.batchUpdate(insertStmt, params.toArray(MapSqlParameterSource[]::new));
        logger.info("'moulting_characters' table updated");
        
        insertInAssociationTable(moultingCharactersTOs, MoultingCharactersTO::getLifeModes,
                "life_mode", "mc_life_mode", "life_mode_id",
                new LifeModeRowMapper());
    }
    
    private <T extends NamedEntityTO> void insertInAssociationTable (
            Set<MoultingCharactersTO> mcTOs, Function<MoultingCharactersTO, Set<String>> func,
            String otherTableName, String associationTableName, String associationFieldName,
            RowMapper<T> mapper) {
        
        Set<String> names = new HashSet<>();
        for (MoultingCharactersTO mcTO : mcTOs) {
            Set<String> apply = func.apply(mcTO);
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
            throw new IllegalStateException("Not all names " + names.stream().collect(Collectors.joining(", ", "[", "]"))
                    + " were found in db " + namedEntityTOs.stream().map(NamedEntityTO::getName).collect(Collectors.joining(", ", "[", "]")) +
                    ": " + sql);
        }
        Map<String, NamedEntityTO> namedEntityTOsByName =
                namedEntityTOs.stream()
                        .collect(Collectors.toMap(NamedEntityTO::getName, Function.identity()));
        
        String stmt = "INSERT INTO " + associationTableName + " (mc_id, " + associationFieldName + ") " +
                "VALUES (:mc_id, :association_id) " +
                "AS new " +
                "ON DUPLICATE KEY UPDATE mc_id = new.mc_id ";
        
        List<MapSqlParameterSource> params = new ArrayList<>();
        for (MoultingCharactersTO mcTO : mcTOs) {
            Set<String> apply = func.apply(mcTO);
            if (apply != null) {
                for (String name: apply) {
                    MapSqlParameterSource source = new MapSqlParameterSource();
                    source.addValue("mc_id", mcTO.getId());
                    source.addValue("association_id", namedEntityTOsByName.get(name).getId());
                    params.add(source);
                }
            }
        }
        template.batchUpdate(stmt, params.toArray(MapSqlParameterSource[]::new));
        logger.info("'" + associationTableName + "' table updated");
    }
    
    private static class MoultingCharactersResultSetExtractor implements ResultSetExtractor<List<MoultingCharactersTO>> {
        
        @Override
        public List<MoultingCharactersTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Integer, MoultingCharactersTO> mcSet = new HashMap<>();
            while (rs.next()) {
                Integer mcId = rs.getInt("mc.id");
                MoultingCharactersTO mcTO = mcSet.get(mcId);
                
                Set<String> lifeModes = extractNames(rs, "lm.name", mcTO == null ? null : mcTO.getLifeModes());
                
                // Build MoultingCharactersTO. Even if it already exists, we create a new one because it's an unmutable object
                mcTO = new MoultingCharactersTO(rs.getInt("mc.id"), rs.getString("mc.life_history_style"),
                        lifeModes, rs.getString("juvenile_moult_count"),
                        DAO.getInteger(rs, "major_morphological_transition_count"), DAO.getBoolean(rs, "mc.terminal_adult_stage"),
                        DAO.getInteger(rs, "mc.observed_moult_stage_count"), DAO.getInteger(rs, "mc.estimated_moult_count"), rs.getString("sam.name"),
                        rs.getString("mc.body_segment_count"), rs.getString("mc.body_segment_count_in_adults"),
                        rs.getBigDecimal("mc.body_length_average"), rs.getBigDecimal("mc.body_length_increase_average"),
                        rs.getBigDecimal("mc.body_mass_increase_average"), rs.getString("mc.intermoult_period"),
                        rs.getString("mc.premoult_period"), rs.getString("mc.postmoult_period"), rs.getString("mc.variation_within_cohorts"),
                        rs.getString("sl.name"), rs.getString("csl.name"), rs.getString("pcsl.name"), rs.getString("rnmc.name"),
                        rs.getString("mc.egress_direction"), rs.getString("mc.position_exuviae_found_in"), rs.getString("mc.moulting_phase"),
                        rs.getString("mc.moulting_variability"), rs.getString("mc.calcification_event"),
                        rs.getString("mc.heavy_metal_reinforcement"), rs.getString("ob.name"), rs.getString("mc.exuviae_consumed"),
                        rs.getString("mc.exoskeletal_reabsorption"), rs.getString("feq.name"), rs.getString("mc.general_comments"));
                mcSet.put(mcId, mcTO);
            }
            return new ArrayList<>(mcSet.values());
        }
        
        // FIXME refactor with SampleSetResultSetExtractor
        private Set<String> extractNames(ResultSet rs, String label, Set<String> previousValues) throws SQLException {
            String value = rs.getString(label);
            if (previousValues == null) {
                previousValues = new HashSet<>();
            }
            if (value != null) {
                previousValues.add(value);
            }
            return previousValues;
        }
    }
    
    private static class LifeModeRowMapper implements RowMapper<LifeModeTO> {
        @Override
        public LifeModeTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new LifeModeTO(rs.getInt("id"), rs.getString("name"));
        }
    }
}