package org.moultdb.api.controller;

import org.moultdb.api.model.Taxon;
import org.moultdb.api.service.TaxonService;
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

@RestController
@RequestMapping(path="/taxa")
@CrossOrigin(origins = "http://localhost:3000")
public class TaxonController {
    
    @Autowired
    TaxonService taxonService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getTaxon(@RequestParam(required = false) String scientificName,
                                                        @RequestParam(required = false) String datasource,
                                                        @RequestParam(required = false) String accession) {
        if (MoultdbController.hasMultipleParams(Arrays.asList(scientificName, datasource))) {
            return generateErrorResponse("Invalid combination of parameters: " +
                            "one parameter and one only must be specified between scientificName, taxonPath, or accession",
                    HttpStatus.BAD_REQUEST);
        }
        if (scientificName != null) {
            return generateValidResponse(taxonService.getTaxonByScientificName(scientificName));
        } else if (datasource != null) {
            return generateValidResponse(taxonService.getTaxonByDbXref(datasource, accession));
        }
        return generateValidResponse(taxonService.getAllTaxa());
    }
    
    @GetMapping("/search")
    public ResponseEntity<Map<String, List<Taxon>>> getByText(@RequestParam String text) {
        return generateValidResponse(taxonService.getTaxonByText(text));
    }
    
    @GetMapping("/{taxonPath}/lineage")
    public ResponseEntity<Map<String, List<Taxon>>> getTaxonLineage(@PathVariable String taxonPath) {
        return generateValidResponse(taxonService.getTaxonLineage(taxonPath));
    }
    
    @GetMapping("/{taxonPath}/children")
    public ResponseEntity<Map<String, List<Taxon>>> getTaxonChildren(@PathVariable String taxonPath) {
        return generateValidResponse(taxonService.getTaxonChildren(taxonPath));
    }
    
    @PostMapping(value = "/import-file")
    public ResponseEntity<Map<String, Object>> insertTaxa(@RequestParam MultipartFile file) {
        Integer integer;
        try {
            integer = taxonService.insertTaxa(file);
        } catch (Exception e) {
            return generateErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Map<String, Object> resp = new HashMap<>();
        resp.put("count", integer);
        return generateValidResponse("Taxa imported",resp);
    }
}
