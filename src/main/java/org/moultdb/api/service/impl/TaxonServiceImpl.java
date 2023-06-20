package org.moultdb.api.service.impl;

import org.moultdb.api.model.Taxon;
import org.moultdb.api.repository.dao.DataSourceDAO;
import org.moultdb.api.repository.dao.TaxonDAO;
import org.moultdb.api.repository.dto.DataSourceTO;
import org.moultdb.api.repository.dto.DbXrefTO;
import org.moultdb.api.repository.dto.TaxonTO;
import org.moultdb.api.service.ServiceUtils;
import org.moultdb.api.service.TaxonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TaxonServiceImpl implements TaxonService {
    
    @Autowired
    DataSourceDAO dataSourceDAO;
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
                       .map(ServiceUtils::mapFromTO)
                       .collect(Collectors.toList());
    }
    
    @Override
    public void insertTaxon(Taxon taxon) {
        taxonDAO.insert(convertToDto(taxon));
    }
    
    private TaxonTO convertToDto(Taxon taxon) {
        Set<DbXrefTO> dbXrefTOs = taxon.getDbXref().stream()
                                       .map(xref -> {
                                           DataSourceTO dataSourceTO = dataSourceDAO.findByName(xref.getDataSource().getName());
                                           return new DbXrefTO(null, xref.getAccession(), dataSourceTO);
                                       })
                                       .collect(Collectors.toSet());
        
        return new TaxonTO(null, taxon.getScientificName(), taxon.getCommonName(), taxon.getParentTaxonPath(),
                taxon.getTaxonRank(), taxon.isExtinct(), dbXrefTOs);
    }
}
