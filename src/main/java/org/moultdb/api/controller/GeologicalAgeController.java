package org.moultdb.api.controller;

import org.moultdb.api.service.GeologicalAgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Valentine Rech de Laval
 * @since 2022-05-16
 */
@RestController
@RequestMapping(path="/geological-age")
public class GeologicalAgeController {
    
    @Autowired
    GeologicalAgeService geologicalAgeService;
    
    @CrossOrigin
    @GetMapping(value = "/create")
    public ResponseEntity<Integer> insertGeologicalAge(@RequestParam("pwd") String pwd) {
        Integer integer = geologicalAgeService.updateGeologicalAges(pwd);
        return ResponseEntity.ok().body(integer);
    }
}
