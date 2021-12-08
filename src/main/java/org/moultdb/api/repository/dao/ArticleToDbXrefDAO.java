package org.moultdb.api.repository.dao;

import org.moultdb.api.repository.dto.ArticleToDbXrefTO;

import java.util.List;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
public interface ArticleToDbXrefDAO extends DAO<ArticleToDbXrefTO> {
    
    List<ArticleToDbXrefTO> findAll();
    
    List<ArticleToDbXrefTO> findByArticleId(Integer articleId);
    
    List<ArticleToDbXrefTO> findByArticleIds(Set<Integer> articleIds);
}
