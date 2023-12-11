package org.moultdb.importer.fossilannotation;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.model.moutldbenum.*;
import org.moultdb.api.repository.dao.*;
import org.moultdb.api.repository.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.cellprocessor.*;
import org.supercsv.cellprocessor.constraint.IsElementOf;
import org.supercsv.cellprocessor.constraint.StrNotNullOrEmpty;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.CsvContext;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Valentine Rech de Laval
 * @since 2021-12-13
 */
public class FossilImporter {
    
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
    @Autowired UserDAO userDAO;
    @Autowired VersionDAO versionDAO;
    
    private final static Logger logger = LogManager.getLogger(FossilImporter.class.getName());
    
    private final static CsvPreference TSV_COMMENTED = new CsvPreference.Builder(CsvPreference.TAB_PREFERENCE).build();
    
    // Anatomical entity used for all annotations
    private final static AnatEntityTO ANAT_ENTITY_TO = new AnatEntityTO("UBERON:0000468", "multicellular organism", null);
    
    private final static String ORDER_COL_NAME = "Order";
    private final static String TAXON_COL_NAME = "Taxon";
    private final static String DETERMINED_BY_COL_NAME = "Determined by"; //DOI or PMID or ORCID ID
    private final static String PUBLISHED_REFERENCE_TEXT_COL_NAME = "Published reference: citation (APA style)";
    private final static String PUBLISHED_REFERENCE_ACC_COL_NAME = "Published reference: accession";
    private final static String CONTRIBUTOR_COL_NAME = "Contributor"; // ORCID ID
    private final static String MUSEUM_COLLECTION_COL_NAME = "Museum collection";
    private final static String MUSEUM_ACCESSION_COL_NAME = "Museum accession";
    private final static String LOCATION_NAME_COL_NAME = "Location: name";
    private final static String LOCATION_GPS_COL_NAME = "Location: GPS coordinates";
    private final static String GEOLOGICAL_FORMATION_COL_NAME = "Geological formation";
    private final static String GEOLOGICAL_AGE_COL_NAME = "Geological age";
    private final static String FOSSIL_PRESERVATION_TYPE_COL_NAME = "Fossil preservation type";
    private final static String ENVIRONMENT_COL_NAME = "Environment";
    private final static String BIOZONE_COL_NAME = "Biozone"; // {free text field}
    private final static String SPECIMEN_COUNT_COL_NAME = "Number of specimens";
    private final static String SPECIMEN_TYPE_COL_NAME = "Type of specimens of interest";
    private final static String LIFE_HISTORY_STYLE_COL_NAME = "Life history style";
    private final static String LIFE_MODE_COL_NAME = "Life mode"; // pelagic; sessile; epifaunal; infaunal;
    private final static String JUVENILE_MOULT_COUNT_COL_NAME = "Number of juvenile moults"; //(free number field)
    private final static String MAJOR_MORPHOLOGICAL_TRANSITION_COUNT_COL_NAME = "Number of major morphological transitions"; // (free number field)
    private final static String ADULT_STAGE_MOULTING_COL_NAME = "Adult stage moulting";
    private final static String OBSERVED_MOULT_STAGES_COUNT_COL_NAME = "Observed # total moult stages";
    private final static String OBSERVED_MOULT_STAGE_COL_NAME = "Observed moult stage";
    private final static String ESTIMATED_MOULT_STAGES_COUNT_COL_NAME = "Estimated # moult stages";
    private final static String SEGMENT_ADDITION_MODE_COL_NAME = "Segment addition mode";
    private final static String BODY_SEGMENT_COUNT_COL_NAME = "# body segments per moult stage";
    private final static String BODY_SEGMENT_COUNT_IN_ADULTS_COL_NAME = "# body segments in adult individuals";
    private final static String BODY_LENGTH_AVERAGE_COL_NAME = "Average body length (in mm)";
    private final static String BODY_LENGTH_INCREASE_AVERAGE_COL_NAME = "Average body length increase from previous moult (in mm)";
    private final static String BODY_MASS_INCREASE_AVERAGE_COL_NAME = "Average body mass increase from previous moult (in g)";
    private final static String INTERMOULT_PERIOD_COL_NAME = "Intermoult period (in days)";
    private final static String PREMOULT_PERIOD_COL_NAME = "Pre-moult period (in days)";
    private final static String POSTMOULT_PERIOD_COL_NAME = "Post-moult period (in days)";
    private final static String VARIATION_WITHIN_COHORTS_COL_NAME = "Variation in moulting time within cohorts (in days)";
    private final static String MOULTING_SUTURE_LOCATION_COL_NAME = "Location of moulting suture";
    private final static String CEPHALIC_SUTURE_LOCATION_COL_NAME = "Cephalic suture location";
    private final static String POST_CEPHALIC_SUTURE_LOCATION_COL_NAME = "Post-cephalic suture location";
    private final static String RESULTING_NAMED_MOULTING_CONFIGURATIONS_COL_NAME = "Resulting named moulting configurations";
    private final static String EGRESS_DIRECTION_DURING_MOULTING_COL_NAME = "Direction of egress during moulting";
    private final static String POSITION_EXUVIAE_FOUND_IN_COL_NAME = "Position exuviae found in";
    private final static String MOULTING_PHASE_COL_NAME = "Moulting phase";
    private final static String MOULTING_VARIABILITY_COL_NAME = "Level of intraspecific variability in moulting mode";
    private final static String CALCIFICATION_EVENT_COL_NAME = "Post-moult cuticle calcification event";
    private final static String HEAVY_METAL_REINFORCEMENT_COL_NAME = "Post-moult heavy metal reinforcement of the cuticle";
    private final static String OTHER_BEHAVIOURS_ASSOCIATED_WITH_MOULTING_COL_NAME = "Other behaviours associated with moulting";
    private final static String EXUVIAE_CONSUMPTION_COL_NAME = "Consumption of exuviae";
    private final static String REABSORPTION_COL_NAME = "Reabsorption during moulting";
    private final static String FOSSIL_EXUVIAE_QUALITY_COL_NAME = "Quality of fossil exuviae";
    private final static String EVIDENCE_CODE_COL_NAME = "Evidence code";
    private final static String CONFIDENCE_COL_NAME = "Confidence";
    private final static String GENERAL_COMMENTS_COL_NAME = "General Comments";
    
