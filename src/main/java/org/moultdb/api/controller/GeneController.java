package org.moultdb.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.moultdb.api.exception.MoultDBException;
import org.moultdb.api.model.Gene;
import org.moultdb.api.model.Taxon;
import org.moultdb.api.service.GeneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

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
            return generateValidResponse(getGeneData(geneService.getGenesByOrthogroup(orthogroupId)));
        } else if (pathwayId != null) {
            return generateValidResponse(getGeneData(geneService.getGenesByPathway(pathwayId)));
        } else if (domainId != null) {
            return generateValidResponse(getGeneData(geneService.getGenesByDomain(domainId)));
        } else if (taxonPath != null) {
            return generateValidResponse(getGeneData(geneService.getGenesByTaxon(taxonPath, inAMoultingPathway)));
        } else {
            return generateValidResponse(geneService.getAllGenes());
        }
    }
    
    public Map<String, Map<String, Map<Integer, Set<Gene>>>> getGeneData(List<Gene> genes) {
        return genes.stream().collect(Collectors.groupingBy(gene -> getFormattedKey(gene.getPathway()),
                Collectors.groupingBy(gene -> getCleanedTaxon(gene.getTaxon()),
                        Collectors.groupingBy(Gene::getOrthogroupId, Collectors.toSet() ))));
    }
    
    private static String getCleanedTaxon(Taxon taxon) {
        return getFormattedKey(new Taxon(taxon.getPath(), taxon.getScientificName(), taxon.getCommonName(),
                taxon.isExtinct(), taxon.getDbXrefs().isEmpty()? taxon.getDbXrefs() : Collections.singleton(taxon.getDbXrefs().get(0))) );
    }
    
    private static String getFormattedKey(Object g) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(g);
        } catch (Exception e) {
            throw new MoultDBException("Error when formatting the object", e);
        }
    }
}
