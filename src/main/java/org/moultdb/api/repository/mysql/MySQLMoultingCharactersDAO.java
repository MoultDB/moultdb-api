package org.moultdb.api.repository.mysql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.repository.dao.MoultingCharactersDAO;
import org.moultdb.api.repository.dto.MoultingCharactersTO;
import org.moultdb.api.repository.dto.TransfertObject;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
public class MySQLMoultingCharactersDAO implements MoultingCharactersDAO {
    
    private final static Logger logger = LogManager.getLogger(MySQLMoultingCharactersDAO.class.getName());
    
    NamedParameterJdbcTemplate template;
    
    private static final String SELECT_STATEMENT = "SELECT * FROM moulting_characters gmc " +
            "INNER JOIN segment_addition_mode sam ON gmc.segment_addition_mode_id = sam.id " +
            "INNER JOIN suture_location sl ON gmc.suture_location_id = sl.id " +
            "INNER JOIN cephalic_suture_location csl ON gmc.cephalic_suture_location_id = csl.id " +
            "INNER JOIN post_cephalic_suture_location pcsl ON gmc.post_cephalic_suture_location_id = pcsl.id " +
            "INNER JOIN resulting_named_moulting_configuration rnmc ON gmc.resulting_named_moulting_configuration_id = rnmc.id " +
            "INNER JOIN other_behaviour ob ON gmc.other_behaviour_id = ob.id " +
            "INNER JOIN fossil_exuviae_quality feq ON gmc.fossil_exuviae_quality_id = feq.id ";
    
    public MySQLMoultingCharactersDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public List<MoultingCharactersTO> findAll() {
        return template.query(SELECT_STATEMENT, new GeneralMoultingCharactersRowMapper());
    }
    
    @Override
    public MoultingCharactersTO findById(int id) {
        return TransfertObject.getOneTO(findByIds(Collections.singleton(id)));
    }
    