    public FossilImporter() {
    }
    
    public static void main(String[] args) {
        logger.traceEntry(Arrays.toString(args));
        
        if (args.length != 1) {
            throw new IllegalArgumentException("Incorrect number of arguments provided, expected 1 argument, " +
                    args.length + " provided.");
        }
    
        FossilImporter parser = new FossilImporter();
        List<FossilAnnotationBean> fossilAnnotationBeans = parser.parseFossilAnnotation(args[0]);
    
        parser.insertFossilAnnotation(fossilAnnotationBeans);
    
        logger.traceExit();
    }
    
    public void insertFossilAnnotation(List<FossilAnnotationBean> fossilAnnotationBeans) {
        insertFossilAnnotation(fossilAnnotationBeans, articleDAO, articleToDbXrefDAO, conditionDAO, dataSourceDAO,
                dbXrefDAO, devStageDAO, geologicalAgeDAO, moultingCharactersDAO, sampleSetDAO, taxonDAO, taxonAnnotationDAO,
                versionDAO, userDAO);
    }
    
    public int insertFossilAnnotation(List<FossilAnnotationBean> fossilAnnotationBeans, ArticleDAO articleDAO,
                                       ArticleToDbXrefDAO articleToDbXrefDAO, ConditionDAO conditionDAO,
                                       DataSourceDAO dataSourceDAO, DbXrefDAO dbXrefDAO, DevStageDAO devStageDAO,
                                       GeologicalAgeDAO geologicalAgeDAO, MoultingCharactersDAO moultingCharactersDAO,
                                       SampleSetDAO sampleSetDAO, TaxonDAO taxonDAO, TaxonAnnotationDAO taxonAnnotationDAO,
                                       VersionDAO versionDAO, UserDAO userDAO) {
        
        Integer mcNextId = getNextId(moultingCharactersDAO.getLastId());
        Integer ssNextId = getNextId(sampleSetDAO.getLastId());
        Integer dbXrefNextId = getNextId(dbXrefDAO.getLastId());
        Integer articleNextId = getNextId(articleDAO.getLastId());
        Integer conditionNextId = getNextId(conditionDAO.getLastId());
        Integer taxonAnnotNextId = getNextId(taxonAnnotationDAO.getLastId());
        Integer versionNextId = getNextId(versionDAO.getLastId());
        
        Set<MoultingCharactersTO> mcTOs = new HashSet<>();
        Set<DbXrefTO> dbXrefTOs = new HashSet<>();
        Set<ArticleTO> articleTOs = new HashSet<>();
        Set<SampleSetTO> sampleSetTOs = new HashSet<>();
        Set<ArticleToDbXrefTO> articleToDbXrefTOs = new HashSet<>();
        Set<ConditionTO> conditionTOs = new HashSet<>();
        Set<TaxonAnnotationTO> taxonAnnotationTOs = new HashSet<>();
        Set<VersionTO> versionTOs = new HashSet<>();
        Map<String, ArticleTO> viewedArticleCitations = new HashMap<>();
        for (FossilAnnotationBean bean: fossilAnnotationBeans) {
            BigDecimal bodyLengthIncreaseAverage = null;
            if (bean.getBodyLengthIncreaseAverage() != null) {
                Matcher m1 = Pattern.compile("^(.+)( \\(.*\\))*$").matcher(bean.getBodyLengthIncreaseAverage());
                if (m1.find()) {
                    bodyLengthIncreaseAverage = new BigDecimal(m1.group(1));
                }
            }
            MoultingCharactersTO moultingCharactersTO = new MoultingCharactersTO(mcNextId, bean.getLifeHistoryStyle(),
                    bean.getLifeMode(), bean.getJuvenileMoultCount(), bean.getMajorMorphologicalTransitionCount(),
                    bean.getAdultStageMoulting(), bean.getObservedMoultStagesCount(), bean.getEstimatedMoultStagesCount(),
                    bean.getSegmentAdditionMode(), bean.getBodySegmentCount(), bean.getBodySegmentCountInAdults(),
                    bean.getBodyLengthAverage(), bodyLengthIncreaseAverage, bean.getBodyMassIncreaseAverage(),
                    bean.getIntermoultPeriod(), bean.getPremoultPeriod(), bean.getPostmoultPeriod(),
                    bean.getVariationWithinCohorts(), bean.getMoultingSutureLocation(), bean.getCephalicSutureLocation(),
                    bean.getPostCephalicSutureLocation(), bean.getResultingNamedMoultingConfigurations(),
                    bean.getEgressDirectionDuringMoulting(), bean.getPositionExuviaeFoundIn(), bean.getMoultingPhase(),
                    bean.getMoultingVariability(),bean.getCalcificationEvent(),bean.getHeavyMetalReinforcement(),
                    bean.getOtherBehavioursAssociatedWithMoulting(), bean.getExuviaeConsumption(),
                    bean.getReabsorption(), bean.getFossilExuviaeQuality(), bean.getGeneralComments());
            mcTOs.add(moultingCharactersTO);
            mcNextId ++;
            
            TaxonTO taxonTO = taxonDAO.findByScientificName(bean.getTaxon());
            if (taxonTO == null) {
                // Try to find genus taxon
                String genus = bean.getTaxon().split(" ")[0];
                logger.warn("Taxon scientific name '" + bean.getTaxon() + "' has not been found. " +
                        "Search for genus '" + genus + "'.");
                taxonTO = taxonDAO.findByScientificName(genus);
            }
            if (taxonTO == null) {
                throw new IllegalArgumentException("Unknown taxon scientific name or genus: " + bean.getTaxon());
            }
    
            // Ex: 'Stage 3' or 'Sandbian to Katian'
            String fromGeoAgeName = bean.getGeologicalAge();
            GeologicalAgeTO toGeologicalAgeTO = null;
            int i = bean.getGeologicalAge().indexOf(" to ");
            if (i != -1) {
                fromGeoAgeName = bean.getGeologicalAge().substring(0, i);
                String toGeoAgeName = bean.getGeologicalAge().substring(i + 4);
                toGeologicalAgeTO = getGeologicalAgeTO(geologicalAgeDAO, toGeoAgeName);
            }
            GeologicalAgeTO fromGeologicalAgeTO = getGeologicalAgeTO(geologicalAgeDAO, fromGeoAgeName);
            if (toGeologicalAgeTO == null) {
                toGeologicalAgeTO = fromGeologicalAgeTO;
            }
            
            if (bean.getPublishedReferenceAcc() != null && bean.getPublishedReferenceText() == null) {
                throw new IllegalArgumentException("You should provide the published reference citation " +
                        "when you provide a published reference accession: " + bean.getPublishedReferenceAcc());
            }
            if (bean.getPublishedReferenceAcc() != null) {
                for (String acc : bean.getPublishedReferenceAcc().split(";")) {
                    Matcher m = Pattern.compile("^([A-Z]+): (\\S+)$").matcher(String.valueOf(acc));
                    if (m.find()) {
                        DataSourceTO dataSourceTO = dataSourceDAO.findByName(m.group(1));
                        if (dataSourceTO == null) {
                            throw new IllegalArgumentException("Unknown data source: " + m.group(1));
                        }
                        DbXrefTO dbXrefTO = dbXrefDAO.findByAccessionAndDatasource(m.group(2), dataSourceTO.getId());
                        if (dbXrefTO == null) {
                            dbXrefTOs.add(new DbXrefTO(dbXrefNextId, m.group(2), null, dataSourceTO));
                            dbXrefNextId++;
                        }
                    }
                }
            }
            
            ArticleTO articleTO = viewedArticleCitations.get(bean.getPublishedReferenceText());
            if (bean.getPublishedReferenceText() != null && articleTO == null) {
                articleTO = articleDAO.findByCitation(bean.getPublishedReferenceText());
                if (articleTO == null) {
                    articleTO = new ArticleTO(articleNextId, bean.getPublishedReferenceText(), null, null, dbXrefTOs);
                    articleTOs.add(articleTO);
                    articleNextId ++;
                    for (DbXrefTO dbXrefTO : dbXrefTOs) {
                        articleToDbXrefTOs.add(new ArticleToDbXrefTO(articleTO.getId(), dbXrefTO.getId()));
                    }
                }
                viewedArticleCitations.put(bean.getPublishedReferenceText(), articleTO);
            }
            
            // We don't check whether a sample set is already present/seen because we need to have one sample set per
            // annotation taxon. Indeed, the user might want to modify a sample set with no impact on the other annotations.
            // FIXME add isCaptive?
            Set<String> specimenTypes = extractValues(bean.getSpecimenType());
            boolean isFossil = specimenTypes != null && specimenTypes.contains("fossil(s)");
            SampleSetTO sampleSetTO = new SampleSetTO(ssNextId, fromGeologicalAgeTO, toGeologicalAgeTO,
                    bean.getSpecimenCount(), isFossil, null, extractValues(bean.getMuseumAccession()),
                    extractValues(bean.getMuseumCollection()), extractValues(bean.getLocationName()),
                    extractValues(bean.getFossilPreservationType()), extractValues(bean.getEnvironment()),
                    extractValues(bean.getGeologicalFormation()), specimenTypes, bean.getBiozone());
            sampleSetTOs.add(sampleSetTO);
            ssNextId ++;
            
            String devStageId = taxonTO.getScientificName().replaceAll(" ", ".") + "." + bean.getObservedMoultStage();
            ConditionTO conditionTO = conditionDAO.find(devStageId, ANAT_ENTITY_TO.getId());
            if (conditionTO == null) {
                DevStageTO devStageTO = devStageDAO.findById(devStageId);
                if (devStageTO == null) {
                    throw new IllegalArgumentException("Unknown developmental stage: " + devStageId);
                }
                conditionTO = new ConditionTO(conditionNextId, devStageTO, ANAT_ENTITY_TO, null, null);
                conditionTOs.add(conditionTO);
                conditionNextId ++;
            }
            
            UserTO userTO = userDAO.findByOrcidId(bean.getContributor());
            if (userTO == null) {
                throw new IllegalArgumentException("User not found");
            }
            Timestamp current = new Timestamp(new Date().getTime());
            VersionTO versionTO = new VersionTO(versionNextId, userTO, current, userTO, current, 1);
            versionTOs.add(versionTO);
            versionNextId ++;
            
            TaxonAnnotationTO taxonAnnotationTO = new TaxonAnnotationTO(taxonAnnotNextId, taxonTO, bean.getTaxon(),
                    bean.getDeterminedBy(), sampleSetTO.getId(), conditionTO, articleTO, null,
                    moultingCharactersTO.getId(), null, null, versionTO.getId());
            taxonAnnotationTOs.add(taxonAnnotationTO);
            taxonAnnotNextId ++;
        }
        moultingCharactersDAO.batchUpdate(mcTOs);
        dbXrefDAO.batchUpdate(dbXrefTOs);
        articleDAO.batchUpdate(articleTOs);
        sampleSetDAO.batchUpdate(sampleSetTOs);
        articleToDbXrefDAO.batchUpdate(articleToDbXrefTOs);
        conditionDAO.batchUpdate(conditionTOs);
        versionDAO.batchUpdate(versionTOs);
        int[] ints = taxonAnnotationDAO.batchUpdate(taxonAnnotationTOs);
        return Arrays.stream(ints).sum();
    }
    
