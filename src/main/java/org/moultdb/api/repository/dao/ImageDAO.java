package org.moultdb.api.repository.dao;

import org.moultdb.api.repository.dto.ConditionTO;
import org.moultdb.api.repository.dto.ImageTO;

import java.util.List;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2022-05-24
 */
public interface ImageDAO extends DAO<ConditionTO> {
    
    List<ImageTO> findAll();
    
    Integer getLastId();
    
    int insert(ImageTO imageTO);
    
    int batchUpdate(Set<ImageTO> imagesTOs);
}
