package org.moultdb.api.repository.mysql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.repository.dao.TaxonRepository;
import org.moultdb.api.repository.dto.TaxonDto;
import org.moultdb.api.repository.mapper.TaxonRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-18
 */
@Repository
public class TaxonRepositoryImpl implements TaxonRepository {
    
    private final static Logger log = LogManager.getLogger(TaxonRepositoryImpl.class.getName());
    
    NamedParameterJdbcTemplate template;
    
    public TaxonRepositoryImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public List<TaxonDto> findAll() {
        String sql = "SELECT * from taxon";
        return template.query(sql, new TaxonRowMapper());
    }
    
    @Override
    public List<TaxonDto> findByScientificName(String taxonScientificName) {
        return template.query(
                "SELECT * from taxon where scientific_name = :scientific_name",
                new MapSqlParameterSource().addValue("scientific_name", taxonScientificName),
                new TaxonRowMapper());
    }
    
    @Override
    public List<TaxonDto> findById(int id) {
        return template.query(
                "SELECT * from taxon where id = :id",
                new MapSqlParameterSource().addValue("id", id),
                new TaxonRowMapper());
    }
    
    @Override
    public void insertTaxon(TaxonDto taxon) {
        String sql = "INSERT INTO taxon (scientific_name, common_name, taxon_rank, ncbi_tax_id, extinct, path) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
    
        Map<String,Object> map = new HashMap<>();
        map.put("scientific_name", taxon.getScientificName());
        map.put("common_name", taxon.getCommonName());
        map.put("taxon_rank", taxon.getTaxonRank());
        map.put("ncbi_tax_id", taxon.getNcbiTaxId());
        map.put("extinct", taxon.isExtinct());
        map.put("path", taxon.getPath());
    
        int result = template.update(sql, map);
    
        if (result > 0) {
            log.info(result + " new row(s) has been inserted.");
        } else {
            log.error("No new row has been inserted.");
        }
    }
}