    private static Integer getNextId(Integer articleDAO) {
        Integer articleLastId = articleDAO;
        Integer articleNextId = articleLastId == null ? 1 : articleLastId + 1;
        return articleNextId;
    }
    
    private Set<String> extractValues(String s) {
        if (StringUtils.isBlank(s)) {
            return null;
        }
        return Arrays.stream(s.split(";"))
                     .map(String::trim)
                     .collect(Collectors.toSet());
    }
    
    private GeologicalAgeTO getGeologicalAgeTO(GeologicalAgeDAO geologicalAgeDAO, String geoAgeName) {
        GeologicalAgeTO geoAgeTO = geologicalAgeDAO.findByLabelOrSynonym(geoAgeName);
        if (geoAgeTO == null) {
            throw new IllegalArgumentException("Unknown geological age name: " + geoAgeName);
        }
        return geoAgeTO;
    }
    
    public List<FossilAnnotationBean> parseFossilAnnotation(MultipartFile uploadedFile) {
    
        try (ICsvBeanReader fossilReader = new CsvBeanReader(new InputStreamReader(uploadedFile.getInputStream()), TSV_COMMENTED)) {
            List<FossilAnnotationBean> fossilAnnotationBeans = getFossilAnnotationBeans(fossilReader);
            return logger.traceExit(fossilAnnotationBeans);
        
        } catch (SuperCsvException e) {
            throw new IllegalArgumentException("The provided file " + uploadedFile.getOriginalFilename()
                    + " could not be properly parsed", e);
        } catch (IOException e) {
            throw new UncheckedIOException("Can not read file " + uploadedFile.getOriginalFilename(), e);
        }
    }
    
