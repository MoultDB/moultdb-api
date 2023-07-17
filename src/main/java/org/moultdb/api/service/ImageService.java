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
    
    public void saveImage(MultipartFile file, String taxonName, String sex, Integer ageInDays, String location,
                          String moultingStep, Integer specimenCount, Boolean isFossil, Boolean isCaptive);

    public void saveImage(MultipartFile file, String taxonName, String sex, Integer ageInDays, String location,
                          String moultingStep, Integer specimenCount, Boolean isFossil, Boolean isCaptive, String email);
    
    public Resource getImage(String filename);
    
    public List<ImageInfo> getAllImageInfos();

    public List<ImageInfo> getNewestImageInfos();

    public List<ImageInfo> getImageInfosByUser(String email, Integer limit);
    
    List<ImageInfo> getImageInfosByTaxon(String taxonName);

    public void deleteAll();
}
