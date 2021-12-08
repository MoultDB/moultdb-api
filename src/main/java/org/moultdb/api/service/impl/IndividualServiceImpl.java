package org.moultdb.api.service.impl;

import org.moultdb.api.model.Individual;
import org.moultdb.api.repository.dao.ArticleDAO;
import org.moultdb.api.repository.dao.ArticleToSampleDAO;
import org.moultdb.api.repository.dao.IndividualDAO;
import org.moultdb.api.repository.dao.SampleDAO;
import org.moultdb.api.repository.dao.DbXrefDAO;
import org.moultdb.api.repository.dao.MoultingCharactersDAO;
import org.moultdb.api.repository.dao.TaxonDAO;
import org.moultdb.api.repository.dao.VersionDAO;
import org.moultdb.api.repository.dto.ArticleTO;
import org.moultdb.api.repository.dto.ArticleToDbXrefTO;
import org.moultdb.api.repository.dto.ArticleToSampleTO;
import org.moultdb.api.repository.dto.DbXrefTO;
import org.moultdb.api.repository.dto.EntityTO;
import org.moultdb.api.repository.dto.IndividualTO;
import org.moultdb.api.repository.dto.MoultingCharactersTO;
import org.moultdb.api.repository.dto.SampleTO;
import org.moultdb.api.repository.dto.TaxonTO;
import org.moultdb.api.repository.dto.VersionTO;
import org.moultdb.api.service.IndividualService;
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
public class IndividualServiceImpl implements IndividualService {
    
    @Autowired
    private ArticleDAO articleDAO;
    
    @Autowired
    private IndividualDAO individualDAO;
    
    @Autowired
    private DbXrefDAO dbXrefDAO;
    
    @Autowired
    private VersionDAO versionDAO;
    
    @Override
    public List<Individual> getAllIndividuals() {
        List<IndividualTO> individualTOs = individualDAO.findAll();
        
        Set<Integer> taxonDbXrefIds = individualTOs.stream()
                                             .map(to -> to.getTaxonTO().getDbXrefId())
                                             .collect(Collectors.toSet());
        Map<Integer, DbXrefTO> taxonDbXrefTOsById = dbXrefDAO.findByIds(taxonDbXrefIds)
                                                        .stream()
                                                        .collect(Collectors.toMap(DbXrefTO::getId, Function.identity()));
    
        Set<Integer> sampleIds = individualTOs.stream()
                                                   .map(to -> to.getSampleTO().getId())
                                                   .collect(Collectors.toSet());
        Map<Integer, Set<ArticleTO>> articleTOsBySampleIds = articleDAO.findBySampleIds(sampleIds);
        Set<Integer> articleIds = articleTOsBySampleIds.values().stream()
                                                     .flatMap(Set::stream)
                                                     .map(EntityTO::getId)
                                                     .collect(Collectors.toSet());
        Map<Integer, Set<DbXrefTO>> dbXrefTOsByArticleIds = dbXrefDAO.findByArticleIds(articleIds);
    
        Set<Integer> versionIds = individualTOs.stream()
                                              .map(IndividualTO::getVersionId)
                                              .collect(Collectors.toSet());
        versionIds.addAll(individualTOs.stream()
                                       .map(to -> to.getSampleTO().getVersionId()).collect(Collectors.toSet()));
        Map<Integer, VersionTO> sampleAndIndividualVersionTOMap = versionDAO.findByIds(versionIds).stream()
                                                           .collect(Collectors.toMap(VersionTO::getId, Function.identity()));
    
        return individualTOs.stream()
                            .map(to -> org.moultdb.api.service.Service.mapFromTO(to,
                                    articleTOsBySampleIds.get(to.getSampleTO().getId()),
                                    dbXrefTOsByArticleIds,
                                    sampleAndIndividualVersionTOMap,
                                    taxonDbXrefTOsById))
                            .collect(Collectors.toList());
    }
}
