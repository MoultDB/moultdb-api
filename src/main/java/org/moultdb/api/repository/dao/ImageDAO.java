package org.moultdb.api.repository.dao;

import org.moultdb.api.repository.dto.ConditionTO;
import org.moultdb.api.repository.dto.ImageTO;

import java.util.List;

/**
 * @author Valentine Rech de Laval
 * @since 2022-05-24
 */
public interface ImageDAO extends DAO<ConditionTO> {
    
    List<ImageTO> findAll();
    
    int insert(ImageTO imageTO);
    
    Integer getLastId();
}
