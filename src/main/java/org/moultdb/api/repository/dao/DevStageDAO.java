package org.moultdb.api.repository.dao;

import org.moultdb.api.repository.dto.DevStageTO;

import java.util.List;

/**
 * @author Valentine Rech de Laval
 * @since 2023-07-03
 */
public interface DevStageDAO extends DAO<DevStageTO> {
    
    List<DevStageTO> findAll();
    
    DevStageTO findById(String id);
    
    DevStageTO findByName(String id, String taxonPath);
    
    Integer getLastId();
}
