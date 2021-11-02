package org.moultdb.api.repository.dao;

import org.moultdb.api.repository.dto.GeologicalAgeTO;

import java.util.List;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
public interface GeologicalAgeDAO extends DAO<GeologicalAgeTO> {
    
    List<GeologicalAgeTO> findAll();
    
    GeologicalAgeTO findById(Integer id);
    
    List<GeologicalAgeTO> findByIds(Set<Integer> ids);
}
