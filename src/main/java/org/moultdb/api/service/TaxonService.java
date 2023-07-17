package org.moultdb.api.service;


import org.moultdb.api.model.Taxon;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TaxonService {

    public List<Taxon> getAllTaxa();
    
    public Taxon getTaxonByScientificName(String scientificName);
    
    public void insertTaxon(Taxon taxon);
    
    public Integer insertTaxa(MultipartFile file);
}
