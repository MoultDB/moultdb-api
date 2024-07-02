package org.moultdb.api.repository.dao;

import org.moultdb.api.repository.dto.GeneToDomainTO;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2024-04-04
 */
public interface GeneToDomainDAO extends DAO<GeneToDomainTO> {
    
    List<GeneToDomainTO> findAll();;
    
    List<GeneToDomainTO> findByGeneId(Integer geneId);
    
    List<GeneToDomainTO> findByGeneIds(Collection<Integer> geneIds);
    
    void insert(GeneToDomainTO geneTO);
    
    void batchUpdate(Set<GeneToDomainTO> geneTOs);
}
