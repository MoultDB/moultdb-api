package org.moultdb.api.repository.dao;

import org.moultdb.api.repository.dto.ArticleTO;

import java.util.List;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
public interface ArticleDAO extends DAO<ArticleTO> {
    
    List<ArticleTO> findAll();
    
    ArticleTO findById(Integer id);
    
    List<ArticleTO> findByIds(Set<Integer> ids);
    
    ArticleTO findByCitation(String citation);
    
    ArticleTO findByDbXref(String accession, Integer dataSourceId);
    
    void insert(ArticleTO articleTO);
    
    void batchUpdate(Set<ArticleTO> articleTOs);
    
    Integer getLastId();
}
