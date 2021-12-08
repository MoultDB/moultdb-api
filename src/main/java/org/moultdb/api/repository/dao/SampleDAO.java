package org.moultdb.api.repository.dao;

import org.moultdb.api.repository.dto.SampleTO;

import java.util.List;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
public interface SampleDAO extends DAO<SampleTO> {
    
    List<SampleTO> findAll();
    
    SampleTO findById(Integer id);
    
    List<SampleTO> findByIds(Set<Integer> sampleIds);
}
