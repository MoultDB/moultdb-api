package org.moultdb.api.service;

import org.apache.commons.lang3.StringUtils;
import org.moultdb.api.model.AnatEntity;
import org.moultdb.api.model.Article;
import org.moultdb.api.model.Condition;
import org.moultdb.api.model.DataSource;
import org.moultdb.api.model.DbXref;
import org.moultdb.api.model.DevStage;
import org.moultdb.api.model.GeologicalAge;
import org.moultdb.api.model.MoultingCharacters;
import org.moultdb.api.model.moutldbenum.MoultingStep;
import org.moultdb.api.model.SampleSet;
import org.moultdb.api.model.Taxon;
import org.moultdb.api.model.TaxonAnnotation;
import org.moultdb.api.model.Term;
import org.moultdb.api.model.TimePeriod;
import org.moultdb.api.model.MoultDBUser;
import org.moultdb.api.model.Version;
import org.moultdb.api.model.moutldbenum.EgressDirection;
import org.moultdb.api.model.moutldbenum.ExuviaeConsumption;
import org.moultdb.api.model.moutldbenum.ExuviaePosition;
import org.moultdb.api.model.moutldbenum.LifeHistoryStyle;
import org.moultdb.api.model.moutldbenum.MoultingPhase;
import org.moultdb.api.model.moutldbenum.MoultingVariability;
import org.moultdb.api.model.moutldbenum.Reabsorption;
import org.moultdb.api.model.moutldbenum.Role;
import org.moultdb.api.repository.dto.ArticleTO;
import org.moultdb.api.repository.dto.ConditionTO;
import org.moultdb.api.repository.dto.DataSourceTO;
import org.moultdb.api.repository.dto.DbXrefTO;
import org.moultdb.api.repository.dto.GeologicalAgeTO;
import org.moultdb.api.repository.dto.MoultingCharactersTO;
import org.moultdb.api.repository.dto.SampleSetTO;
import org.moultdb.api.repository.dto.TaxonAnnotationTO;
import org.moultdb.api.repository.dto.TaxonTO;
import org.moultdb.api.repository.dto.TermTO;
import org.moultdb.api.repository.dto.UserTO;
import org.moultdb.api.repository.dto.VersionTO;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-27
 */
public class ServiceUtils {
    
    public static DataSource mapFromTO(DataSourceTO dataSourceTO) {
        return new DataSource(dataSourceTO.getName(), dataSourceTO.getDescription(),
                dataSourceTO.getBaseURL(), dataSourceTO.getLastImportDate(),
                dataSourceTO.getReleaseVersion());
    }
    
    public static Term mapFromTO(TermTO termTO) {
        return new Term(termTO.getId(), termTO.getName(), termTO.getDescription());
    }
    
    public static TaxonAnnotation mapFromTO(TaxonAnnotationTO taxonAnnotationTO, SampleSetTO sampleSetTO, MoultingCharactersTO moultingCharactersTO, Map<Integer, VersionTO> versionTOByIds) {
        // TODO first check if TaxonAnnotation is updated
        Version taxonAnnotVersion = ServiceUtils.mapFromTO(
                versionTOByIds.get(taxonAnnotationTO.getVersionId()));
        
        Article article = mapFromTO(taxonAnnotationTO.getArticleTO());

        return new TaxonAnnotation(
                mapFromTO(taxonAnnotationTO.getTaxonTO()),
                taxonAnnotationTO.getAnnotatedSpeciesName(),
                mapFromTO(sampleSetTO),
                mapFromTO(taxonAnnotationTO.getConditionTO()),
                mapFromTO(moultingCharactersTO),
                article,
                mapFromTO(taxonAnnotationTO.getEcoTO()),
                mapFromTO(taxonAnnotationTO.getCioTO()),
                taxonAnnotVersion);
    }
    
    public static SampleSet mapFromTO(SampleSetTO sampleSetTO) {
        TimePeriod timePeriod = new TimePeriod(mapFromTO(sampleSetTO.getFromGeologicalAgeTO()),
                mapFromTO(sampleSetTO.getToGeologicalAgeTO()));
        return new SampleSet(
                timePeriod,
                sampleSetTO.getCollectionLocationNames(),
                sampleSetTO.getStorageAccessions(), sampleSetTO.getStorageLocationNames(),
                sampleSetTO.getGeologicalFormations(), sampleSetTO.getFossilPreservationTypes(),
                sampleSetTO.getEnvironments(), sampleSetTO.getSpecimenTypes(),
                sampleSetTO.getSpecimenCount());
    }
    
