package org.moultdb.api.repository.dao;

import org.moultdb.api.repository.dto.PathwayTO;

import java.util.List;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2024-03-22
 */
public interface PathwayDAO extends DAO<PathwayTO> {
    
    List<PathwayTO> findAll();
    
    PathwayTO findById(String id);
    
    void insert(PathwayTO pathwayTO);
    
    void batchUpdate(Set<PathwayTO> pathwayTOs);
}
