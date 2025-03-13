package org.moultdb.api.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.moultdb.api.exception.MoultDBException;
import org.moultdb.api.model.Observation;
import org.moultdb.api.repository.dao.TaxonAnnotationDAO;
import org.moultdb.api.repository.dao.TaxonDAO;
import org.moultdb.api.repository.dto.TaxonAnnotationTO;
import org.moultdb.api.repository.dto.TaxonTO;
import org.moultdb.api.service.ObservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * @author Valentine Rech de Laval
 * @since 2023-05-31
 */
@Service
public class ObservationServiceImpl implements ObservationService {
    
    @Autowired
    TaxonDAO taxonDAO;
    @Autowired
    TaxonAnnotationDAO taxonAnnotationDAO;
    
    @Override
    public List<Observation> getAllObservations() {
        return getObservations(null, null);
    }
    
    @Override
    public List<Observation> getNewestObservations(int limit) {
        return getObservations(null, limit);
    }
    
    @Override
    public List<Observation> getObservationsByUsername(String username) {
        return getObservations(username, null);
    }
    
    private List<Observation> getObservations(String username, Integer limit) {
        List<TaxonAnnotationTO> annots;
        if (StringUtils.isNotBlank(username)) {
            annots = taxonAnnotationDAO.findByUsername(username, limit);
        } else if (limit != null && limit > 0) {
            annots = taxonAnnotationDAO.findLastCreated(limit);
        } else {
            annots = taxonAnnotationDAO.findAll();
        }
        if (annots.isEmpty()) {
            return null;
        }
        return getObservations(annots);
    }
    
    @Override
    public List<Observation> getObservationsByTaxon(String taxonName) {
        TaxonTO taxonTO = taxonDAO.findByScientificName(taxonName);
        if (taxonTO == null) {
            throw new MoultDBException("Taxon [" + taxonName + "] not found");
        }
        return getObservations(taxonAnnotationDAO.findByTaxonPath(taxonTO.getPath()));
    }
    
    private List<Observation> getObservations(List<TaxonAnnotationTO> taxonAnnotTOs) {
        return taxonAnnotTOs.stream()
                .filter(ta -> ta.getObservationTO() != null)
                .map(ta ->  new Observation(ta.getObservationTO().getId(), ta.getObservationTO().getUrl(),
                        ta.getTaxonTO().getScientificName()))
                .sorted(Comparator.comparing(Observation::getDescription))
                .toList();
    }
}
