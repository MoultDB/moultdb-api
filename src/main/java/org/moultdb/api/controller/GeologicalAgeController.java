package org.moultdb.api.controller;

import org.moultdb.api.service.GeologicalAgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.moultdb.api.controller.ResponseHandler.generateValidResponse;

/**
 * @author Valentine Rech de Laval
 * @since 2022-05-16
 */
@RestController
@RequestMapping(path="/geological-age")
public class GeologicalAgeController {
    
    @Autowired
    GeologicalAgeService geologicalAgeService;
    
    @GetMapping(value = "/create")
    public ResponseEntity<Map<String, Integer>> insertGeologicalAge(@RequestParam("pwd") String pwd) {
        Integer integer = geologicalAgeService.updateGeologicalAges(pwd);
        return generateValidResponse(integer);
    }
}
