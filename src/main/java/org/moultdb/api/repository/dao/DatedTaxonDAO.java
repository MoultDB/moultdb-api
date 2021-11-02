package org.moultdb.api.repository.dao;

import org.moultdb.api.repository.dto.DatedTaxonTO;

import java.util.List;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
public interface DatedTaxonDAO extends DAO<DatedTaxonTO> {
    
    List<DatedTaxonTO> findAll();
    
    DatedTaxonTO findByTaxonId(int id);
    
}
