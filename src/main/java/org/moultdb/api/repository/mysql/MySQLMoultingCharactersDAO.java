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
            "LEFT JOIN mc_life_mode mclm ON (mc.id = mclm.mc_id) " +
            "LEFT JOIN life_mode lm ON (mclm.life_mode_id = lm.id) " +
            "LEFT JOIN mc_suture_location mccl ON (mc.id = mccl.mc_id = mc.id) " +
            "LEFT JOIN suture_location sl ON (sl.id = mccl.suture_location_id) " +
            "LEFT JOIN mc_cephalic_suture_location mccsl ON mccsl.mc_id = mc.id " +
            "LEFT JOIN cephalic_suture_location csl ON mccsl.cephalic_suture_location_id = csl.id " +
            "LEFT JOIN mc_post_cephalic_suture_location mcpcsl ON mcpcsl.mc_id = mc.id " +
            "LEFT JOIN post_cephalic_suture_location pcsl ON mcpcsl.post_cephalic_suture_location_id = pcsl.id " +
            "LEFT JOIN mc_resulting_named_moulting_configuration mcrnmc ON mcrnmc.mc_id = mc.id " +
            "LEFT JOIN resulting_named_moulting_configuration rnmc ON mcrnmc.resulting_named_moulting_configuration_id = rnmc.id " +
            "LEFT JOIN mc_egress_direction mced ON (mc.id = mced.mc_id) " +
            "LEFT JOIN egress_direction ed ON (mced.egress_direction_id = ed.id) " +
            "LEFT JOIN mc_exuviae_position mcep ON (mc.id = mcep.mc_id) " +
            "LEFT JOIN exuviae_position ep ON (mcep.egress_direction_id = ep.id) " +
            "LEFT JOIN mc_heavy_metal_reinforcement mchmr ON (mc.id = mchmr.mc_id) " +
            "LEFT JOIN heavy_metal_reinforcement mchmr ON (mchmr.heavy_metal_reinforcement_id = hmr.id) " +
            "LEFT JOIN mc_other_behaviour mcob ON mcob.mc_id = mc.id " +
            "LEFT JOIN other_behaviour ob ON mcob.other_behaviour_id = ob.id " +
            "LEFT JOIN mc_fossil_exuviae_quality mcfeq ON mcfeq.mc_id = mc.id " +
            "LEFT JOIN fossil_exuviae_quality feq ON mcfeq.fossil_exuviae_quality_id = feq.id ";
    
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
                "body_length_average, body_width_average, body_length_increase_average, body_width_increase_average," +
                " body_mass_increase_average, dev_stage_period, intermoult_period, premoult_period, postmoult_period, " +
                "variation_within_cohorts, moulting_phase, moulting_variability, calcification_event, " +
                "exuviae_consumed, exoskeletal_reabsorption, general_comments) " +
                "VALUES (:id, :life_history_style, :juvenile_moult_count, :major_morphological_transition_count, " +
                ":terminal_adult_stage, :observed_moult_stage_count, :estimated_moult_count, " +
                "(SELECT id FROM segment_addition_mode WHERE name = :segment_addition_mode_id), " +
                ":body_segment_count, :body_segment_count_in_adults, " +
                ":body_length_average, :body_width_average, :body_length_increase_average, :body_width_increase_average, " +
                ":body_mass_increase_average, :dev_stage_period, :intermoult_period, :premoult_period, :postmoult_period, " +
                ":variation_within_cohorts, :moulting_phase, :moulting_variability, " +
                ":calcification_event, :exuviae_consumed, :exoskeletal_reabsorption, :general_comments) " +
                "AS new " +
                "ON DUPLICATE KEY UPDATE life_history_style = new.life_history_style, " +
                "juvenile_moult_count = new.juvenile_moult_count, " +
                "major_morphological_transition_count = new.major_morphological_transition_count, " +
                "terminal_adult_stage = new.terminal_adult_stage, observed_moult_stage_count = new.observed_moult_stage_count, " +
                "estimated_moult_count = new.estimated_moult_count, segment_addition_mode_id = new.segment_addition_mode_id, " +
                "body_segment_count = new.body_segment_count, body_segment_count_in_adults = new.body_segment_count_in_adults, " +
                "body_length_average = new.body_length_average, body_width_average = new.body_width_average, " +
                "body_length_increase_average = new.body_length_increase_average, " +
                "body_width_increase_average = new.body_width_increase_average, " +
                "body_mass_increase_average = new.body_mass_increase_average, dev_stage_period = new.dev_stage_period, " +
                "intermoult_period = new.intermoult_period, premoult_period = new.premoult_period, " +
                "postmoult_period = new.postmoult_period, variation_within_cohorts = new.variation_within_cohorts, " +
                "moulting_phase = new.moulting_phase, moulting_variability = new.moulting_variability, " +
                "calcification_event = new.calcification_event, exuviae_consumed = new.exuviae_consumed, " +
                "exoskeletal_reabsorption = new.exoskeletal_reabsorption, general_comments = new.general_comments";
        
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
            mcSource.addValue("body_width_average", mcTO.getBodyWidthAverage());
            mcSource.addValue("body_length_increase_average", mcTO.getBodyLengthIncreaseAverage());
            mcSource.addValue("body_width_increase_average", mcTO.getBodyWidthIncreaseAverage());
            mcSource.addValue("body_mass_increase_average", mcTO.getBodyMassIncreaseAverage());
            mcSource.addValue("dev_stage_period", mcTO.getDevStagePeriod());
            mcSource.addValue("intermoult_period", mcTO.getIntermoultPeriod());
            mcSource.addValue("premoult_period", mcTO.getPremoultPeriod());
            mcSource.addValue("postmoult_period", mcTO.getPostmoultPeriod());
            mcSource.addValue("variation_within_cohorts", mcTO.getVariationWithinCohorts());
            mcSource.addValue("moulting_phase", mcTO.getMoultingPhase());
            mcSource.addValue("moulting_variability", mcTO.getMoultingVariability());
            mcSource.addValue("calcification_event", mcTO.getCalcificationEvent());
            mcSource.addValue("exuviae_consumed", mcTO.getExuviaeConsumed());
            mcSource.addValue("exoskeletal_reabsorption", mcTO.getExoskeletalMaterialReabsorption());
            mcSource.addValue("general_comments", mcTO.getGeneralComments());
            params.add(mcSource);
        }
        template.batchUpdate(insertStmt, params.toArray(MapSqlParameterSource[]::new));
        logger.info("'moulting_characters' table updated");
        
        insertInAssociationTable(moultingCharactersTOs, MoultingCharactersTO::getLifeModes,
                "life_mode", "mc_life_mode", "life_mode_id",
                new LifeModeRowMapper());
        insertInAssociationTable(moultingCharactersTOs, MoultingCharactersTO::getSutureLocations,
                "suture_location", "mc_suture_location", "suture_location_id",
                new SutureLocationRowMapper());
        insertInAssociationTable(moultingCharactersTOs, MoultingCharactersTO::getCephalicSutureLocations,
                "cephalic_suture_location", "mc_cephalic_suture_location", "cephalic_suture_location_id",
                new CephalicSutureRowMapper());
        insertInAssociationTable(moultingCharactersTOs, MoultingCharactersTO::getPostCephalicSutureLocations,
                "post_cephalic_suture_location", "mc_post_cephalic_suture_location", "post_cephalic_suture_location_id",
                new PostCephalicSutureRowMapper());
        insertInAssociationTable(moultingCharactersTOs, MoultingCharactersTO::getResultingNamedMoultingConfigurations,
                "resulting_named_moulting_configuration", "mc_resulting_named_moulting_configuration", "resulting_named_moulting_configuration_id",
                new ResultingNamedMoultingConfigurationRowMapper());
        insertInAssociationTable(moultingCharactersTOs, MoultingCharactersTO::getEgressDirections,
                "egress_direction", "mc_egress_direction", "egress_direction_id",
                new EgressDirectionRowMapper());
        insertInAssociationTable(moultingCharactersTOs, MoultingCharactersTO::getPositionsExuviaeFoundIn,
                "exuviae_position", "mc_exuviae_position", "exuviae_position_id",
                new ExuviaePositionRowMapper());
        insertInAssociationTable(moultingCharactersTOs, MoultingCharactersTO::getHeavyMetalReinforcements,
                "heavy_metal_reinforcement", "mc_heavy_metal_reinforcement", "heavy_metal_reinforcement_id",
                new HeavyMetalReinforcementRowMapper());
        insertInAssociationTable(moultingCharactersTOs, MoultingCharactersTO::getOtherBehaviours,
                "other_behaviour", "mc_other_behaviour", "other_behaviour_id",
                new OtherBehaviourRowMapper());
        insertInAssociationTable(moultingCharactersTOs, MoultingCharactersTO::getFossilExuviaeQualities,
                "fossil_exuviae_quality", "mc_fossil_exuviae_quality", "fossil_exuviae_quality_id",
                new FossilExuviaeQualityRowMapper());
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
                        .collect(Collectors.toMap(t -> t.getName().toLowerCase().replaceAll("'", "\\\\'"), Function.identity()));
        
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
                    source.addValue("association_id", namedEntityTOsByName.get(name.toLowerCase()).getId());
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
                Set<String> sutureLocations = extractNames(rs, "sl.name", mcTO == null ? null : mcTO.getSutureLocations());
                Set<String> cephalicSutureLocations = extractNames(rs, "csl.name", mcTO == null ? null : mcTO.getCephalicSutureLocations());
                Set<String> postCephalicSutureLocations = extractNames(rs, "pcsl.name", mcTO == null ? null : mcTO.getPostCephalicSutureLocations());
                Set<String> resultingNamedMoultingConfigurations = extractNames(rs, "rnmc.name", mcTO == null ? null : mcTO.getResultingNamedMoultingConfigurations());
                Set<String> egressDirections = extractNames(rs, "ed.name", mcTO == null ? null : mcTO.getEgressDirections());
                Set<String> exuviaePositions = extractNames(rs, "ep.name", mcTO == null ? null : mcTO.getPositionsExuviaeFoundIn());
                Set<String> heavyMetalReinforcements = extractNames(rs, "hmr.name", mcTO == null ? null : mcTO.getHeavyMetalReinforcements());
                Set<String> otherBehaviours = extractNames(rs, "ob.name", mcTO == null ? null : mcTO.getOtherBehaviours());
                Set<String> fossilExuviaeQualities = extractNames(rs, "feq.name", mcTO == null ? null : mcTO.getFossilExuviaeQualities());
                
                // Build MoultingCharactersTO. Even if it already exists, we create a new one because it's an unmutable object
                mcTO = new MoultingCharactersTO(rs.getInt("mc.id"), rs.getString("mc.life_history_style"), lifeModes,
                        rs.getString("juvenile_moult_count"), rs.getString("major_morphological_transition_count"),
                        DAO.getBoolean(rs, "mc.terminal_adult_stage"), DAO.getInteger(rs, "mc.observed_moult_stage_count"),
                        rs.getString("mc.estimated_moult_count"), rs.getString("sam.name"),
                        rs.getString("mc.body_segment_count"), rs.getString("mc.body_segment_count_in_adults"),
                        rs.getBigDecimal("mc.body_length_average"), rs.getBigDecimal("mc.body_width_average"),
                        rs.getBigDecimal("mc.body_length_increase_average"), rs.getBigDecimal("mc.body_width_increase_average"),
                        rs.getBigDecimal("mc.body_mass_increase_average"), rs.getString("mc.dev_stage_period"),
                        rs.getString("mc.intermoult_period"), rs.getString("mc.premoult_period"),
                        rs.getString("mc.postmoult_period"), rs.getString("mc.variation_within_cohorts"),
                        sutureLocations, cephalicSutureLocations, postCephalicSutureLocations, resultingNamedMoultingConfigurations,
                        egressDirections, exuviaePositions, rs.getString("mc.moulting_phase"),
                        rs.getString("mc.moulting_variability"), rs.getString("mc.calcification_event"),
                        heavyMetalReinforcements, otherBehaviours, rs.getString("mc.exuviae_consumed"),
                        rs.getString("mc.exoskeletal_reabsorption"), fossilExuviaeQualities, rs.getString("mc.general_comments"));
                mcSet.put(mcId, mcTO);
            }
            return new ArrayList<>(mcSet.values());
        }
        
        // FIXME refactor with SampleSetResultSetExtractor
        private Set<String> extractNames(ResultSet rs, String label, Set<String> previousValues) throws SQLException {
            String value = rs.getString(label);
            Set<String> newValues = new HashSet<>();
            if (previousValues != null && value != null) {
                newValues.addAll(previousValues);
            }
            return newValues;
        }
    }
    
    private static class LifeModeRowMapper implements RowMapper<LifeModeTO> {
        @Override
        public LifeModeTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new LifeModeTO(rs.getInt("id"), rs.getString("name"));
        }
    }
    
    private static class SutureLocationRowMapper implements RowMapper<SutureLocationTO> {
        @Override
        public SutureLocationTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new SutureLocationTO(rs.getInt("id"), rs.getString("name"));
        }
    }
    
    private static class CephalicSutureRowMapper implements RowMapper<CephalicSutureTO> {
        @Override
        public CephalicSutureTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new CephalicSutureTO(rs.getInt("id"), rs.getString("name"));
        }
    }
    
    private static class PostCephalicSutureRowMapper implements RowMapper<PostCephalicSutureTO> {
        @Override
        public PostCephalicSutureTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new PostCephalicSutureTO(rs.getInt("id"), rs.getString("name"));
        }
    }
    
    private static class ResultingNamedMoultingConfigurationRowMapper implements RowMapper<ResultingNamedMoultingConfigurationTO> {
        @Override
        public ResultingNamedMoultingConfigurationTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ResultingNamedMoultingConfigurationTO(rs.getInt("id"), rs.getString("name"));
        }
    }
    
    private static class EgressDirectionRowMapper implements RowMapper<EgressDirectionTO> {
        @Override
        public EgressDirectionTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new EgressDirectionTO(rs.getInt("id"), rs.getString("name"));
        }
    }
    
    private static class ExuviaePositionRowMapper implements RowMapper<ExuviaePositionTO> {
        @Override
        public ExuviaePositionTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ExuviaePositionTO(rs.getInt("id"), rs.getString("name"));
        }
    }
    
    private static class HeavyMetalReinforcementRowMapper implements RowMapper<HeavyMetalReinforcementTO> {
        @Override
        public HeavyMetalReinforcementTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new HeavyMetalReinforcementTO(rs.getInt("id"), rs.getString("name"));
        }
    }
    
    private static class OtherBehaviourRowMapper implements RowMapper<OtherBehaviourTO> {
        @Override
        public OtherBehaviourTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new OtherBehaviourTO(rs.getInt("id"), rs.getString("name"));
        }
    }
    
    private static class FossilExuviaeQualityRowMapper implements RowMapper<FossilExuviaeQualityTO> {
        @Override
        public FossilExuviaeQualityTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new FossilExuviaeQualityTO(rs.getInt("id"), rs.getString("name"));
        }
    }
}