package org.moultdb.api.controller;

import org.moultdb.api.service.OrthogroupService;
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
 * @since 2024-04-30
 */
@RestController
@RequestMapping(path="/orthogroups")
public class OrthogroupController {
    
    @Autowired OrthogroupService orthogroupService;
    
    @PostMapping
    public ResponseEntity <Map<String, Object>> insertOrthogroups(@RequestParam MultipartFile file) {
        try {
            orthogroupService.importOrthogroups(file);
        } catch (Exception e) {
            return generateErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return generateValidResponse("Orthogroups imported/updated");
    }
}
