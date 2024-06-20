package org.moultdb.api.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.exception.ImportException;
import org.moultdb.api.repository.dao.GeneDAO;
import org.moultdb.api.repository.dao.OrthogroupDAO;
import org.moultdb.api.repository.dto.GeneTO;
import org.moultdb.api.repository.dto.OrthogroupTO;
import org.moultdb.api.service.OrthogroupService;
import org.moultdb.importer.genomics.OrthogroupParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2024-04-30
 */
@Service
public class OrthogroupServiceImpl implements OrthogroupService {
    
    private final static Logger logger = LogManager.getLogger(OrthogroupServiceImpl.class.getName());
    
    @Autowired GeneDAO geneDAO;
    @Autowired OrthogroupDAO orthogroupDAO;
    
    @Override
    public void importOrthogroups(MultipartFile orthogroupFile, MultipartFile pathwayFile) {
        String originalFilename = orthogroupFile.getOriginalFilename();
        if (StringUtils.isBlank(originalFilename)) {
            throw new IllegalArgumentException("File name cannot be blank");
        }
        
        logger.info("Start orthogroup data import...");
        
        OrthogroupParser parser = new OrthogroupParser();
        try {
            logger.info("Parse orthogroup data file " + originalFilename + " to create orthogroups with " + pathwayFile.getOriginalFilename() + "...");

            Set<OrthogroupTO> orthogroupTOs = parser.getOrthogroups(orthogroupFile, pathwayFile);

            logger.info("Load orthogroup data in db...");
            orthogroupDAO.batchUpdate(orthogroupTOs);
            
            logger.info("Parse orthogroup data file " + originalFilename + " to update genes...");
            Set<GeneTO> geneTOs = parser.updatedGenes(orthogroupFile, geneDAO, orthogroupDAO);
            
            logger.info("Update genes in db...");
            geneDAO.batchUpdate(geneTOs);
            
        } catch (Exception e) {
            throw new ImportException("Unable to import orthogroup data from " + originalFilename + ". " +
                    "Error: " + e.getMessage());
        }
        logger.info("End orthogroup data import");
    }
}
