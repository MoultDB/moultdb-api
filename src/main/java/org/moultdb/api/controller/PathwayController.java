package org.moultdb.api.controller;

import org.moultdb.api.model.Orthogroup;
import org.moultdb.api.model.Pathway;
import org.moultdb.api.service.PathwayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.moultdb.api.controller.ResponseHandler.generateErrorResponse;
import static org.moultdb.api.controller.ResponseHandler.generateValidResponse;

/**
 * @author Valentine Rech de Laval
 * @since 2024-04-02
 */
@RestController
@RequestMapping(path="/pathways")
public class PathwayController {
    
    @Autowired PathwayService pathwayService;
    
    @PostMapping("/import-cv-file")
    public ResponseEntity <Map<String, Object>> insertPathways(@RequestParam MultipartFile pathwayCvFile) {
        try {
            pathwayService.importPathways(pathwayCvFile);
        } catch (Exception e) {
            return generateErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
        return generateValidResponse("Pathways imported/updated");
    }
    
    @PostMapping("/import-data-file")
    public ResponseEntity <Map<String, Object>> insertPathwayData(@RequestParam MultipartFile geneToPathwayFile) {
        try {
            pathwayService.importPathwayData(geneToPathwayFile);
        } catch (Exception e) {
            return generateErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
        return generateValidResponse("Pathways imported/updated");
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getById(@RequestParam(required = false) String pathwayId) {
        if (pathwayId != null) {
            return generateValidResponse(pathwayService.getPathwayById(pathwayId));
        }
        return generateValidResponse(pathwayService.getAllPathways());
    }
    
    @GetMapping("/with-orthogroups")
    public ResponseEntity<Map<String, List<PathwayOrthogroup>>> getPathwaysWithOrthogroups() {
        List<PathwayOrthogroup> collect = pathwayService.getAllPathwaysWithOrthogroups().entrySet().stream()
                .map(entry -> new PathwayOrthogroup(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(PathwayOrthogroup::id))
                .collect(Collectors.toList());
        return generateValidResponse(collect);
    }
    
    public record PathwayOrthogroup(String id, String name, String description, Set<Orthogroup> orthogroups) {
        public PathwayOrthogroup(Pathway pathway, Set<Orthogroup> orthogroups) {
            this(pathway.getId(), pathway.getName(), pathway.getDescription(), orthogroups);
        }
    }
}
