package org.moultdb.api.service.impl;

import org.moultdb.api.model.TaxonAnnotation;
import org.moultdb.api.repository.dao.MoultingCharactersDAO;
import org.moultdb.api.repository.dao.SampleSetDAO;
import org.moultdb.api.repository.dao.TaxonAnnotationDAO;
import org.moultdb.api.repository.dao.VersionDAO;
import org.moultdb.api.repository.dto.MoultingCharactersTO;
import org.moultdb.api.repository.dto.SampleSetTO;
import org.moultdb.api.repository.dto.TaxonAnnotationTO;
import org.moultdb.api.repository.dto.VersionTO;
import org.moultdb.api.service.TaxonAnnotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    
    @Autowired
    private MoultingCharactersDAO moultingCharactersDAO;
    
    @Autowired
    private SampleSetDAO sampleSetDAO;
    
    @Autowired
    private TaxonAnnotationDAO taxonAnnotationDAO;
    
    @Autowired
    private VersionDAO versionDAO;
    
    @Override
    public List<TaxonAnnotation> getAllTaxonAnnotations() {
        List<TaxonAnnotationTO> taxonAnnotationTOs = taxonAnnotationDAO.findAll();
    
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
}
