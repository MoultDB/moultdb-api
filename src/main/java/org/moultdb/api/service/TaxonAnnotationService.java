package org.moultdb.api.service;

import org.moultdb.api.model.TaxonAnnotation;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-26
 */
public interface TaxonAnnotationService {
    
    public List<TaxonAnnotation> getAllTaxonAnnotations();
    
    public List<TaxonAnnotation> getLastUpdatedTaxonAnnotations(int limit);
    
    public List<TaxonAnnotation> getUserTaxonAnnotations(String username);
    
    public List<TaxonAnnotation> getTaxonAnnotationsByTaxonPath(String taxonPath);
        
    public List<TaxonAnnotation> getTaxonAnnotationsByTaxonName(String taxonName);
    
    public List<TaxonAnnotation> getTaxonAnnotationsByDbXref(String datasource, String accession);
    
    public Integer importTaxonAnnotations(MultipartFile dataFile, MultipartFile mappingFile) throws IOException;
    
    public Integer importINaturalistAnnotations();
}
