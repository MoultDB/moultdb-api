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
    
    List<TaxonAnnotationTO> findLast(int limit);
    
    List<TaxonAnnotationTO> findByUser(String username, Integer limit);
    
    List<TaxonAnnotationTO> findByTaxonPath(String taxonPath);

    TaxonAnnotationTO findByImageFilename(String imageFilename);
    
    Integer getLastId();
    
    int insertImageTaxonAnnotation(TaxonAnnotationTO taxonAnnotationTO);
    
    int[] batchUpdate(Set<TaxonAnnotationTO> taxonAnnotationTOs);
    
    void deleteByImageFilename(String imageFilename);
}
