package org.moultdb.api.controller;

import org.moultdb.api.model.TaxonAnnotation;
import org.moultdb.api.service.TaxonAnnotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    
    @PostMapping("/import")
    public ResponseEntity<Integer> insertTaxonAnnotations(@RequestParam("file") MultipartFile file,
                                                          @RequestParam("pwd") String pwd) {
        Integer integer = null;
        try {
            integer = taxonAnnotationService.importTaxonAnnotations(file, pwd);
        } catch (IOException e) {
            e.printStackTrace();
            ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().body(integer);
    }

}
