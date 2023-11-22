package org.moultdb.api.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.exception.ImportException;
import org.moultdb.api.model.TaxonAnnotation;
import org.moultdb.api.repository.dao.*;
import org.moultdb.api.repository.dto.*;
import org.moultdb.api.service.ServiceUtils;
import org.moultdb.api.service.TaxonAnnotationService;
import org.moultdb.importer.fossilannotation.FossilAnnotationBean;
import org.moultdb.importer.fossilannotation.FossilImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-26
 */
@Service
public class TaxonAnnotationServiceImpl implements TaxonAnnotationService {
    
    private final static Logger logger = LogManager.getLogger(TaxonAnnotationServiceImpl.class.getName());
    
    @Autowired ArticleDAO articleDAO;
    @Autowired ArticleToDbXrefDAO articleToDbXrefDAO;
    @Autowired ConditionDAO conditionDAO;
    @Autowired DataSourceDAO dataSourceDAO;
    @Autowired DbXrefDAO dbXrefDAO;
    @Autowired DevStageDAO devStageDAO;
    @Autowired GeologicalAgeDAO geologicalAgeDAO;
    @Autowired MoultingCharactersDAO moultingCharactersDAO;
    @Autowired SampleSetDAO sampleSetDAO;
    @Autowired TaxonDAO taxonDAO;
    @Autowired TaxonAnnotationDAO taxonAnnotationDAO;
    @Autowired VersionDAO versionDAO;
    @Autowired UserDAO userDAO;
    
    @Override
    public List<TaxonAnnotation> getAllTaxonAnnotations() {
        List<TaxonAnnotationTO> taxonAnnotationTOs = taxonAnnotationDAO.findAll();
        return getTaxonAnnotations(taxonAnnotationTOs);
    }
    
    @Override
    public List<TaxonAnnotation> getLastTaxonAnnotations(int limit) {
        List<TaxonAnnotationTO> taxonAnnotationTOs = taxonAnnotationDAO.findLast(limit);
        return null;
    }
    
    @Override
    public List<TaxonAnnotation> getUserTaxonAnnotations(String email) {
        List<TaxonAnnotationTO> taxonAnnotationTOs = taxonAnnotationDAO.findByUser(email, null);
        return getTaxonAnnotations(taxonAnnotationTOs);
    }
    
    @Override
    public TaxonAnnotation getTaxonAnnotationsByImageFilename(String imageFilename) {
        TaxonAnnotationTO taxonAnnotationTO = taxonAnnotationDAO.findByImageFilename(imageFilename);
        List<TaxonAnnotation> taxonAnnotations = getTaxonAnnotations(Collections.singletonList(taxonAnnotationTO));
        if (taxonAnnotations.isEmpty()) {
            return null;
        }
        return taxonAnnotations.get(0);
    }
    
    @Override
    public List<TaxonAnnotation> getTaxonAnnotationsByTaxonPath(String taxonPath) {
        return getTaxonAnnotations(taxonAnnotationDAO.findByTaxonPath(taxonPath));
    }
    
    @Override
    public List<TaxonAnnotation> getTaxonAnnotationsByTaxonName(String taxonName) {
        TaxonTO taxonTO = taxonDAO.findByScientificName(taxonName);
        if (taxonTO == null) {
            return new ArrayList<>();
        }
        return getTaxonAnnotationsByTaxonPath(taxonTO.getPath());
    }
    
    private List<TaxonAnnotation> getTaxonAnnotations(List<TaxonAnnotationTO> taxonAnnotationTOs) {
        if (taxonAnnotationTOs.isEmpty()) {
            return new ArrayList<>();
        }
        
        // Get SampleSetTOs
        Set<Integer> sampleIds = taxonAnnotationTOs.stream()
                                                   .map(TaxonAnnotationTO::getSampleSetId)
                                                   .collect(Collectors.toSet());
        Map<Integer, SampleSetTO> sampleSetTOsbyIds =
                sampleSetDAO.findByIds(sampleIds)
                            .stream()
                            .collect(Collectors.toMap(SampleSetTO::getId, Function.identity()));
        
        // Get MoultingCharactersTOs
        Set<Integer> charactersIds = taxonAnnotationTOs.stream()
                                                       .map(TaxonAnnotationTO::getMoultingCharactersId)
                                                       .collect(Collectors.toSet());
        Map<Integer, MoultingCharactersTO> charactersTOsbyIds =
                moultingCharactersDAO.findByIds(charactersIds)
                                     .stream()
                                     .collect(Collectors.toMap(MoultingCharactersTO::getId, Function.identity()));
        
        // Get VersionTOs for taxon annotations and sample sets
        Set<Integer> versionIds = taxonAnnotationTOs.stream()
                                                    .map(TaxonAnnotationTO::getVersionId)
                                                    .collect(Collectors.toSet());
        Map<Integer, VersionTO> versionTOsByIds = versionDAO.findByIds(versionIds).stream()
                                                            .collect(Collectors.toMap(VersionTO::getId, Function.identity()));
        
        return taxonAnnotationTOs.stream()
                                 .map(to -> ServiceUtils.mapFromTO(to, sampleSetTOsbyIds.get(to.getSampleSetId()),
                                         charactersTOsbyIds.get(to.getMoultingCharactersId()), versionTOsByIds))
                                 .collect(Collectors.toList());
    }
    
    @Override
    public Integer importTaxonAnnotations(MultipartFile file) {
    
        logger.info("Start taxon annotations import...");
        
        FossilImporter importer = new FossilImporter();
        int count;
        try {
            logger.info("Parse annotation file " + file.getOriginalFilename() + "...");
            List<FossilAnnotationBean> fossilAnnotationBeans = importer.parseFossilAnnotation(file);
            
            logger.info("Load annotations in db...");
            count = importer.insertFossilAnnotation(fossilAnnotationBeans, articleDAO, articleToDbXrefDAO, conditionDAO,
                    dataSourceDAO, dbXrefDAO, devStageDAO, geologicalAgeDAO, moultingCharactersDAO, sampleSetDAO,
                    taxonDAO, taxonAnnotationDAO, versionDAO, userDAO);
        
        } catch (Exception e) {
            throw new ImportException("Unable to import annotations from " + file.getOriginalFilename() + ". " +
                    "Error: " + e.getMessage());
        }
        logger.info("End taxon annotations import.");
        
        return count;
    }
    
    @Override
    public void deleteTaxonAnnotationsByImageFilename(String imageFilename) {
        taxonAnnotationDAO.deleteByImageFilename(imageFilename);
    }
}
