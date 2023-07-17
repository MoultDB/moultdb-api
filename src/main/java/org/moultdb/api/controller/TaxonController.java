package org.moultdb.api.controller;

import org.moultdb.api.model.Taxon;
import org.moultdb.api.service.TaxonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.moultdb.api.controller.ResponseHandler.generateErrorResponse;
import static org.moultdb.api.controller.ResponseHandler.generateValidResponse;

@RestController
@RequestMapping(path="/taxon")
public class TaxonController {
    
    @Autowired
    TaxonService taxonService;

    @GetMapping("/all")
    public ResponseEntity<Map<String, List<Taxon>>> getAllTaxa() {
        return generateValidResponse(taxonService.getAllTaxa());
    }
    
    @GetMapping("/scientific-name")
    public ResponseEntity<Map<String, Taxon>> getByScientificName(@RequestParam String name) {
        return generateValidResponse(taxonService.getTaxonByScientificName(name));
    }
    
    @PostMapping(value = "/import-file")
    public ResponseEntity<Map<String, Object>> insertTaxa(@RequestParam("file") MultipartFile file) {
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
