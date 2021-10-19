package org.moultdb.api.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.model.Taxon;
import org.moultdb.api.repository.dao.TaxonRepository;
import org.moultdb.api.repository.dto.TaxonDto;
import org.moultdb.api.service.TaxonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaxonServiceImpl implements TaxonService {
    
    private final static Logger log = LogManager.getLogger(TaxonServiceImpl.class.getName());
    
    @Autowired
    private TaxonRepository taxonRepo;

    @Override
    public List<Taxon> getAllTaxa() {
        return taxonRepo.findAll().stream()
                        .map(this::convertToEntity)
                        .collect(Collectors.toList());
    }
    
    @Override
    public Taxon getTaxonByScientificName(String scientificName) {
        List<TaxonDto> taxa = taxonRepo.findByScientificName(scientificName);
    
        if (taxa.isEmpty()) {
            return null;
        } else if (taxa.size() > 1) {
            throw log.throwing(new IllegalStateException(
                    "Unique taxon could not be found for scientific name: " + scientificName));
        }
    
        return convertToEntity(taxa.get(0));
    }
    
    @Override
    public void insertTaxon(Taxon taxon) {
        taxonRepo.insertTaxon(convertToDto(taxon));
    }
    
    private TaxonDto convertToDto(Taxon taxon) {
        return new TaxonDto(null, taxon.getScientificName(), taxon.getCommonName(), taxon.getTaxonRank(),
                taxon.getNcbiTaxId(), taxon.isExtinct(), taxon.getPath());
    }
    private Taxon convertToEntity(TaxonDto taxonDto) {
        return new Taxon(taxonDto.getScientificName(), taxonDto.getCommonName(), taxonDto.getTaxonRank(),
                taxonDto.getNcbiTaxId(), taxonDto.isExtinct(), taxonDto.getPath());
    }
    
}
