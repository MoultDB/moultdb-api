package org.moultdb.api.service;

import org.moultdb.api.model.TaxonSearchResult;

import java.util.List;

/**
 * @author Valentine Rech de Laval
 * @since 2024-10-22
 */
public interface SearchService {
    
    public List<String> taxonAutocomplete(String searchTerm, int i);
    
    public List<TaxonSearchResult> taxonSearch(String searchTerm, int i);
    
    public List<TaxonSearchResult> inatTaxonSearch(String searchTerm, int limit);
}
