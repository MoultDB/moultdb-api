package org.moultdb.api.controller;

import org.moultdb.api.model.TaxonAnnotation;
import org.moultdb.api.service.TaxonAnnotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-27
 */
@RestController
@RequestMapping(path="/taxon-annotation")
public class TaxonAnnotationController {
    
    @Autowired
    TaxonAnnotationService taxonAnnotationService;
    
    @CrossOrigin
    @GetMapping("/all")
    public List<TaxonAnnotation> getAllTaxonAnnotations() {
        return taxonAnnotationService.getAllTaxonAnnotations();
    }
    
//    @CrossOrigin
//    @GetMapping(value = "/create")
//    public ResponseEntity<Integer> insertTaxonAnnotations(@RequestParam("pwd") String pwd) {
//        Integer integer = taxonAnnotationService.updateGeologicalAges(pwd);
//        return ResponseEntity.ok().body(integer);
//    }

}
