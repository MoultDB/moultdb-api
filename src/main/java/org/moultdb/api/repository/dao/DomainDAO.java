package org.moultdb.api.repository.dao;

import org.moultdb.api.repository.dto.DomainTO;

import java.util.List;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2024-03-22
 */
public interface DomainDAO extends DAO<DomainTO> {
    
    List<DomainTO> findAll();
    
    DomainTO findById(String id);

    void insert(DomainTO domainTO);
    
    void batchUpdate(Set<DomainTO> domainTOs);
}
