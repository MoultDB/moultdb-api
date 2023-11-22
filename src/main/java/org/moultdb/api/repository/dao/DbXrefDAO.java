package org.moultdb.api.repository.dao;

import org.moultdb.api.repository.dto.DbXrefTO;

import java.util.List;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2021-11-01
 */
public interface DbXrefDAO extends DAO<DbXrefTO> {
    
    List<DbXrefTO> findAll();
    
    DbXrefTO findById(Integer id);
    
    List<DbXrefTO> findByIds(Set<Integer> ids);
    
    DbXrefTO findByAccessionAndDatasource(String accession, Integer dataSourceId);
    
    DbXrefTO find(String accession, String name, Integer dataSourceId);
    
    void insert(DbXrefTO dbXrefTO);
    
    void batchUpdate(Set<DbXrefTO> dbXrefTOs);
    
    Integer getLastId();
}
