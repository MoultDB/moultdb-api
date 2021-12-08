package org.moultdb.api.repository.dao;

import org.moultdb.api.repository.dto.TaxonTO;

import java.util.List;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-14
 */
public interface TaxonDAO extends DAO<TaxonTO> {
    
    List<TaxonTO> findAll();
    
    List<TaxonTO> findByScientificName(String taxonScientificName);
    
    TaxonTO findById(int id);
    
    List<TaxonTO> findByIds(Set<Integer> taxonIds);
    
    void insertTaxon(TaxonTO taxon);
}
