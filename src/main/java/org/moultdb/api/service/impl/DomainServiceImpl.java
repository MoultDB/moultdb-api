package org.moultdb.api.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.exception.ImportException;
import org.moultdb.api.model.Domain;
import org.moultdb.api.repository.dao.DomainDAO;
import org.moultdb.api.repository.dao.GeneDAO;
import org.moultdb.api.repository.dao.GeneToDomainDAO;
import org.moultdb.api.repository.dto.DomainTO;
import org.moultdb.api.repository.dto.GeneToDomainTO;
import org.moultdb.api.service.DomainService;
import org.moultdb.api.service.ServiceUtils;
import org.moultdb.importer.genomics.DomainParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.moultdb.api.service.ServiceUtils.mapFromTO;

/**
 * @author Valentine Rech de Laval
 * @since 2024-04-02
 */
@Service
public class DomainServiceImpl implements DomainService {
    
    private final static Logger logger = LogManager.getLogger(DomainServiceImpl.class.getName());
    
    @Autowired DomainDAO domainDAO;
    @Autowired GeneDAO geneDAO;
    @Autowired GeneToDomainDAO geneToDomainDAO;
    
    @Override
    public List<Domain> getAllDomains() {
        return getDomains(domainDAO.findAll());
    }
    
    @Override
    public Domain getDomainById(String domainId) {
        return mapFromTO(domainDAO.findById(domainId));
    }
    
    @Override
    public void updateDomains(MultipartFile file) {
        String originalGeneFilename = file.getOriginalFilename();
        if (StringUtils.isBlank(originalGeneFilename)) {
            throw new IllegalArgumentException("File name cannot be blank.");
        }
        
        logger.info("Start domains import...");
        
        DomainParser importer = new DomainParser();
        try {
            logger.info("Parse file " + originalGeneFilename + "...");
            Set<DomainTO> domainTOs = importer.getDomainTOs(file);
            Set<GeneToDomainTO> geneToDomainTOs = importer.getGeneToDomainTOs(file, geneDAO);
            
            logger.info("Load domains in db...");
            domainDAO.batchUpdate(domainTOs);
            geneToDomainDAO.batchUpdate(geneToDomainTOs);
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new ImportException("Unable to import domains from " + originalGeneFilename + ". " +
                    "Error: " + e.getMessage());
        }
        logger.info("End domains import.");
    }
    
    private List<Domain> getDomains(List<DomainTO> domainTOs) {
        return domainTOs.stream()
                .map(ServiceUtils::mapFromTO)
                .collect(Collectors.toList());
    }
}
