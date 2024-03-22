package org.moultdb.api.repository.mysql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.repository.dao.DAO;
import org.moultdb.api.repository.dao.GeneDAO;
import org.moultdb.api.repository.dto.*;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author Valentine Rech de Laval
 * @since 2024-03-22
 */
public class MySQLGeneDAO implements GeneDAO {
    
    private final static Logger logger = LogManager.getLogger(MySQLGeneDAO.class.getName());
    
    NamedParameterJdbcTemplate template;
    
    private static final String SELECT_STATEMENT = "SELECT * FROM gene g " +
            "LEFT JOIN data_source ds ON ds.id = g.data_source_id " +
            "LEFT JOIN gene_pathway gp ON gp.gene_id = g.id " +
            "LEFT JOIN pathway p ON p.id = gp.pathway_id ";
    
    public MySQLGeneDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public List<GeneTO> findAll() {
        return template.query(SELECT_STATEMENT, new GeneResultSetExtractor());
    }
    
    @Override
    public GeneTO findById(String id) {
        return TransfertObject.getOneTO(findByIds(Collections.singleton(id)));
    }
    
    @Override
    public List<GeneTO> findByIds(Set<String> ids) {
        if (ids == null || ids.size() == 0 || ids.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("An ID can not be null");
        }
        return template.query(SELECT_STATEMENT + "WHERE g.id IN (:ids)",
                new MapSqlParameterSource().addValue("ids", ids), new GeneResultSetExtractor());
    }
    
    @Override
    public List<GeneTO> findByPathwayId(String pathwayId) {
        return template.query(SELECT_STATEMENT + "WHERE LOWER(p.id) = LOWER(:pathwayId)",
                new MapSqlParameterSource().addValue("pathwayId", pathwayId), new GeneResultSetExtractor());
    }
    
//    id INT UNSIGNED NOT NULL,
//    gene_id VARCHAR(45),
//    gene_name VARCHAR(45),
//    locus_tag VARCHAR(45),
//    genome_acc VARCHAR(255) NOT NULL,
//    orthogroup_id INT UNSIGNED,
//    transcript_id VARCHAR(45),
//    transcript_url_suffix VARCHAR(255) NOT NULL,
//    protein_id VARCHAR(45) NOT NULL,
//    protein_description VARCHAR(255) NOT NULL,
//    protein_length SMALLINT UNSIGNED NOT NULL,
//    data_source_id SMALLINT UNSIGNED NOT NULL,
//    
    @Override
    public GeneTO findByProteinId(String proteinId) {
        return TransfertObject.getOneTO(
                template.query(SELECT_STATEMENT + "WHERE LOWER(g.protein_id) = LOWER(:proteinId)",
                        new MapSqlParameterSource().addValue("proteinId", proteinId), new GeneResultSetExtractor()));
    }
    
    @Override
    public Integer getLastId() {
        String sql = "SELECT id FROM gene ORDER BY id DESC LIMIT 1";
        try {
            return template.queryForObject(sql, new MapSqlParameterSource(), Integer.class);
        } catch (EmptyResultDataAccessException e) {
            logger.debug("No record found in 'gene' table");
            return 0;
        }
    }
    
    @Override
    public void insert(GeneTO geneTO) {
        batchUpdate(Collections.singleton(geneTO));
    }
    
    @Override
    public void batchUpdate(Set<GeneTO> geneTOs) {
        throw new UnsupportedOperationException("Method gene insertion not implemented");
    }
    
    private static class GeneResultSetExtractor implements ResultSetExtractor<List<GeneTO>> {
        
        @Override
        public List<GeneTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Integer, GeneTO> genes = new HashMap<>();
            while(rs.next()) {
                Integer id = rs.getInt("g.id");
                GeneTO geneTO = genes.get(id);
                
                // Build domains
                DomainTO domainTO = new MySQLDomainDAO.DomainRowMapper().mapRow(rs, rs.getRow());
                Set<DomainTO> domainTOs = geneTO == null ? null: geneTO.getDomainTOs();
                if (domainTOs == null) {
                    domainTOs = new HashSet<>();
                }
                domainTOs.add(domainTO);

                DataSourceTO dataSourceTO = new MySQLDataSourceDAO.DataSourceRowMapper().mapRow(rs, rs.getRow());
                
                PathwayTO pathwayTO = new MySQLPathwayDAO.PathwayRowMapper().mapRow(rs, rs.getRow());
                
                // Build GeneTO. Even if it already exists, we create a new one because it's an unmutable object
                geneTO = new GeneTO(id, rs.getString("g.gene_id"), rs.getString("g.gene_name"), rs.getString("g.locus_tag"),
                        rs.getString("g.genome_acc"), DAO.getInteger(rs, "g.orthogroup_id"), rs.getString("g.transcript_id"),
                        rs.getString("g.transcript_url_suffix"), rs.getString("g.protein_id"),
                        rs.getString("g.protein_description"), DAO.getInteger(rs, "g.protein_length"), dataSourceTO,
                        domainTOs, pathwayTO);
                genes.put(id, geneTO);
            }
            return new ArrayList<>(genes.values());
        }
    }
    
}
