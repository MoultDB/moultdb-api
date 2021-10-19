package org.moultdb.api.service;


import org.moultdb.api.model.Taxon;

import java.util.List;

public interface TaxonService {

    public List<Taxon> getAllTaxa();
    
    public Taxon getTaxonByScientificName(String scientificName);
    
    public void insertTaxon(Taxon taxon);
    
}
