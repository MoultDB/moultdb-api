package org.moultdb.api.repository.mysql;

import org.moultdb.api.repository.dao.ArticleToTaxonDAO;
import org.moultdb.api.repository.dto.ArticleTO;
import org.moultdb.api.repository.dto.ArticleToTaxonTO;
import org.moultdb.api.repository.dto.TaxonTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
@Repository
public class MySQLArticleToTaxonDAO implements ArticleToTaxonDAO {
    
    NamedParameterJdbcTemplate template;
    
    private static final String SELECT_STATEMENT = "SELECT at.*, a.*, t.* FROM article_taxon at " +
            "INNER JOIN article a ON (at.article_id = a.id) " +
            "INNER JOIN taxon t ON (at.taxon_id = t.id) ";
    
    public MySQLArticleToTaxonDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public List<ArticleToTaxonTO> findAll() {
        return template.query(SELECT_STATEMENT, new ArticleToTaxonRowMapper());
    }
    
    @Override
    public List<ArticleToTaxonTO> findByTaxonId(int taxonId) {
        return template.query(SELECT_STATEMENT + "WHERE at.taxon_id = :id",
                new MapSqlParameterSource().addValue("id", taxonId), new ArticleToTaxonRowMapper());
    }
    
    private static class ArticleToTaxonRowMapper implements RowMapper<ArticleToTaxonTO> {
        @Override
        public ArticleToTaxonTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ArticleToTaxonTO(
                    new ArticleTO(rs.getInt("a.id"), rs.getString("a.title"), rs.getString("a.authors")),
                    new TaxonTO(rs.getInt("t.id"), rs.getString("t.scientific_name"), rs.getString("t.common_name"),
                            rs.getInt("t.db_xref_id"), rs.getInt("t.parentTaxonId"), rs.getString("t.taxon_rank"),
                            rs.getBoolean("t.extinct"), rs.getString("t.path")),
                    rs.getInt("authors"));
        }
    }
}
