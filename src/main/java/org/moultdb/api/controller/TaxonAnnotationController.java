package org.moultdb.api.controller;

import org.moultdb.api.model.TaxonAnnotation;
import org.moultdb.api.service.TaxonAnnotationService;
import org.moultdb.api.service.TokenService;
import org.moultdb.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.moultdb.api.controller.ResponseHandler.generateErrorResponse;
import static org.moultdb.api.controller.ResponseHandler.generateValidResponse;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-27
 */
@RestController
@RequestMapping(path="/taxon-annotation")
public class TaxonAnnotationController {
    
    @Autowired
    TaxonAnnotationService taxonAnnotationService;
    @Autowired
    TokenService tokenService;
    
    @GetMapping("/all")
    public ResponseEntity<Map<String, List<TaxonAnnotation>>> getAllTaxonAnnotations() {
        return generateValidResponse(taxonAnnotationService.getAllTaxonAnnotations());
    }
    
    @GetMapping("/user-specific")
    public ResponseEntity<Map<String, List<TaxonAnnotation>>> getUserTaxonAnnotations(@RequestParam("email") String email) {
        return generateValidResponse(taxonAnnotationService.getUserTaxonAnnotations(email));
    }
    
    @GetMapping("/one")
    public ResponseEntity<Map<String, TaxonAnnotation>> getTaxonAnnotationByImageId(@RequestParam("imageFilename") String imageFilename) {
        return generateValidResponse(taxonAnnotationService.getTaxonAnnotationsByImageFilename(imageFilename));
    }
    
    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteTaxonAnnotationByImageId(@RequestBody Map<String, String> json) {
        String email = getParam(json, "email");
        String token = getParam(json, "token");
        if (!tokenService.validateToken(email, token)) {
            return generateErrorResponse("Your token is not valid.", HttpStatus.UNAUTHORIZED);
        }
        String imageFilename = getParam(json, "imageFilename");
        return generateValidResponse(taxonAnnotationService.deleteTaxonAnnotationsByImageFilename(imageFilename));
    }
    private static String getParam(Map<String, String> json, String paramKey) {
        return json == null ? null : json.get(paramKey);
    }
    
    @PostMapping("/import-file")
    public
    ResponseEntity <Map<String, Object>> insertTaxonAnnotations(@RequestParam("file") MultipartFile file) {
        Integer integer;
        try {
            integer = taxonAnnotationService.importTaxonAnnotations(file);
        } catch (IOException e) {
            return generateErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Map<String, Object> resp = new HashMap<>();
        resp.put("count", integer);
        return generateValidResponse("Taxon annotations imported",resp);
    }
}