    public List<FossilAnnotationBean> parseFossilAnnotation(String fileName) {
        logger.info("Start parsing of fossil annotation file " + fileName + "...");
        
        try (ICsvBeanReader fossilReader = new CsvBeanReader(new FileReader(fileName), TSV_COMMENTED)) {
    
            logger.info("End parsing of fossil annotation file");
    
            return logger.traceExit(getFossilAnnotationBeans(fossilReader));
            
        } catch (SuperCsvException e) {
            //hide implementation details
            throw new IllegalArgumentException("The provided file " + fileName + " could not be properly parsed", e);
        } catch (IOException e) {
            throw new UncheckedIOException("Can not read file " + fileName, e);
        }
    }
    
    private List<FossilAnnotationBean> getFossilAnnotationBeans(ICsvBeanReader fossilReader) throws IOException {
        List<FossilAnnotationBean> annots = new ArrayList<>();
        
        final String[] header = fossilReader.getHeader(true);
        
        String[] attributeMapping = mapFossilAnnotationHeaderToAttributes(header);
        CellProcessor[] cellProcessorMapping = mapFossilAnnotationHeaderToCellProcessors(header);
        FossilAnnotationBean fossil;
        
        while((fossil = fossilReader.read(FossilAnnotationBean.class, attributeMapping, cellProcessorMapping)) != null) {
            annots.add(fossil);
        }
        if (annots.isEmpty()) {
            throw new IllegalArgumentException("The provided file did not allow to retrieve any fossil");
        }
        
        return annots;
    }
    
    
    private CellProcessor[] mapFossilAnnotationHeaderToCellProcessors(String[] header) {
        CellProcessor[] processors = new CellProcessor[header.length];
        for (int i = 0; i < header.length; i++) {
            if (header[i] == null || header[i].equals(GENERAL_COMMENTS_COL_NAME)) {
                continue;
            }
            // For the moment, we're keeping the switch not refactored to simplify the update
            processors[i] = switch (header[i]) {
                case ORDER_COL_NAME
                        -> new StrNotNullOrEmpty(new Trim());
                case TAXON_COL_NAME
                        -> new StrNotNullOrEmpty(new Trim());
                case DETERMINED_BY_COL_NAME
                        -> new ParseCustomOptional(new Trim());
                case PUBLISHED_REFERENCE_TEXT_COL_NAME
                        -> new ParseCustomOptional(new Trim());
                case PUBLISHED_REFERENCE_ACC_COL_NAME
                        -> new ParseCustomOptional(new Trim());
                case CONTRIBUTOR_COL_NAME
                        -> new StrNotNullOrEmpty(new Trim());
                case MUSEUM_COLLECTION_COL_NAME
                        -> new ParseCustomOptional(new Trim());
                case MUSEUM_ACCESSION_COL_NAME
                        -> new ParseCustomOptional(new Trim());
                case LOCATION_NAME_COL_NAME
                        -> new StrNotNullOrEmpty(new Trim());
                case LOCATION_GPS_COL_NAME
                        -> new StrNotNullOrEmpty(new Trim());
                case GEOLOGICAL_FORMATION_COL_NAME
                        -> new ParseCustomOptional(new Trim());
                case GEOLOGICAL_AGE_COL_NAME
                        -> new StrNotNullOrEmpty(new Trim());
                case FOSSIL_PRESERVATION_TYPE_COL_NAME
                        -> new ParseCustomOptional(new Trim());
                case ENVIRONMENT_COL_NAME
                        -> new ParseCustomOptional(new Trim());
                case BIOZONE_COL_NAME
                        -> new ParseCustomOptional(new Trim());
                case SPECIMEN_COUNT_COL_NAME
                        -> new ParseCustomOptional(new ParseInt());
                case SPECIMEN_TYPE_COL_NAME
                        -> new StrNotNullOrEmpty(new Trim());
                case LIFE_HISTORY_STYLE_COL_NAME
                        -> new ParseCustomOptional(new ParseStrIgnoringCase(
                        new IsElementOf(Arrays.asList(LifeHistoryStyle.values()))));
                case LIFE_MODE_COL_NAME
                        -> new ParseCustomOptional(new ParseStrIgnoringCase(
                        new IsElementOf(Arrays.asList(LifeMode.values()))));
                case JUVENILE_MOULT_COUNT_COL_NAME
                        -> new ParseCustomOptional(new ParseInt());
                case MAJOR_MORPHOLOGICAL_TRANSITION_COUNT_COL_NAME
                        -> new ParseCustomOptional(new ParseInt());
                case ADULT_STAGE_MOULTING_COL_NAME
                        -> new ParseCustomOptional(new ParseBool("yes", "no"));
                case OBSERVED_MOULT_STAGES_COUNT_COL_NAME
                        -> new ParseCustomOptional(new ParseInt());
                case OBSERVED_MOULT_STAGE_COL_NAME
                        -> new StrNotNullOrEmpty(new Trim());
                case ESTIMATED_MOULT_STAGES_COUNT_COL_NAME
                        -> new ParseCustomOptional(new ParseInt());
                case SEGMENT_ADDITION_MODE_COL_NAME
                        -> new ParseCustomOptional(new Trim());
                case BODY_SEGMENT_COUNT_COL_NAME
                        -> new ParseCustomOptional(new ParseTwoInt());
                case BODY_SEGMENT_COUNT_IN_ADULTS_COL_NAME
                        -> new ParseCustomOptional(new ParseTwoInt());
                case BODY_LENGTH_AVERAGE_COL_NAME
                        -> new ParseCustomOptional(new ParseBigDecimal());
                case BODY_LENGTH_INCREASE_AVERAGE_COL_NAME
                        -> new ParseCustomOptional(new Trim());
                case BODY_MASS_INCREASE_AVERAGE_COL_NAME
                        -> new ParseCustomOptional(new ParseBigDecimal());
                case INTERMOULT_PERIOD_COL_NAME
                        -> new ParseCustomOptional(new ParseTwoInt());
                case PREMOULT_PERIOD_COL_NAME
                        -> new ParseCustomOptional(new ParseTwoInt());
                case POSTMOULT_PERIOD_COL_NAME
                        -> new ParseCustomOptional(new ParseTwoInt());
                case VARIATION_WITHIN_COHORTS_COL_NAME
                        -> new ParseCustomOptional(new ParseTwoInt());
                case MOULTING_SUTURE_LOCATION_COL_NAME
                        -> new ParseCustomOptional(new Trim());
                case CEPHALIC_SUTURE_LOCATION_COL_NAME
                        -> new ParseCustomOptional(new Trim());
                case POST_CEPHALIC_SUTURE_LOCATION_COL_NAME
                        -> new ParseCustomOptional(new Trim());
                case RESULTING_NAMED_MOULTING_CONFIGURATIONS_COL_NAME
                        -> new ParseCustomOptional(new Trim());
                case EGRESS_DIRECTION_DURING_MOULTING_COL_NAME
                        -> new ParseCustomOptional(new ParseStrIgnoringCase(
                        new IsElementOf(Arrays.asList(EgressDirection.values()))));
                case POSITION_EXUVIAE_FOUND_IN_COL_NAME
                        -> new ParseCustomOptional(new ParseStrIgnoringCase(
                        new IsElementOf(Arrays.asList(ExuviaePosition.values()))));
                case MOULTING_PHASE_COL_NAME
                        -> new ParseCustomOptional(new ParseStrIgnoringCase(
                        new IsElementOf(Arrays.asList(MoultingPhase.values()))));
                case MOULTING_VARIABILITY_COL_NAME
                        -> new ParseCustomOptional(new ParseStrIgnoringCase(
                        new IsElementOf(Arrays.asList(MoultingVariability.values()))));
                case CALCIFICATION_EVENT_COL_NAME
                        -> new ParseCustomOptional(new ParseStrIgnoringCase(
                        new IsElementOf(Arrays.asList(Calcification.values()))));
                case HEAVY_METAL_REINFORCEMENT_COL_NAME
                        -> new ParseCustomOptional(new ParseStrIgnoringCase(
                        new IsElementOf(Arrays.asList(HeavyMetalReinforcement.values()))));
                case OTHER_BEHAVIOURS_ASSOCIATED_WITH_MOULTING_COL_NAME
                        -> new ParseCustomOptional(new Trim());
                case EXUVIAE_CONSUMPTION_COL_NAME
                        -> new ParseCustomOptional(new ParseStrIgnoringCase(
                        new IsElementOf(Arrays.asList(ExuviaeConsumption.values()))));
                case REABSORPTION_COL_NAME
                        -> new ParseCustomOptional(new ParseStrIgnoringCase(
                        new IsElementOf(Arrays.asList(Reabsorption.values()))));
                    case FOSSIL_EXUVIAE_QUALITY_COL_NAME
                        -> new ParseCustomOptional(new Trim());
                case EVIDENCE_CODE_COL_NAME
                        -> new StrNotNullOrEmpty(new Trim());
                case CONFIDENCE_COL_NAME
                        -> new StrNotNullOrEmpty(new Trim());
                case GENERAL_COMMENTS_COL_NAME
                        -> new ParseCustomOptional(new Trim());
                default -> throw new IllegalArgumentException("Unrecognized header: " + header[i] + " for FossilAnnotationBean");
            };
        }
        return processors;
    }
    
