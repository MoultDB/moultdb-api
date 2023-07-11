package org.moultdb.api.controller;

import org.moultdb.api.model.ImageInfo;
import org.moultdb.api.service.ImageService;
import org.moultdb.api.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    @Autowired
    TokenService tokenService;
    
    @PostMapping("/import")
    public ResponseEntity<Map<String, Object>> uploadImage(@RequestParam MultipartFile file,
                                                           @RequestParam String taxonName,
                                                           @RequestParam String moultingStep,
                                                           @RequestParam Integer specimenCount,
                                                           @RequestParam Boolean isFossil,
                                                           @RequestParam(required = false) String sex,
                                                           @RequestParam(required = false) Integer ageInDays,
                                                           @RequestParam(required = false) String location,
                                                           @RequestParam String email,
                                                           @RequestParam String token) {
        try {
            if (!tokenService.validateToken(email, token)) {
                return generateErrorResponse("Your token is not valid.", HttpStatus.UNAUTHORIZED);
            }
            imageService.saveImage(file, taxonName, sex, ageInDays, location, moultingStep, specimenCount, isFossil, email);
        } catch (Exception e) {
            return generateErrorResponse("Could not upload the image: " + file.getOriginalFilename() + ". Error: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return generateValidResponse("Uploaded the image successfully: " + file.getOriginalFilename());
    }
    
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getUserImages() {
        List<ImageInfo> imageInfos = imageService.getAllImageInfos();
        return generateValidResponse("List of all images", imageInfos);
    }
    
    @GetMapping("/last")
    public ResponseEntity<Map<String, Object>> getLastImages() {
        return generateValidResponse(imageService.getImageInfosByUser(null, 10));
    }
    
    @GetMapping("/user-specific")
    public ResponseEntity<Map<String, Object>> getUserImages(@RequestParam String email) {
        List<ImageInfo> imageInfos = imageService.getImageInfosByUser(email, null);
        return generateValidResponse("List of images loaded by " + email, imageInfos);
    }

    @GetMapping("/taxon-specific")
    public ResponseEntity<Map<String, Object>> getTaxonImages(@RequestParam String taxonName) {
        List<ImageInfo> imageInfos = imageService.getImageInfosByTaxon(taxonName);
        return generateValidResponse("List of images of " + taxonName + " taxon and it's children", imageInfos);
    }
    
    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        Resource file = imageService.getImage(filename);
        return ResponseEntity.ok()
                             .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                             .body(file);
    }
}
