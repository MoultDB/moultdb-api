//package org.moultdb.api.repository.mysql;
//
//import org.moultdb.api.repository.dao.ArticleToDbXrefDAO;
//import org.moultdb.api.repository.dto.ArticleToDbXrefTO;
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
//import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
//import org.springframework.stereotype.Repository;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.Collections;
//import java.util.List;
//import java.util.Objects;
//import java.util.Set;
//
///**
// * @author Valentine Rech de Laval
// * @since 2021-11-01
// */
//@Repository
//public class MySQLArticleToDbXrefDAO implements ArticleToDbXrefDAO {
//
//    NamedParameterJdbcTemplate template;
//
//    private static final String SELECT_STATEMENT = "SELECT * FROM article_db_xref ";
//
//    public MySQLArticleToDbXrefDAO(NamedParameterJdbcTemplate template) {
//        this.template = template;
//    }
//
//    @Override
//    public List<ArticleToDbXrefTO> findAll() {
//        return template.query(SELECT_STATEMENT, new ArticleToDbXrefRowMapper());
//    }
//
//    @Override
//    public List<ArticleToDbXrefTO> findByArticleId(Integer articleId) {
//        return findByArticleIds(Collections.singleton(articleId));
//    }
//
//    @Override
//    public List<ArticleToDbXrefTO> findByArticleIds(Set<Integer> articleIds) {
//        if (articleIds == null || articleIds.stream().anyMatch(Objects::isNull)) {
//            throw new IllegalArgumentException("An ID can not be null");
//        }
//        return template.query(SELECT_STATEMENT + "WHERE article_id IN (:ids)",
//                new MapSqlParameterSource().addValue("ids", articleIds), new ArticleToDbXrefRowMapper());
//    }
//
//    private static class ArticleToDbXrefRowMapper implements RowMapper<ArticleToDbXrefTO> {
//        @Override
//        public ArticleToDbXrefTO mapRow(ResultSet rs, int rowNum) throws SQLException {
//            return new ArticleToDbXrefTO(rs.getInt("article_id"), rs.getInt("db_xref_id"));
//        }
//    }
//}
