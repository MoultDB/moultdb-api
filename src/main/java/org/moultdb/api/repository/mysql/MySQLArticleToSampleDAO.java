//package org.moultdb.api.repository.mysql;
//
//import org.moultdb.api.repository.dao.ArticleToSampleDAO;
//import org.moultdb.api.repository.dto.ArticleToDbXrefTO;
//import org.moultdb.api.repository.dto.ArticleToSampleTO;
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
//import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
//import org.springframework.stereotype.Repository;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.List;
//import java.util.Objects;
//import java.util.Set;
//
///**
// * @author Valentine Rech de Laval
// * @since 2021-10-25
// */
//@Repository
//public class MySQLArticleToSampleDAO implements ArticleToSampleDAO {
//
//    NamedParameterJdbcTemplate template;
//
//    private static final String SELECT_STATEMENT = "SELECT * FROM article_sample ";
//
//    public MySQLArticleToSampleDAO(NamedParameterJdbcTemplate template) {
//        this.template = template;
//    }
//
//    @Override
//    public List<ArticleToSampleTO> findAll() {
//        return template.query(SELECT_STATEMENT, new ArticleToSampleRowMapper());
//    }
//
//    @Override
//    public List<ArticleToSampleTO> findBySampleId(int sampleId) {
//        return template.query(SELECT_STATEMENT + "WHERE sample_id = :id",
//                new MapSqlParameterSource().addValue("id", sampleId), new ArticleToSampleRowMapper());
//    }
//
//    @Override
//    public List<ArticleToSampleTO> findBySampleIds(Set<Integer> sampleIds) {
//        if (sampleIds == null || sampleIds.stream().anyMatch(Objects::isNull)) {
//            throw new IllegalArgumentException("An ID can not be null");
//        }
//        return template.query(SELECT_STATEMENT + "WHERE sample_id IN (:ids)",
//                new MapSqlParameterSource().addValue("ids", sampleIds), new ArticleToSampleRowMapper());
//    }
//
//    private static class ArticleToSampleRowMapper implements RowMapper<ArticleToSampleTO> {
//        @Override
//        public ArticleToSampleTO mapRow(ResultSet rs, int rowNum) throws SQLException {
//
//            return new ArticleToSampleTO(rs.getInt("article_id"), rs.getInt("sample_id"));
//        }
//    }
//}
