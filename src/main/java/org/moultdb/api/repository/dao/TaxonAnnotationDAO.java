package org.moultdb.api.repository.dao;

import org.moultdb.api.repository.dto.TaxonAnnotationTO;

import java.util.List;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
public interface TaxonAnnotationDAO extends DAO<TaxonAnnotationTO> {
    
    List<TaxonAnnotationTO> findAll();
    
    List<TaxonAnnotationTO> findByUser(String username);
    
    Integer getLastId();
    
    int insert(TaxonAnnotationTO taxonAnnotationTO);
    
    int insertImageTaxonAnnotation(TaxonAnnotationTO taxonAnnotationTO);
    
    int[] batchUpdate(Set<TaxonAnnotationTO> taxonAnnotationTOs);
    
}