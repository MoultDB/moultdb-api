package org.moultdb.api.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.exception.ImportException;
import org.moultdb.api.exception.MoultDBException;
import org.moultdb.api.model.INaturalistResponse;
import org.moultdb.api.model.TaxonAnnotation;
import org.moultdb.api.model.moutldbenum.DatasourceEnum;
import org.moultdb.api.model.moutldbenum.Role;
import org.moultdb.api.repository.dao.*;
import org.moultdb.api.repository.dto.*;
import org.moultdb.api.service.ServiceUtils;
import org.moultdb.api.service.TaxonAnnotationService;
import org.moultdb.importer.taxonannotation.TaxonAnnotationBean;
import org.moultdb.importer.taxonannotation.TaxonAnnotationParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-26
 */
@Service
public class TaxonAnnotationServiceImpl implements TaxonAnnotationService {
    
    @Value("${inaturalist.url.api.observations}")
    private String INAT_API_PROJECT_CALL_URL;
    @Value("${inaturalist.url.observation}")
    private String INAT_OBSERVATION_URL;
    
    private final static Logger logger = LogManager.getLogger(TaxonAnnotationServiceImpl.class.getName());
    
    @Autowired AnatEntityDAO anatEntityDAO;
    @Autowired ArticleDAO articleDAO;
    @Autowired ArticleToDbXrefDAO articleToDbXrefDAO;
    @Autowired CIOStatementDAO cioDAO;
    @Autowired ConditionDAO conditionDAO;
    @Autowired DataSourceDAO dataSourceDAO;
    @Autowired DbXrefDAO dbXrefDAO;
    @Autowired DevStageDAO devStageDAO;
    @Autowired ECOTermDAO ecoTermDAO;
    @Autowired GeologicalAgeDAO geologicalAgeDAO;
    @Autowired ObservationDAO observationDAO;
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
    public List<TaxonAnnotation> getLastUpdatedTaxonAnnotations(int limit) {
        List<TaxonAnnotationTO> taxonAnnotationTOs = taxonAnnotationDAO.findLastUpdated(limit);
        return getTaxonAnnotations(taxonAnnotationTOs);
    }
    
