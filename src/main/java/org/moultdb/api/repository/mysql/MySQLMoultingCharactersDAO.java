package org.moultdb.api.repository.mysql;

import org.moultdb.api.repository.dao.MoultingCharactersDAO;
import org.moultdb.api.repository.dto.MoultingCharactersTO;
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
public class MySQLMoultingCharactersDAO implements MoultingCharactersDAO {
    
    NamedParameterJdbcTemplate template;
    
    private static final String SELECT_STATEMENT = "SELECT * FROM moulting_characters gmc " +
            "INNER JOIN segment_addition_mode sam ON gmc.segment_addition_mode_id = sam.id " +
            "INNER JOIN suture_location sl ON gmc.suture_location_id = sl.id " +
            "INNER JOIN suture_location jsl ON gmc.juvenile_suture_location_id = jsl.id " +
            "INNER JOIN cephalic_suture_location csl ON gmc.cephalic_suture_location_id = csl.id " +
            "INNER JOIN cephalic_suture_location jcsl ON gmc.juvenile_cephalic_suture_location_id = jcsl.id " +
            "INNER JOIN post_cephalic_suture_location pcsl ON gmc.post_cephalic_suture_location_id = pcsl.id " +
            "INNER JOIN post_cephalic_suture_location jpcsl ON gmc.post_cephalic_suture_location_id = jpcsl.id " +
            "INNER JOIN resulting_named_moulting_configuration rnmc ON gmc.resulting_named_moulting_configuration_id = rnmc.id " +
            "INNER JOIN resulting_named_moulting_configuration jrnmc ON gmc.juvenile_resulting_named_moulting_configuration_id = jrnmc.id " +
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
    
    private static class GeneralMoultingCharactersRowMapper implements RowMapper<MoultingCharactersTO> {
        @Override
        public MoultingCharactersTO mapRow(ResultSet rs, int rowNum) throws SQLException {

            return new MoultingCharactersTO(rs.getInt("gmc.id"), rs.getString("gmc.life_history_style"),
                    rs.getBoolean("gmc.terminal_adult_stage"), rs.getInt("gmc.observed_moult_stage_count"),
                    rs.getInt("gmc.estimated_moult_count"), rs.getInt("gmc.specimen_count"), rs.getString("sam.name"),
                    rs.getInt("gmc.body_segments_count_per_moult_stage"), rs.getInt("gmc.body_segments_count_in_adults"),
                    rs.getInt("gmc.body_length_average"), rs.getInt("gmc.body_length_increase_average"),
                    rs.getBigDecimal("gmc.body_length_increase_average_per_moult"), rs.getString("gmc.measurement_unit"),
                    rs.getString("sl.name"), rs.getString("csl.name"), rs.getString("pcsl.name"), rs.getString("rnmc.name"),
                    rs.getString("gmc.egress_direction"), rs.getString("gmc.position_exuviae_found_in"), rs.getString("gmc.moulting_phase"),
                    rs.getString("gmc.moulting_variability"), rs.getBoolean("gmc.juvenile_moulting_behaviours"), rs.getString("jsl.name"),
                    rs.getString("jcsl.name"), rs.getString("jpcsl.name"), rs.getString("jrnmc.name"), rs.getString("ob.name"),
                    rs.getString("gmc.exuviae_consumed"), rs.getString("gmc.exoskeletal_reabsorption"), rs.getString("feq.name"));
        }
    }
}
