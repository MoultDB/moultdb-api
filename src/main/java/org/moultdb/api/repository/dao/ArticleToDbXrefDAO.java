package org.moultdb.api.repository.dao;

import org.moultdb.api.repository.dto.ArticleToDbXrefTO;

import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
public interface ArticleToDbXrefDAO extends DAO<ArticleToDbXrefTO> {
    
    int insert(ArticleToDbXrefTO dbXrefTO);
    
    int[] batchUpdate(Set<ArticleToDbXrefTO> dbXrefTOs);
}