    private String[] mapFossilAnnotationHeaderToAttributes(String[] header) {
        
        String[] mapping = new String[header.length];
        for (int i = 0; i < header.length; i++) {
            if (header[i] == null || header[i].equals(GENERAL_COMMENTS_COL_NAME)) {
                continue;
            }
            mapping[i] = switch (header[i]) {
                case ORDER_COL_NAME -> "order";
                case TAXON_COL_NAME -> "taxon";
                case DETERMINED_BY_COL_NAME -> "determinedBy";
                case PUBLISHED_REFERENCE_TEXT_COL_NAME -> "publishedReferenceText";
                case PUBLISHED_REFERENCE_ACC_COL_NAME -> "publishedReferenceAcc";
                case CONTRIBUTOR_COL_NAME -> "contributor";
                case MUSEUM_COLLECTION_COL_NAME -> "museumCollection";
                case MUSEUM_ACCESSION_COL_NAME -> "museumAccession";
                case LOCATION_NAME_COL_NAME -> "locationName";
                case LOCATION_GPS_COL_NAME -> "locationGps";
                case GEOLOGICAL_FORMATION_COL_NAME -> "geologicalFormation";
                case GEOLOGICAL_AGE_COL_NAME -> "geologicalAge";
                case FOSSIL_PRESERVATION_TYPE_COL_NAME -> "fossilPreservationType";
                case ENVIRONMENT_COL_NAME -> "environment";
                case BIOZONE_COL_NAME -> "biozone";
                case SPECIMEN_COUNT_COL_NAME -> "specimenCount";
                case SPECIMEN_TYPE_COL_NAME -> "specimenType";
                case LIFE_HISTORY_STYLE_COL_NAME -> "lifeHistoryStyle";
                case LIFE_MODE_COL_NAME -> "lifeMode";
                case JUVENILE_MOULT_COUNT_COL_NAME -> "juvenileMoultCount";
                case MAJOR_MORPHOLOGICAL_TRANSITION_COUNT_COL_NAME -> "majorMorphologicalTransitionCount";
                case ADULT_STAGE_MOULTING_COL_NAME -> "adultStageMoulting";
                case OBSERVED_MOULT_STAGES_COUNT_COL_NAME -> "observedMoultStagesCount";
                case OBSERVED_MOULT_STAGE_COL_NAME -> "observedMoultStage";
                case ESTIMATED_MOULT_STAGES_COUNT_COL_NAME -> "estimatedMoultStagesCount";
                case SEGMENT_ADDITION_MODE_COL_NAME -> "segmentAdditionMode";
                case BODY_SEGMENT_COUNT_COL_NAME -> "bodySegmentCount";
                case BODY_SEGMENT_COUNT_IN_ADULTS_COL_NAME -> "bodySegmentCountInAdults";
                case BODY_LENGTH_AVERAGE_COL_NAME -> "bodyLengthAverage";
                case BODY_LENGTH_INCREASE_AVERAGE_COL_NAME -> "bodyLengthIncreaseAverage";
                case BODY_MASS_INCREASE_AVERAGE_COL_NAME -> "bodyMassAverage";
                case INTERMOULT_PERIOD_COL_NAME -> "intermoultPeriod";
                case PREMOULT_PERIOD_COL_NAME -> "premoultPeriod";
                case POSTMOULT_PERIOD_COL_NAME -> "postmoultPeriod";
                case VARIATION_WITHIN_COHORTS_COL_NAME -> "variationWithinCohorts";
                case MOULTING_SUTURE_LOCATION_COL_NAME -> "moultingSutureLocation";
                case CEPHALIC_SUTURE_LOCATION_COL_NAME -> "cephalicSutureLocation";
                case POST_CEPHALIC_SUTURE_LOCATION_COL_NAME -> "postCephalicSutureLocation";
                case RESULTING_NAMED_MOULTING_CONFIGURATIONS_COL_NAME -> "resultingNamedMoultingConfigurations";
                case EGRESS_DIRECTION_DURING_MOULTING_COL_NAME -> "egressDirectionDuringMoulting";
                case POSITION_EXUVIAE_FOUND_IN_COL_NAME -> "positionExuviaeFoundIn";
                case MOULTING_PHASE_COL_NAME -> "moultingPhase";
                case MOULTING_VARIABILITY_COL_NAME -> "moultingVariability";
                case CALCIFICATION_EVENT_COL_NAME -> "calcificationEvent";
                case HEAVY_METAL_REINFORCEMENT_COL_NAME -> "heavyMetalReinforcement";
                case OTHER_BEHAVIOURS_ASSOCIATED_WITH_MOULTING_COL_NAME -> "otherBehavioursAssociatedWithMoulting";
                case EXUVIAE_CONSUMPTION_COL_NAME -> "exuviaeConsumption";
                case REABSORPTION_COL_NAME -> "reabsorption";
                case FOSSIL_EXUVIAE_QUALITY_COL_NAME -> "fossilExuviaeQuality";
                case EVIDENCE_CODE_COL_NAME -> "evidenceCode";
                case CONFIDENCE_COL_NAME -> "confidence";
                case GENERAL_COMMENTS_COL_NAME -> "generalComments";
                default -> throw new IllegalArgumentException("Unrecognized header: '" + header[i] + "' for FossilAnnotationBean");
            };
        }
        return mapping;
    }
    
