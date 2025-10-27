package org.moultdb.api.repository.dao;

import org.moultdb.api.repository.dto.TaxonStatisticsTO;

import java.util.List;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-14
 */
public interface TaxonStatisticsDAO extends DAO<TaxonStatisticsTO> {
    
    List<TaxonStatisticsTO> findAll();
    
    TaxonStatisticsTO findByPath(String taxonPath);
    
    List<TaxonStatisticsTO> findByPaths(Set<String> taxonPaths);
    
    List<TaxonStatisticsTO> findByPathWithChildren(String taxonPath);
    
    void insert(TaxonStatisticsTO taxonStats);
    
    int batchUpdate(Set<TaxonStatisticsTO> taxonStatsTOs);
}
