package org.moultdb.api.controller;

import org.moultdb.api.controller.response.GeneResponse;
import org.moultdb.api.controller.response.OrthogroupResponse;
import org.moultdb.api.controller.response.PathwayResponse;
import org.moultdb.api.controller.response.TaxonResponse;
import org.moultdb.api.model.Gene;
import org.moultdb.api.model.Orthogroup;
import org.moultdb.api.model.Pathway;
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
    
    @PostMapping("/import-files")
    public ResponseEntity <Map<String, Object>> insertGenes(@RequestParam MultipartFile[] files,
                                                            @RequestParam boolean throwException) {
        try {
            geneService.importGenes(files, throwException);
        } catch (Exception e) {
            return generateErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, e);
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

        Gene gene = null;
        if (geneId != null) {
            gene = geneService.getGene(geneId);
        } else if (locusTag != null) {
            gene = geneService.getGeneByLocusTag(locusTag);
        } else if (proteinId != null) {
            gene = geneService.getGeneByProtein(proteinId);
        } 
        if (orthogroupId != null) {
            return generateValidResponse(getGeneData(geneService.getGenesByOrthogroup(orthogroupId, gene)));
        } else if (pathwayId != null) {
            return generateValidResponse(getGeneData(geneService.getGenesByPathway(pathwayId)));
        } else if (domainId != null) {
            return generateValidResponse(getGeneData(geneService.getGenesByDomain(domainId)));
        } else if (taxonPath != null) {
            return generateValidResponse(getGeneData(geneService.getGenesByTaxon(taxonPath, inAMoultingPathway)));
        } else if (gene == null) {
            return generateValidResponse(geneService.getAllGenes());
        }
        return generateValidResponse(gene);
    }
    
    private List<PathwayResponse> getGeneData(List<Gene> genes) {
        
        Map<Pathway, Map<Taxon, Map<Orthogroup, Set<Gene>>>> collect =
                genes.stream()
                        .collect(Collectors.groupingBy(g -> Optional.ofNullable(g.getPathway())
                                        .orElse(new Pathway("NOT_IN_MOULTING_PATHWAY", "NOT_IN_MOULTING_PATHWAY", null, null, null)),
                                Collectors.groupingBy(Gene::getTaxon,
                                        Collectors.groupingBy(g -> Optional.ofNullable(g.getOrthogroup()).orElse(new Orthogroup(0, null)),
                                                Collectors.toSet()))));
        
        return collect.entrySet().stream()
                .map(pathwayEntry -> {
                    Pathway pathway = pathwayEntry.getKey();
                    Map<Taxon, Map<Orthogroup, Set<Gene>>> taxonMap = pathwayEntry.getValue();
                    List<TaxonResponse> taxonResponses = taxonMap.entrySet().stream()
                            .map(taxonEntry -> {
                                Taxon taxon = taxonEntry.getKey();
                                Map<Orthogroup, Set<Gene>> orthogroupMap = taxonEntry.getValue();
                                List<OrthogroupResponse> orthogroupResponses = orthogroupMap.entrySet().stream()
                                        .map(orthogroupEntry -> {
                                            Orthogroup orthogroup = orthogroupEntry.getKey();
                                            List<GeneResponse> geneGroup = orthogroupEntry.getValue().stream()
                                                    .map(g -> new GeneResponse(g.getId(), g.getName(), g.getLocusTag()))
                                                    .sorted(Comparator.comparing(GeneResponse::name, Comparator.nullsLast(String::compareTo))
                                                            .thenComparing(GeneResponse::id, Comparator.nullsLast(String::compareTo))
                                                            .thenComparing(GeneResponse::locusTag, Comparator.nullsLast(String::compareTo)))
                                                    .toList();
                                            return new OrthogroupResponse(orthogroup.getId(), orthogroup.getName(), geneGroup);
                                        })
                                        .sorted(Comparator.comparing(OrthogroupResponse::name, Comparator.nullsLast(String::compareTo))
                                                .thenComparing(OrthogroupResponse::id))
                                        .collect(Collectors.toList());
                                return new TaxonResponse(taxon.getId(), taxon.getScientificName(), taxon.getAccession(), orthogroupResponses);
                            })
                            .sorted(Comparator.comparing(TaxonResponse::scientificName))
                            .collect(Collectors.toList());
                    return new PathwayResponse(pathway.getId(), pathway.getName(), taxonResponses);
                })
                .sorted(Comparator.comparing(PathwayResponse::id))
                .collect(Collectors.toList());
    }
}
