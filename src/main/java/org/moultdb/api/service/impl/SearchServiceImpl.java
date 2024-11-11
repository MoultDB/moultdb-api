package org.moultdb.api.service.impl;

import org.moultdb.api.model.TaxonSearchResult;
import org.moultdb.api.service.SearchService;
import org.sphx.api.SphinxClient;
import org.sphx.api.SphinxException;
import org.sphx.api.SphinxResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Valentine Rech de Laval
 * @since 2024-10-22
 */
@Service
public class SearchServiceImpl implements SearchService {
    
    @Value("${moultdb.search.host}")
    private String searchHost;
    
    @Value("${moultdb.search.port}")
    private int searchPort;
    
    @Override
    public List<String> taxonAutocomplete(String searchTerm, int limit) {
        SphinxResult result = search(searchTerm, "moultdb_taxon_autocomplete", null, limit);
        
        // if result is empty, return an empty list
        if (result == null || result.totalFound == 0) {
            return List.of();
        }
        return Arrays.stream(result.matches)
                .map(m -> String.valueOf(m.attrValues.get(0)))
                .toList();
    }
    
    @Override
    public List<TaxonSearchResult> taxonSearch(String searchTerm, int limit) {

        // We give a higher weight to the scientific name.
        // The logic is that we expect to find a scientific name before the synonyms.
        Map<String, Integer> weights = new HashMap<>();
        weights.put("t_scientific_name", 1000);
        weights.put("t_common_name", 1);
        weights.put("t_syn", 1);
        
        SphinxResult result = search(searchTerm, "moultdb_taxon_search", weights, limit);

        // if result is empty, return an empty list
        if (result == null || result.totalFound == 0) {
            return List.of();
        }
        
        // Get mapping between attribute names and their index
        Map<String, Integer> attrNameToIdx = new HashMap<>();
        for (int idx = 0; idx < result.attrNames.length; idx++) {
            attrNameToIdx.put(result.attrNames[idx], idx);
        }
        
        return Arrays.stream(result.matches)
                .map(m -> {
                    return new TaxonSearchResult(String.valueOf(m.attrValues.get(attrNameToIdx.get("t_path"))),
                            String.valueOf(m.attrValues.get(attrNameToIdx.get("t_accession"))),
                            String.valueOf(m.attrValues.get(attrNameToIdx.get("t_scientific_name"))),
                            String.valueOf(m.attrValues.get(attrNameToIdx.get("t_common_name"))),
                            String.valueOf(m.attrValues.get(attrNameToIdx.get("t_syn"))),
                            String.valueOf(m.attrValues.get(attrNameToIdx.get("t_url"))));
                }) 
                .toList();
    }
    
    private SphinxResult search(String searchTerm, String indexName, Map<String, Integer> weights, int limit) {
        SphinxClient sphinxClient = new SphinxClient(searchHost, searchPort);
        SphinxResult result;
        try {
            sphinxClient.ResetFilters();
            sphinxClient.SetLimits(0, limit, limit);
            sphinxClient.SetRankingMode(SphinxClient.SPH_RANK_SPH04, null);
            sphinxClient.SetSortMode(SphinxClient.SPH_SORT_RELEVANCE, null);
            if (weights != null) {
                sphinxClient.SetFieldWeights(weights);
            }
            result = sphinxClient.Query(searchTerm, indexName);
        } catch (SphinxException e) {
            throw new IllegalStateException("Sphinx search has generated an exception", e);
        }
        if (result != null && result.getStatus() == SphinxClient.SEARCHD_ERROR) {
            throw new IllegalStateException("Sphinx search has generated an error: " + result.error);
        }
        return result;
    }
}
