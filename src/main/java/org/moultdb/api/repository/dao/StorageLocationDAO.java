package org.moultdb.api.repository.dao;

import org.moultdb.api.repository.dto.StorageLocationTO;

import java.util.List;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2022-06-14
 */
public interface StorageLocationDAO {
    
    List<StorageLocationTO> findAll();
    
    int insert(StorageLocationTO storageLocationTO);
    
    int[] batchUpdate(Set<StorageLocationTO> storageLocationTOs);
    
    Integer getLastId();
}
