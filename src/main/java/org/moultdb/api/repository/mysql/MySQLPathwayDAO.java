package org.moultdb.api.repository.mysql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.exception.MoultDBException;
import org.moultdb.api.repository.dao.DAO;
import org.moultdb.api.repository.dao.PathwayDAO;
import org.moultdb.api.repository.dto.*;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author Valentine Rech de Laval
 * @since 2024-03-22
 */
@Repository
public class MySQLPathwayDAO implements PathwayDAO {
    
    private final static Logger logger = LogManager.getLogger(MySQLPathwayDAO.class.getName());
    
    NamedParameterJdbcTemplate template;
    
    private static final String SELECT_STATEMENT = "SELECT * FROM pathway p " +
            "LEFT JOIN pathway_figure pf ON pf.pathway_id = p.id " +
            "LEFT JOIN article a ON a.id = p.article_id " +
            "LEFT JOIN article_db_xref adx ON a.id = adx.article_id " +
            "LEFT JOIN db_xref dx ON adx.db_xref_id = dx.id " +
            "LEFT JOIN data_source ds ON (dx.data_source_id = ds.id) ";
    
    public MySQLPathwayDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public List<PathwayTO> findAll() {
        return template.query(SELECT_STATEMENT, new PathwayResultSetExtractor());
    }
    
    @Override
    public Map<PathwayTO, OrthogroupTO> findPathwayOrthogroups() {
        String sql = "SELECT DISTINCT p.*, og.* " +
                "FROM pathway p " +
                "INNER JOIN gene g ON g.pathway_id = p.id " +
                "INNER JOIN orthogroup og on og.id = g.orthogroup_id ";
        
        // TODO: remove this warning when the issue is fixed
        logger.warn("When retrieving pathways with orthogroups, the pathways do not include articles or figure IDs.");
        
        return template.query(sql, rs -> {
            Map<PathwayTO, OrthogroupTO> pathwayMap = new HashMap<>();
            
            while (rs.next()) {
                PathwayTO pathwayTO = new PathwayTO(rs.getString("p.id"), rs.getString("p.name"), rs.getString("p.description"), null, null);
                OrthogroupTO orthogroupTO = new OrthogroupTO(rs.getInt("og.id"), rs.getString("og.name"));
                pathwayMap.put(pathwayTO, orthogroupTO);
            }
            return pathwayMap;
        });
    }
    
    @Override
    public PathwayTO findById(String id) {
        return TransfertObject.getOneTO(template.query(SELECT_STATEMENT + "WHERE LOWER(p.id) = LOWER(:id)",
                new MapSqlParameterSource().addValue("id", id), new PathwayResultSetExtractor()));
    }
    
    @Override
    public List<PathwayTO> findByIds(Set<String> ids) {
        if (ids == null || ids.isEmpty() || ids.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("An ID can not be null");
        }
        return template.query(SELECT_STATEMENT + "WHERE p.id IN (:ids)",
                new MapSqlParameterSource().addValue("ids", ids), new PathwayResultSetExtractor());
    }
    
    @Override
    public void insert(PathwayTO pathwayTO) {
        batchUpdate(Collections.singleton(pathwayTO));
    }
    
    @Override
    public int batchUpdate(Set<PathwayTO> pathwayTOs) {
        String pathwaySql = "INSERT INTO pathway (id, name, description, article_id) " +
                "VALUES (:id, :name, :description, :articleId) " +
                "AS new " +
                "ON DUPLICATE KEY UPDATE name = new.name, description = new.description, article_id = new.article_id";
        
        String figureSql = "INSERT INTO pathway_figure (pathway_id, figure_id) " +
                "VALUES (:pathway_id, :figure_id) " +
                "AS new " +
                "ON DUPLICATE KEY UPDATE figure_id = new.figure_id ";
        
        List<MapSqlParameterSource> pathwayParams = new ArrayList<>();
        List<MapSqlParameterSource> figureParams = new ArrayList<>();
        
        for (PathwayTO pathwayTO : pathwayTOs) {
            MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue("id", pathwayTO.getId());
            source.addValue("name", pathwayTO.getName());
            source.addValue("description", pathwayTO.getDescription());
            source.addValue("articleId", pathwayTO.getArticleTO() == null?
                    null : pathwayTO.getArticleTO().getId());
            pathwayParams.add(source);
            
            for (Integer id : pathwayTO.getFigureIds()) {
                MapSqlParameterSource taxonSource = new MapSqlParameterSource();
                taxonSource.addValue("pathway_id", pathwayTO.getId());
                taxonSource.addValue("figure_id", id);
                figureParams.add(taxonSource);
            }
        }
        int[] ints;
        try {
            ints = template.batchUpdate(pathwaySql, pathwayParams.toArray(MapSqlParameterSource[]::new));
            logger.debug("'pathway' table updated");
            
            template.batchUpdate(figureSql, figureParams.toArray(MapSqlParameterSource[]::new));
            logger.debug("'pathway_figure' table updated");
            
        } catch (Exception e) {
            throw new MoultDBException("Insertion of pathways failed: " + e.getMessage());
        }
        return Arrays.stream(ints).sum();
    }
    
    private static class PathwayResultSetExtractor implements ResultSetExtractor<List<PathwayTO>> {
        @Override
        public List<PathwayTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
            
            Map<String, PathwayTO> pathwayTOs = new HashMap<>();
            while(rs.next()) {
                String id = rs.getString("p.id");
                PathwayTO pathwayTO = pathwayTOs.get(id);
                
                ArticleTO articleTO = null;
                if (DAO.getInteger(rs, "p.article_id") != null) {
                    if (pathwayTO != null) {
                        articleTO = pathwayTO.getArticleTO();
                    }
                    // Build DbXrefs
                    Set<DbXrefTO> articleDbXrefTOs = articleTO == null? new HashSet<>() : new HashSet<>(articleTO.getDbXrefTOs());
                    DbXrefTO articleDbXrefTO = new MySQLDbXrefDAO.DbXrefRowMapper().mapRow(rs, rs.getRow(), "dx", "ds");
                    if (articleDbXrefTO != null) {
                        articleDbXrefTOs.add(articleDbXrefTO);
                    }
                    articleTO = new ArticleTO(rs.getInt("a.id"), rs.getString("a.citation"),
                            rs.getString("a.title"), rs.getString("a.authors"), articleDbXrefTOs);
                }
                
                Set<Integer> figureIds = null;
                if (DAO.getInteger(rs, "pf.figure_id") != null) {
                    figureIds = new HashSet<>();
                    if (pathwayTO != null) {
                        figureIds.addAll(pathwayTO.getFigureIds());
                    }
                    figureIds.add(rs.getInt("pf.figure_id"));
                }
                
                pathwayTOs.put(id, new PathwayTO(rs.getString("p.id"), rs.getString("p.name"),
                        rs.getString("p.description"), articleTO, figureIds));
            }
            return new ArrayList<>(pathwayTOs.values());
        }
    }
    
    //FIXME : to be removed, keep temporarily for external usage
    protected static class PathwayRowMapper implements RowMapper<PathwayTO> {
        @Override
        public PathwayTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            if (rs.getString("p.id") != null) {
                return new PathwayTO(rs.getString("p.id"), rs.getString("p.name"), rs.getString("p.description"), null, null);
            }
            return null;
        }
    }
}
