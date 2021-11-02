package org.moultdb.api.repository.mysql;

import org.moultdb.api.repository.dao.ArticleToDbXrefDAO;
import org.moultdb.api.repository.dto.ArticleTO;
import org.moultdb.api.repository.dto.ArticleToDbXrefTO;
import org.moultdb.api.repository.dto.DataSourceTO;
import org.moultdb.api.repository.dto.DbXrefTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Valentine Rech de Laval
 * @since 2021-11-01
 */
@Repository
public class MySQLArticleToDbXrefDAO implements ArticleToDbXrefDAO {
    
    NamedParameterJdbcTemplate template;
    
    private static final String SELECT_STATEMENT =
            "SELECT ax.*, a.*, ds.* FROM article_db_xref ax " +
                    "INNER JOIN article a ON (ax.article_id = a.id) " +
                    "INNER JOIN db_xref x ON (ax.db_xref_id = x.id)" +
                    "INNER JOIN data_source ds ON (x.data_source_id = ds.id)";
    
    public MySQLArticleToDbXrefDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    
    @Override
    public List<ArticleToDbXrefTO> findAll() {
        return template.query(SELECT_STATEMENT, new ArticleToDbXrefRowMapper());
    }
    
    private static class ArticleToDbXrefRowMapper implements RowMapper<ArticleToDbXrefTO> {
        @Override
        public ArticleToDbXrefTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ArticleToDbXrefTO(
                    new ArticleTO(rs.getInt("a.id"), rs.getString("a.title"), rs.getString("a.authors")),
                    new DbXrefTO(rs.getInt("x.id"), rs.getString("x.accession"),
                            new DataSourceTO(rs.getInt("ds.id"), rs.getString("ds.name"), rs.getString("ds.description"),
                                    rs.getString("ds.base_url") , rs.getDate("ds.release_date"), rs.getString("ds.release_version") )));
        
        }
    }
}
