package org.moultdb.api.repository.dao;

import org.moultdb.api.repository.dto.ArticleToDbXrefTO;

import java.util.List;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
public interface ArticleToDbXrefDAO extends DAO<ArticleToDbXrefTO> {
    
    List<ArticleToDbXrefTO> findAll();
    
}