    public static class ParseCustomOptional extends CellProcessorAdaptor {
        
        public ParseCustomOptional() {
            super();
        }
        
        public ParseCustomOptional(CellProcessor next) {
            // this constructor allows other processors to be chained after this processor
            super(next);
        }
        
        public Object execute(Object value, CsvContext context) {
    
            String stringValue = String.valueOf(value);
            if (value == null || stringValue.equals("NA") || stringValue.equals("?")) {
                return null;
            }
    
            return next.execute(value, context);
        }
    }
    
    public static class ParseTwoInt extends CellProcessorAdaptor {
        
        public ParseTwoInt() {
            super();
        }
        
        public ParseTwoInt(CellProcessor next) {
            // this constructor allows other processors to be chained after this processor
            super(next);
        }
        
        public Object execute(Object value, CsvContext context) {
            
            // Throws an Exception if the input is null
            validateInputNotNull(value, context);
            
            // Try to match free number field, but might need the ability to put two, such as '0/1' or '0-1'
            Matcher m = Pattern.compile("^[0-9]+([/-][0-9]+)*$").matcher(String.valueOf(value));
            if (m.matches()) {
                return next.execute(value, context);
            }
            
            throw new SuperCsvCellProcessorException(
                    String.format("Could not parse '%s' as a TwoInt", value), context, this);
        }
    }
    
