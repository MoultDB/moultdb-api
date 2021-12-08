package org.moultdb.api.repository.mysql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.repository.dao.TaxonDAO;
import org.moultdb.api.repository.dto.TaxonTO;
import org.moultdb.api.repository.dto.TransfertObject;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-18
 */
@Repository
public class MySQLTaxonDAO implements TaxonDAO {
    
    private final static Logger logger = LogManager.getLogger(MySQLTaxonDAO.class.getName());
    
    NamedParameterJdbcTemplate template;
    
    private static final String SELECT_STATEMENT = "SELECT * from taxon t ";
    
    public MySQLTaxonDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public List<TaxonTO> findAll() {
        return template.query(SELECT_STATEMENT, new TaxonRowMapper());
    }
    
    @Override
    public List<TaxonTO> findByScientificName(String taxonScientificName) {
        return template.query(SELECT_STATEMENT + "WHERE scientific_name = :scientific_name",
                new MapSqlParameterSource().addValue("scientific_name", taxonScientificName), new TaxonRowMapper());
    }
    
    @Override
    public TaxonTO findById(int id) {
        return TransfertObject.getOneTO(findByIds(Collections.singleton(id)));
    }
    
    @Override
    public List<TaxonTO> findByIds(Set<Integer> taxonIds) {
        return template.query(SELECT_STATEMENT + "WHERE id IN (:ids)",
                new MapSqlParameterSource().addValue("ids", taxonIds), new TaxonRowMapper());
    }
    
    @Override
    public void insertTaxon(TaxonTO taxon) {
        String sql = "INSERT INTO taxon (scientific_name, common_name, db_xref_id, parent_taxon_id, taxon_rank, extinct, path) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
    
        Map<String,Object> map = new HashMap<>();
        map.put("scientific_name", taxon.getName());
        map.put("common_name", taxon.getCommonName());
        map.put("db_xref_id", taxon.getDbXrefId());
        map.put("parent_taxon_id", taxon.getParentTaxonId());
        map.put("taxon_rank", taxon.getTaxonRank());
        map.put("extinct", taxon.isExtinct());
        map.put("path", taxon.getPath());
    
        int result = template.update(sql, map);
    
        if (result > 0) {
            logger.info(result + " new row(s) has been inserted.");
        } else {
            logger.error("No new row has been inserted.");
        }
    }
    
    private static class TaxonRowMapper implements RowMapper<TaxonTO> {
        @Override
        public TaxonTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new TaxonTO(rs.getInt("t.id"), rs.getString("t.scientific_name"), rs.getString("t.common_name"),
                    rs.getInt("t.db_xref_id"), rs.getInt("t.parent_taxon_id"), rs.getString("t.taxon_rank"),
                    rs.getBoolean("t.extinct"), rs.getString("t.path"));
        }
    }
}
