//package org.moultdb.api.repository.mysql;
//
//import org.moultdb.api.repository.dao.ArticleToTaxonDAO;
//import org.moultdb.api.repository.dto.ArticleToTaxonTO;
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
// * @since 2021-10-25
// */
//@Repository
//public class MySQLArticleToTaxonDAO implements ArticleToTaxonDAO {
//
//    NamedParameterJdbcTemplate template;
//
//    private static final String SELECT_STATEMENT = "SELECT * FROM article_taxon ";
//
//    public MySQLArticleToTaxonDAO(NamedParameterJdbcTemplate template) {
//        this.template = template;
//    }
//
//    @Override
//    public List<ArticleToTaxonTO> findAll() {
//        return template.query(SELECT_STATEMENT, new ArticleToTaxonRowMapper());
//    }
//
//    @Override
//    public List<ArticleToTaxonTO> findByTaxonId(Integer taxonId) {
//        return findByTaxonIds(Collections.singleton(taxonId));
//    }
//
//    @Override
//    public List<ArticleToTaxonTO> findByTaxonIds(Set<Integer> taxonIds) {
//        if (taxonIds == null || taxonIds.stream().anyMatch(Objects::isNull)) {
//            throw new IllegalArgumentException("An ID can not be null");
//        }
//        return template.query(SELECT_STATEMENT + "WHERE taxon_id IN (:ids)",
//                new MapSqlParameterSource().addValue("ids", taxonIds), new ArticleToTaxonRowMapper());
//    }
//
//    private static class ArticleToTaxonRowMapper implements RowMapper<ArticleToTaxonTO> {
//        @Override
//        public ArticleToTaxonTO mapRow(ResultSet rs, int rowNum) throws SQLException {
//            return new ArticleToTaxonTO(rs.getInt("article_id"), rs.getInt("taxon_id"), rs.getInt("version_id"));
//        }
//    }
//}
