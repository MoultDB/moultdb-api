package org.moultdb.api.repository.dao;

import org.moultdb.api.repository.dto.TaxonDto;

import java.util.List;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-14
 */
public interface TaxonRepository {
    
    List<TaxonDto> findAll();
    
    List<TaxonDto> findByScientificName(String taxonScientificName);
    
    List<TaxonDto> findById(int id);
    
    void insertTaxon(TaxonDto taxon);
    
}
