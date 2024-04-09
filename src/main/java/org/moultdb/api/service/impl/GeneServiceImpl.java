package org.moultdb.api.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.exception.ImportException;
import org.moultdb.api.model.Gene;
import org.moultdb.api.repository.dao.*;
import org.moultdb.api.repository.dto.*;
import org.moultdb.api.service.GeneService;
import org.moultdb.api.service.ServiceUtils;
import org.moultdb.importer.genomics.GeneParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Valentine Rech de Laval
 * @since 2024-03-25
 */
@Service
public class GeneServiceImpl implements GeneService {
    
    private final static Logger logger = LogManager.getLogger(GeneServiceImpl.class.getName());
    
    @Autowired DataSourceDAO dataSourceDAO;
    @Autowired GeneDAO geneDAO;
    @Autowired GeneToDomainDAO geneToDomainDAO;
    @Autowired GenomeDAO genomeDAO;
    @Autowired TaxonDAO taxonDAO;
    
    @Override
    public List<Gene> getAllGenes() {
        return getGenes(geneDAO.findAll());
    }
    
    @Override
    public Gene getGeneByProtein(String proteinId) {
        return getGene(geneDAO.findByProteinId(proteinId));
    }
    
    @Override
    public List<Gene> getGenesByTaxon(String taxonPath, Boolean inAMoultingPathway) {
        return getGenes(geneDAO.findByTaxon(taxonPath, inAMoultingPathway));
    }
    
    @Override
    public List<Gene> getGenesByPathway(String pathwayId) {
        return getGenes(geneDAO.findByPathwayId(pathwayId));
    }
    
    @Override
    public List<Gene> getGenesByDomain(String domainId) {
        return getGenes(geneDAO.findByDomainId(domainId));
    }
    
    @Override
    public void importGenes(MultipartFile geneFile) {
        String originalGeneFilename = geneFile.getOriginalFilename();
        if (StringUtils.isBlank(originalGeneFilename)) {
            throw new IllegalArgumentException("File name cannot be blank.");
        }
        
        logger.info("Start genes import...");
        
        GeneParser importer = new GeneParser();
        try {
            logger.info("Parse file " + originalGeneFilename + "...");
            Set<GeneTO> geneTOs = importer.getGeneTOs(geneFile, dataSourceDAO, geneDAO, genomeDAO);
            
            logger.info("Load genes in db...");
            geneDAO.batchUpdate(geneTOs);
            
        } catch (Exception e) {
            throw new ImportException("Unable to import genes from " + originalGeneFilename + ". " +
                    "Error: " + e.getMessage());
        }
        logger.info("End genes import.");
    }
    
    private Gene getGene(GeneTO geneTO) {
        if (geneTO == null) {
            return null;
        }
        List<Gene> genes = getGenes(Collections.singletonList(geneTO));
        if (genes == null || genes.isEmpty()) {
            return null;
        } 
        return genes.get(0);
    }
    
    private List<Gene> getGenes(List<GeneTO> geneTOs) {
        List<GeneToDomainTO> geneToDomainTOs = geneToDomainDAO.findByGeneIds(
                geneTOs.stream().map(GeneTO::getId).collect(Collectors.toSet()));
        Map<Integer, List<GeneToDomainTO>> domains = geneToDomainTOs.stream()
                .collect(Collectors.groupingBy(GeneToDomainTO::getGeneId));
        
        List<TaxonTO> taxonTOs = taxonDAO.findByPaths(geneTOs.stream().map(GeneTO::getTaxonPath).collect(Collectors.toSet()));
        Map<String, TaxonTO> taxonTOsById = taxonTOs.stream()
                .collect(Collectors.toMap(TaxonTO::getPath, Function.identity()));
        
        return geneTOs.stream()
                .map(to -> ServiceUtils.mapFromTO(to, domains.get(to.getId()), taxonTOsById.get(to.getTaxonPath())))
                .collect(Collectors.toList());
    }
}
