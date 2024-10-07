package org.moultdb.api.controller;

import org.moultdb.api.service.ImageService;
import org.moultdb.api.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Map;

import static org.moultdb.api.controller.ResponseHandler.generateErrorResponse;
import static org.moultdb.api.controller.ResponseHandler.generateValidResponse;

/**
 * @author Valentine Rech de Laval
 * @since 2023-05-31
 */
@RestController
@RequestMapping(path="/images")
public class ImageController {
    
    @Autowired
    ImageService imageService;
    @Autowired
    TokenService tokenService;
    
    @PostMapping("/import")
    public ResponseEntity<Map<String, Object>> uploadImage(@RequestParam MultipartFile file,
                                                           @RequestParam String taxonName,
                                                           @RequestParam String moultingStep,
                                                           @RequestParam String specimenCount,
                                                           @RequestParam Boolean isFossil,
                                                           @RequestParam Boolean isCaptive,
                                                           @RequestParam(required = false) String sex,
                                                           @RequestParam(required = false) Integer ageInDays,
                                                           @RequestParam(required = false) String location,
                                                           @RequestParam String email,
                                                           @RequestParam String token) {
        try {
            if (!tokenService.validateToken(email, token)) {
                return generateErrorResponse("Your token is not valid.", HttpStatus.UNAUTHORIZED);
            }
            imageService.saveImage(file, taxonName, sex, ageInDays, location, moultingStep, specimenCount, isFossil, isCaptive, email);
        } catch (Exception e) {
            return generateErrorResponse("Could not upload the image: " + file.getOriginalFilename() + ". Error: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return generateValidResponse("Uploaded the image successfully: " + file.getOriginalFilename());
    }
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> getImages(@RequestParam(required = false) String email,
                                                         @RequestParam(required = false) String taxonName) {
        if (MoultdbController.hasMultipleParams(Arrays.asList(email, taxonName))) {
            return generateErrorResponse("Invalid combination of parameters: " +
                            "one parameter and one only must be specified between email or taxonName",
                    HttpStatus.BAD_REQUEST);
        }
        if (email != null) {
            return generateValidResponse("List of images loaded by " + email, imageService.getImageInfosByUser(email));
        } else if (taxonName != null) {
            return generateValidResponse("List of images of " + taxonName + " taxon and it's children",
                    imageService.getImageInfosByTaxon(taxonName));
        }
        return generateValidResponse("List of all images", imageService.getAllImageInfos());
    }
    
    @GetMapping("/last")
    public ResponseEntity<Map<String, Object>> getLastImages() {
        return generateValidResponse(imageService.getNewestImageInfos(10));
    }
    
    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        Resource file = imageService.getImage(filename);
        return ResponseEntity.ok()
                             .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                             .body(file);
    }
}
