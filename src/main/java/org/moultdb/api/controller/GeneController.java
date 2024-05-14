package org.moultdb.api.controller;

import org.moultdb.api.service.GeneService;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @since 2024-04-02
 */
@RestController
@RequestMapping(path="/genes")
public class GeneController {
    
    @Autowired GeneService geneService;
    
    @PostMapping
    public ResponseEntity <Map<String, Object>> insertGenes(@RequestParam MultipartFile file,
                                                            @RequestParam boolean throwException) {
        try {
            geneService.importGenes(file, throwException);
        } catch (Exception e) {
            return generateErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return generateValidResponse("Genes imported/updated");
    }
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> getGenes(
            @RequestParam(required = false) String geneId,
            @RequestParam(required = false) String locusTag,
            @RequestParam(required = false) String proteinId,
            @RequestParam(required = false) String pathwayId,
            @RequestParam(required = false) String domainId,
            @RequestParam(required = false) Integer orthogroupId,
            @RequestParam(required = false) String taxonPath,
            @RequestParam(required = false) Boolean inAMoultingPathway
    ) {
        // Validate combinations (can be done at controller or service level)
        if (MoultdbController.hasMultipleParams(Arrays.asList(geneId, locusTag, proteinId, pathwayId, domainId, taxonPath))) {
            return generateErrorResponse("Invalid combination of parameters: " +
                    "a maximum of one of the following elements can be specified: " +
                    "proteinId, pathwayId, domainId or taxonPath", HttpStatus.BAD_REQUEST);
        }

        // Handle each case based on provided parameters
        if (geneId != null) {
            return generateValidResponse(geneService.getGene(geneId));
        } else if (locusTag != null) {
            return generateValidResponse(geneService.getGeneByLocusTag(locusTag));
        } else if (proteinId != null) {
            return generateValidResponse(geneService.getGeneByProtein(proteinId));
        } else if (orthogroupId != null) {
            return generateValidResponse(geneService.getGenesByOrthogroup(orthogroupId));
        } else if (pathwayId != null) {
            return generateValidResponse(geneService.getGenesByPathway(pathwayId));
        } else if (domainId != null) {
            return generateValidResponse(geneService.getGenesByDomain(domainId));
        } else if (taxonPath != null) {
            return generateValidResponse(geneService.getGenesByTaxon(taxonPath, inAMoultingPathway));
        } else {
            return generateValidResponse(geneService.getAllGenes());
        }
    }
}
