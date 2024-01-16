package org.moultdb.api.repository.dao;

import org.moultdb.api.repository.dto.DataSourceTO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Valentine Rech de Laval
 * @since 2022-05-17
 */
@Repository
public interface DataSourceDAO extends DAO<DataSourceTO> {
    
    List<DataSourceTO> findAll();
    
    DataSourceTO findByName(String name);
}
