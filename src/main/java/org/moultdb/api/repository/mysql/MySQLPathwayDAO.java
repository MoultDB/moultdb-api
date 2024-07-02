package org.moultdb.api.repository.mysql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
            "LEFT JOIN article a ON a.id = p.article_id "+
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
    public void batchUpdate(Set<PathwayTO> pathwayTOs) {
        String insertStmt = "INSERT INTO pathway (id, name, description, article_id) " +
                "VALUES (:id, :name, :description, :articleId) " +
                "AS new " +
                "ON DUPLICATE KEY UPDATE name = new.name, description = new.description, article_id = new.article_id";
        List<MapSqlParameterSource> params = new ArrayList<>();
        for (PathwayTO pathwayTO : pathwayTOs) {
            MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue("id", pathwayTO.getId());
            source.addValue("name", pathwayTO.getName());
            source.addValue("description", pathwayTO.getDescription());
            source.addValue("articleId", pathwayTO.getArticleTO() == null?
                    null : pathwayTO.getArticleTO().getId());
            params.add(source);
        }
        template.batchUpdate(insertStmt, params.toArray(MapSqlParameterSource[]::new));
        logger.info("'pathway' table updated");
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
                    DbXrefTO articleDbXrefTO = new MySQLDbXrefDAO.DbXrefRowMapper().mapRow(rs, rs.getRow(), "dx", "ds");
                    Set<DbXrefTO> articleDbXrefTOs = articleTO == null? new HashSet<>() : new HashSet<>(articleTO.getDbXrefTOs());
                    if (articleDbXrefTO != null && articleDbXrefTO.getAccession() != null) {
                        articleDbXrefTOs.add(articleDbXrefTO);
                    }
                    articleTO = new ArticleTO(rs.getInt("a.id"), rs.getString("a.citation"),
                            rs.getString("a.title"), rs.getString("a.authors"), articleDbXrefTOs);
                }
                pathwayTOs.put(id, new PathwayTO(rs.getString("p.id"), rs.getString("p.name"),
                        rs.getString("p.description"), articleTO));
            }
            return new ArrayList<>(pathwayTOs.values());
        }
    }
    
    protected static class PathwayRowMapper implements RowMapper<PathwayTO> {
        @Override
        public PathwayTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            if (rs.getString("p.id") != null) {
                return new PathwayTO(rs.getString("p.id"), rs.getString("p.name"), rs.getString("p.description"), null);
            }
            return null;
        }
    }
}
