package org.moultdb.api.service;

import org.apache.commons.lang3.StringUtils;
import org.moultdb.api.model.*;
import org.moultdb.api.model.moutldbenum.*;
import org.moultdb.api.repository.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-27
 */
@Service
public class ServiceUtils {
    
    public static String API_URL;
    
    @Value("${url.api}")
    public void setApiUrl(String apiUrl) {
        API_URL = apiUrl;
    }
    
    public static String getImageUrl(String filename) {
        String url = API_URL;
        if (!API_URL.endsWith("/")) {
            url = url + "/";
        }
        url = url + "image/" + filename;
        return url;
    }
    
    public static DataSource mapFromTO(DataSourceTO dataSourceTO) {
        if (dataSourceTO == null) {
            return null;
        }
        return new DataSource(dataSourceTO.getName(), dataSourceTO.getDescription(),
                dataSourceTO.getBaseURL(), dataSourceTO.getLastImportDate(),
                dataSourceTO.getReleaseVersion());
    }
    
    public static Term mapFromTO(TermTO termTO) {
        if (termTO == null) {
            return null;
        }
        return new Term(termTO.getId(), termTO.getName(), termTO.getDescription());
    }
    
    public static TaxonAnnotation mapFromTO(TaxonAnnotationTO taxonAnnotationTO, SampleSetTO sampleSetTO,
                                            MoultingCharactersTO moultingCharactersTO,
                                            Map<Integer, VersionTO> versionTOByIds) {
        // TODO first check if TaxonAnnotation is updated
        if (taxonAnnotationTO == null) {
            return null;
        }
        Version taxonAnnotVersion = null;
        if (versionTOByIds != null) {
            taxonAnnotVersion = ServiceUtils.mapFromTO(versionTOByIds.get(taxonAnnotationTO.getVersionId()));
        }
        
        Article article = mapFromTO(taxonAnnotationTO.getArticleTO());

        return new TaxonAnnotation(
                mapFromTO(taxonAnnotationTO.getTaxonTO()),
                taxonAnnotationTO.getAnnotatedSpeciesName(),
                taxonAnnotationTO.getDeterminedBy(), 
                mapFromTO(sampleSetTO),
                mapFromTO(taxonAnnotationTO.getConditionTO()),
                mapFromTO(moultingCharactersTO),
                article,
                mapFromTO(taxonAnnotationTO.getImageTO(), taxonAnnotationTO),
                mapFromTO(taxonAnnotationTO.getEcoTO()),
                mapFromTO(taxonAnnotationTO.getCioTO()),
                taxonAnnotVersion);
    }
    
    private static ImageInfo mapFromTO(ImageTO imageTO, TaxonAnnotationTO taxonAnnotationTO) {
        if (imageTO == null) {
            return null;
        }
        return new ImageInfo(imageTO.getFileName().substring(0, imageTO.getFileName().indexOf(".")),
                taxonAnnotationTO.getAnnotatedSpeciesName(), getImageUrl(imageTO.getFileName()));
    }
    
    public static SampleSet mapFromTO(SampleSetTO sampleSetTO) {
        if (sampleSetTO == null) {
            return null;
        }
        TimePeriod timePeriod = new TimePeriod(mapFromTO(sampleSetTO.getFromGeologicalAgeTO()),
                mapFromTO(sampleSetTO.getToGeologicalAgeTO()));
        return new SampleSet(
                timePeriod,
                sampleSetTO.getCollectionLocationNames(),
                sampleSetTO.getStorageAccessions(), sampleSetTO.getStorageLocationNames(),
                sampleSetTO.getGeologicalFormations(), sampleSetTO.getFossilPreservationTypes(),
                sampleSetTO.getEnvironments(), sampleSetTO.getSpecimenTypes(),
                sampleSetTO.getSpecimenCount(), sampleSetTO.isFossil(), sampleSetTO.isCaptive());
    }
    
    public static Condition mapFromTO(ConditionTO conditionTO) {
        if (conditionTO == null) {
            return null;
        }
        DevStage devStage = null;
        if (conditionTO.getDevStageTO() != null) {
            devStage = new DevStage(conditionTO.getDevStageTO().getId(),
                    conditionTO.getDevStageTO().getName(), conditionTO.getDevStageTO().getDescription(),
                    conditionTO.getDevStageTO().getLeftBound(), conditionTO.getDevStageTO().getRightBound());
        }
        
        AnatEntity anatEntity = null;
        if (conditionTO.getAnatomicalEntityTO() != null) {
            anatEntity = new AnatEntity(conditionTO.getAnatomicalEntityTO().getId(),
                    conditionTO.getAnatomicalEntityTO().getName(), conditionTO.getAnatomicalEntityTO().getDescription());
        }
        
        MoultingStep moultingStep = null;
        if (conditionTO.getMoultingStep() != null) {
            moultingStep = MoultingStep.valueOfByStringRepresentation(conditionTO.getMoultingStep());
        }
        
        return new Condition(devStage, anatEntity, conditionTO.getSex(),moultingStep);
    }
    
