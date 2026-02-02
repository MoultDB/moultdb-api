package org.moultdb.api.service;

import org.moultdb.api.model.TaxonStatistics;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TaxonStatisticsService {

    public List<TaxonStatistics> getAllStats();
    
    public TaxonStatistics getTaxonStatsByPath(String path);
    
    Map<String, TaxonStatistics> getTaxonStatsByPathWithChildren(String path);
    
    Map<String, TaxonStatistics> getTaxonStatsByPaths(Set<String> paths);
    
    public Integer updateTaxonStatistics();
}
