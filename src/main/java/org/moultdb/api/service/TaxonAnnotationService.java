package org.moultdb.api.service;

import org.moultdb.api.model.TaxonAnnotation;

import java.util.List;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-26
 */
public interface TaxonAnnotationService extends Service {
    
    public List<TaxonAnnotation> getAllTaxonAnnotations();
    
}
