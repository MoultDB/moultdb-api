package org.moultdb.api.repository.dao;

import org.moultdb.api.repository.dto.SutureLocationTO;

import java.util.List;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
public interface SutureLocationDAO extends DAO<SutureLocationTO> {
    
    List<SutureLocationTO> findAll();
    
    SutureLocationTO findById(int id);
    
    List<SutureLocationTO> findByIds(Set<Integer> ids);
}
