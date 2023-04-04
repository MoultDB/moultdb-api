package org.moultdb.api.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.exception.MoultDBException;
import org.moultdb.api.model.TaxonAnnotation;
import org.moultdb.api.repository.dao.ArticleDAO;
import org.moultdb.api.repository.dao.ArticleToDbXrefDAO;
import org.moultdb.api.repository.dao.ConditionDAO;
import org.moultdb.api.repository.dao.DataSourceDAO;
import org.moultdb.api.repository.dao.DbXrefDAO;
import org.moultdb.api.repository.dao.GeologicalAgeDAO;
import org.moultdb.api.repository.dao.MoultingCharactersDAO;
import org.moultdb.api.repository.dao.SampleSetDAO;
import org.moultdb.api.repository.dao.TaxonAnnotationDAO;
import org.moultdb.api.repository.dao.TaxonDAO;
import org.moultdb.api.repository.dao.VersionDAO;
import org.moultdb.api.repository.dto.MoultingCharactersTO;
import org.moultdb.api.repository.dto.SampleSetTO;
import org.moultdb.api.repository.dto.TaxonAnnotationTO;
import org.moultdb.api.repository.dto.VersionTO;
import org.moultdb.api.service.TaxonAnnotationService;
import org.moultdb.importer.fossilannotation.FossilAnnotationBean;
import org.moultdb.importer.fossilannotation.FossilImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    @Autowired DbXrefDAO dbXrefDAO;
    @Autowired DataSourceDAO dataSourceDAO;
    @Autowired GeologicalAgeDAO geologicalAgeDAO;
    @Autowired MoultingCharactersDAO moultingCharactersDAO;
    @Autowired SampleSetDAO sampleSetDAO;
    @Autowired TaxonDAO taxonDAO;
    @Autowired TaxonAnnotationDAO taxonAnnotationDAO;
    @Autowired VersionDAO versionDAO;
    
    @Value("${import.password}")
    final String password = null;
    
    void checkPassword(String pwd) {
        if (pwd == null || !pwd.equals(password)) {
            throw new MoultDBException("Wrong password");
        }
    }
    
    @Override
    public List<TaxonAnnotation> getAllTaxonAnnotations() {
        List<TaxonAnnotationTO> taxonAnnotationTOs = taxonAnnotationDAO.findAll();
        
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
        versionIds.addAll(sampleSetTOsbyIds.values().stream()
                                           .map(SampleSetTO::getVersionId)
                                           .collect(Collectors.toSet()));
        Map<Integer, VersionTO> versionTOsByIds =
                versionDAO.findByIds(versionIds).stream()
                          .collect(Collectors.toMap(VersionTO::getId, Function.identity()));
        
        return taxonAnnotationTOs.stream()
                                 .map(to -> org.moultdb.api.service.Service.mapFromTO(to,
                                         sampleSetTOsbyIds.get(to.getSampleSetId()),
                                         charactersTOsbyIds.get(to.getMoultingCharactersId()),
                                         versionTOsByIds))
                                 .collect(Collectors.toList());
    }
    
    @Override
    public Integer importTaxonAnnotations(@RequestParam("file") MultipartFile file, String pwd) {
    
        checkPassword(pwd);
    
        logger.info("Start taxon annotations import...");
    
        FossilImporter importer = new FossilImporter();

        List<FossilAnnotationBean> fossilAnnotationBeans = importer.parseFossilAnnotation(file);
    
        importer.insertFossilAnnotation(fossilAnnotationBeans, articleDAO, articleToDbXrefDAO, conditionDAO, dataSourceDAO,
                dbXrefDAO, geologicalAgeDAO, moultingCharactersDAO, sampleSetDAO, taxonDAO, taxonAnnotationDAO);
    
        logger.info("End taxon annotations import.");
    
        return null;
    }
}
