package org.moultdb.api.controller;

import org.moultdb.api.model.Domain;
import org.moultdb.api.service.DomainService;
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
 * @since 2024-04-02
 */
@RestController
@RequestMapping(path="/domains")
public class DomainController {
    
    @Autowired
    DomainService domainService;
    
    @PostMapping("/import-files")
    public ResponseEntity <Map<String, Object>> insertDomains(@RequestParam MultipartFile[] files) {
        try {
            domainService.updateDomains(files);
        } catch (Exception e) {
            return generateErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
        return generateValidResponse("Domains imported/updated");
    }
    
    @GetMapping
    public ResponseEntity<Map<String, List<Domain>>> getAllDomains() {
        return generateValidResponse(domainService.getAllDomains());
    }
    
    
    @GetMapping("/{domain}")
    public ResponseEntity<Map<String, Domain>> getById(@PathVariable String domain) {
        return generateValidResponse(domainService.getDomainById(domain));
    }
}