    public static class ParseBigDecimalRange extends CellProcessorAdaptor {
        
        public ParseBigDecimalRange() {
            super();
        }
        
        public ParseBigDecimalRange(CellProcessor next) {
            // this constructor allows other processors to be chained after this processor
            super(next);
        }
        
        public Object execute(Object value, CsvContext context) {
            
            // Throws an Exception if the input is null
            validateInputNotNull(value, context);
            
            // Try to match free number field, but might need the ability to put a range, such as '13.276-14.654'
            Matcher m = Pattern.compile("^[0-9\\.]+([-][0-9\\.]+)*$").matcher(String.valueOf(value));
            if (m.matches()) {
                return next.execute(value, context);
            }
            
            throw new SuperCsvCellProcessorException(
                    String.format("Could not parse '%s' as a BigDecimal range", value), context, this);
        }
    }
    
    public static class ParseStrIgnoringCase extends CellProcessorAdaptor {
        
        public ParseStrIgnoringCase() {
            super();
        }
        
        public ParseStrIgnoringCase(CellProcessor next) {
            // this constructor allows other processors to be chained after this processor
            super(next);
        }
        
        public Object execute(Object value, CsvContext context) {
            
            // Throws an Exception if the input is null
            validateInputNotNull(value, context);
            
            return next.execute(String.valueOf(value).toLowerCase(), context);
        }
    }
}
