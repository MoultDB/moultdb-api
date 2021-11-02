package org.moultdb.api.repository.mysql;

import org.moultdb.api.repository.dao.DatedTaxonDAO;
import org.moultdb.api.repository.dto.DatedTaxonTO;
import org.moultdb.api.repository.dto.GeologicalAgeTO;
import org.moultdb.api.repository.dto.TaxonTO;
import org.moultdb.api.repository.dto.TransfertObject;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
@Repository
public class MySQLDatedTaxonDAO implements DatedTaxonDAO {
    
    NamedParameterJdbcTemplate template;
    
    private static final String SELECT_STATEMENT =
            "SELECT dt.*, ga.*, t.* " +
                    "FROM dated_taxon dt " +
                    "INNER JOIN geological_age ga ON (ga.id = dt.geological_age_id) " +
                    "INNER JOIN taxon t ON (t.id = dt.taxon_id) ";
    
    public MySQLDatedTaxonDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public List<DatedTaxonTO> findAll() {
        return template.query(SELECT_STATEMENT, new DatedTaxonRowMapper());
    }
    
    @Override
    public DatedTaxonTO findByTaxonId(int taxonId) {
        List<DatedTaxonTO> datedTaxonTOs = template.query(SELECT_STATEMENT + "WHERE dt.taxon_id = :id",
                new MapSqlParameterSource().addValue("id", taxonId), new DatedTaxonRowMapper());
        return TransfertObject.getOneTO(datedTaxonTOs);
        
    }
    
    private static class DatedTaxonRowMapper implements RowMapper<DatedTaxonTO> {
        @Override
        public DatedTaxonTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new DatedTaxonTO(rs.getInt("id"),
                    new GeologicalAgeTO(rs.getInt("ga.id"), rs.getString("ga.name"), rs.getString("ga.symbol"),
                            rs.getBigDecimal("ga.younger_bound"), rs.getBigDecimal("ga.older_bound")),
                    rs.getInt("dt.taxon_id"), rs.getInt("version_id"));
        }
    }
}
