package org.moultdb.api.repository.mysql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.exception.MoultDBException;
import org.moultdb.api.repository.dao.DAO;
import org.moultdb.api.repository.dao.TaxonStatisticsDAO;
import org.moultdb.api.repository.dto.TaxonStatisticsTO;
import org.moultdb.api.repository.dto.TransfertObject;
import org.springframework.jdbc.core.RowMapper;
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
public class MySQLTaxonStatisticsDAO implements TaxonStatisticsDAO {
    
    private final static Logger logger = LogManager.getLogger(MySQLTaxonStatisticsDAO.class.getName());
    
    NamedParameterJdbcTemplate template;
    
    private static final String SELECT_STATEMENT = "SELECT * from taxon_statistics ts ";
    
    public MySQLTaxonStatisticsDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public List<TaxonStatisticsTO> findAll() {
        return template.query(SELECT_STATEMENT, new TaxonStatisticsRowMapper());
    }
    
    @Override
    public TaxonStatisticsTO findByPath(String path) {
        return TransfertObject.getOneTO(findByPaths(Collections.singleton(path)));
    }
    
    @Override
    public List<TaxonStatisticsTO> findByPaths(Set<String> taxonPaths) {
        return template.query(SELECT_STATEMENT + "WHERE ts.taxon_path IN (:taxonPaths)",
                new MapSqlParameterSource().addValue("taxonPaths", taxonPaths), new TaxonStatisticsRowMapper());
    }
    
    @Override
    public List<TaxonStatisticsTO> findByPathWithChildren(String taxonPath) {
        return template.query(SELECT_STATEMENT +
                        "WHERE ts.taxon_path = :taxonPath " +
                        "OR (ts.taxon_path like CONCAT(:taxonPath, '.%') AND ts.taxon_path NOT LIKE CONCAT(:taxonPath, '.%.%')) ",
                new MapSqlParameterSource().addValue("taxonPath", taxonPath),
                new TaxonStatisticsRowMapper());
    }
    
    @Override
    public void insert(TaxonStatisticsTO taxonStatsTO) {
        batchUpdate(Collections.singleton(taxonStatsTO));
    }
    
    @Override
    public int batchUpdate(Set<TaxonStatisticsTO> taxonStatsTOs) {

        String taxonStatsSql =
                "INSERT INTO taxon_statistics (taxon_path, depth, is_leaf, species_count, genome_count, ta_count, " +
                        "og_count, og_range) " +
                "VALUES (:taxonPath, :depth, :isLeaf, :speciesCount, :genomeCount, :taCount, :ogCount, :ogRange) " +
                "AS new " +
                "ON DUPLICATE KEY UPDATE depth = new.depth, is_leaf = new.is_leaf, species_count = new.species_count," +
                        " genome_count = new.genome_count, ta_count = new.ta_count, " +
                        "og_count = new.og_count, og_range = new.og_range";
        
        List<MapSqlParameterSource> taxonStatsParams = new ArrayList<>();
        
        for (TaxonStatisticsTO taxonStatsTO : taxonStatsTOs) {
            MapSqlParameterSource taxonStatsSource = new MapSqlParameterSource();
            taxonStatsSource.addValue("taxonPath", taxonStatsTO.getPath());
            taxonStatsSource.addValue("depth", taxonStatsTO.getDepth());
            taxonStatsSource.addValue("isLeaf", taxonStatsTO.isLeaf());
            taxonStatsSource.addValue("speciesCount", taxonStatsTO.getSpeciesCount());
            taxonStatsSource.addValue("genomeCount", taxonStatsTO.getGenomeCount());
            taxonStatsSource.addValue("taCount", taxonStatsTO.getTaxonAnnotationCount());
            taxonStatsSource.addValue("ogCount", taxonStatsTO.getOrthogroupCount());
            taxonStatsSource.addValue("ogRange", taxonStatsTO.getOrthogroupRange());
            taxonStatsParams.add(taxonStatsSource);
        }
        int[] ints;
        try {
            ints = template.batchUpdate(taxonStatsSql, taxonStatsParams.toArray(MapSqlParameterSource[]::new));
            logger.debug("'taxon_statistics' table updated");

        } catch (Exception e) {
            throw new MoultDBException("Insertion of taxa statistics failed: " + e.getMessage());
        }
        return Arrays.stream(ints).sum();
    }
    
    private static class TaxonStatisticsRowMapper implements RowMapper<TaxonStatisticsTO> {
        @Override
        public TaxonStatisticsTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new TaxonStatisticsTO(rs.getString("ts.taxon_path"), DAO.getInteger(rs, "ts.depth"),
                    rs.getBoolean("ts.is_leaf"), DAO.getInteger(rs, "ts.species_count"),
                    DAO.getInteger(rs, "ts.genome_count"), DAO.getInteger(rs, "ts.ta_count"),
                    DAO.getInteger(rs, "ts.og_count"), rs.getString("ts.og_range"));
        }
    }
}
