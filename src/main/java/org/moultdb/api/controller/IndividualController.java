package org.moultdb.api.controller;

import org.moultdb.api.model.Individual;
import org.moultdb.api.service.IndividualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-27
 */
@RestController
@RequestMapping(path="/individual")
public class IndividualController {
    
    @Autowired
    IndividualService individualService;
    
    @CrossOrigin
    @GetMapping("/all")
    public List<Individual> getAllIndividuals() {
        return individualService.getAllIndividuals();
    }
}
