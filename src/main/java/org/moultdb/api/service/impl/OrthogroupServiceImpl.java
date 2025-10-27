package org.moultdb.api.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.exception.ImportException;
import org.moultdb.api.model.Orthogroup;
import org.moultdb.api.repository.dao.GeneDAO;
import org.moultdb.api.repository.dao.OrthogroupDAO;
import org.moultdb.api.repository.dto.GeneTO;
import org.moultdb.api.repository.dto.OrthogroupTO;
import org.moultdb.api.service.OrthogroupService;
import org.moultdb.api.service.ServiceUtils;
import org.moultdb.importer.genomics.OrthogroupBean;
import org.moultdb.importer.genomics.OrthogroupParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.stream.Collectors;

import static org.moultdb.api.service.ServiceUtils.mapFromTO;
import static org.moultdb.api.service.ServiceUtils.splitSet;

/**
 * @author Valentine Rech de Laval
 * @since 2024-04-30
 */
@Service
public class OrthogroupServiceImpl implements OrthogroupService {
    
    private final static Logger logger = LogManager.getLogger(OrthogroupServiceImpl.class.getName());
    private final static int SUBSET_SIZE = 100000;
    
    @Autowired GeneDAO geneDAO;
    @Autowired OrthogroupDAO orthogroupDAO;
    
    @Override
    public void importOrthogroups(MultipartFile orthogroupFile, MultipartFile pathwayFile) {
        String originalFilename = orthogroupFile.getOriginalFilename();
        if (StringUtils.isBlank(originalFilename)) {
            throw new IllegalArgumentException("File name cannot be blank");
        }
        
        try {
            logger.info("Start orthogroup data import...");
        
            OrthogroupParser parser = new OrthogroupParser();
        
            logger.info("Parse orthogroup data file {} to create orthogroups with {}...",
                    originalFilename, pathwayFile.getOriginalFilename());
            Set<OrthogroupBean> orthogroupBeans = parser.getOrthogroupBeans(orthogroupFile);
        
            logger.info("Load {} rows in DB by subset of {}...", orthogroupBeans.size(), SUBSET_SIZE);
            int subsetCount = orthogroupBeans.size() / SUBSET_SIZE;
            int idx = 1;
            for (Set<OrthogroupBean> beanSubset : splitSet(orthogroupBeans, SUBSET_SIZE)) {
                logger.debug("Load subset {}/{} of {} beans in db...", idx, subsetCount, beanSubset.size());
            
                Set<OrthogroupTO> orthogroupTOs = parser.getOrthogroupTOs(beanSubset, pathwayFile);
                orthogroupDAO.batchUpdate(orthogroupTOs);
                
                // To save memory, we clear the orthogroupTOs set after insertion in DB
                orthogroupTOs.clear();
                
                Set<GeneTO> geneTOs = parser.getGeneTOs(beanSubset, geneDAO, orthogroupDAO);
                
                int updatedGeneCount = geneDAO.batchUpdate(geneTOs);
                logger.debug("    {} updated genes", updatedGeneCount);
                
                if (geneTOs.size() != updatedGeneCount) {
                    logger.warn("Not all genes were updated: {} genes were parsed, but only {} were updated.",
                            geneTOs.size(), updatedGeneCount);
                }
                
                // To save memory, we clear the geneTOs set after insertion in DB
                geneTOs.clear();
                
                idx++;
            }
        } catch (Exception e) {
            logger.error("Unable to import orthogroup data from {}.", originalFilename, e);
            throw new ImportException("Unable to import orthogroup data from " + originalFilename + ". " +
                    "Error: " + e.getMessage());
        }
        logger.info("End orthogroup data import");
    }
    
    @Override
    public Orthogroup getOrthogroupById(String orthogroupId) {
        return mapFromTO(orthogroupDAO.findById(orthogroupId));
    }
    
    @Override
    public Set<Orthogroup> getMoultingOrthogroups() {
        return orthogroupDAO.getMoultingOrthogroups().stream()
                .map(ServiceUtils::mapFromTO)
                .collect(Collectors.toSet());
    }
    
    @Override
    public Set<Orthogroup> getMoultingOrthogroupsByTaxon(String taxonPath) {
        return orthogroupDAO.findMoultingOrthogroupsByTaxon(taxonPath).stream()
                .map(ServiceUtils::mapFromTO)
                .collect(Collectors.toSet());
    }
}
