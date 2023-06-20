package org.moultdb.api.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.moultdb.api.controller.ImageController;
import org.moultdb.api.exception.ImageUploadException;
import org.moultdb.api.exception.MoultDBException;
import org.moultdb.api.model.ImageInfo;
import org.moultdb.api.repository.dao.ConditionDAO;
import org.moultdb.api.repository.dao.GeologicalAgeDAO;
import org.moultdb.api.repository.dao.ImageDAO;
import org.moultdb.api.repository.dao.SampleSetDAO;
import org.moultdb.api.repository.dao.TaxonAnnotationDAO;
import org.moultdb.api.repository.dao.TaxonDAO;
import org.moultdb.api.repository.dao.UserDAO;
import org.moultdb.api.repository.dao.VersionDAO;
import org.moultdb.api.repository.dto.ConditionTO;
import org.moultdb.api.repository.dto.GeologicalAgeTO;
import org.moultdb.api.repository.dto.ImageTO;
import org.moultdb.api.repository.dto.SampleSetTO;
import org.moultdb.api.repository.dto.TaxonAnnotationTO;
import org.moultdb.api.repository.dto.TaxonTO;
import org.moultdb.api.repository.dto.UserTO;
import org.moultdb.api.repository.dto.VersionTO;
import org.moultdb.api.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Valentine Rech de Laval
 * @since 2023-05-31
 */
@Service
public class ImageServiceImpl implements ImageService {
    
    @Autowired
    ConditionDAO conditionDAO;
    @Autowired
    GeologicalAgeDAO geologicalAgeDAO;
    @Autowired
    SampleSetDAO sampleSetDAO;
    @Autowired
    TaxonDAO taxonDAO;
    @Autowired
    ImageDAO imageDAO;
    @Autowired
    TaxonAnnotationDAO taxonAnnotationDAO;
    @Autowired
    UserDAO userDAO;
    @Autowired
    VersionDAO versionDAO;
    
    @Value("${file.storage}")
    private String FILE_STORAGE;
    
    private final long FILE_MAX_SIZE = 1 * 1024 * 1024; // 1 Mo
    private final Set<String> ALLOWED_CONTENT_TYPES = new HashSet<>(Arrays.asList("image/jpeg", "image/png"));
    
    private Path getPath() {
        return Paths.get(FILE_STORAGE);
    }
    
    @Override
    public String getAbsolutePath() {
        return Paths.get(FILE_STORAGE).toFile().getAbsolutePath();
    }
    
    @Override
    public void saveImage(MultipartFile file, String speciesName, String sex, Integer ageInDays, String location,
                          String moultingStep, Integer specimenCount) {
        String originalFilename = file.getOriginalFilename();
        if (StringUtils.isBlank(originalFilename)) {
            throw new IllegalArgumentException("Filename cannot be blank.");
        }
        if (file.getSize() > FILE_MAX_SIZE) {
            throw new MaxUploadSizeExceededException(FILE_MAX_SIZE);
        }
        if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
            throw new ImageUploadException("Format file cannot be other than: " + String.join(", ", ALLOWED_CONTENT_TYPES));
        }
        
        String fileName = generateImageFileName(originalFilename);
        try {
            Files.copy(file.getInputStream(), getPath().resolve(fileName));
            
        } catch (FileAlreadyExistsException e) {
            throw new MoultDBException("A file of that name already exists.");
        } catch (Exception e) {
            throw new MoultDBException(e.getMessage());
        }
        
        TaxonTO taxonTO = taxonDAO.findByScientificName(speciesName);
        
        GeologicalAgeTO currentGeoAgeTO = geologicalAgeDAO.findByNotation("a1.1.1.1.1.1"); // Meghalayan age (= current)
        
        // To be able to update sample set and condition of a specific taxon annotation (as we have user annotations)
        // we should have 1 sample set and 1 condition for each taxon annotation.
        // So, we don't need to check if they already exist
        Integer sampleSetLastId = sampleSetDAO.getLastId();
        Integer sampleSetNextId = sampleSetLastId == null ? 1 : sampleSetLastId + 1;
        SampleSetTO sampleSetTO = new SampleSetTO(sampleSetNextId, currentGeoAgeTO, currentGeoAgeTO, specimenCount, location);
        sampleSetDAO.insert(sampleSetTO);
        
        Integer conditionLastId = conditionDAO.getLastId();
        Integer conditionNextId = conditionLastId == null ? 1 : conditionLastId + 1;
        ConditionTO conditionTO = new ConditionTO(conditionNextId, ageInDays, sex, moultingStep);
        conditionDAO.insert(conditionTO);
        
        Integer imageLastId = imageDAO.getLastId();
        Integer imageNextId = imageLastId == null ? 1 : imageLastId + 1;
        ImageTO imageTO = new ImageTO(imageNextId, fileName, null);
        imageDAO.insert(imageTO);
        
        UserTO userTO = userDAO.findByEmail("valdelaval@yahoo.fr");
        
        Integer versionLastId = versionDAO.getLastId();
        Integer versionNextId = versionLastId == null ? 1 : versionLastId + 1;
        Timestamp current = new Timestamp(new Date().getTime());
        VersionTO versionTO = new VersionTO(versionNextId, userTO, current, userTO, current, 1);
        versionDAO.insert(versionTO);
        
        TaxonAnnotationTO taxonAnnotationTO = new TaxonAnnotationTO(null, taxonTO, speciesName, null,
                sampleSetTO.getId(), conditionTO, null, imageTO, null, null, versionNextId);
        taxonAnnotationDAO.insertImageTaxonAnnotation(taxonAnnotationTO);
    }
    
    private static String generateImageFileName(String filename) {
        String extension = "";
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < filename.length() - 1) {
            extension = filename.substring(dotIndex).toLowerCase();
        } else {
            throw new ImageUploadException("Filename should have an extension.");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = sdf.format(new Date());
        
        return "image_" + timestamp + extension;
    }
    
    @Override
    public Resource getImage(String filename) {
        try {
            Path file = getPath().resolve(filename);
            Resource resource = new UrlResource(file.toUri());
        
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new ImageUploadException("Could not read the file.");
            }
        } catch (MalformedURLException e) {
            throw new ImageUploadException("Error: " + e.getMessage());
        }
    }
    
    @Override
    public List<ImageInfo> getAllImages() {
        try (Stream<Path> entries = Files.walk(getPath(), 1)) {
            return entries
                    .filter(path -> !path.equals(getPath()))
                    .map(path -> getPath().relativize(path))
                    .map(path -> {
                        String filename = path.getFileName().toString();
                        String url = MvcUriComponentsBuilder
                                .fromMethodName(ImageController.class, "getImage", path.getFileName().toString()).build().toString();
    
                        return new ImageInfo(filename, url);
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new ImageUploadException("Could not load the files!");
        }
    }
    
    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(getPath().toFile());
    }
}