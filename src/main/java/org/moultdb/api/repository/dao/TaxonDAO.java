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

    List<TaxonTO> findByText(String searchedText);
    
    TaxonTO findByScientificName(String taxonScientificName);
    
    TaxonTO findByAccession(String accession, String datasourceName);
    
    TaxonTO findByPath(String id);
    
    List<TaxonTO> findByPaths(Set<String> taxonIds);
    
    List<TaxonTO> findLineageByPath(String taxonPath);
    
    void insert(TaxonTO taxon);
    
    void batchUpdate(Set<TaxonTO> taxonTOs);
}
