package org.moultdb.api.service;

import org.moultdb.api.model.Orthogroup;
import org.moultdb.api.model.Pathway;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2024-04-02
 */
public interface PathwayService {
    
    public List<Pathway> getAllPathways();
    
    public Map<Pathway, Set<Orthogroup>> getAllPathwaysWithOrthogroups();
    
    public Pathway getPathwayById(String pathwayId);
    
    public void importPathways(MultipartFile pathwayCvFile);
    
    public void importPathwayData(MultipartFile geneToPathwayFile);
}
