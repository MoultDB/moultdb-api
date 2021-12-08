package org.moultdb.api.repository.dao;

import org.moultdb.api.repository.dto.IndividualTO;

import java.util.List;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
public interface IndividualDAO extends DAO<IndividualTO> {
    
    List<IndividualTO> findAll();
    
    List<IndividualTO> findByTaxonId(int id);
    
}