    public static Condition mapFromTO(ConditionTO conditionTO) {
        DevStage devStage = new DevStage(conditionTO.getDevStageTO().getId(),
                conditionTO.getDevStageTO().getName(), conditionTO.getDevStageTO().getDescription(),
                conditionTO.getDevStageTO().getLeftBound(), conditionTO.getDevStageTO().getRightBound());
        
        AnatEntity anatEntity = new AnatEntity(conditionTO.getAnatomicalEntityTO().getId(),
                conditionTO.getAnatomicalEntityTO().getName(), conditionTO.getAnatomicalEntityTO().getDescription());

        return new Condition(devStage, anatEntity, conditionTO.getSex(),
                MoultingStep.valueOf(conditionTO.getMoultingStep()));
    }
    
    public static DbXref mapFromTO(DbXrefTO dbXrefTO) {
        return new DbXref(dbXrefTO.getAccession(), mapFromTO(dbXrefTO.getDataSourceTO()));
    }
    
    public static Set<DbXref> mapFromTOs(Collection<DbXrefTO> dbXrefTOs) {
        return dbXrefTOs.stream()
                        .map(ServiceUtils::mapFromTO)
                        .collect(Collectors.toSet());
    }
    
    public static MoultingCharacters mapFromTO(MoultingCharactersTO mcTO) {
        return new MoultingCharacters(LifeHistoryStyle.valueOf(mcTO.getLifeHistoryStyle()), mcTO.getHasTerminalAdultStage(),
                mcTO.getObservedMoultStageCount(), mcTO.getEstimatedMoultStageCount(), mcTO.getSpecimenCount(),
                mcTO.getSegmentAdditionMode(), mcTO.getBodySegmentsCountPerMoultStage(), mcTO.getBodySegmentsCountInAdults(),
                mcTO.getBodyLengthAverage(), mcTO.getBodyLengthIncreaseAverage(),
                mcTO.getMeasurementUnit(), mcTO.getSutureLocation(), mcTO.getCephalicSutureLocation(), mcTO.getPostCephalicSutureLocation(),
                mcTO.getResultingNamedMoultingConfiguration(), EgressDirection.valueOf(mcTO.getEgressDirection()),
                ExuviaePosition.valueOf(mcTO.getPositionExuviaeFoundIn()), MoultingPhase.valueOf(mcTO.getMoultingPhase()),
                MoultingVariability.valueOf(mcTO.getMoultingVariability()), mcTO.getOtherBehaviour(),
                ExuviaeConsumption.valueOf(mcTO.getExuviaeConsumed()), Reabsorption.valueOf(mcTO.getExoskeletalMaterialReabsorption()),
                mcTO.getFossilExuviaeQuality());
    }
    
    public static GeologicalAge mapFromTO(GeologicalAgeTO geologicalAgeTO) {
        return new GeologicalAge(geologicalAgeTO.getNotation(), geologicalAgeTO.getName(), geologicalAgeTO.getRank(),
                geologicalAgeTO.getYoungerBound(), geologicalAgeTO.getYoungerBoundImprecision(),
                geologicalAgeTO.getOlderBound(), geologicalAgeTO.getOlderBoundImprecision(), geologicalAgeTO.getSynonyms());
    }
    
    public static Taxon mapFromTO(TaxonTO taxonTO) {
        return new Taxon(taxonTO.getPath(), taxonTO.getScientificName(), taxonTO.getCommonName(), taxonTO.getRank(),
                taxonTO.getParentTaxonPath(), taxonTO.isExtincted(), mapFromTOs(taxonTO.getDbXrefTOs()));
    }
    
    public static Article mapFromTO(ArticleTO articleTO) {
        return new Article(articleTO.getCitation() , articleTO.getTitle(), articleTO.getAuthors(), mapFromTOs(articleTO.getDbXrefTOs()));
    }
    
    public static Version mapFromTO(VersionTO versionTO) {
        MoultDBUser creator = mapFromTO(versionTO.getCreationUserTO());
        MoultDBUser lastUpdateUser = mapFromTO(versionTO.getLastUpdateUserTO());
        return new Version(creator, versionTO.getCreationDate(), lastUpdateUser, versionTO.getLastUpdateDate(),
                versionTO.getVersionNumber());
    }
    
    public static MoultDBUser mapFromTO(UserTO userTO) {
        Set<Role> roles = null;
        if (StringUtils.isNotBlank(userTO.getRoles())) {
            roles = Arrays.stream(userTO.getRoles().split(","))
                          .map(Role::valueOf)
                          .collect(Collectors.toSet());
        }
        return new MoultDBUser(userTO.getEmail(), userTO.getName(), userTO.getPassword(),
                roles, userTO.isVerified(), userTO.getOrcidId());
    }
}