    @Override
    public List<MoultingCharactersTO> findByIds(Set<Integer> ids) {
        return template.query(SELECT_STATEMENT + " WHERE gmc.id IN (:ids)",
                new MapSqlParameterSource().addValue("ids", ids), new GeneralMoultingCharactersRowMapper());
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
    public int insert(MoultingCharactersTO moultingCharactersTO) {
        int[] ints = batchUpdate(Collections.singleton(moultingCharactersTO));
        return ints[0];
    }
    
    @Transactional
    @Override
    public int[] batchUpdate(Set<MoultingCharactersTO> moultingCharactersTOs) {
    
        String insertStmt = "INSERT INTO moulting_characters (id, life_history_style, terminal_adult_stage, observed_moult_stage_count, " +
                "estimated_moult_count, specimen_count, segment_addition_mode_id, body_segments_count_per_moult_stage, " +
                "body_segments_count_in_adults, body_length_average, body_length_increase_average_per_moult, " +
                "measurement_unit, suture_location_id, cephalic_suture_location_id, post_cephalic_suture_location_id, " +
                "resulting_named_moulting_configuration_id, egress_direction, position_exuviae_found_in, moulting_phase, " +
                "moulting_variability, other_behaviour_id, " +
                "exuviae_consumed, exoskeletal_reabsorption, fossil_exuviae_quality_id) " +
                "VALUES (:id, :life_history_style, :terminal_adult_stage, :observed_moult_stage_count, :estimated_moult_count, " +
                ":specimen_count, " +
                "(SELECT id FROM segment_addition_mode WHERE name = :segment_addition_mode_id), " +
                ":body_segments_count_per_moult_stage, :body_segments_count_in_adults, " +
                ":body_length_average, :body_length_increase_average_per_moult, :measurement_unit, " +
                "(SELECT id FROM suture_location WHERE name = :suture_location_id), " +
                "(SELECT id FROM cephalic_suture_location WHERE name = :cephalic_suture_location_id), " +
                "(SELECT id FROM post_cephalic_suture_location WHERE name = :post_cephalic_suture_location_id), " +
                "(SELECT id FROM resulting_named_moulting_configuration WHERE name = :resulting_named_moulting_configuration_id), " +
                ":egress_direction, :position_exuviae_found_in, :moulting_phase, :moulting_variability, " +
                "(SELECT id FROM other_behaviour WHERE name = :other_behaviour_id), " +
                ":exuviae_consumed, :exoskeletal_reabsorption, " +
                "(SELECT id FROM fossil_exuviae_quality WHERE name = :fossil_exuviae_quality_id))" +
                "AS new " +
                "ON DUPLICATE KEY UPDATE life_history_style = new.life_history_style, terminal_adult_stage = new.terminal_adult_stage, " +
                "observed_moult_stage_count = new.observed_moult_stage_count, estimated_moult_count = new.estimated_moult_count, " +
                "specimen_count = new.specimen_count, segment_addition_mode_id = new.segment_addition_mode_id, " +
                "body_segments_count_per_moult_stage = new.body_segments_count_per_moult_stage, " +
                "body_segments_count_in_adults = new.body_segments_count_in_adults, body_length_average = new.body_length_average, " +
                "body_length_increase_average_per_moult = new.body_length_increase_average_per_moult, measurement_unit = new.measurement_unit, " +
                "suture_location_id = new.suture_location_id, cephalic_suture_location_id = new.cephalic_suture_location_id, " +
                "post_cephalic_suture_location_id = new.post_cephalic_suture_location_id, " +
                "resulting_named_moulting_configuration_id = new.resulting_named_moulting_configuration_id, " +
                "egress_direction = new.egress_direction, position_exuviae_found_in = new.position_exuviae_found_in, " +
                "moulting_phase = new.moulting_phase, moulting_variability = new.moulting_variability, " +
                "other_behaviour_id = new.other_behaviour_id, exuviae_consumed = new.exuviae_consumed, " +
                "exoskeletal_reabsorption = new.exoskeletal_reabsorption, fossil_exuviae_quality_id = new.fossil_exuviae_quality_id " ;
    
        List<MapSqlParameterSource> params = new ArrayList<>();
        for (MoultingCharactersTO mcTO : moultingCharactersTOs) {
            MapSqlParameterSource mcSource = new MapSqlParameterSource();
            mcSource.addValue("id", mcTO.getId());
            mcSource.addValue("life_history_style", mcTO.getLifeHistoryStyle());
            mcSource.addValue("terminal_adult_stage", mcTO.getHasTerminalAdultStage());
            mcSource.addValue("observed_moult_stage_count", mcTO.getObservedMoultStageCount());
            mcSource.addValue("estimated_moult_count", mcTO.getEstimatedMoultStageCount());
            mcSource.addValue("specimen_count", mcTO.getSpecimenCount());
            mcSource.addValue("segment_addition_mode_id", mcTO.getSegmentAdditionMode());
            mcSource.addValue("body_segments_count_per_moult_stage", mcTO.getBodySegmentsCountPerMoultStage());
            mcSource.addValue("body_segments_count_in_adults", mcTO.getBodySegmentsCountInAdults());
            mcSource.addValue("body_length_average", mcTO.getBodyLengthAverage());
            mcSource.addValue("body_length_increase_average_per_moult", mcTO.getBodyLengthIncreaseAverage());
            mcSource.addValue("measurement_unit", mcTO.getMeasurementUnit());
            mcSource.addValue("suture_location_id", mcTO.getSutureLocation());
            mcSource.addValue("cephalic_suture_location_id", mcTO.getCephalicSutureLocation());
            mcSource.addValue("post_cephalic_suture_location_id", mcTO.getPostCephalicSutureLocation());
            mcSource.addValue("resulting_named_moulting_configuration_id", mcTO.getResultingNamedMoultingConfiguration());
            mcSource.addValue("egress_direction", mcTO.getEgressDirection());
            mcSource.addValue("position_exuviae_found_in", mcTO.getPositionExuviaeFoundIn());
            mcSource.addValue("moulting_phase", mcTO.getMoultingPhase());
            mcSource.addValue("moulting_variability", mcTO.getMoultingVariability());
            mcSource.addValue("other_behaviour_id", mcTO.getOtherBehaviour());
            mcSource.addValue("exuviae_consumed", mcTO.getExuviaeConsumed());
            mcSource.addValue("exoskeletal_reabsorption", mcTO.getExoskeletalMaterialReabsorption());
            mcSource.addValue("fossil_exuviae_quality_id", mcTO.getFossilExuviaeQuality());
            params.add(mcSource);
        }
        int[] ints = template.batchUpdate(insertStmt, params.toArray(MapSqlParameterSource[]::new));
        logger.info(Arrays.stream(ints).sum() + " new rows(s) in 'moulting_characters' table.");
        return ints;
    }
    
    private static class GeneralMoultingCharactersRowMapper implements RowMapper<MoultingCharactersTO> {
        @Override
        public MoultingCharactersTO mapRow(ResultSet rs, int rowNum) throws SQLException {

            return new MoultingCharactersTO(rs.getInt("gmc.id"), rs.getString("gmc.life_history_style"),
                    rs.getBoolean("gmc.terminal_adult_stage"), rs.getInt("gmc.observed_moult_stage_count"),
                    rs.getInt("gmc.estimated_moult_count"), rs.getInt("gmc.specimen_count"), rs.getString("sam.name"),
                    rs.getString("gmc.body_segments_count_per_moult_stage"), rs.getString("gmc.body_segments_count_in_adults"),
                    rs.getBigDecimal("gmc.body_length_average"),
                    rs.getString("gmc.body_length_increase_average_per_moult"), rs.getString("gmc.measurement_unit"),
                    rs.getString("sl.name"), rs.getString("csl.name"), rs.getString("pcsl.name"), rs.getString("rnmc.name"),
                    rs.getString("gmc.egress_direction"), rs.getString("gmc.position_exuviae_found_in"), rs.getString("gmc.moulting_phase"),
                    rs.getString("gmc.moulting_variability"), rs.getString("ob.name"),
                    rs.getString("gmc.exuviae_consumed"), rs.getString("gmc.exoskeletal_reabsorption"), rs.getString("feq.name"));
        }
    }
}