    public static DbXref mapFromTO(DbXrefTO dbXrefTO) {
        if (dbXrefTO == null) {
            return null;
        }
        return new DbXref(dbXrefTO.getAccession(), dbXrefTO.getName(), mapFromTO(dbXrefTO.getDataSourceTO()));
    }
    
    public static Set<DbXref> mapFromTOs(Collection<DbXrefTO> dbXrefTOs) {
        if (dbXrefTOs == null) {
            return null;
        }
        return dbXrefTOs.stream()
                        .map(ServiceUtils::mapFromTO)
                        .collect(Collectors.toSet());
    }
    
    public static MoultingCharacters mapFromTO(MoultingCharactersTO mcTO) {
        if (mcTO == null) {
            return null;
        }
        return new MoultingCharacters(LifeHistoryStyle.valueOfByStringRepresentation(mcTO.getLifeHistoryStyle()),
                LifeMode.valueOfByStringRepresentation(mcTO.getLifeMode()), mcTO.getJuvenileMoultCount(),
                mcTO.getMajorMorphologicalTransitionCount(), mcTO.getHasTerminalAdultStage(),
                mcTO.getObservedMoultStageCount(), mcTO.getEstimatedMoultStageCount(), mcTO.getSegmentAdditionMode(),
                mcTO.getBodySegmentCount(), mcTO.getBodySegmentCountInAdults(), mcTO.getBodyLengthAverage(),
                mcTO.getBodyLengthIncreaseAverage(), mcTO.getBodyMassIncreaseAverage(), mcTO.getIntermoultPeriod(),
                mcTO.getPremoultPeriod(), mcTO.getPostmoultPeriod(), mcTO.getVariationWithinCohorts(),
                mcTO.getSutureLocation(), mcTO.getCephalicSutureLocation(), mcTO.getPostCephalicSutureLocation(),
                mcTO.getResultingNamedMoultingConfiguration(),
                EgressDirection.valueOfByStringRepresentation(mcTO.getEgressDirection()),
                ExuviaePosition.valueOfByStringRepresentation(mcTO.getPositionExuviaeFoundIn()),
                MoultingPhase.valueOfByStringRepresentation(mcTO.getMoultingPhase()),
                MoultingVariability.valueOfByStringRepresentation(mcTO.getMoultingVariability()),
                Calcification.valueOfByStringRepresentation(mcTO.getCalcificationEvent()),
                HeavyMetalReinforcement.valueOfByStringRepresentation(mcTO.getHeavyMetalReinforcement()),
                mcTO.getOtherBehaviour(), ExuviaeConsumption.valueOfByStringRepresentation(mcTO.getExuviaeConsumed()),
                Reabsorption.valueOfByStringRepresentation(mcTO.getExoskeletalMaterialReabsorption()),
                mcTO.getFossilExuviaeQuality(), mcTO.getGeneralComments());
    }
    
    public static GeologicalAge mapFromTO(GeologicalAgeTO geologicalAgeTO) {
        if (geologicalAgeTO == null) {
            return null;
        }
        return new GeologicalAge(geologicalAgeTO.getName(), geologicalAgeTO.getNotation(), geologicalAgeTO.getRank(),
                geologicalAgeTO.getYoungerBound(), geologicalAgeTO.getYoungerBoundImprecision(),
                geologicalAgeTO.getOlderBound(), geologicalAgeTO.getOlderBoundImprecision(), geologicalAgeTO.getSynonyms());
    }
    
    public static Taxon mapFromTO(TaxonTO taxonTO) {
        if (taxonTO == null) {
            return null;
        }
        return new Taxon(taxonTO.getPath(), taxonTO.getScientificName(), taxonTO.getCommonName(),
                taxonTO.getParentTaxonPath(), taxonTO.isExtincted(), mapFromTOs(taxonTO.getDbXrefTOs()));
    }
    
    public static Article mapFromTO(ArticleTO articleTO) {
        if (articleTO == null) {
            return null;
        }
        return new Article(articleTO.getCitation() , articleTO.getTitle(), articleTO.getAuthors(), mapFromTOs(articleTO.getDbXrefTOs()));
    }
    
    public static Version mapFromTO(VersionTO versionTO) {
        if (versionTO == null) {
            return null;
        }
        MoultDBUser creator = mapFromTO(versionTO.getCreationUserTO());
        MoultDBUser lastUpdateUser = mapFromTO(versionTO.getLastUpdateUserTO());
        return new Version(creator, versionTO.getCreationDate(), lastUpdateUser, versionTO.getLastUpdateDate(),
                versionTO.getVersionNumber());
    }
    
    public static MoultDBUser mapFromTO(UserTO userTO) {
        if (userTO == null) {
            return null;
        }
        Set<Role> roles = null;
        if (StringUtils.isNotBlank(userTO.getRoles())) {
            roles = Arrays.stream(userTO.getRoles().split(","))
                          .map(Role::valueOf)
                          .collect(Collectors.toSet());
        }
        return new MoultDBUser(userTO.getEmail(), userTO.getName(), userTO.getPassword(), userTO.getOrcidId(),
                roles, userTO.isVerified());
    }
}
