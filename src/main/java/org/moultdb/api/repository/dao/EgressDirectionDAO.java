package org.moultdb.api.repository.dao;

import org.moultdb.api.repository.dto.EgressDirectionTO;

import java.util.List;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
public interface EgressDirectionDAO extends DAO<EgressDirectionTO> {
    
    List<EgressDirectionTO> findAll();
    
    EgressDirectionTO findById(int id);
    
    List<EgressDirectionTO> findByIds(Set<Integer> ids);
}
