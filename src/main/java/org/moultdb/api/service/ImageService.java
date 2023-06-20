package org.moultdb.api.service;

import org.moultdb.api.model.ImageInfo;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Valentine Rech de Laval
 * @since 2023-05-31
 */
public interface ImageService {
    
    public String getAbsolutePath();
    
    public void saveImage(MultipartFile file, String speciesName, String sex, Integer ageInDays, String location,
                          String moultingStep, Integer specimenCount);
    
    public Resource getImage(String filename);
    
    public List<ImageInfo> getAllImages();
    
    public void deleteAll();
}
