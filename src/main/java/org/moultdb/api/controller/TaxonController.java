package org.moultdb.api.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.model.Taxon;
import org.moultdb.api.service.TaxonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path="/taxon")
public class TaxonController {
    
    private final static Logger log = LogManager.getLogger(TaxonController.class.getName());
    
    @Autowired
    TaxonService taxonService;

    @CrossOrigin
    @GetMapping("/all")
    public List<Taxon> getAllTaxa() {
        return taxonService.getAllTaxa();
    }
    
    @CrossOrigin
    @GetMapping("/scientific-name")
    public ResponseEntity<Taxon> getByScientificName(@RequestParam String name) {
        return ResponseEntity.ok().body(taxonService.getTaxonByScientificName(name));
    
    }
    
    @CrossOrigin
    @PostMapping(value = "/create",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Taxon> insertTaxon(@RequestBody Taxon taxon) {
        taxonService.insertTaxon(taxon);
        
        return ResponseEntity.ok().body(taxon);
    }
}
