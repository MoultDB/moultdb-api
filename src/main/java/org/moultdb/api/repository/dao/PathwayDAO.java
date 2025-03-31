package org.moultdb.api.repository.dao;

import org.moultdb.api.repository.dto.OrthogroupTO;
import org.moultdb.api.repository.dto.PathwayTO;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2024-03-22
 */
public interface PathwayDAO extends DAO<PathwayTO> {
    
    List<PathwayTO> findAll();
    
    Map<PathwayTO, OrthogroupTO> findPathwayOrthogroups();

    PathwayTO findById(String id);
    
    List<PathwayTO> findByIds(Set<String> ids);
    
    void insert(PathwayTO pathwayTO);
    
    int batchUpdate(Set<PathwayTO> pathwayTOs);
}
