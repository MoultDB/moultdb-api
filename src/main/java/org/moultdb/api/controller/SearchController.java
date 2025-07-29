package org.moultdb.api.controller;

import org.moultdb.api.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.moultdb.api.controller.ResponseHandler.generateErrorResponse;
import static org.moultdb.api.controller.ResponseHandler.generateValidResponse;

/**
 * @author Valentine Rech de Laval
 * @since 2024-10-22
 */
@RestController
@RequestMapping(path="/search")
public class SearchController {
    
    @Autowired SearchService searchService;
    
    private final static int LIMIT_MAX = 10000;
    
    private final static int DEFAULT_AUTOCOMPLETE_LIMIT = 100;
    
    private final static int DEFAULT_SEARCH_LIMIT = 1000;
    
    @GetMapping("/taxon_autocomplete")
    public ResponseEntity<Map<String, Object>> getTaxonAutocomplete(@RequestParam String q,
                                                                    @RequestParam(required = false) Integer limit) {
        if (limit != null && limit > LIMIT_MAX) {
            return generateErrorResponse("It is not possible to request more than "
                    + LIMIT_MAX + " results.", HttpStatus.BAD_REQUEST);
        }
        return generateValidResponse("Taxon autocomplete result for [" + q + "]",
                searchService.taxonAutocomplete(q, limit == null? DEFAULT_AUTOCOMPLETE_LIMIT : limit));
    }
    
    
    @GetMapping("/taxon_search")
    public ResponseEntity<Map<String, Object>> getTaxonSearch(@RequestParam String q,
                                                              @RequestParam(required = false) Integer limit) {
        if (limit != null && limit > LIMIT_MAX) {
            return generateErrorResponse("It is not possible to request more than "
                    + LIMIT_MAX + " results.", HttpStatus.BAD_REQUEST);
        }
        return generateValidResponse("Taxon search result for [" + q + "]",
                searchService.taxonSearch(q, limit == null? DEFAULT_SEARCH_LIMIT : limit));
    }
    
    @GetMapping("/inat_search")
    public ResponseEntity<Map<String, Object>> getInatTaxonSearch(@RequestParam String q,
                                                                  @RequestParam(required = false) Integer limit) {
        if (limit != null && limit > LIMIT_MAX) {
            return generateErrorResponse("It is not possible to request more than "
                    + LIMIT_MAX + " results.", HttpStatus.BAD_REQUEST);
        }
        return generateValidResponse("Taxon autocomplete result for [" + q + "]",
                searchService.inatTaxonSearch(q, limit == null? DEFAULT_AUTOCOMPLETE_LIMIT : limit));
    }

}
