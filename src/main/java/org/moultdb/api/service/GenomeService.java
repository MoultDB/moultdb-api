package org.moultdb.api.service;

import org.moultdb.api.model.Genome;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Valentine Rech de Laval
 * @since 2023-11-21
 */
public interface GenomeService {
    
    public List<Genome> getAllGenomes();
    
    public List<Genome> getGenomesByTaxon(String taxonPath, boolean withSubspeciesGenomes);
    
    public void updateGenomes(MultipartFile file);
    
}
