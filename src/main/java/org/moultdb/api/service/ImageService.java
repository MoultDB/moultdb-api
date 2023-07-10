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
                          String moultingStep, Integer specimenCount, Boolean isFossil);
    
    public Resource getImage(String filename);
    
    public List<ImageInfo> getAllImageInfos();

    public List<ImageInfo> getNewestImageInfos();

    public List<ImageInfo> getImageInfosByUser(String email, Integer limit);

    public void deleteAll();
}
