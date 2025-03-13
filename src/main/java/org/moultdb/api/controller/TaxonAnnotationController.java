package org.moultdb.api.controller;

import org.moultdb.api.model.TaxonAnnotation;
import org.moultdb.api.service.TaxonAnnotationService;
import org.moultdb.api.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
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
@RequestMapping(path="/taxon-annotations")
public class TaxonAnnotationController {
    
    @Autowired
    TaxonAnnotationService taxonAnnotationService;
    @Autowired
    TokenService tokenService;
    
    @GetMapping
    public ResponseEntity<Map<String, List<TaxonAnnotation>>> getAllTaxonAnnotations() {
        return generateValidResponse(taxonAnnotationService.getAllTaxonAnnotations());
    }
    
    @GetMapping("/user-specific")
    public ResponseEntity<Map<String, List<TaxonAnnotation>>> getUserTaxonAnnotations(@RequestParam String username) {
        return generateValidResponse(taxonAnnotationService.getUserTaxonAnnotations(username));
    }
    
    @GetMapping("/species")
    public ResponseEntity<Map<String, Object>> getTaxonAnnotationBySpecies(
            @RequestParam(required = false) String scientificName,
            @RequestParam(required = false) String taxonPath,
            @RequestParam(required = false) String datasource,
            @RequestParam(required = false) String accession
            ) {
        if (MoultdbController.hasMultipleParams(Arrays.asList(scientificName, taxonPath, datasource))) {
            return generateErrorResponse("Invalid combination of parameters: " +
                    "one parameter and one only must be specified between scientificName, taxonPath, or accession",
                    HttpStatus.BAD_REQUEST);
        }
        if (scientificName != null) {
            return generateValidResponse(taxonAnnotationService.getTaxonAnnotationsByTaxonName(scientificName));
        } else if (taxonPath != null) {
            return generateValidResponse(taxonAnnotationService.getTaxonAnnotationsByTaxonPath(taxonPath));
        }
        assert (datasource != null);
        return generateValidResponse(taxonAnnotationService.getTaxonAnnotationsByDbXref(datasource, accession));
    }
    
    @PostMapping("/import-file")
    public ResponseEntity <Map<String, Object>> insertTaxonAnnotations(@RequestParam MultipartFile dataFile,
                                                                       @RequestParam MultipartFile mappingFile) {
        Integer integer;
        try {
            integer = taxonAnnotationService.importTaxonAnnotations(dataFile, mappingFile);
        } catch (Exception e) {
            return generateErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Map<String, Object> resp = new HashMap<>();
        resp.put("count", integer);
        return generateValidResponse("Taxon annotations imported",resp);
    }
    
    @GetMapping("/import-inat-metadata")
    public ResponseEntity <Map<String, Object>> insertINaturalistAnnotations() {
        Integer integer;
        try {
            integer = taxonAnnotationService.importINaturalistAnnotations();
        } catch (Exception e) {
            return generateErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Map<String, Object> resp = new HashMap<>();
        resp.put("count", integer);
        return generateValidResponse("Taxon annotations imported",resp);
    }
    
    @GetMapping("/last-updated")
    public ResponseEntity<Map<String, Object>> getLastTaxonAnnotation() {
        List<TaxonAnnotation> lastTaxonAnnotations = taxonAnnotationService.getLastUpdatedTaxonAnnotations(1);
        if (lastTaxonAnnotations.isEmpty()) {
            return generateValidResponse(null);
        }
        return generateValidResponse(lastTaxonAnnotations.get(0));
    }
}
