package org.moultdb.api.repository.dao;

import org.moultdb.api.repository.dto.AnatEntityTO;

import java.util.List;

/**
 * @author Valentine Rech de Laval
 * @since 2023-07-03
 */
public interface AnatEntityDAO extends DAO<AnatEntityTO> {
    
    List<AnatEntityTO> findAll();

    AnatEntityTO findById(String id);

    Integer getLastId();
}
