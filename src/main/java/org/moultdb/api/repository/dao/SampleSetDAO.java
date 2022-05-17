package org.moultdb.api.repository.dao;

import org.moultdb.api.repository.dto.SampleSetTO;

import java.util.List;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
public interface SampleSetDAO extends DAO<SampleSetTO> {
    
    List<SampleSetTO> findAll();
    
    SampleSetTO findById(Integer id);
    
    List<SampleSetTO> findByIds(Set<Integer> sampleIds);
}
