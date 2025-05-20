package org.moultdb.api.service;


import org.moultdb.api.model.Taxon;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TaxonService {

    public List<Taxon> getAllTaxa();
    
    public List<Taxon> getTaxonByText(String searchedText);
    
    public Taxon getTaxonByScientificName(String scientificName);
    
    public Taxon getTaxonByPath(String path);
    
    public Taxon getTaxonByDbXref(String datasource, String accession);
    
    public List<Taxon> getTaxonLineage(String taxonPath);
    
    public List<Taxon> getTaxonDirectChildren(String taxonPath);
    
    public void insertTaxon(Taxon taxon);
    
    public Integer insertTaxa(MultipartFile file);
}
