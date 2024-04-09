package org.moultdb.api.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.exception.ImportException;
import org.moultdb.api.model.Pathway;
import org.moultdb.api.repository.dao.GeneDAO;
import org.moultdb.api.repository.dao.PathwayDAO;
import org.moultdb.api.repository.dto.GeneTO;
import org.moultdb.api.repository.dto.PathwayTO;
import org.moultdb.api.service.PathwayService;
import org.moultdb.api.service.ServiceUtils;
import org.moultdb.importer.genomics.PathwayParser;
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
public class PathwayServiceImpl implements PathwayService {
    
    private final static Logger logger = LogManager.getLogger(PathwayServiceImpl.class.getName());
    
    @Autowired PathwayDAO pathwayDAO;
    @Autowired GeneDAO geneDAO;
    
    @Override
    public List<Pathway> getAllPathways() {
        return getPathways(pathwayDAO.findAll());
    }
    
    @Override
    public Pathway getPathwayById(String pathwayId) {
        return mapFromTO(pathwayDAO.findById(pathwayId));
    }
    
    @Override
    public void importPathways(MultipartFile pathwayCvFile) {
        String originalFilename = pathwayCvFile.getOriginalFilename();
        if (StringUtils.isBlank(originalFilename)) {
            throw new IllegalArgumentException("File name cannot be blank.");
        }
        
        logger.info("Start pathways import...");
        
        int count;
        PathwayParser parser = new PathwayParser();
        try {
            logger.info("Parse pathway file " + originalFilename + "...");
            Set<PathwayTO> pathwayTOs = parser.getPathwayTOs(pathwayCvFile);
            
            logger.info("Load pathways in db...");
            pathwayDAO.batchUpdate(pathwayTOs);
            
        } catch (Exception e) {
            throw new ImportException("Unable to import pathways from " + originalFilename + ". " +
                    "Error: " + e.getMessage());
        }
        logger.info("End pathways import.");
    }
    
    @Override
    public void importPathwayData(MultipartFile geneToPathwayFile) {
        String originalFilename = geneToPathwayFile.getOriginalFilename();
        if (StringUtils.isBlank(originalFilename)) {
            throw new IllegalArgumentException("File name cannot be blank.");
        }
        
        logger.info("Start pathway data import...");
        
        int count;
        PathwayParser parser = new PathwayParser();
        try {
            logger.info("Parse pathway data file " + originalFilename + "...");
            Set<GeneTO> geneTOs = parser.getUpdatedGeneTOs(geneToPathwayFile, geneDAO, pathwayDAO);
            
            logger.info("Load pathway data in db...");
            geneDAO.batchUpdate(geneTOs);
            
        } catch (Exception e) {
            throw new ImportException("Unable to import pathway data from " + originalFilename + ". " +
                    "Error: " + e.getMessage());
        }
        logger.info("End pathway data import.");
    }
    
    private List<Pathway> getPathways(List<PathwayTO> pathwayTOs) {
        return pathwayTOs.stream()
                .map(ServiceUtils::mapFromTO)
                .collect(Collectors.toList());
    }
}
