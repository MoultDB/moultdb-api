package org.moultdb.api.repository.dao;

import org.moultdb.api.repository.dto.OrthogroupTO;

import java.util.List;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2024-05-22
 */
public interface OrthogroupDAO extends DAO<OrthogroupTO> {
    
    List<OrthogroupTO> findAll();
    
    OrthogroupTO findById(String id);
    
    List<OrthogroupTO> findByIds(Set<Integer> ids);
    
    
    void batchUpdate(Set<OrthogroupTO> orthogroupTOs);
}
