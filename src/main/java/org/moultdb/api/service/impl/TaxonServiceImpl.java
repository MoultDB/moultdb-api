package org.moultdb.api.service.impl;

import org.moultdb.api.model.Taxon;
import org.moultdb.api.repository.dao.TaxonDAO;
import org.moultdb.api.repository.dto.TaxonTO;
import org.moultdb.api.repository.dto.TransfertObject;
import org.moultdb.api.service.TaxonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaxonServiceImpl implements TaxonService {
    
    @Autowired
    private TaxonDAO taxonDAO;
    
    @Override
    public List<Taxon> getAllTaxa() {
        return getTaxons(taxonDAO.findAll());
    }
    
    @Override
    public Taxon getTaxonByScientificName(String scientificName) {
        TaxonTO taxonTO = taxonDAO.findByScientificName(scientificName);
        if (taxonTO != null) {
            return getTaxons(Collections.singletonList(taxonTO)).get(0);
        }
        return null;
    }
    
    private List<Taxon> getTaxons(List<TaxonTO> taxonTOs) {
        return taxonTOs.stream()
                       .map(org.moultdb.api.service.Service::mapFromTO)
                       .collect(Collectors.toList());
    }
    
    @Override
    public void insertTaxon(Taxon taxon) {
        taxonDAO.insertTaxon(convertToDto(taxon));
    }
    
    private static TaxonTO convertToDto(Taxon taxon) {
        throw new UnsupportedOperationException("Conversion from entity to TO not available");
    }
}
