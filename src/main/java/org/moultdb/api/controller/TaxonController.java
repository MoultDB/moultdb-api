package org.moultdb.api.controller;

import org.moultdb.api.model.Taxon;
import org.moultdb.api.service.TaxonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.moultdb.api.controller.ResponseHandler.generateValidResponse;

@RestController
@RequestMapping(path="/taxon")
public class TaxonController {
    
    @Autowired
    TaxonService taxonService;

    @CrossOrigin
    @GetMapping("/all")
    public ResponseEntity<List<Taxon>> getAllTaxa() {
        return generateValidResponse(taxonService.getAllTaxa());
    }
    
    @CrossOrigin
    @GetMapping("/scientific-name")
    public ResponseEntity<Taxon> getByScientificName(@RequestParam String name) {
        return generateValidResponse(taxonService.getTaxonByScientificName(name));
    }
    
    @CrossOrigin
    @PostMapping(value = "/create",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    //TODO: to be tested
    public ResponseEntity<Taxon> insertTaxon(@RequestBody Taxon taxon, @RequestParam("pwd") String pwd) {
        taxonService.insertTaxon(taxon);
        return generateValidResponse(taxon);
    }
}
