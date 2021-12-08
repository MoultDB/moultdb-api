package org.moultdb.api.service.impl;

import org.moultdb.api.model.Article;
import org.moultdb.api.model.Taxon;
import org.moultdb.api.repository.dao.ArticleDAO;
import org.moultdb.api.repository.dao.ArticleToDbXrefDAO;
import org.moultdb.api.repository.dao.ArticleToTaxonDAO;
import org.moultdb.api.repository.dao.DbXrefDAO;
import org.moultdb.api.repository.dao.TaxonDAO;
import org.moultdb.api.repository.dto.ArticleTO;
import org.moultdb.api.repository.dto.ArticleToDbXrefTO;
import org.moultdb.api.repository.dto.ArticleToTaxonTO;
import org.moultdb.api.repository.dto.DbXrefTO;
import org.moultdb.api.repository.dto.EntityTO;
import org.moultdb.api.repository.dto.TaxonTO;
import org.moultdb.api.repository.dto.TransfertObject;
import org.moultdb.api.service.TaxonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.moultdb.api.service.Service.mapFromTO;

@Service
public class TaxonServiceImpl implements TaxonService {
    
    @Autowired
    private ArticleDAO articleDAO;
    
    @Autowired
    private ArticleToDbXrefDAO articleToDbXrefDAO;
    
    @Autowired
    private ArticleToTaxonDAO articleToTaxonDAO;
    
    @Autowired
    private DbXrefDAO dbXrefDao;
    
    @Autowired
    private TaxonDAO taxonDao;
    
    @Override
    public List<Taxon> getAllTaxa() {
        return getTaxons(taxonDao.findAll());
    }
    
    @Override
    public Taxon getTaxonByScientificName(String scientificName) {
        TaxonTO taxonTO = TransfertObject.getOneTO(taxonDao.findByScientificName(scientificName));
        if (taxonTO != null) {
            return getTaxons(Collections.singletonList(taxonTO)).get(0);
        }
        return null;
    }
    
    private List<Taxon> getTaxons(List<TaxonTO> taxonTOs) {
    
        Set<Integer> taxonIds = taxonTOs.stream().map(EntityTO::getId).collect(Collectors.toSet());
        
        Set<Integer> dbXrefIds = taxonTOs.stream()
                                         .map(TaxonTO::getDbXrefId)
                                         .collect(Collectors.toSet());
        Map<Integer, DbXrefTO> dbXrefTOsById = dbXrefDao.findByIds(dbXrefIds)
                                                        .stream()
                                                        .collect(Collectors.toMap(DbXrefTO::getId, Function.identity()));
    

        Map<Integer, Set<ArticleTO>> articleTOsbyTaxonIds = articleDAO.findByTaxonIds(taxonIds);
        Set<Integer> articlesIds = articleTOsbyTaxonIds.values().stream()
                                                       .flatMap(Set::stream)
                                                       .map(EntityTO::getId)
                                                       .collect(Collectors.toSet());
        Map<Integer, Set<DbXrefTO>> dbXrefTOsByArticleIds = dbXrefDao.findByArticleIds(articlesIds);
        
        Map<Integer, Set<Article>> articlesByTaxonId = articleTOsbyTaxonIds.entrySet().stream()
                                                                           .collect(Collectors.toMap(Map.Entry::getKey,
                                                                                   e -> e.getValue().stream()
                                                                                         .map(to -> mapFromTO(to, dbXrefTOsByArticleIds))
                                                                                         .collect(Collectors.toSet())));
        
        return taxonTOs.stream()
                       .map(to-> mapFromTO(to, dbXrefTOsById, articlesByTaxonId.get(to.getId())))
                       .collect(Collectors.toList());
    }
    
    @Override
    public void insertTaxon(Taxon taxon) {
        taxonDao.insertTaxon(convertToDto(taxon));
    }
    
    private static TaxonTO convertToDto(Taxon taxon) {
        throw new UnsupportedOperationException("Conversion from entity to TO not available");
    }
}
