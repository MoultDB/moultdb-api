package org.moultdb.api.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.exception.ImportException;
import org.moultdb.api.model.Genome;
import org.moultdb.api.repository.dao.GenomeDAO;
import org.moultdb.api.repository.dao.TaxonDAO;
import org.moultdb.api.repository.dto.GenomeTO;
import org.moultdb.api.service.GenomeService;
import org.moultdb.api.service.ServiceUtils;
import org.moultdb.importer.genome.GenomeParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Valentine Rech de Laval
 * @since 2023-11-21
 */
@Service
public class GenomeServiceImpl implements GenomeService {
    
    private final static Logger logger = LogManager.getLogger(GenomeServiceImpl.class.getName());
    
    @Autowired GenomeDAO genomeDAO;
    @Autowired TaxonDAO taxonDAO;
    
    @Override
    public List<Genome> getAllGenomes() {
        return getGenomes(genomeDAO.findAll());
    }
    
    @Override
    public List<Genome> getGenomesByTaxon(String taxonPath, boolean withSubspeciesGenomes) {
        return getGenomes(genomeDAO.findByTaxonPath(taxonPath, withSubspeciesGenomes));
    }
    
    private List<Genome> getGenomes(List<GenomeTO> genomeTOs) {
        return genomeTOs.stream()
                .map(ServiceUtils::mapFromTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public void updateGenomes(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (StringUtils.isBlank(originalFilename)) {
            throw new IllegalArgumentException("File name cannot be blank.");
        }
        
        logger.info("Start genomes import...");
        
        int count;
        GenomeParser importer = new GenomeParser();
        try {
            logger.info("Parse genome file " + file.getOriginalFilename() + "...");
            Set<GenomeTO> genomeTOs = importer.getGenomeTOs(file, taxonDAO);
            
            logger.info("Load genomes in db...");
            genomeDAO.batchUpdate(genomeTOs);
            
        } catch (Exception e) {
            throw new ImportException("Unable to import genomes from " + file.getOriginalFilename() + ". " +
                    "Error: " + e.getMessage());
        }
        logger.info("End genomes import.");
    }
}
