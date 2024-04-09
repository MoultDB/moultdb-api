package org.moultdb.api.controller;

import org.moultdb.api.service.GeologicalAgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.moultdb.api.controller.ResponseHandler.generateValidResponse;

/**
 * @author Valentine Rech de Laval
 * @since 2022-05-16
 */
@RestController
@RequestMapping(path="/geological-ages")
public class GeologicalAgeController {
    
    @Autowired
    GeologicalAgeService geologicalAgeService;
    
    // FIXME improve service name to match conventions (ex: remove verb and set @PutMapping)
    @GetMapping(value = "/create")
    public ResponseEntity<Map<String, Object>> insertGeologicalAge() {
        geologicalAgeService.updateGeologicalAges();
        return generateValidResponse("Geological ages imported/updated");
    }
}
