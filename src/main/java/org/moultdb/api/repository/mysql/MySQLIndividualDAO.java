package org.moultdb.api.repository.mysql;

import org.moultdb.api.repository.dao.IndividualDAO;
import org.moultdb.api.repository.dto.CollectionLocationTO;
import org.moultdb.api.repository.dto.ConditionTO;
import org.moultdb.api.repository.dto.DbXrefTO;
import org.moultdb.api.repository.dto.GeologicalAgeTO;
import org.moultdb.api.repository.dto.IndividualTO;
import org.moultdb.api.repository.dto.MoultingCharactersTO;
import org.moultdb.api.repository.dto.SampleTO;
import org.moultdb.api.repository.dto.StorageLocationTO;
import org.moultdb.api.repository.dto.TaxonTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Valentine Rech de Laval
 * @since 2021-11-22
 */
@Repository
public class MySQLIndividualDAO implements IndividualDAO {
    
    NamedParameterJdbcTemplate template;
    
    private static final String SELECT_STATEMENT = "SELECT i.*, c.* FROM individual i " +
            "INNER JOIN taxon t ON i.taxon_id = t.id " +
            "INNER JOIN moulting_characters mc ON i.moulting_characters_id = mc.id " +
            "INNER JOIN egress_direction ed ON mc.egress_direction_id = ed.id " +
            "INNER JOIN suture_location sl ON mc.suture_location_id = sl.id " +
            "INNER JOIN sample s ON i.sample_id = s.id " +
            "INNER JOIN storage_location sl ON s.storage_location_id = sl.id " +
            "INNER JOIN collection_location cl ON s.collection_location_id = cl.id " +
            "INNER JOIN cond c ON (c.id = i.condition_id) " +
            "";
    
    public MySQLIndividualDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public List<IndividualTO> findAll() {
        return template.query(SELECT_STATEMENT, new IndividualRowMapper());
    }
    
    @Override
    public List<IndividualTO> findByTaxonId(int id) {
        return null;
    }
    
    private static class IndividualRowMapper implements RowMapper<IndividualTO> {
        @Override
        public IndividualTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            ConditionTO conditionTO = new ConditionTO(rs.getInt("c.id"), rs.getInt("c.dev_stage_id"),
                    rs.getInt("c.anatomical_entity_id"), rs.getInt("c.sex_id"),
                    rs.getString("c.moulting_step"));
            
            TaxonTO taxonTO = new TaxonTO(rs.getInt("t.id"), rs.getString("t.scientific_name"), rs.getString("t.common_name"),
                    rs.getInt("t.db_xref_id"), rs.getInt("c.parent_taxon_id") ,
                    rs.getString("t.taxon_rank") , rs.getBoolean("t.extinct"), rs.getString("t.path"));
            
            MoultingCharactersTO moultingCharactersTO = new MoultingCharactersTO(rs.getInt("mc.id"),
                    rs.getBoolean("mc.life_history"), rs.getInt("mc.moult_count"), rs.getInt("mc.size_increase"),
                    rs.getBoolean("mc.adult_stage"), rs.getBoolean("mc.segment_addition_mode"),
                    rs.getBoolean("mc.variability"), rs.getString("sl.name"), rs.getString("eg.name"),
                    rs.getBoolean("mc.exuviae_state"), rs.getBoolean("mc.moulting_phase"), rs.getBoolean("mc.reabsorption"),
                    rs.getBoolean("mc.exuviae_consumed"), rs.getInt("mc.repair"), rs.getBoolean("mc.mass_moulting"),
                    rs.getBoolean("mc.mating"), rs.getString("mc.hormone_regulation"));
    
            GeologicalAgeTO geologicalAgeTO = new GeologicalAgeTO(rs.getInt("ga.id"), rs.getString("ga.name"), rs.getString("ga.symbol"),
                    rs.getBigDecimal("ga.younger_bound"), rs.getBigDecimal("ga.older_bound"));

            SampleTO sampleTO = new SampleTO(rs.getInt("s.id"), geologicalAgeTO, rs.getString("cl.name"),
                    rs.getString("s.storage_accession"), rs.getString("sl.name"), rs.getString("s.collector"), rs.getInt("s.version_id"));
        
                return new IndividualTO(rs.getInt("i.id"), taxonTO, moultingCharactersTO,
                        sampleTO, conditionTO, rs.getInt("i.version_id"));
        }
    }
}
