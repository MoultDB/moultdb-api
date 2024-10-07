package org.moultdb.api.repository.dao;

import org.moultdb.api.repository.dto.ECOTermTO;

import java.util.List;

/**
 * @author Valentine Rech de Laval
 * @since 2024-09-17
 */
public interface ECOTermDAO extends DAO<ECOTermTO> {
    
    List<ECOTermTO> findAll();
    
    ECOTermTO findById(String id);
}