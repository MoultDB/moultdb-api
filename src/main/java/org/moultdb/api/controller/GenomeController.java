package org.moultdb.api.controller;

import org.moultdb.api.service.GenomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

import static org.moultdb.api.controller.ResponseHandler.generateErrorResponse;
import static org.moultdb.api.controller.ResponseHandler.generateValidResponse;

/**
 * @author Valentine Rech de Laval
 * @since 2023-11-21
 */
@RestController
@RequestMapping(path="/genomes")
//@CrossOrigin(origins = "http://localhost:3000")
public class GenomeController {
    
    @Autowired GenomeService genomeService;
    
    @PostMapping("/import-file")
    public ResponseEntity <Map<String, Object>> insertGenomes(@RequestParam MultipartFile file) {
        try {
            genomeService.updateGenomes(file);
        } catch (Exception e) {
            return generateErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return generateValidResponse("Genomes imported/updated");
    }
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> getTaxa(@RequestParam(required = false) String taxonPath,
                                                       @RequestParam(required = false) Boolean withSubspeciesGenomes) {
        if (taxonPath == null && withSubspeciesGenomes == null) {
            return generateValidResponse(genomeService.getAllGenomes());
        } else if (taxonPath != null && withSubspeciesGenomes != null) {
            return generateValidResponse(genomeService.getGenomesByTaxon(taxonPath, withSubspeciesGenomes));
        }
        return generateErrorResponse("Invalid combination of parameters: " +
                "withSubspeciesGenomes can be (and should be) specified only with taxonPath", HttpStatus.BAD_REQUEST);
    }
}
