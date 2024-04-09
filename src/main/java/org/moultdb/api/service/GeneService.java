package org.moultdb.api.service;

import org.moultdb.api.model.Gene;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Valentine Rech de Laval
 * @since 2024-03-25
 */
public interface GeneService {
    
    public List<Gene> getAllGenes();
    
    public Gene getGeneByProtein(String proteinId);
    
    public List<Gene> getGenesByTaxon(String taxonPath, Boolean inAMoultingPathway);
    
    public List<Gene> getGenesByPathway(String pathwayId);
    
    public List<Gene> getGenesByDomain(String domainId);
    
    public void importGenes(MultipartFile geneFile);
}