    @Override
    public List<TaxonAnnotation> getUserTaxonAnnotations(String username) {
        List<TaxonAnnotationTO> taxonAnnotationTOs = taxonAnnotationDAO.findByUsername(username, null);
        return getTaxonAnnotations(taxonAnnotationTOs);
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
    
    @Override
    public List<TaxonAnnotation> getTaxonAnnotationsByDbXref(String datasource, String accession) {
        DatasourceEnum datasourceEnum = DatasourceEnum.valueOfByStringRepresentation(datasource);
        TaxonTO taxonTO = taxonDAO.findByAccession(accession, datasourceEnum.getStringRepresentation());
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
    public Integer importTaxonAnnotations(MultipartFile dataFile, MultipartFile mappingFile) {
    
        logger.info("Start taxon annotations import...");
        
        TaxonAnnotationParser importer = new TaxonAnnotationParser();
        int count;
        try {
            logger.info("Parse annotation file " + dataFile.getOriginalFilename() + "...");
            List<TaxonAnnotationBean> taxonAnnotationBeans = importer.parseAnnotations(dataFile);
            
            logger.info("Parse mapping dataFile " + mappingFile.getOriginalFilename() + "...");
            Map<String, String> devStageMapping = importer.parseDevStageMapping(mappingFile);
            
            logger.info("Load annotations in db...");
            count = importer.insertTaxonAnnotations(taxonAnnotationBeans, devStageMapping, articleDAO, articleToDbXrefDAO,
                    cioDAO, conditionDAO, dataSourceDAO, dbXrefDAO, devStageDAO, ecoTermDAO, geologicalAgeDAO,
                    moultingCharactersDAO, sampleSetDAO, taxonDAO, taxonAnnotationDAO, versionDAO, userDAO);
        
        } catch (Exception e) {
            throw new ImportException("Unable to import annotations from " + dataFile.getOriginalFilename() + ". " +
                    "Error: " + e.getMessage());
        }
        logger.info("End taxon annotations import");
        
        return count;
    }
    
    @Override
    public Integer importINaturalistAnnotations() {
        
        WebClient client = WebClient.create();
        
        Mono<INaturalistResponse> initialResponse = client.get()
                .uri(INAT_API_PROJECT_CALL_URL + "&per_page=0")
                .retrieve()
                .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(), clientResponse
                        -> Mono.error(new MoultDBException("Initial request to INaturalist API failed")))
                .bodyToMono(INaturalistResponse.class);
        
        INaturalistResponse iNaturalistResponse = initialResponse.block();
        
        if (iNaturalistResponse != null) {
            
            int pageCount = iNaturalistResponse.total_results() / 2;
            if (iNaturalistResponse.total_results() % 2 != 0) {
                pageCount ++;
            }
            
            Set<MoultingCharactersTO> mcTOs = new HashSet<>();
            Set<SampleSetTO> sampleSetTOs = new HashSet<>();
            Set<ConditionTO> conditionTOs = new HashSet<>();
            Set<ObservationTO> observationTOs = new HashSet<>();
            Set<TaxonAnnotationTO> taxonAnnotationTOs = new HashSet<>();
            Set<VersionTO> versionTOs = new HashSet<>();
            
            Integer conditionLastId = conditionDAO.getLastId();
            Integer conditionNextId = conditionLastId == null ? 1 : conditionLastId + 1;
            Integer lastId = moultingCharactersDAO.getLastId();
            Integer mcNextId = lastId == null ? 1 : lastId + 1;
            Integer versionLastId = versionDAO.getLastId();
            Integer versionNextId = versionLastId == null ? 1 : versionLastId + 1;
            Integer sampleSetLastId = sampleSetDAO.getLastId();
            Integer sampleSetNextId = sampleSetLastId == null ? 1 : sampleSetLastId + 1;
            Integer userLastId = userDAO.getLastId();
            Integer userNextId = userLastId == null ? 1 : userLastId + 1;
            
            Map<String, UserTO> userTOs = userDAO.findAll().stream()
                    .collect(Collectors.toMap(UserTO::getUsername, Function.identity()));
            
            ECOTermTO ecoTO = ecoTermDAO.findById("ECO:0000322"); // imported manually asserted information used in automatic assertion
            if (ecoTO == null) {
                throw new IllegalArgumentException("ECO term not found: ECO:0000322");
            }
            CIOStatementTO cioTO = cioDAO.findById("CIO:0000029"); // high confidence level
            if (cioTO == null) {
                throw new IllegalArgumentException("CIO term not found: CIO:0000029");
            }
            
            Set<INaturalistResponse.INaturalistObservation> allResults = new HashSet<>();
            for (int i = 1; i < pageCount; i++) {
                String url = INAT_API_PROJECT_CALL_URL + "&per_page=2&page=" + i;
                logger.debug("iNaturalist API call URL ({}/{}): {}", i, pageCount, url);
//                if (i == 4) {
//                    // FIXME remove this break
//                    logger.debug("Temporary break for testing");
//                    break;
//                }
                Mono<INaturalistResponse> response = client.get()
                        .uri(url)
                        .retrieve()
                        .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(), clientResponse
                                -> Mono.error(new MoultDBException("Request to INaturalist API failed: " + url)))
                        .bodyToMono(INaturalistResponse.class);
                INaturalistResponse inatResp = response.block();
                
                if (inatResp == null) {
                    throw new MoultDBException("Failed to build response from INaturalist API");
                }
                
                allResults.addAll(inatResp.results());
                for (INaturalistResponse.INaturalistObservation obs : inatResp.results()) {
                    
                    // FIXME check management of user id => maybe remove id and use username as unique identifier
                    UserTO creatorUserTO = getUserTO(obs.user(), userTOs, userNextId);
                    if (Objects.equals(creatorUserTO.getId(), userNextId)) {
                        userNextId++;
                    }
                    // Get all curators
                    List<INaturalistResponse.INaturalistUser> curators = obs.ofvs().stream()
                            .map(INaturalistResponse.INaturalistOfv::user).toList();
                    if (curators.isEmpty()) throw new IllegalArgumentException("No curator found");

                    // Get most frequent curator, if equality, keep one randomly
                    List<INaturalistResponse.INaturalistUser> bestCurators = curators.stream()
                            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                            .entrySet()
                            .stream()
                            .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                            .map(Map.Entry::getKey).toList();
                    if (bestCurators.size() > 1) {
                        logger.warn("Multiple curators for observation {}: {}", obs.id(),
                                curators.stream().map(INaturalistResponse.INaturalistUser::login).toList());
                    }
                    UserTO lastUpdateUserTO = getUserTO(bestCurators.get(0), userTOs, userNextId);
                    if (Objects.equals(lastUpdateUserTO.getId(), userNextId)) {
                        userNextId++;
                    }
                    
                    TaxonTO taxonTO = taxonDAO.findByAccession(obs.taxon().id(),
                            DatasourceEnum.INATURALIST.getStringRepresentation().toLowerCase());
                    if (taxonTO == null) {
                        logger.error("Unknown iNaturalist taxon ID: {}", obs.taxon().id());
                        continue;
                    }
                    
                    String sex = null;
                    Integer ageInDays = null;
                    Boolean isCaptive = null;
                    Boolean isFossil = null;
                    String moult = null;
                    String providedMoultingStep = null;
                    String generalComment = null;
                    
                    for (INaturalistResponse.INaturalistOfv ofv : obs.ofvs()) {
                        if (ofv.name() == null || ofv.value() == null) {
                            continue;
                        }
                        String ofvName = ofv.name().startsWith("Sex") ? "Sex" : ofv.name();
                        switch (ofvName) {
                            case "Sex" -> sex = ofv.value().toLowerCase();
                            case "Age (in days)" -> ageInDays = Integer.parseInt(ofv.value());
                            case "Fossil" -> isFossil = Boolean.parseBoolean(ofv.value());
                            case "Moult?" -> moult = ofv.value();
                            case "Moulting Stage" -> providedMoultingStep = ofv.value().toLowerCase();
                            case "Additional notes of interest" -> generalComment = ofv.value();
                            case "Captive/cultivated" -> {
                                if ("yes".equalsIgnoreCase(ofv.value())) {
                                    isCaptive = true;
                                } else if ("no".equalsIgnoreCase(ofv.value())) {
                                    isCaptive = false;
                                }
                            }
                        }
                    }
                    
                    GeologicalAgeTO fromGeologicalAgeTO;
                    GeologicalAgeTO toGeologicalAgeTO;
                    if (Boolean.TRUE.equals(isFossil)) {
                        fromGeologicalAgeTO = geologicalAgeDAO.findByNotation("a2"); // a2 Precambrian
                        toGeologicalAgeTO = geologicalAgeDAO.findByNotation("a1.1"); // a1.1 Phanerozoic
                    } else {
                        // Current, Meghalayan age
                        fromGeologicalAgeTO = null;
                        toGeologicalAgeTO = null;
                    }
                    
                    // To be able to update sample set and condition of a specific taxon annotation (as we have user annotations)
                    // we should have 1 sample set and 1 condition for each taxon annotation.
                    // So, we don't need to check if they already exist
                    SampleSetTO sampleSetTO = new SampleSetTO(sampleSetNextId, fromGeologicalAgeTO, toGeologicalAgeTO,
                            null, isFossil, isCaptive, obs.taxon().place_guess());
                    sampleSetTOs.add(sampleSetTO);
                    
                    String anatEntityId = "UBERON:0013702"; // body proper
                    String moultingStep = providedMoultingStep; // pre-moult, moulting, post-moult, exuviae, unknown
                    if ("unknown".equalsIgnoreCase(providedMoultingStep)) {
                        moultingStep = null;
                    } else if ("exuviae".equalsIgnoreCase(providedMoultingStep)) {
                        anatEntityId = "UBERON:0006611"; // exoskeleton
                        moultingStep = "post-moult";
                    }
                    AnatEntityTO anatEntityTO = anatEntityDAO.findById(anatEntityId);
                    if (anatEntityTO == null) {
                        throw new MoultDBException("Missing anat. entity");
                    }
                    
                    ConditionTO conditionTO = new ConditionTO(conditionNextId, ageInDays, anatEntityTO, sex, null, moultingStep);
                    
                    MoultingCharactersTO moultingCharactersTO = new MoultingCharactersTO(mcNextId, generalComment);
                    
                    INaturalistResponse.INaturalistUser firstTaxonIdentificator = obs.identifications().stream()
                            .sorted(Comparator.comparing(INaturalistResponse.INaturalistIdentification::created_at))
                            .filter(id -> id.taxon() != null && id.taxon().id().equals(obs.taxon().id()))
                            .map(INaturalistResponse.INaturalistIdentification::user)
                            .limit(1)
                            .toList().get(0);
                    String determinedBy = StringUtils.isNotBlank(firstTaxonIdentificator.orcid()) ?
                            getOrcidId(firstTaxonIdentificator) : firstTaxonIdentificator.login();
                    logger.debug("determinedBy: {}", determinedBy);
                    
                    Timestamp creationTimestamp = new Timestamp(obs.observed_on().getTime());
                    Timestamp currentTimestamp = new Timestamp(new Date().getTime());
                    VersionTO versionTO = new VersionTO(versionNextId, creatorUserTO, creationTimestamp,
                            lastUpdateUserTO, currentTimestamp, 1);
                    
                    ObservationTO observationTO = new ObservationTO(obs.id(), INAT_OBSERVATION_URL + obs.id(),
                            taxonTO.getScientificName());
                    
                    TaxonAnnotationTO taxonAnnotationTO = new TaxonAnnotationTO(
                            null,                   // Integer id
                            taxonTO,                // TaxonTO taxonTO
                            null,                   // String authorSpeciesName
                            determinedBy,           // String determinedBy
                            sampleSetTO.getId(),    // Integer sampleSetId
                            null,                   // String specimenCount
                            conditionTO,            // ConditionTO conditionTO
                            ageInDays == null ? null : ageInDays + " day(s)",  //String authorDevStage
                            null,                   // String authorAnatEntity
                            null,                   // ArticleTO articleTO
                            observationTO,          // ObservationTO observationTO
                            mcNextId,               // Integer moultingCharactersId
                            ecoTO,                  // ECOTermTO ecoTermTO
                            cioTO,                  // CIOStatementTO cioStatementTO
                            versionNextId);         // Integer version
                    
                    observationTOs.add(observationTO);
                    conditionTOs.add(conditionTO);
                    mcTOs.add(moultingCharactersTO);
                    versionTOs.add(versionTO);
                    taxonAnnotationTOs.add(taxonAnnotationTO);
                    
                    conditionNextId++;
                    mcNextId++;
                    versionNextId++;
                    sampleSetNextId++;
                }
                // We need to avoid making too many calls per minute, see https://api.inaturalist.org/v1/ 
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            Set<UserTO> newUsers = userTOs.values().stream()
                    .filter(u -> u.getId() > userLastId)
                    .collect(Collectors.toSet());
            userDAO.batchUpdate(newUsers);
            moultingCharactersDAO.batchUpdate(mcTOs);
            sampleSetDAO.batchUpdate(sampleSetTOs);
            conditionDAO.batchUpdate(conditionTOs);
            versionDAO.batchUpdate(versionTOs);
            observationDAO.batchUpdate(observationTOs);
            taxonAnnotationDAO.batchUpdate(taxonAnnotationTOs);
            
            logger.debug("iNaturalist observation count: {} - TaxonAnnotationTO count: {}",
                    allResults.size(), taxonAnnotationTOs.size());
            
            return taxonAnnotationTOs.size();
        }
        
        return 0;
    }
    
    private UserTO getUserTO(INaturalistResponse.INaturalistUser iNatUser, Map<String, UserTO> userTOs, int userNextId) {
        UserTO userTO = userTOs.get(iNatUser.login());
        if (userTO == null) {
            userTO = userDAO.findByUsername(iNatUser.login());
        }
        if (userTO == null) {
            // Get the string after the last '/' of iNatUser.orcid(). Ex: https://orcid.org/0000-0003-2722-6854
            // TODO It would be better to check pattern of orcid
            String orcid = getOrcidId(iNatUser);
            userTO = new UserTO(userNextId, iNatUser.login(), iNatUser.name(),
                    Role.ROLE_EXTERNAL.getStringRepresentation(), orcid);
        }
        userTOs.put(iNatUser.login(), userTO);
        return userTO;
    }
    
    private static String getOrcidId(INaturalistResponse.INaturalistUser iNatUser) {
        String orcid = iNatUser.orcid() == null ?
                null : iNatUser.orcid().substring(iNatUser.orcid().lastIndexOf('/') + 1);
        return orcid;
    }
}
