package org.moultdb.api.service.impl;

import org.moultdb.api.model.Taxon;
import org.moultdb.api.repository.dao.DbXrefDAO;
import org.moultdb.api.repository.dao.TaxonDAO;
import org.moultdb.api.repository.dto.DbXrefTO;
import org.moultdb.api.repository.dto.TaxonTO;
import org.moultdb.api.repository.dto.TransfertObject;
import org.moultdb.api.service.TaxonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TaxonServiceImpl implements TaxonService {
    
    @Autowired
    private TaxonDAO taxonDao;
    
    @Autowired
    private DbXrefDAO dbXrefDao;

    @Override
    public List<Taxon> getAllTaxa() {
    
        List<TaxonTO> taxonTOs = taxonDao.findAll();
    
        Map<Integer, DbXrefTO> dbXrefTOsById = dbXrefDao.findByIds(taxonTOs.stream()
                                                                     .map(TaxonTO::getDbXrefId)
                                                                     .collect(Collectors.toSet()))
                                                  .stream()
                                                  .collect(Collectors.toMap(DbXrefTO::getId, Function.identity()));
        
        return taxonTOs.stream()
                  .map(to-> org.moultdb.api.service.Service.mapFromTO(to, dbXrefTOsById))
                  .collect(Collectors.toList());
    }
    
    @Override
    public Taxon getTaxonByScientificName(String scientificName) {
        TaxonTO taxonTO = TransfertObject.getOneTO(taxonDao.findByScientificName(scientificName));
        if (taxonTO != null) {
            return org.moultdb.api.service.Service.mapFromTO(taxonTO, dbXrefDao.findById(taxonTO.getDbXrefId()));
        }
        return null;
    }
    
    @Override
    public void insertTaxon(Taxon taxon) {
        taxonDao.insertTaxon(convertToDto(taxon));
    }
    
    private static TaxonTO convertToDto(Taxon taxon) {
        throw new UnsupportedOperationException("Conversion from entity to TO not available");
    }
}
