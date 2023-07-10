package org.moultdb.api.service;

import org.moultdb.api.model.TaxonAnnotation;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-26
 */
public interface TaxonAnnotationService {
    
    public List<TaxonAnnotation> getAllTaxonAnnotations();
    
    public List<TaxonAnnotation> getLastTaxonAnnotations(int limit);

    public List<TaxonAnnotation> getUserTaxonAnnotations(String username);
    
    public Integer importTaxonAnnotations(@RequestParam("file") MultipartFile file) throws IOException;
    
}
