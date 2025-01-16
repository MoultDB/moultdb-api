package org.moultdb.api.repository.dao;

import org.moultdb.api.repository.dto.ConditionTO;
import org.moultdb.api.repository.dto.ObservationTO;

import java.util.List;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2022-05-24
 */
public interface ObservationDAO extends DAO<ConditionTO> {
    
    List<ObservationTO> findAll();
    
    int insert(ObservationTO observationTO);
    
    int batchUpdate(Set<ObservationTO> observationTOs);
}
