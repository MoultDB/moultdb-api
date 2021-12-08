package org.moultdb.api.repository.mysql;

import org.moultdb.api.repository.dao.ArticleDAO;
import org.moultdb.api.repository.dto.ArticleTO;
import org.moultdb.api.repository.dto.TransfertObject;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
@Repository
public class MySQLArticleDAO implements ArticleDAO {
    
    NamedParameterJdbcTemplate template;
    
    private static final String SELECT_STATEMENT = "SELECT a.* FROM article a ";
    
    public MySQLArticleDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public List<ArticleTO> findAll() {
        return template.query(SELECT_STATEMENT, new ArticleRowMapper());
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
                new MapSqlParameterSource().addValue("ids", ids), new ArticleRowMapper());
    }
    
    @Override
    public Map<Integer, Set<ArticleTO>> findBySampleIds(Set<Integer> sampleIds) {
        if (sampleIds == null || sampleIds.size() == 0 || sampleIds.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("An ID can not be null");
        }
    
        return template.query("SELECT * FROM article a " +
                               "INNER JOIN article_sample asa ON a.id = asa.article_id " +
                               "WHERE asa.sample_id IN (:ids)",
                               new MapSqlParameterSource().addValue("ids", sampleIds), new SampleArticleRowMapper()).stream()
                       .collect(Collectors.toMap(Map.Entry::getKey,
                               e -> new HashSet<>(Arrays.asList(e.getValue())),
                               (v1, v2) -> { v1.addAll(v2); return v1; }));
    }
    
    @Override
    public Map<Integer, Set<ArticleTO>> findByTaxonIds(Set<Integer> taxonIds) {
        if (taxonIds == null || taxonIds.size() == 0 || taxonIds.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("An ID can not be null");
        }

        return template.query("SELECT * FROM article a " +
                                       "INNER JOIN article_taxon at ON a.id = at.article_id " +
                                       "WHERE at.taxon_id IN (:ids)",
                               new MapSqlParameterSource().addValue("ids", taxonIds), new TaxonArticleRowMapper()).stream()
                       .collect(Collectors.toMap(Map.Entry::getKey,
                               e -> new HashSet<>(Arrays.asList(e.getValue())),
                               (v1, v2) -> { v1.addAll(v2); return v1; }));
    }
    
    private static class ArticleRowMapper implements RowMapper<ArticleTO> {
        @Override
        public ArticleTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ArticleTO(rs.getInt("a.id"), rs.getString("a.title"), rs.getString("a.authors"));
        }
    }
    
    private static class SampleArticleRowMapper implements RowMapper<Map.Entry<Integer, ArticleTO>> {
        @Override
        public Map.Entry<Integer, ArticleTO> mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new AbstractMap.SimpleEntry<>(rs.getInt("asa.id"),
                    new ArticleTO(rs.getInt("a.id"), rs.getString("a.title"), rs.getString("a.authors")));
        }
    }
    
    private static class TaxonArticleRowMapper implements RowMapper<Map.Entry<Integer, ArticleTO>> {
        @Override
        public Map.Entry<Integer, ArticleTO> mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new AbstractMap.SimpleEntry<>(rs.getInt("at.taxon_id"),
                    new ArticleTO(rs.getInt("a.id"), rs.getString("a.title"), rs.getString("a.authors")));
        }
    }
}
