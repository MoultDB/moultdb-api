package org.moultdb.api.repository.mysql;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.exception.MoultDBException;
import org.moultdb.api.repository.dao.TaxonDAO;
import org.moultdb.api.repository.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;

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
    public Long countAll() {
        String sql = "SELECT COUNT(*) FROM taxon ";
        return template.queryForObject(sql, new MapSqlParameterSource(), Long.class);
    }
    
    @Override
    public List<TaxonTO> findAll() {
        return template.query(SELECT_STATEMENT, new TaxonResultSetExtractor());
    }
    
    @Override
    public List<TaxonTO> findAllPaginated(int pageNumber, int batchSize) {
        if (pageNumber < 0 || batchSize <= 0) {
            throw new IllegalArgumentException("Invalid pagination parameters: pageNumber must be ≥ 0 and batchSize must be > 0");
        }
        int offset = pageNumber * batchSize;
        
        String sql = "SELECT * from taxon t LIMIT :limit OFFSET :offset";
        return template.query(sql,
                new MapSqlParameterSource()
                        .addValue("limit", batchSize)
                        .addValue("offset", offset),
                new TaxonRowMapper());
    }
    
    @Override
    public void processAllInBatches(int batchSize, Consumer<List<TaxonTO>> consumer) {
        if (batchSize <= 0) {
            throw new IllegalArgumentException("Invalid batch size: batchSize must be > 0");
        }
        
        long total = countAll();
        int totalPages = (int) Math.ceil((double) total / batchSize);
        
        logger.info("Processing {} taxa in {} batches of {} elements each", total, totalPages, batchSize);
        
        for (int page = 0; page < totalPages; page++) {
            List<TaxonTO> batch = findAllPaginated(page, batchSize);
            if (!batch.isEmpty()) {
                logger.debug("## Processing batch {}/{} ({} taxa)", page + 1, totalPages, batch.size());
                consumer.accept(batch);
            }
        }
        
        logger.info("Processing completed");
    }
    
    @Override
    public List<TaxonTO> findByText(String searchedText) {
        if (StringUtils.isBlank(searchedText)) {
            throw new UnsupportedOperationException("Empty searched text not supported");
        }
        List<TaxonTO> taxonTOs = template.query(SELECT_STATEMENT +
                        "WHERE lower(scientific_name) like :searched_text " +
                        "ORDER BY scientific_name",
                new MapSqlParameterSource().addValue("searched_text", "%" + searchedText.trim().toLowerCase() + "%"),
                new TaxonResultSetExtractor());
        // TaxonResultSetExtractor doesn't keep the order so the sort should be done after 
        if (taxonTOs != null) {
            taxonTOs.sort(Comparator.comparing(TaxonTO::getScientificName));
        }
        return taxonTOs;
    }
    
    @Override
    public TaxonTO findByScientificName(String taxonScientificName) {
        return TransfertObject.getOneTO(template.query(SELECT_STATEMENT + "WHERE lower(scientific_name) = lower(:scientific_name)",
                new MapSqlParameterSource().addValue("scientific_name", taxonScientificName), new TaxonResultSetExtractor()));
    }
    
    @Override
    public TaxonTO findBySynonym(String taxonName) {
        return TransfertObject.getOneTO(template.query(SELECT_STATEMENT + "WHERE lower(x.name) = lower(:taxon_name)",
                new MapSqlParameterSource().addValue("taxon_name", taxonName), new TaxonResultSetExtractor()));
    }
    
    @Override
    public TaxonTO findByAccession(String accession, String datasourceName) {
        if (StringUtils.isBlank(accession) || StringUtils.isBlank(datasourceName)) {
            throw new UnsupportedOperationException("Empty parameters not supported: accession [" + accession + 
                    "] ; datasourceName [" + datasourceName + "]" );
        }
        // To get all x-refs, we should do a subquery to retrieve path then the main query to retrieve all data
        String sql = SELECT_STATEMENT +" WHERE t.path = (SELECT t.path from taxon t " +
                "LEFT JOIN taxon_db_xref tx ON t.path = tx.taxon_path " +
                "LEFT JOIN db_xref x ON tx.db_xref_id = x.id " +
                "LEFT JOIN data_source ds ON (x.data_source_id = ds.id) " +
                "WHERE x.accession = :accession " +
                "AND ds.name = :datasourceName)";
        return TransfertObject.getOneTO(template.query(sql,
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
    public List<TaxonTO> findLineageByPath(String taxonPath) {
        List<TaxonTO> taxonTOs = template.query(SELECT_STATEMENT + "WHERE :taxonPath LIKE CONCAT(path, '\\.',  '%') ",
                new MapSqlParameterSource().addValue("taxonPath", taxonPath), new TaxonResultSetExtractor());
        // TaxonResultSetExtractor doesn't keep the order so the sort should be done after 
        if (taxonTOs != null) {
            taxonTOs.sort(Comparator.comparing(TaxonTO::getPath));
        }
        return taxonTOs;
    }
    
    @Override
    public List<TaxonTO> findDirectChildrenByPath(String taxonPath) {
        List<TaxonTO> taxonTOs = template.query(SELECT_STATEMENT +
                        "WHERE path LIKE CONCAT(:taxonPath, '.%') " +
                        "  AND path NOT LIKE CONCAT(:taxonPath, '.%.%') ",
                new MapSqlParameterSource().addValue("taxonPath", taxonPath), new TaxonResultSetExtractor());
        // TaxonResultSetExtractor does not keep the order, so sorting should be done afterward.
        // This ensures that the order remains the same between two identical queries.
        if (taxonTOs != null) {
            taxonTOs.sort(Comparator.comparing(TaxonTO::getScientificName));
        }
        return taxonTOs;
    }
    
    @Override
    public List<String> findAllPathsHavingChildren() {
        String sql = "SELECT DISTINCT SUBSTRING_INDEX(path, '.', LENGTH(path) - LENGTH(REPLACE(path, '.', ''))) " +
                "FROM taxon WHERE path LIKE '%.%'";
        return template.queryForList(sql, new MapSqlParameterSource(), String.class);
    }
    
    @Override
    public List<String> findAllPaths() {
        String sql = "SELECT path FROM taxon";
        return template.queryForList(sql, new MapSqlParameterSource(), String.class);
    }
    
    @Override
    public List<String> findAllDescendantPaths(String taxonPath) {
        String sql = "SELECT DISTINCT path FROM taxon WHERE path LIKE CONCAT(:taxonPath, '.%') ";
        return template.queryForList(sql,
                new MapSqlParameterSource().addValue("taxonPath", taxonPath), String.class);
    }
    
    @Override
    public void insert(TaxonTO taxonTO) {
        batchUpdate(Collections.singleton(taxonTO));
    }
    
    @Override
    public int batchUpdate(Set<TaxonTO> taxonTOs) {
        String taxonSql = "INSERT INTO taxon (path, scientific_name, common_name, taxon_rank) " +
                "VALUES (:path, :scientific_name, :common_name, :taxon_rank) " +
                "AS new " +
                "ON DUPLICATE KEY UPDATE scientific_name = new.scientific_name, common_name = new.common_name, " +
                " taxon_rank = new.taxon_rank";
        
        String dbXrefSql = "INSERT INTO db_xref (id, accession, name, data_source_id) " +
                "VALUES (:id, :accession, :name, :data_source_id) " +
                "AS new " +
                "ON DUPLICATE KEY UPDATE accession = new.accession, name = new.name, " +
                " data_source_id = new.data_source_id ";
        
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
            taxonSource.addValue("taxon_rank", taxonTO.getRank());
            taxonParams.add(taxonSource);
            
            for (DbXrefTO dbXrefTO : taxonTO.getDbXrefTOs()) {
                MapSqlParameterSource dbXrefSource = new MapSqlParameterSource();
                dbXrefSource.addValue("id", dbXrefTO.getId());
                dbXrefSource.addValue("accession", dbXrefTO.getAccession());
                dbXrefSource.addValue("name", dbXrefTO.getName());
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
        int[] ints;
        try {
            ints = template.batchUpdate(taxonSql, taxonParams.toArray(MapSqlParameterSource[]::new));
            logger.debug("'taxon' table updated");

            template.batchUpdate(dbXrefSql, dbXrefParams.toArray(MapSqlParameterSource[]::new));
            logger.debug("'db_xref' table updated");
            
            template.batchUpdate(taxonToDbXrefSql, taxonToDbXrefParams.toArray(MapSqlParameterSource[]::new));
            logger.debug("'taxon_db_xref' table updated");
        } catch (Exception e) {
            throw new MoultDBException("Insertion of taxa failed: " + e.getMessage());
        }
        return Arrays.stream(ints).sum();
    }
    
    private static class TaxonResultSetExtractor implements ResultSetExtractor<List<TaxonTO>> {
        
        @Override
        public List<TaxonTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<String, TaxonTO> taxa = new HashMap<>();
            while(rs.next()) {
                String taxonPath = rs.getString("t.path");
                TaxonTO taxonTO = taxa.get(taxonPath);

                // Build DbXrefs
                Set<DbXrefTO> dbXrefTOs = taxonTO == null ? new HashSet<>() : new HashSet<>(taxonTO.getDbXrefTOs());
                DbXrefTO dbXrefTO = new MySQLDbXrefDAO.DbXrefRowMapper().mapRow(rs, rs.getRow());
                if (dbXrefTO != null) {
                    dbXrefTOs.add(dbXrefTO);
                }
    
                TaxonToDbXrefTO taxonToDbXrefTO = new TaxonToDbXrefTO(rs.getString("tx.taxon_path"),
                        rs.getInt("tx.db_xref_id"), rs.getBoolean("tx.main"));
                Set<TaxonToDbXrefTO> taxonToDbXrefTOs = taxonTO == null ? new HashSet<>(): new HashSet<>(taxonTO.getTaxonToDbXrefTOs());
                taxonToDbXrefTOs.add(taxonToDbXrefTO);
                
                // Build TaxonTO. Even if it already exists, we create a new one because it's an unmutable object
                taxonTO = new TaxonTO(rs.getString("t.path"), rs.getString("t.scientific_name"), rs.getString("t.common_name"),
                        rs.getString("t.taxon_rank"), dbXrefTOs, taxonToDbXrefTOs);
    
                taxa.put(taxonPath, taxonTO);
            }
            return new ArrayList<>(taxa.values());
        }
    }
    
    private static class TaxonRowMapper implements RowMapper<TaxonTO> {
        
        @Override
        public TaxonTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new TaxonTO(rs.getString("t.path"), rs.getString("t.scientific_name"), rs.getString("t.common_name"),
                    rs.getString("t.taxon_rank"), null, null);
        }
    }
}
