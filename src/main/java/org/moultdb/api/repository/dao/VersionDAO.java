package org.moultdb.api.repository.dao;

import org.moultdb.api.repository.dto.VersionTO;

import java.util.List;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
public interface VersionDAO extends DAO<VersionTO> {
    
    List<VersionTO> findAll();
    
    VersionTO findById(int id);
    
    List<VersionTO> findByIds(Set<Integer> ids);
    
    int insert(VersionTO versionTO);
    
    Integer getLastId();
}
