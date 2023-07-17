package org.moultdb.api.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.model.Taxon;
import org.moultdb.api.repository.dao.DataSourceDAO;
import org.moultdb.api.repository.dao.DbXrefDAO;
import org.moultdb.api.repository.dao.TaxonDAO;
import org.moultdb.api.repository.dto.DataSourceTO;
import org.moultdb.api.repository.dto.DbXrefTO;
import org.moultdb.api.repository.dto.TaxonTO;
import org.moultdb.api.service.ServiceUtils;
import org.moultdb.api.service.TaxonService;
import org.moultdb.importer.taxon.TaxonBean;
import org.moultdb.importer.taxon.TaxonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaxonServiceImpl implements TaxonService {
    
    private final static Logger logger = LogManager.getLogger(TaxonServiceImpl.class.getName());
    
    @Autowired
    DataSourceDAO dataSourceDAO;
    @Autowired
    private TaxonDAO taxonDAO;
    @Autowired
    private DbXrefDAO dbXrefDAO;
    
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
    
    @Override
    public Integer insertTaxa(MultipartFile file) {
        logger.info("Start taxon annotations import...");
        
        TaxonParser parser = new TaxonParser();
        
        Set<TaxonBean> taxonBeans = parser.getTaxonBeans(file);
        
        
        Set<TaxonTO> taxonTOs = parser.getTaxonTOs(taxonBeans, taxonDAO, dataSourceDAO, dbXrefDAO);
        
        int[] ints = taxonDAO.batchUpdate(taxonTOs);
        
        int sum = Arrays.stream(ints).sum();
        
        logger.info("End taxon annotations import: " + Arrays.stream(ints).sum()+ " new row(s) in 'taxon' table.");
        
        return sum;
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
