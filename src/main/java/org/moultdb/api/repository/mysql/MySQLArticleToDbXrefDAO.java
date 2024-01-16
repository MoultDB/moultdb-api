package org.moultdb.api.repository.mysql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.repository.dao.ArticleToDbXrefDAO;
import org.moultdb.api.repository.dto.ArticleToDbXrefTO;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2021-11-01
 */
@Repository
public class MySQLArticleToDbXrefDAO implements ArticleToDbXrefDAO {
    
    private final static Logger logger = LogManager.getLogger(MySQLArticleToDbXrefDAO.class.getName());

    NamedParameterJdbcTemplate template;

    public MySQLArticleToDbXrefDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public void insert(ArticleToDbXrefTO articleToDbXrefTO) {
        batchUpdate(Collections.singleton(articleToDbXrefTO));
    }
    
    @Override
    public void batchUpdate(Set<ArticleToDbXrefTO> articleToDbXrefTOs) {
        String insertStmt = "INSERT INTO article_db_xref (article_id, db_xref_id) " +
                "VALUES (:article_id, :db_xref_id) " +
                "AS new " +
                "ON DUPLICATE KEY UPDATE article_id = new.article_id";
        List<MapSqlParameterSource> params = new ArrayList<>();
        for (ArticleToDbXrefTO articleToDbXrefTO : articleToDbXrefTOs) {
            MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue("article_id", articleToDbXrefTO.getArticleId());
            source.addValue("db_xref_id", articleToDbXrefTO.getDbXrefId());
            params.add(source);
        }
        int[] ints = template.batchUpdate(insertStmt, params.toArray(MapSqlParameterSource[]::new));
        logger.info("'article_db_xref' table updated.");
    }
}
