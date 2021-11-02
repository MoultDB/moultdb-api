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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
@Repository
public class MySQLArticleDAO implements ArticleDAO {
    
    NamedParameterJdbcTemplate template;
    
    private static final String SELECT_STATEMENT = "SELECT * FROM article ";
    
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
        if (ids == null || ids.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("An ID can not be null");
        }
        return template.query(SELECT_STATEMENT + "WHERE id IN (:ids)",
                new MapSqlParameterSource().addValue("ids", ids), new ArticleRowMapper());
    }
    
    private static class ArticleRowMapper implements RowMapper<ArticleTO> {
        @Override
        public ArticleTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ArticleTO(rs.getInt("a.id"), rs.getString("a.title"), rs.getString("a.authors"));
        }
    }
}
