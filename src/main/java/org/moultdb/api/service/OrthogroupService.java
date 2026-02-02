package org.moultdb.api.service;

import org.moultdb.api.model.Orthogroup;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2024-04-30
 */
public interface OrthogroupService {
    
    void importOrthogroups(MultipartFile orthogroupFile, MultipartFile pathwayFile);
    
    public Orthogroup getOrthogroupById(String orthogroupId);
    
    public Set<Orthogroup> getMoultingOrthogroups();
    
    public Set<Orthogroup> getMoultingOrthogroupsByTaxon(String taxonPath);
}
