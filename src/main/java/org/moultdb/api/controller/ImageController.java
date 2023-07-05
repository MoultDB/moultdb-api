package org.moultdb.api.controller;

import org.moultdb.api.model.ImageInfo;
import org.moultdb.api.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

import static org.moultdb.api.controller.ResponseHandler.generateErrorResponse;
import static org.moultdb.api.controller.ResponseHandler.generateValidResponse;

/**
 * @author Valentine Rech de Laval
 * @since 2023-05-31
 */
@RestController
@RequestMapping(path="/image")
public class ImageController {
    
    @Autowired
    ImageService imageService;
    
    @PostMapping("/import")
    public ResponseEntity<Map<String, Object>> uploadImage(@RequestParam MultipartFile file,
                                                           @RequestParam String speciesName,
                                                           @RequestParam String moultingStep,
                                                           @RequestParam Integer specimenCount,
                                                           @RequestParam Boolean isFossil,
                                                           @RequestParam(required = false) String sex,
                                                           @RequestParam(required = false) Integer ageInDays,
                                                           @RequestParam(required = false) String location) {
        try {
            imageService.saveImage(file, speciesName, sex, ageInDays, location, moultingStep, specimenCount, isFossil);
        } catch (Exception e) {
            return generateErrorResponse("Could not upload the image: " + file.getOriginalFilename() + ". Error: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return generateValidResponse("Uploaded the image successfully: " + file.getOriginalFilename());
    }
    
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getListImages() {
        List<ImageInfo> imageInfos = imageService.getAllImageInfos();
        return generateValidResponse("List of all images", imageInfos);
    }
    
    @GetMapping("/user-specific")
    public ResponseEntity<Map<String, Object>> getListImages(@RequestParam String email) {
        List<ImageInfo> imageInfos = imageService.getImageInfosByUser(email);
        return generateValidResponse("List of images loaded by " + email, imageInfos);
    }

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        Resource file = imageService.getImage(filename);
        return ResponseEntity.ok()
                             .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                             .body(file);
    }
}
