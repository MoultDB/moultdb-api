package org.moultdb.api.service;

import org.moultdb.api.model.Domain;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Valentine Rech de Laval
 * @since 2024-03-25
 */
public interface DomainService {
    
    public List<Domain> getAllDomains();
    
    public Domain getDomainById(String domainId);
    
    public void updateDomains(MultipartFile file);
    
}
