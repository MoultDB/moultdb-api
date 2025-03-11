package org.moultdb.api.service;

import org.moultdb.api.model.Orthogroup;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Valentine Rech de Laval
 * @since 2024-04-30
 */
public interface OrthogroupService {
    
    void importOrthogroups(MultipartFile orthogroupFile, MultipartFile pathwayFile);
    
    public Orthogroup getOrthogroupById(String orthogroupId);
}
