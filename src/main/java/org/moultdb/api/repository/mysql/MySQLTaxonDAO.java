package org.moultdb.api.repository.mysql;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.exception.MoultDBException;
import org.moultdb.api.repository.dao.DAO;
import org.moultdb.api.repository.dao.TaxonDAO;
import org.moultdb.api.repository.dto.DbXrefTO;
import org.moultdb.api.repository.dto.TaxonTO;
import org.moultdb.api.repository.dto.TaxonToDbXrefTO;
import org.moultdb.api.repository.dto.TransfertObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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
            "LEFT JOIN taxon_db_xref tx ON t.path = tx.taxon_path " +
            "LEFT JOIN db_xref x ON tx.db_xref_id = x.id " +
            "LEFT JOIN data_source ds ON (x.data_source_id = ds.id) ";
    
    public MySQLTaxonDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public List<TaxonTO> findAll() {
        return template.query(SELECT_STATEMENT, new TaxonResultSetExtractor());
    }
    
    @Override
    public TaxonTO findByScientificName(String taxonScientificName) {
        return TransfertObject.getOneTO(template.query(SELECT_STATEMENT + "WHERE lower(scientific_name) = lower(:scientific_name)",
                new MapSqlParameterSource().addValue("scientific_name", taxonScientificName), new TaxonResultSetExtractor()));
    }
    
    @Override
    public TaxonTO findByAccession(String accession, String datasourceName) {
        if (StringUtils.isBlank(accession) || StringUtils.isBlank(datasourceName)) {
            throw new UnsupportedOperationException("Empty parameters not supported: accession [" + accession + 
                    "] ; datasourceName [" + datasourceName + "]" );
        }
        return TransfertObject.getOneTO(template.query(SELECT_STATEMENT + 
                        "WHERE x.accession = :accession " +
                        "AND ds.name = :datasourceName ",
                new MapSqlParameterSource().addValue("accession", accession).addValue("datasourceName", datasourceName),
                new TaxonResultSetExtractor()));
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
    
    @Override
    public int[] batchUpdate(Set<TaxonTO> taxonTOs) {
        String taxonSql = "INSERT INTO taxon (path, scientific_name, common_name, " +
                "parent_taxon_path, extinct) " +
                "VALUES (:path, :scientific_name, :common_name, :parent_taxon_path, :extinct) " +
                "AS new " +
                "ON DUPLICATE KEY UPDATE scientific_name = new.scientific_name, common_name = new.common_name," +
                " parent_taxon_path = new.parent_taxon_path, extinct = new.extinct";
        
        String dbXrefSql = "INSERT INTO db_xref (id, accession, data_source_id) " +
                "VALUES (:id, :accession, :data_source_id) " +
                "AS new " +
                "ON DUPLICATE KEY UPDATE accession = new.accession";
        
        String taxonToDbXrefSql = "INSERT INTO taxon_db_xref (taxon_path, db_xref_id, main) " +
                "VALUES (:taxon_path, :db_xref_id, :main) " +
                "AS new " +
                "ON DUPLICATE KEY UPDATE db_xref_id = new.db_xref_id, main = new.main";
        
        List<MapSqlParameterSource> taxonParams = new ArrayList<>();
        List<MapSqlParameterSource> dbXrefParams = new ArrayList<>();
        List<MapSqlParameterSource> taxonToDbXrefParams = new ArrayList<>();
        
        for (TaxonTO taxonTO : taxonTOs) {
            MapSqlParameterSource taxonSource = new MapSqlParameterSource();
            taxonSource.addValue("path", taxonTO.getPath());
            taxonSource.addValue("scientific_name", taxonTO.getScientificName());
            taxonSource.addValue("common_name", taxonTO.getCommonName());
            taxonSource.addValue("parent_taxon_path", taxonTO.getParentTaxonPath());
            taxonSource.addValue("extinct", taxonTO.isExtincted());
            taxonParams.add(taxonSource);
            
            for (DbXrefTO dbXrefTO : taxonTO.getDbXrefTOs()) {
                MapSqlParameterSource dbXrefSource = new MapSqlParameterSource();
                dbXrefSource.addValue("id", dbXrefTO.getId());
                dbXrefSource.addValue("accession", dbXrefTO.getAccession());
                dbXrefSource.addValue("data_source_id", dbXrefTO.getDataSourceTO().getId());
                dbXrefParams.add(dbXrefSource);
                
            }
            
            for (TaxonToDbXrefTO taxonToDbXrefTO : taxonTO.getTaxonToDbXrefTOs()) {
                MapSqlParameterSource taxonToDbXrefSource = new MapSqlParameterSource();
                taxonToDbXrefSource.addValue("taxon_path", taxonToDbXrefTO.getTaxonPath());
                taxonToDbXrefSource.addValue("db_xref_id", taxonToDbXrefTO.getDbXrefId());
                taxonToDbXrefSource.addValue("main", taxonToDbXrefTO.getMain());
                taxonToDbXrefParams.add(taxonToDbXrefSource);
            }
        }
        int[] taxonRowCounts = null ;
        try {
            taxonRowCounts = template.batchUpdate(taxonSql, taxonParams.toArray(MapSqlParameterSource[]::new));
            logger.debug(Arrays.stream(taxonRowCounts).sum() + " updated row(s) in 'taxon' table.");

            int[] dbXrefRowCounts = template.batchUpdate(dbXrefSql, dbXrefParams.toArray(MapSqlParameterSource[]::new));
            logger.debug(Arrays.stream(dbXrefRowCounts).sum() + " updated row(s) in 'db_xref' table.");
            
            int[] taxonToDbXrefRowCounts = template.batchUpdate(taxonToDbXrefSql, taxonToDbXrefParams.toArray(MapSqlParameterSource[]::new));
            logger.debug(Arrays.stream(taxonToDbXrefRowCounts).sum() + " updated row(s) in 'taxon_db_xref' table.");
        } catch (Exception e) {
            throw new MoultDBException("Insertion of taxa failed: " + e.getMessage());
        }
        return taxonRowCounts;
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
                        rs.getString("t.parent_taxon_path"), DAO.getBoolean(rs, "t.extinct"), dbXrefTOs);
    
                taxa.put(taxonPath, taxonTO);
            }
            return new ArrayList<>(taxa.values());
        }
    }
}
