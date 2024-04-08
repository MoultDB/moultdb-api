package org.moultdb.api.repository.mysql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.exception.MoultDBException;
import org.moultdb.api.repository.dao.GeneToDomainDAO;
import org.moultdb.api.repository.dto.*;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author Valentine Rech de Laval
 * @since 2024-04-04
 */
@Repository
public class MySQLGeneToDomainDAO implements GeneToDomainDAO {
    
    private final static Logger logger = LogManager.getLogger(MySQLGeneToDomainDAO.class.getName());
    
    NamedParameterJdbcTemplate template;
    
    private static final String SELECT_STATEMENT = "SELECT * FROM gene_domain gd " +
            "LEFT JOIN domain d ON d.id = gd.domain_id ";
    
    public MySQLGeneToDomainDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public List<GeneToDomainTO> findAll() {
        return template.query(SELECT_STATEMENT, new GeneToDomainRowMapper());
    }
    
    @Override
    public List<GeneToDomainTO> findByGeneId(Integer geneId) {
        return findByGeneIds(Collections.singleton(geneId));
    }
    
    @Override
    public List<GeneToDomainTO> findByGeneIds(Collection<Integer> geneIds) {
        if (geneIds == null || geneIds.isEmpty() || geneIds.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("An ID can not be null");
        }
        return template.query(SELECT_STATEMENT + "WHERE gd.gene_id IN (:gene_ids)",
                new MapSqlParameterSource().addValue("gene_ids", geneIds), new GeneToDomainRowMapper());
    }
    
    @Override
    public void insert(GeneToDomainTO geneToDomainTO) {
        batchUpdate(Collections.singleton(geneToDomainTO));
    }
    
    @Override
    public void batchUpdate(Set<GeneToDomainTO> geneToDomainTOs) {
        String geneSql = "INSERT INTO gene_domain (gene_id, domain_id, start, end) " +
                "VALUES (:gene_id, :domain_id, :start, :end) " +
                "AS new " +
                "ON DUPLICATE KEY UPDATE gene_id = new.gene_id"; // Avoid errors when a duplicate is found
        
        List<MapSqlParameterSource> params = new ArrayList<>();
        for (GeneToDomainTO geneToDomainTO : geneToDomainTOs) {
            MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue("gene_id", geneToDomainTO.getGeneId());
            source.addValue("domain_id", geneToDomainTO.getDomainTO() == null ? null : geneToDomainTO.getDomainTO().getId());
            source.addValue("start", geneToDomainTO.getStart());
            source.addValue("end", geneToDomainTO.getEnd());
            params.add(source);
        }
        
        try {
            template.batchUpdate(geneSql, params.toArray(MapSqlParameterSource[]::new));
            logger.debug("'gene_domain' table updated.");
        } catch (Exception e) {
            throw new MoultDBException("Insertion of gene_domain(s) failed: " + e.getMessage());
        }
    }
    
    protected static class GeneToDomainRowMapper implements RowMapper<GeneToDomainTO> {
        @Override
        public GeneToDomainTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            DomainTO domainTO = new MySQLDomainDAO.DomainRowMapper().mapRow(rs, rs.getRow());
            return new GeneToDomainTO(rs.getInt("gd.gene_id"), domainTO,
                    rs.getInt("gd.start"), rs.getInt("gd.end"));
        }
    }
}
