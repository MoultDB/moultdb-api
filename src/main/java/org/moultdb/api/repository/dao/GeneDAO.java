package org.moultdb.api.repository.dao;

import org.moultdb.api.repository.dto.GeneTO;

import java.util.List;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2024-03-22
 */
public interface GeneDAO extends DAO<GeneTO> {
    
    List<GeneTO> findAll();
    
    GeneTO findById(String id);
    
    List<GeneTO> findByIds(Set<String> ids);
    
    List<GeneTO> findByPathwayId(String pathwayId);
    
    GeneTO findByProteinId(String proteinId);

    Integer getLastId();
    
    void insert(GeneTO geneTO);
    
    void batchUpdate(Set<GeneTO> geneTOs);
}
