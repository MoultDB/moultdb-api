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
    
    List<GeneTO> findByDomainId(String domainId);
    
    List<GeneTO> findByOrthogroupId(Integer orthogroupId);
    
    GeneTO findByProteinId(String proteinId);
    
    List<GeneTO> findByProteinIds(Set<String> proteinIds);
    
    List<GeneTO> findByTaxon(String taxonPath, Boolean inAMoultingPathway);

    Integer getLastId();
    
    void insert(GeneTO geneTO);
    
    void batchUpdate(Set<GeneTO> geneTOs);
    
    void deleteByIds(Set<Integer> dbGeneIds);
}
