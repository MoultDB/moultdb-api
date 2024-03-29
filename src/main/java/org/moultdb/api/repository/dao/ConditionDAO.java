package org.moultdb.api.repository.dao;

import org.moultdb.api.repository.dto.ConditionTO;

import java.util.List;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2022-05-24
 */
public interface ConditionDAO extends DAO<ConditionTO> {
    
    List<ConditionTO> findAll();
    
    ConditionTO find(String devStageName, String anatEntityName);
    
    List<ConditionTO> find(Integer ageInDays, String sex, String moultingStep);
    
    void insert(ConditionTO conditionTO);
    
    void batchUpdate(Set<ConditionTO> conditionTOs);
    
    Integer getLastId();
}
