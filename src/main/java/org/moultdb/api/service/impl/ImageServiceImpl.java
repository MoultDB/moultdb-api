package org.moultdb.api.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.moultdb.api.controller.ImageController;
import org.moultdb.api.exception.ImageUploadException;
import org.moultdb.api.exception.MoultDBException;
import org.moultdb.api.model.ImageInfo;
import org.moultdb.api.repository.dao.*;
import org.moultdb.api.repository.dto.*;
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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Valentine Rech de Laval
 * @since 2023-05-31
 */
@Service
public class ImageServiceImpl implements ImageService {
    
    @Autowired
    AnatEntityDAO anatEntityDAO;
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
                          String providedMoultingStep, Integer specimenCount, Boolean isFossil) {
        String originalFilename = file.getOriginalFilename();
        if (StringUtils.isBlank(originalFilename)) {
            throw new IllegalArgumentException("File name cannot be blank.");
        }
        if (file.getSize() > FILE_MAX_SIZE) {
            throw new MaxUploadSizeExceededException(FILE_MAX_SIZE);
        }
        if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
            throw new ImageUploadException("Format file cannot be other than: " + String.join(", ", ALLOWED_CONTENT_TYPES));
        }
        
        TaxonTO taxonTO = taxonDAO.findByScientificName(speciesName);
        if (taxonTO == null) {
            throw new MoultDBException("Unknown species scientific name: " + speciesName);
        }
        
        String fileName = generateImageFileName(originalFilename, taxonTO);
        try {
            Files.copy(file.getInputStream(), getPath().resolve(fileName));
            
        } catch (FileAlreadyExistsException e) {
            throw new MoultDBException("A file of that name already exists.");
        } catch (Exception e) {
            throw new MoultDBException(e.getMessage());
        }
        
        GeologicalAgeTO fromGeologicalAgeTO;
        GeologicalAgeTO toGeologicalAgeTO;
        if (isFossil) {
            fromGeologicalAgeTO = geologicalAgeDAO.findByNotation("a2"); // a2 Precambrian
            toGeologicalAgeTO = geologicalAgeDAO.findByNotation("a1.1.1.1.1.2"); // a1.1.1.1.1.2 Northgrippian
        } else {
            GeologicalAgeTO currentGeoAgeTO = geologicalAgeDAO.findByNotation("a1.1.1.1.1.1"); // Meghalayan age (= current)
            fromGeologicalAgeTO = currentGeoAgeTO;
            toGeologicalAgeTO = currentGeoAgeTO;
        }

        // To be able to update sample set and condition of a specific taxon annotation (as we have user annotations)
        // we should have 1 sample set and 1 condition for each taxon annotation.
        // So, we don't need to check if they already exist
        Integer sampleSetLastId = sampleSetDAO.getLastId();
        Integer sampleSetNextId = sampleSetLastId == null ? 1 : sampleSetLastId + 1;
        SampleSetTO sampleSetTO = new SampleSetTO(sampleSetNextId, fromGeologicalAgeTO, toGeologicalAgeTO,
                specimenCount, location);
        sampleSetDAO.insert(sampleSetTO);
        

        String anatEntityId = "UBERON:0013702"; // body proper
        String moultingStep = providedMoultingStep;
        if (providedMoultingStep.equalsIgnoreCase("exoskeleton")) {
            anatEntityId = "UBERON:0006611"; // exoskeleton
            moultingStep = "post-moult";
        }
        AnatEntityTO anatEntityTO = anatEntityDAO.findById(anatEntityId);
        if (anatEntityTO == null) {
            throw new MoultDBException("Missing anat. entity");
        }

        Integer conditionLastId = conditionDAO.getLastId();
        Integer conditionNextId = conditionLastId == null ? 1 : conditionLastId + 1;
        ConditionTO conditionTO = new ConditionTO(conditionNextId, ageInDays, anatEntityTO, sex, moultingStep);
        conditionDAO.insert(conditionTO);
        
        Integer imageLastId = imageDAO.getLastId();
        Integer imageNextId = imageLastId == null ? 1 : imageLastId + 1;
        ImageTO imageTO = new ImageTO(imageNextId, fileName, null);
        imageDAO.insert(imageTO);

        // FIXME getuser

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
    
    private static String generateImageFileName(String filename, TaxonTO taxonTO) {
        String spName = taxonTO.getScientificName().replaceAll(" ", "_").toLowerCase();
        String extension;
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < filename.length() - 1) {
            extension = filename.substring(dotIndex).toLowerCase();
        } else {
            throw new ImageUploadException("Filename should have an extension.");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmssSS");
        String timestamp = sdf.format(new Date());
        
        return spName + "_" + timestamp + extension;
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
    public List<ImageInfo> getAllImageInfos() {
        return getImageInfosByUser(null, null);
    }
    
    @Override
    public List<ImageInfo> getNewestImageInfos() {
        return getImageInfosByUser(null, 1);
    }
    
    @Override
    public List<ImageInfo> getImageInfosByUser(String email, Integer limit) {
        List<TaxonAnnotationTO> annots;
        if (StringUtils.isNotBlank(email)) {
            annots = taxonAnnotationDAO.findByUser(email, limit);
        } else if (limit != null && limit > 0) {
            annots = taxonAnnotationDAO.findLast(limit);
        } else {
            annots = taxonAnnotationDAO.findAll();
        }
        if (annots.isEmpty()) {
            return null;
        }
        return annots.stream()
                .filter(ta -> ta.getImageTO() != null)
                .map(ta -> getImageInfo(ta.getTaxonTO().getScientificName(), ta.getImageTO().getFileName()))
                .sorted(Comparator.comparing(ImageInfo::getName))
                .toList();
    }
    
    private static ImageInfo getImageInfo(String scientificName, String filename) {
        String id = filename.substring(0, filename.lastIndexOf('.'));
        return new ImageInfo(id, scientificName,
                MvcUriComponentsBuilder.fromMethodName(ImageController.class, "getImage", filename).build().toString());
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(getPath().toFile());
    }
}
