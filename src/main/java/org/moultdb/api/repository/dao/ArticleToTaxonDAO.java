package org.moultdb.api.repository.dao;

import org.moultdb.api.repository.dto.ArticleToTaxonTO;

import java.util.List;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
public interface ArticleToTaxonDAO extends DAO<ArticleToTaxonTO> {
    
    List<ArticleToTaxonTO> findAll();
    
    List<ArticleToTaxonTO> findByTaxonId(int taxonId);
    
}
