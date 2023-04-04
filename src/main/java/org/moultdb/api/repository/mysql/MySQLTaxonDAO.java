package org.moultdb.api.repository.mysql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.repository.dao.TaxonDAO;
import org.moultdb.api.repository.dto.DbXrefTO;
import org.moultdb.api.repository.dto.TaxonTO;
import org.moultdb.api.repository.dto.TransfertObject;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
    
    @Autowired
    MySQLDbXrefDAO dbXrefDAO;
    
    private static final String SELECT_STATEMENT = "SELECT * from taxon t " +
            "INNER JOIN taxon_db_xref tx ON t.path = tx.taxon_path " +
            "INNER JOIN db_xref x ON tx.db_xref_id = x.id " +
            "INNER JOIN data_source ds ON (x.data_source_id = ds.id) ";
    
    public MySQLTaxonDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public List<TaxonTO> findAll() {
        return template.query(SELECT_STATEMENT, new TaxonResultSetExtractor());
    }
    
    @Override
    public TaxonTO findByScientificName(String taxonScientificName) {
        return TransfertObject.getOneTO(template.query(SELECT_STATEMENT + "WHERE scientific_name = :scientific_name",
                new MapSqlParameterSource().addValue("scientific_name", taxonScientificName), new TaxonResultSetExtractor()));
    }
    
    @Override
    public TaxonTO findByPath(String path) {
        return TransfertObject.getOneTO(findByPaths(Collections.singleton(path)));
    }
    
    @Override
    public List<TaxonTO> findByPaths(Set<String> taxonPaths) {
        return template.query(SELECT_STATEMENT + "WHERE t.path IN (:taxonPaths)",
                new MapSqlParameterSource().addValue("taxonPaths", taxonPaths), new TaxonResultSetExtractor());
    }
    
    @Override
    public int insert(TaxonTO taxonTO) {
        int[] ints = batchUpdate(Collections.singleton(taxonTO));
        return ints[0];
    }
    
    @Transactional
    @Override
    public int[] batchUpdate(Set<TaxonTO> taxonTOs) {
        String taxonSql = "INSERT INTO taxon (path, scientific_name, common_name, " +
                "parent_taxon_path, taxon_rank, extinct) " +
                "VALUES (:path, :scientific_name, :common_name, :parent_taxon_path, :taxon_rank, :extinct) " +
                "AS new " +
                "ON DUPLICATE KEY UPDATE scientific_name = new.scientific_name, common_name = new.common_name," +
                " parent_taxon_path = new.parent_taxon_path, taxon_rank = new.taxon_rank, extinct = new.extinct";
        
        List<MapSqlParameterSource> params = new ArrayList<>();
        for (TaxonTO taxonTO : taxonTOs) {
            MapSqlParameterSource taxonSource = new MapSqlParameterSource();
            taxonSource.addValue("path", taxonTO.getPath());
            taxonSource.addValue("scientific_name", taxonTO.getScientificName());
            taxonSource.addValue("common_name", taxonTO.getCommonName());
            taxonSource.addValue("parent_taxon_path", taxonTO.getParentTaxonPath());
            taxonSource.addValue("taxon_rank", taxonTO.getRank());
            taxonSource.addValue("extinct", taxonTO.isExtincted());
            params.add(taxonSource);
        }
        int[] ints = template.batchUpdate(taxonSql, params.toArray(MapSqlParameterSource[]::new));
        logger.info(Arrays.stream(ints).sum() + " new row(s) in 'taxon' table.");
        return ints;
    }
    
    private static class TaxonResultSetExtractor implements ResultSetExtractor<List<TaxonTO>> {
        
        @Override
        public List<TaxonTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<String, TaxonTO> taxa = new HashMap<>();
            while(rs.next()) {
                String taxonPath = rs.getString("t.path");
                TaxonTO taxonTO = taxa.get(taxonPath);

                // Build DbXrefs
                DbXrefTO dbXrefTO = new MySQLDbXrefDAO.DbXrefRowMapper().mapRow(rs, rs.getRow());
                Set<DbXrefTO> dbXrefTOs = taxonTO == null ? null: new HashSet<>(taxonTO.getDbXrefTOs());
                if (dbXrefTOs == null) {
                    dbXrefTOs = new HashSet<>();
                }
                dbXrefTOs.add(dbXrefTO);
    
                // Build TaxonTO. Even if it already exists, we create a new one because it's an unmutable object
                taxonTO = new TaxonTO(rs.getString("t.path"), rs.getString("t.scientific_name"), rs.getString("t.common_name"),
                        rs.getString("t.parent_taxon_path"), rs.getString("t.taxon_rank"), rs.getBoolean("t.extinct"), dbXrefTOs);
    
                taxa.put(taxonPath, taxonTO);
            }
            return new ArrayList<>(taxa.values());
        }
    }
}
