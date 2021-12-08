package org.moultdb.api.service;

import org.moultdb.api.model.Article;
import org.moultdb.api.model.Condition;
import org.moultdb.api.model.DataSource;
import org.moultdb.api.model.DbXref;
import org.moultdb.api.model.GeologicalAge;
import org.moultdb.api.model.Individual;
import org.moultdb.api.model.MoultingCharacters;
import org.moultdb.api.model.MoultingStep;
import org.moultdb.api.model.Sample;
import org.moultdb.api.model.Taxon;
import org.moultdb.api.model.User;
import org.moultdb.api.model.Version;
import org.moultdb.api.repository.dto.ArticleTO;
import org.moultdb.api.repository.dto.ArticleToDbXrefTO;
import org.moultdb.api.repository.dto.ConditionTO;
import org.moultdb.api.repository.dto.DataSourceTO;
import org.moultdb.api.repository.dto.DbXrefTO;
import org.moultdb.api.repository.dto.GeologicalAgeTO;
import org.moultdb.api.repository.dto.IndividualTO;
import org.moultdb.api.repository.dto.MoultingCharactersTO;
import org.moultdb.api.repository.dto.SampleTO;
import org.moultdb.api.repository.dto.TaxonTO;
import org.moultdb.api.repository.dto.VersionTO;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-27
 */
public interface Service {
    
    static DataSource mapFromTO(DataSourceTO dataSourceTO) {
        return new DataSource(dataSourceTO.getName(), dataSourceTO.getDescription(),
                dataSourceTO.getBaseURL(), dataSourceTO.getReleaseDate(), dataSourceTO.getReleaseVersion());
    }
    
    static Individual mapFromTO(IndividualTO individualTO,
                                Collection<ArticleTO> sampleArticleTOs,
                                Map<Integer, Set<DbXrefTO>> dbXrefTOsByArticleIds,
                                Map<Integer, VersionTO> sampleAndIndividualVersionTOMap,
                                Map<Integer, DbXrefTO> taxonDbXrefTOsById) {
        
        Version individualVersion = Service.mapFromTO(sampleAndIndividualVersionTOMap.get(individualTO.getVersionId()));
        
        Taxon taxon = mapFromTO(individualTO.getTaxonTO(), taxonDbXrefTOsById);
    
        SampleTO sampleTO = individualTO.getSampleTO();
        
        return new Individual(taxon,
                mapFromTO(sampleTO, sampleArticleTOs, dbXrefTOsByArticleIds, sampleAndIndividualVersionTOMap),
                mapFromTO(individualTO.getConditionTO()),
                mapFromTO(individualTO.getMoultingCharactersTO()),
                individualVersion);
    }
    
    static Sample mapFromTO(SampleTO sampleTO,
                            Collection<ArticleTO> articleTOs,
                            Map<Integer, Set<DbXrefTO>> dbXrefTOsByArticleIds,
                            Map<Integer, VersionTO> versionTOsById) {
        return new Sample(
                mapFromTO(sampleTO.getGeologicalAgeTO()),
                sampleTO.getCollectionLocationName(),
                sampleTO.getStorageAccession(),
                sampleTO.getStorageLocationName(),
                sampleTO.getCollector(),
                articleTOs.stream().map(to -> mapFromTO(to, dbXrefTOsByArticleIds)).collect(Collectors.toSet()),
                mapFromTO(versionTOsById.get(sampleTO.getVersionId())));
    }
    
    static Condition mapFromTO(ConditionTO conditionTO) {
        return new Condition(conditionTO.getDevStageId(), conditionTO.getAnatomicalEntityId(), conditionTO.getSexId(),
                MoultingStep.valueOf(conditionTO.getMoultingStep()));
    }
    
    static DbXref mapFromTO(DbXrefTO dbXrefTO) {
        return new DbXref(dbXrefTO.getAccession(), mapFromTO(dbXrefTO.getDataSourceTO()));
    }
    
    static MoultingCharacters mapFromTO(MoultingCharactersTO charactersTO) {
        return new MoultingCharacters(charactersTO.getHemimetabolous(),
                charactersTO.getMoultCount(), charactersTO.getSizeIncrease(), charactersTO.getHasAdultStage(),
                charactersTO.getAnamorphic(), charactersTO.getHasFixedMoultNumber(), charactersTO.getSutureLocation(),
                charactersTO.getEgressDirection(), charactersTO.getFragmentedExuviae(), charactersTO.getMonoPhasicMoulting(),
                charactersTO.getHasExoskeletalMaterialReabsorption(), charactersTO.getHasExuviaeConsumed(),
                charactersTO.getRepairExtent(), charactersTO.getMassMoulting(), charactersTO.getMatingLinked(),
                charactersTO.getHormoneRegulation());
    }
    
    static GeologicalAge mapFromTO(GeologicalAgeTO geologicalAgeTO) {
        return new GeologicalAge(geologicalAgeTO.getName(), geologicalAgeTO.getSymbol(),
                geologicalAgeTO.getYoungerBound(), geologicalAgeTO.getOlderBound());
    }
    
    static Taxon mapFromTO(TaxonTO taxonTO, Map<Integer, DbXrefTO> dbXrefTOsById) {
        return new Taxon(taxonTO.getName(), taxonTO.getCommonName(), mapFromTO(dbXrefTOsById.get(taxonTO.getDbXrefId())),
                taxonTO.getTaxonRank(), taxonTO.getParentTaxonId(), taxonTO.isExtinct(), taxonTO.getPath(), null);
    }
    
    static Taxon mapFromTO(TaxonTO taxonTO, Map<Integer, DbXrefTO> dbXrefTOsById, Set<Article> articles) {
        return new Taxon(taxonTO.getName(), taxonTO.getCommonName(), mapFromTO(dbXrefTOsById.get(taxonTO.getDbXrefId())),
                taxonTO.getTaxonRank(), taxonTO.getParentTaxonId(), taxonTO.isExtinct(), taxonTO.getPath(),
                articles);
    }
    
    static Article mapFromTO(ArticleTO articleTO, Map<Integer, Set<DbXrefTO>> dbXrefTOsByArticleIds) {
        Set<DbXref> dbXrefs = dbXrefTOsByArticleIds == null || dbXrefTOsByArticleIds.size() == 0?
                new HashSet<>() :
                dbXrefTOsByArticleIds.get(articleTO.getId()).stream().map(Service::mapFromTO).collect(Collectors.toSet());
        return new Article(articleTO.getTitle(), articleTO.getAuthors(), dbXrefs);
    }
    
    static Version mapFromTO(VersionTO versionTO) {
        return new Version(new User(versionTO.getCreationUserTO().getName()),
                versionTO.getCreationDate(), new User(versionTO.getLastUpdateUserTO().getName()),
                versionTO.getLastUpdateDate(), versionTO.getVersionNumber());
    }
}
