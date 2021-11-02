package org.moultdb.api.repository.mysql;

import org.moultdb.api.repository.dao.GeneralMoultingCharactersDAO;
import org.moultdb.api.repository.dto.EgressDirectionTO;
import org.moultdb.api.repository.dto.GeneralMoultingCharactersTO;
import org.moultdb.api.repository.dto.SutureLocationTO;
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
public class MySQLGeneralMoultingCharactersDAO implements GeneralMoultingCharactersDAO {
    
    NamedParameterJdbcTemplate template;
    
    private static final String SELECT_STATEMENT = "SELECT * FROM general_moulting_characters gmc " +
            "INNER JOIN suture_location sl ON gmc.suture_location_id = sl.id " +
            "INNER JOIN egress_direction ed ON gmc.egress_direction_id = ed.id ";
    
    public MySQLGeneralMoultingCharactersDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public List<GeneralMoultingCharactersTO> findAll() {
        return template.query(SELECT_STATEMENT, new GeneralMoultingCharactersRowMapper());
    }
    
    @Override
    public GeneralMoultingCharactersTO findById(int id) {
        return TransfertObject.getOneTO(findByIds(Collections.singleton(id)));
    }
    
    @Override
    public List<GeneralMoultingCharactersTO> findByIds(Set<Integer> ids) {
        return template.query(SELECT_STATEMENT + " WHERE gmc.id IN (:ids)",
                new MapSqlParameterSource().addValue("ids", ids), new GeneralMoultingCharactersRowMapper());
    }
    
    private static class GeneralMoultingCharactersRowMapper implements RowMapper<GeneralMoultingCharactersTO> {
        @Override
        public GeneralMoultingCharactersTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new GeneralMoultingCharactersTO(rs.getInt("gmc.id"), rs.getInt("gmc.dated_taxon_id"),
                    rs.getBoolean("gmc.life_history"), rs.getInt("gmc.moult_count"), rs.getInt("gmc.size_increase"),
                    rs.getBoolean("gmc.adult_stage"), rs.getBoolean("gmc.segment_addition_mode"), rs.getBoolean("gmc.variability"),
                    new SutureLocationTO(rs.getInt("sl.id"), rs.getString("sl.name")),
                    new EgressDirectionTO(rs.getInt("ed.id"), rs.getString("ed.name")), rs.getBoolean("gmc.exuviae_state"),
                    rs.getBoolean("gmc.moulting_phase"), rs.getBoolean("gmc.reabsorption"), rs.getBoolean("gmc.exuviae_consumed"),
                    rs.getInt("gmc.repair"), rs.getBoolean("gmc.mass_moulting"), rs.getBoolean("gmc.mating"),
                    rs.getString("gmc.hormone_regulation"), rs.getInt("gmc.version_id"));
        }
    }
}
