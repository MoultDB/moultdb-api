package org.moultdb.api.repository.dao;

import org.moultdb.api.repository.dto.TaxonTO;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-14
 */
public interface TaxonDAO extends DAO<TaxonTO> {
    
    Long countAll();
    
    List<TaxonTO> findAll();
    
    /**
     * Retrieves taxa with pagination to avoid memory issues
     */
    List<TaxonTO> findAllPaginated(int pageNumber, int batchSize);

    /**
     * Process all taxa in batches to avoid memory issues.
     */
    void processAllInBatches(int batchSize, Consumer<List<TaxonTO>> consumer);
    
    List<TaxonTO> findByText(String searchedText);
    
    TaxonTO findByScientificName(String taxonScientificName);
    
    TaxonTO findBySynonym(String taxonName);
    
    TaxonTO findByAccession(String accession, String datasourceName);
    
    TaxonTO findByPath(String id);
    
    List<TaxonTO> findByPaths(Set<String> taxonIds);
    
    List<TaxonTO> findLineageByPath(String taxonPath);
    
    List<TaxonTO> findDirectChildrenByPath(String taxonPath);
    
    List<String> findAllPathsHavingChildren();
    
    List<String> findAllPaths();
    
    void insert(TaxonTO taxon);
    
    int batchUpdate(Set<TaxonTO> taxonTOs);
}
