package org.moultdb.api.repository.mysql;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.repository.dao.ArticleDAO;
import org.moultdb.api.repository.dto.ArticleTO;
import org.moultdb.api.repository.dto.DbXrefTO;
import org.moultdb.api.repository.dto.TransfertObject;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
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
import java.util.Objects;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
@Repository
public class MySQLArticleDAO implements ArticleDAO {
    
    private final static Logger logger = LogManager.getLogger(MySQLArticleDAO.class.getName());
    
    NamedParameterJdbcTemplate template;
    
    private static final String SELECT_STATEMENT = "SELECT a.*, ax.*, x.*, ds.* FROM article a " +
            "INNER JOIN article_db_xref ax ON a.id = ax.article_id " +
            "INNER JOIN db_xref x ON ax.db_xref_id = x.id " +
            "INNER JOIN data_source ds ON (x.data_source_id = ds.id) ";
    
    public MySQLArticleDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public List<ArticleTO> findAll() {
        return template.query(SELECT_STATEMENT, new ArticleResultSetExtractor());
    }
    
    @Override
    public ArticleTO findById(Integer id) {
        return TransfertObject.getOneTO(findByIds(Collections.singleton(id)));
    }
    
    @Override
    public List<ArticleTO> findByIds(Set<Integer> ids) {
        if (ids == null || ids.size() == 0 || ids.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("An ID can not be null");
        }
        return template.query(SELECT_STATEMENT + "WHERE id IN (:ids)",
                new MapSqlParameterSource().addValue("ids", ids), new ArticleResultSetExtractor());
    }
    
    @Override
    public ArticleTO findByCitation(String citation) {
        if (StringUtils.isBlank(citation)) {
            throw new IllegalArgumentException("A citation can not be null");
        }
        return TransfertObject.getOneTO(template.query(SELECT_STATEMENT + "WHERE citation = :citation",
                new MapSqlParameterSource().addValue("citation", citation), new ArticleResultSetExtractor()));
    }
    
    @Override
    public int insert(ArticleTO articleTO) {
        int[] ints = batchUpdate(Collections.singleton(articleTO));
        return ints[0];
    }
    
    @Transactional
    @Override
    public int[] batchUpdate(Set<ArticleTO> articleTOs) {
        String insertStmt = "INSERT INTO article (id, citation, title, authors) " +
                "VALUES (:id, :citation, :title, :authors) " +
                "AS new " +
                "ON DUPLICATE KEY UPDATE citation = new.citation, title = new.title, authors = new.authors ";
        List<MapSqlParameterSource> params = new ArrayList<>();
        for (ArticleTO articleTO : articleTOs) {
            MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue("id", articleTO.getId());
            source.addValue("citation", articleTO.getCitation());
            source.addValue("title", articleTO.getTitle());
            source.addValue("authors", articleTO.getAuthors());
            params.add(source);
        }
        int[] ints = template.batchUpdate(insertStmt, params.toArray(MapSqlParameterSource[]::new));
        logger.info(Arrays.stream(ints).sum()+ " new row(s) in 'article' table.");
        return ints;
    }
    
    @Override
    public Integer getLastId() {
        String sql = "SELECT id FROM article ORDER BY id DESC LIMIT 1";
        try {
            return template.queryForObject(sql, new MapSqlParameterSource(), Integer.class);
        } catch (EmptyResultDataAccessException e) {
            logger.debug("No record found in 'article' table");
            return 0;
        }
    }
    
    private static class ArticleResultSetExtractor implements ResultSetExtractor<List<ArticleTO>> {
        
        @Override
        public List<ArticleTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Integer, ArticleTO> articles = new HashMap<>();
            while(rs.next()) {
                Integer articleId = rs.getInt("a.id");
                ArticleTO articleTO = articles.get(articleId);
                
                // Build DbXrefs
                DbXrefTO dbXrefTO = new MySQLDbXrefDAO.DbXrefRowMapper().mapRow(rs, rs.getRow());
                Set<DbXrefTO> dbXrefTOs = articleTO == null ? null: articleTO.getDbXrefTOs();
                if (dbXrefTOs == null) {
                    dbXrefTOs = new HashSet<>();
                }
                dbXrefTOs.add(dbXrefTO);
                
                // Build ArticleTO. Even if it already exists, we create a new one because it's an unmutable object
                articleTO = new ArticleTO(rs.getInt("a.id"), rs.getString("a.citation"),
                        rs.getString("a.title"), rs.getString("a.authors"), dbXrefTOs);
                articles.put(articleId, articleTO);
            }
            return new ArrayList<>(articles.values());
        }
    }
}
