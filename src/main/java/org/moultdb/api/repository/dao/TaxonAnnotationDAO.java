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
    
    List<TaxonAnnotationTO> findLastCreated(int limit);
    
    List<TaxonAnnotationTO> findLastUpdated(int limit);
    
    List<TaxonAnnotationTO> findByUsername(String username, Integer limit);
    
    List<TaxonAnnotationTO> findByTaxonPath(String taxonPath);

    Integer getLastId();
    
    int insertTaxonAnnotation(TaxonAnnotationTO taxonAnnotationTO);
    
    int batchUpdate(Set<TaxonAnnotationTO> taxonAnnotationTOs);
}
