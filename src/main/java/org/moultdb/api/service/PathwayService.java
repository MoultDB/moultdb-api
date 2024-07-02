package org.moultdb.api.service;

import org.moultdb.api.model.Pathway;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Valentine Rech de Laval
 * @since 2024-04-02
 */
public interface PathwayService {
    
    public List<Pathway> getAllPathways();
    
    public Pathway getPathwayById(String pathwayId);
    
    public void importPathways(MultipartFile pathwayCvFile);
    
    public void importPathwayData(MultipartFile geneToPathwayFile);
}
