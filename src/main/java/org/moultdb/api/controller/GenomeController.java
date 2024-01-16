package org.moultdb.api.controller;

import org.moultdb.api.model.Genome;
import org.moultdb.api.service.GenomeService;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @since 2023-11-21
 */
@RestController
@RequestMapping(path="/genome")
public class GenomeController {
    
    @Autowired GenomeService genomeService;
    
    @PostMapping("/import-file")
    public ResponseEntity <Map<String, Object>> insertGenomes(@RequestParam("file") MultipartFile file) {
        try {
            genomeService.updateGenomes(file);
        } catch (Exception e) {
            return generateErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return generateValidResponse("Genomes imported/updated");
    }
    
    @GetMapping("/all")
    public ResponseEntity<Map<String, List<Genome>>> getAllTaxa() {
        return generateValidResponse(genomeService.getAllGenomes());
    }
    
    @GetMapping("/taxon")
    public ResponseEntity<Map<String, List<Genome>>> getByTaxonPath(
            @RequestParam String taxonPath, @RequestParam boolean withSubspeciesGenomes) {
        return generateValidResponse(genomeService.getGenomesByTaxon(taxonPath, withSubspeciesGenomes));
    }
}
