package org.moultdb.importer.taxonannotation;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.exception.ParseException;
import org.moultdb.api.model.moutldbenum.*;
import org.moultdb.api.repository.dao.*;
import org.moultdb.api.repository.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.*;
import org.supercsv.cellprocessor.constraint.IsElementOf;
import org.supercsv.cellprocessor.constraint.StrNotNullOrEmpty;
import org.supercsv.cellprocessor.constraint.StrRegEx;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.CsvContext;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Valentine Rech de Laval
 * @since 2021-12-13
 */
public class TaxonAnnotationParser {
    
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
    
    private final static Logger logger = LogManager.getLogger(TaxonAnnotationParser.class.getName());
    
    private final static CsvPreference TSV_COMMENTED = new CsvPreference.Builder(CsvPreference.TAB_PREFERENCE).build();
    
    private final static String BIGDECIMAL_REGEXP = "[0-9]+(\\.[0-9]+)*";
    private final static Pattern BIG_DECIMAL_PATTERN = Pattern.compile("^" + BIGDECIMAL_REGEXP + "$");
    private final static Pattern MULTIPLE_BIG_DECIMAL_PATTERN = Pattern.compile("^" + BIGDECIMAL_REGEXP + "(/" + BIGDECIMAL_REGEXP + ")*$");
    private final static Pattern RANGE_PATTERN = Pattern.compile("^" + BIGDECIMAL_REGEXP + "-" + BIGDECIMAL_REGEXP + "$");
    private final static Pattern MORE_OR_LESS_PATTERN = Pattern.compile("^[<>]" + BIGDECIMAL_REGEXP + "$");
    private final static Pattern UNCERTAINTY_PATTERN = Pattern.compile("^" + BIGDECIMAL_REGEXP + "\\?$");
    private final static String ORCID_ID_REGEXP = "^[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{3}[0-9X]$";
    private final static String ECO_ID_REGEXP = "^ECO:[0-9]{7}$";
    private final static String CIO_ID_REGEXP = "^CIO:[0-9]{7}$";
    private final static String LIST_SEPARATOR = ";";
    
    // Anatomical entity used for all annotations
    private final static AnatEntityTO ANAT_ENTITY_TO = new AnatEntityTO("UBERON:0000468", "multicellular organism", null);
    
    private final static String ORDER_COL_NAME = "Order";
    private final static String TAXON_COL_NAME = "Taxon";
    private final static String DETERMINED_BY_COL_NAME = "Determined by";
    private final static String PUBLISHED_REFERENCE_TEXT_COL_NAME = "Published reference: citation (APA style)";
    private final static String PUBLISHED_REFERENCE_ACC_COL_NAME = "Published reference: accession";
    private final static String CONTRIBUTOR_COL_NAME = "Contributor";
    private final static String MUSEUM_COLLECTION_COL_NAME = "Museum collection";
    private final static String MUSEUM_ACCESSION_COL_NAME = "Museum accession";
    private final static String LOCATION_NAME_COL_NAME = "Location: name";
    private final static String LOCATION_GPS_COL_NAME = "Location: GPS coordinates";
    private final static String GEOLOGICAL_FORMATION_COL_NAME = "Geological formation";
    private final static String GEOLOGICAL_AGE_COL_NAME = "Geological age";
    private final static String FOSSIL_PRESERVATION_TYPE_COL_NAME = "Fossil preservation type";
    private final static String ENVIRONMENT_COL_NAME = "Environment";
    private final static String BIOZONE_COL_NAME = "Biozone";
    private final static String SAMPLE_SPECIMEN_COUNT_COL_NAME = "Number of specimens in the sample";
    private final static String ANNOT_SPECIMEN_COUNT_COL_NAME = "Number of specimens for this annotation";
    private final static String SEX_COL_NAME = "Sex";
    private final static String PREVIOUS_REPRODUCTIVE_STATE_COL_NAME = "Previous reproductive state";
    private final static String PREVIOUS_DEV_STAGE_COL_NAME = "Previous ontogenetic stage";
    private final static String REPRODUCTIVE_STATE_COL_NAME = "Reproductive state";
    private final static String OBSERVED_DEV_STAGE_COL_NAME = "Observed ontogenetic stage";
    private final static String SPECIMEN_TYPE_COL_NAME = "Type of specimens of interest";
    private final static String LIFE_HISTORY_STYLE_COL_NAME = "Life history style";
    private final static String LIFE_MODES_COL_NAME = "Life mode";
    private final static String JUVENILE_MOULT_COUNT_COL_NAME = "Number of juvenile moults";
    private final static String MAJOR_MORPHOLOGICAL_TRANSITION_COUNT_COL_NAME = "Number of major morphological transitions";
    private final static String ADULT_STAGE_MOULTING_COL_NAME = "Adult stage moulting";
    private final static String OBSERVED_MOULT_STAGES_COUNT_COL_NAME = "Observed # total moult stages";
    private final static String ESTIMATED_MOULT_STAGES_COUNT_COL_NAME = "Estimated # moult stages";
    private final static String SEGMENT_ADDITION_MODE_COL_NAME = "Segment addition mode";
    private final static String BODY_SEGMENT_COUNT_COL_NAME = "# body segments per moult stage";
    private final static String BODY_SEGMENT_COUNT_IN_ADULTS_COL_NAME = "# body segments in adult individuals";
    private final static String BODY_LENGTH_AVERAGE_COL_NAME = "Average body length (in mm)";
    private final static String BODY_WIDTH_AVERAGE_COL_NAME = "Average body width (in mm)";
    private final static String BODY_LENGTH_INCREASE_AVERAGE_COL_NAME = "Average body length increase from previous moult (in mm)";
    private final static String BODY_WIDTH_INCREASE_AVERAGE_COL_NAME = "Average body width increase from previous moult (in mm)";
    private final static String BODY_MASS_INCREASE_AVERAGE_COL_NAME = "Average body mass increase from previous moult (in g)";
    private final static String ONTOGENETIC_STAGE_PERIOD_COL_NAME = "Ontogenetic stage period (in days)";
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
    private final static String MOULTING_VARIABILITY_COL_NAME = "Intraspecific variability in moulting mode";
    private final static String CALCIFICATION_EVENT_COL_NAME = "Post-moult cuticle calcification event";
    private final static String HEAVY_METAL_REINFORCEMENT_COL_NAME = "Post-moult heavy metal reinforcement of the cuticle";
    private final static String OTHER_BEHAVIOURS_ASSOCIATED_WITH_MOULTING_COL_NAME = "Other behaviours associated with moulting";
    private final static String EXUVIAE_CONSUMPTION_COL_NAME = "Consumption of exuviae";
    private final static String REABSORPTION_COL_NAME = "Reabsorption during moulting";
    private final static String FOSSIL_EXUVIAE_QUALITY_COL_NAME = "Quality of fossil exuviae";
    private final static String EVIDENCE_CODE_COL_NAME = "Evidence code";
    private final static String CONFIDENCE_COL_NAME = "Confidence";
    private final static String GENERAL_COMMENTS_COL_NAME = "General Comments";
    
    public TaxonAnnotationParser() {
    }
    
    public static void main(String[] args) {
        logger.traceEntry(Arrays.toString(args));
        
        if (args.length != 1) {
            throw new IllegalArgumentException("Incorrect number of arguments provided, expected 1 argument, " +
                    args.length + " provided");
        }
    
        TaxonAnnotationParser parser = new TaxonAnnotationParser();
        List<TaxonAnnotationBean> taxonAnnotationBeans = parser.parseAnnotations(args[0]);
    
        parser.insertTaxonAnnotations(taxonAnnotationBeans);
    
        logger.traceExit();
    }
    
    public void insertTaxonAnnotations(List<TaxonAnnotationBean> taxonAnnotationBeans) {
        insertTaxonAnnotations(taxonAnnotationBeans, articleDAO, articleToDbXrefDAO, conditionDAO, dataSourceDAO,
                dbXrefDAO, devStageDAO, geologicalAgeDAO, moultingCharactersDAO, sampleSetDAO, taxonDAO, taxonAnnotationDAO,
                versionDAO, userDAO);
    }
    
    public int insertTaxonAnnotations(List<TaxonAnnotationBean> taxonAnnotationBeans, ArticleDAO articleDAO,
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
        for (int beanCount = 0; beanCount < taxonAnnotationBeans.size(); beanCount++) {
            TaxonAnnotationBean bean = taxonAnnotationBeans.get(beanCount);
            if (beanCount % 5 == 0) {
                logger.debug(beanCount + " annotations converted");
            }
            MoultingCharactersTO moultingCharactersTO = new MoultingCharactersTO(
                    mcNextId,
                    bean.getLifeHistoryStyle(),
                    bean.getLifeModes(),
                    bean.getJuvenileMoultCount(),
                    bean.getMajorMorphologicalTransitionCount(),
                    bean.getAdultStageMoulting(),
                    bean.getObservedMoultStagesCount(),
                    bean.getEstimatedMoultStagesCount(),
                    bean.getSegmentAdditionMode(),
                    bean.getBodySegmentCount(),
                    bean.getBodySegmentCountInAdults(),
                    bean.getBodyLengthAverage(),
                    bean.getBodyWidthAverage(),
                    bean.getBodyLengthIncreaseAverage(),
                    bean.getBodyWidthIncreaseAverage(),
                    bean.getBodyMassIncreaseAverage(),
                    bean.getStagePeriod(),
                    bean.getIntermoultPeriod(),
                    bean.getPremoultPeriod(),
                    bean.getPostmoultPeriod(),
                    bean.getVariationWithinCohorts(),
                    bean.getSutureLocations(),
                    bean.getCephalicSutureLocations(),
                    bean.getPostCephalicSutureLocations(),
                    bean.getResultingNamedMoultingConfigurations(),
                    bean.getEgressDirectionsDuringMoulting(),
                    bean.getPositionsExuviaeFoundIn(),
                    bean.getMoultingPhase(),
                    bean.getMoultingVariability(),
                    bean.getCalcificationEvent(),
                    bean.getHeavyMetalReinforcements(),
                    bean.getOtherBehaviours(),
                    bean.getExuviaeConsumption(),
                    bean.getReabsorption(),
                    bean.getFossilExuviaeQualities(),
                    bean.getGeneralComments());
            mcTOs.add(moultingCharactersTO);
            mcNextId++;
            
            TaxonTO taxonTO = taxonDAO.findByScientificName(bean.getTaxon());
            String taxonType = null;
            if (taxonTO == null) {
                // Try to find synonym taxon
                taxonTO = taxonDAO.findBySynonym(bean.getTaxon());
                taxonType = (taxonTO == null ? null : "synonym");
            }
            if (taxonTO == null) {
                // Try to find genus taxon
                String genus = bean.getTaxon().split(" ")[0];
                taxonTO = taxonDAO.findByScientificName(genus);
                taxonType = (taxonTO == null ? null : "genus");
            }
            if (taxonTO == null) {
                // Try to find order taxon
                taxonTO = taxonDAO.findByScientificName(bean.getOrder());
                taxonType = (taxonTO == null ? null : "order");
            }
    
            if (taxonTO == null) {
                throw new IllegalArgumentException("Unknown taxon scientific name or genus '" + bean.getTaxon() +
                        "' or order '" + bean.getOrder() + "'");
            }
            
            if (StringUtils.isNotBlank(taxonType)) {
                logger.warn("Taxon scientific name '" + bean.getTaxon() + "' has not been found. " +
                        "Found " + taxonType + " '" + taxonTO.getScientificName() + "'.");
            }
            
            // Ex: 'Stage 3' or 'Sandbian to Katian'
            String fromGeoAgeName = bean.getGeologicalAge();
            if (bean.getSpecimenTypes() != null && bean.getSpecimenTypes().contains("fossil(s)") && StringUtils.isBlank(fromGeoAgeName)) {
                throw new IllegalArgumentException("Specimen type contains 'fossil(s) but no geological age is defined");
            }
            GeologicalAgeTO toGeologicalAgeTO = null;
            int i = StringUtils.isNotBlank(bean.getGeologicalAge()) ? bean.getGeologicalAge().indexOf(" to ") : -1;
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
            
            Set<DbXrefTO> articleDbXrefTOs = new HashSet<>();
            if (bean.getPublishedReferenceAcc() != null) {
                for (String acc : bean.getPublishedReferenceAcc().split(";")) {
                    Matcher m = Pattern.compile("^([A-Z]+):(\\S+)$").matcher(String.valueOf(acc));
                    if (m.find()) {
                        DataSourceTO dataSourceTO = dataSourceDAO.findByName(m.group(1));
                        if (dataSourceTO == null) {
                            throw new IllegalArgumentException("Unknown data source: " + m.group(1));
                        }
                        DbXrefTO dbXrefTO = dbXrefDAO.findByAccessionAndDatasource(m.group(2).trim(), dataSourceTO.getId());
                        if (dbXrefTO == null) {
                            dbXrefTO = new DbXrefTO(dbXrefNextId, m.group(2).trim(), null, dataSourceTO);
                            dbXrefTOs.add(dbXrefTO);
                            dbXrefNextId++;
                        }
                        articleDbXrefTOs.add(dbXrefTO);
                    }
                }
            }
            
            ArticleTO articleTO = viewedArticleCitations.get(bean.getPublishedReferenceText());
            if (bean.getPublishedReferenceText() != null && articleTO == null) {
                articleTO = articleDAO.findByCitation(bean.getPublishedReferenceText());
                if (articleTO == null) {
                    articleTO = new ArticleTO(articleNextId, bean.getPublishedReferenceText(), null, null, dbXrefTOs);
                    articleTOs.add(articleTO);
                    articleNextId++;
                }
                for (DbXrefTO dbXrefTO : articleDbXrefTOs) {
                    articleToDbXrefTOs.add(new ArticleToDbXrefTO(articleTO.getId(), dbXrefTO.getId()));
                }
                viewedArticleCitations.put(bean.getPublishedReferenceText(), articleTO);
            }
            
            // We don't check whether a sample set is already present/seen because we need to have one sample set per
            // annotation taxon. Indeed, the user might want to modify a sample set with no impact on the other annotations.
            // FIXME add isCaptive?
            boolean isFossil = bean.getSpecimenTypes() != null && bean.getSpecimenTypes().contains("fossil(s)");
            SampleSetTO sampleSetTO = new SampleSetTO(ssNextId, fromGeologicalAgeTO, toGeologicalAgeTO,
                    bean.getSampleSpecimenCount(), isFossil, null, extractValues(bean.getMuseumAccession(), false),
                    extractValues(bean.getMuseumCollection(), false), extractValues(bean.getLocationName(), false),
                    bean.getFossilPreservationTypes(), extractValues(bean.getEnvironment(), true),
                    extractValues(bean.getGeologicalFormation(), false), bean.getSpecimenTypes(), bean.getBiozone());
            sampleSetTOs.add(sampleSetTO);
            ssNextId++;
            
            ConditionTO conditionTO = conditionDAO.find(bean.getDevStage(), ANAT_ENTITY_TO.getId(),
                    bean.getSex(), bean.getReproductiveState());
            if (conditionTO == null) {
                DevStageTO devStageTO = devStageDAO.findById(bean.getDevStage());
                if (devStageTO == null) {
                    devStageTO = devStageDAO.findByName(bean.getDevStage(), taxonTO.getPath());
                }
                if (devStageTO == null) {
                    throw new IllegalArgumentException("Unknown developmental stage: " + bean.getDevStage());
                }
                conditionTO = new ConditionTO(conditionNextId, devStageTO, ANAT_ENTITY_TO,
                        bean.getSex(), bean.getReproductiveState(), null);
                conditionTOs.add(conditionTO);
                conditionNextId++;
            }
            
            UserTO userTO = userDAO.findByOrcidId(bean.getContributor());
            if (userTO == null) {
                throw new IllegalArgumentException("User not found: " + bean.getContributor());
            }
            Timestamp current = new Timestamp(new Date().getTime());
            VersionTO versionTO = new VersionTO(versionNextId, userTO, current, userTO, current, 1);
            versionTOs.add(versionTO);
            versionNextId++;
            
            TaxonAnnotationTO taxonAnnotationTO = new TaxonAnnotationTO(taxonAnnotNextId, taxonTO, bean.getTaxon(),
                    bean.getDeterminedBy(), sampleSetTO.getId(), bean.getAnnotSpecimenCount(), conditionTO, articleTO, null,
                    moultingCharactersTO.getId(), null, null, versionTO.getId());
            taxonAnnotationTOs.add(taxonAnnotationTO);
            taxonAnnotNextId++;
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
    
    private static Integer getNextId(Integer lastId) {
        return lastId == null ? 1 : lastId + 1;
    }
    
    private Set<String> extractValues(String s, boolean toLowerCase) {
        if (StringUtils.isBlank(s)) {
            return null;
        }
        if (toLowerCase) {
            s = s.toLowerCase();
        }
        return Arrays.stream(s.split(";"))
                     .map(String::trim)
                     .collect(Collectors.toSet());
    }
    
    private GeologicalAgeTO getGeologicalAgeTO(GeologicalAgeDAO geologicalAgeDAO, String geoAgeName) {
        if (StringUtils.isBlank(geoAgeName)) {
            return null;
        }
        GeologicalAgeTO geoAgeTO = geologicalAgeDAO.findByLabelOrSynonym(geoAgeName);
        if (geoAgeTO == null) {
            throw new IllegalArgumentException("Unknown geological age name: " + geoAgeName);
        }
        return geoAgeTO;
    }
    
    public List<TaxonAnnotationBean> parseAnnotations(MultipartFile uploadedFile) {
        logger.info("Start parsing of taxon annotation file ...");
        
        try (ICsvBeanReader reader = new CsvBeanReader(new InputStreamReader(uploadedFile.getInputStream()), TSV_COMMENTED)) {
            List<TaxonAnnotationBean> taxonAnnotationBeans = getTaxonAnnotationBeans(reader);
            return logger.traceExit(taxonAnnotationBeans);
        
        } catch (SuperCsvException e) {
            throw new IllegalArgumentException("The provided file " + uploadedFile.getOriginalFilename()
                    + " could not be properly parsed. Error: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new UncheckedIOException("Can not read file " + uploadedFile.getOriginalFilename(), e);
        }
    }
    
    public List<TaxonAnnotationBean> parseAnnotations(String fileName) {
        logger.info("Start parsing of taxon annotation file " + fileName + "...");
        
        try (ICsvBeanReader reader = new CsvBeanReader(new FileReader(fileName), TSV_COMMENTED)) {
    
            logger.info("End parsing of taxon annotation file");
    
            return logger.traceExit(getTaxonAnnotationBeans(reader));
            
        } catch (SuperCsvException e) {
            //hide implementation details
            throw new IllegalArgumentException("The provided file " + fileName + " could not be properly parsed. " +
                    "Error: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new UncheckedIOException("Can not read file " + fileName, e);
        }
    }
    
    private List<TaxonAnnotationBean> getTaxonAnnotationBeans(ICsvBeanReader annotReader) throws IOException {
        List<TaxonAnnotationBean> annots = new ArrayList<>();
        
        final String[] header = annotReader.getHeader(true);
        
        String[] attributeMapping = mapHeaderToAttributes(header);
        CellProcessor[] cellProcessorMapping = mapHeaderToCellProcessors(header);
        TaxonAnnotationBean beans;
        
        while((beans = annotReader.read(TaxonAnnotationBean.class, attributeMapping, cellProcessorMapping)) != null) {
            logger.trace(beans);
            annots.add(beans);
        }
        if (annots.isEmpty()) {
            throw new IllegalArgumentException("The provided file did not allow to retrieve any annotation");
        }
        
        return annots;
    }
    
    
    private CellProcessor[] mapHeaderToCellProcessors(String[] header) {
        CellProcessor[] processors = new CellProcessor[header.length];
        for (int i = 0; i < header.length; i++) {
            if (header[i] == null) {
                continue;
            }
            // For the moment, we're keeping the switch not refactored to simplify the update
            processors[i] = switch (header[i]) {
                case ORDER_COL_NAME
                        -> new FmtCustomNull(new StrNotNullOrEmpty(new Trim()));
                case TAXON_COL_NAME
                        -> new FmtCustomNull(new StrNotNullOrEmpty(new Trim()));
                case DETERMINED_BY_COL_NAME
                        -> new FmtCustomNull(new Optional(new Trim()));
                case PUBLISHED_REFERENCE_TEXT_COL_NAME
                        -> new FmtCustomNull(new Optional(new Trim()));
                case PUBLISHED_REFERENCE_ACC_COL_NAME
                        -> new FmtCustomNull(new Optional(new Trim()));
                case CONTRIBUTOR_COL_NAME
                        -> new FmtCustomNull(new StrNotNullOrEmpty(new ParseFirstElement(" ", new StrRegEx(ORCID_ID_REGEXP))));
                case MUSEUM_COLLECTION_COL_NAME
                        -> new FmtCustomNull(new Optional(new Trim()));
                case MUSEUM_ACCESSION_COL_NAME
                        -> new FmtCustomNull(new Optional(new Trim()));
                case LOCATION_NAME_COL_NAME
                        -> new FmtCustomNull(new StrNotNullOrEmpty(new Trim()));
                case LOCATION_GPS_COL_NAME
                        -> new FmtCustomNull(new Optional(new Trim()));
                case GEOLOGICAL_FORMATION_COL_NAME
                        -> new FmtCustomNull(new Optional(new Trim()));
                case GEOLOGICAL_AGE_COL_NAME
                        -> new FmtCustomNull(new Optional(new Trim()));
                case FOSSIL_PRESERVATION_TYPE_COL_NAME
                        -> new FmtCustomNull(new Optional(new ParseStringSet(LIST_SEPARATOR)));
                case ENVIRONMENT_COL_NAME
                        -> new FmtCustomNull(new Optional(new Trim()));
                case BIOZONE_COL_NAME
                        -> new FmtCustomNull(new Optional(new Trim()));
                case SAMPLE_SPECIMEN_COUNT_COL_NAME
                        -> new FmtCustomNull(new Optional(new CustomDecimalFormat()));
                case ANNOT_SPECIMEN_COUNT_COL_NAME
                        -> new FmtCustomNull(new Optional(new CustomDecimalFormat()));
                case SEX_COL_NAME
                        -> new FmtCustomNull(new Optional(new FmtStrLowerCase(
                                new IsElementOf(getObjects(Sex.class)))));
                case PREVIOUS_REPRODUCTIVE_STATE_COL_NAME
                        -> new FmtCustomNull(new Optional(new Trim()));
                case PREVIOUS_DEV_STAGE_COL_NAME
                        -> new FmtCustomNull(new Optional((new Trim())));
                case REPRODUCTIVE_STATE_COL_NAME
                        -> new FmtCustomNull(new Optional(new Trim()));
                case OBSERVED_DEV_STAGE_COL_NAME
                        -> new FmtCustomNull(new Optional((new Trim())));
                case SPECIMEN_TYPE_COL_NAME
                        -> new FmtCustomNull(new Optional(new ParseStringSet(LIST_SEPARATOR)));
                case LIFE_HISTORY_STYLE_COL_NAME
                        -> new FmtCustomNull(new Optional(new FmtStrLowerCase(
                            new IsElementOf(getObjects(LifeHistoryStyle.class)))));
                case LIFE_MODES_COL_NAME
                        -> new FmtCustomNull(new Optional(new ParseEnumSet(LifeMode.class, LIST_SEPARATOR)));
                case JUVENILE_MOULT_COUNT_COL_NAME
                        -> new FmtCustomNull(new Optional(new CustomDecimalFormat()));
                case MAJOR_MORPHOLOGICAL_TRANSITION_COUNT_COL_NAME
                        -> new FmtCustomNull(new Optional(new CustomDecimalFormat()));
                case ADULT_STAGE_MOULTING_COL_NAME
                        -> new FmtCustomNull(new Optional(new ParseBool("yes", "no")));
                case OBSERVED_MOULT_STAGES_COUNT_COL_NAME
                        -> new FmtCustomNull(new Optional(new ParseInt()));
                case ESTIMATED_MOULT_STAGES_COUNT_COL_NAME
                        -> new FmtCustomNull(new Optional(new CustomDecimalFormat()));
                case SEGMENT_ADDITION_MODE_COL_NAME
                        -> new FmtCustomNull(new Optional(new Trim()));
                case BODY_SEGMENT_COUNT_COL_NAME
                        -> new FmtCustomNull(new Optional(new CustomDecimalFormat()));
                case BODY_SEGMENT_COUNT_IN_ADULTS_COL_NAME
                        -> new FmtCustomNull(new Optional(new CustomDecimalFormat()));
                case BODY_LENGTH_AVERAGE_COL_NAME
                        -> new FmtCustomNull(new Optional(new ParseBigDecimal()));
                case BODY_WIDTH_AVERAGE_COL_NAME
                        -> new FmtCustomNull(new Optional(new ParseBigDecimal()));
                case BODY_LENGTH_INCREASE_AVERAGE_COL_NAME
                        -> new FmtCustomNull(new Optional(new ParseBigDecimal()));
                case BODY_WIDTH_INCREASE_AVERAGE_COL_NAME
                        -> new FmtCustomNull(new Optional(new ParseBigDecimal()));
                case BODY_MASS_INCREASE_AVERAGE_COL_NAME
                        -> new FmtCustomNull(new Optional(new ParseBigDecimal()));
                case ONTOGENETIC_STAGE_PERIOD_COL_NAME
                        -> new FmtCustomNull(new Optional(new CustomDecimalFormat()));
                case INTERMOULT_PERIOD_COL_NAME
                        -> new FmtCustomNull(new Optional(new CustomDecimalFormat()));
                case PREMOULT_PERIOD_COL_NAME
                        -> new FmtCustomNull(new Optional(new CustomDecimalFormat()));
                case POSTMOULT_PERIOD_COL_NAME
                        -> new FmtCustomNull(new Optional(new CustomDecimalFormat()));
                case VARIATION_WITHIN_COHORTS_COL_NAME
                        -> new FmtCustomNull(new Optional(new CustomDecimalFormat()));
                case MOULTING_SUTURE_LOCATION_COL_NAME
                        -> new FmtCustomNull(new Optional(new ParseStringSet(LIST_SEPARATOR)));
                case CEPHALIC_SUTURE_LOCATION_COL_NAME
                        -> new FmtCustomNull(new Optional(new ParseStringSet(LIST_SEPARATOR)));
                case POST_CEPHALIC_SUTURE_LOCATION_COL_NAME
                        -> new FmtCustomNull(new Optional(new ParseStringSet(LIST_SEPARATOR)));
                case RESULTING_NAMED_MOULTING_CONFIGURATIONS_COL_NAME
                        -> new FmtCustomNull(new Optional( new StrReplace("'", "\\\\'", new ParseStringSet(LIST_SEPARATOR))));
                case EGRESS_DIRECTION_DURING_MOULTING_COL_NAME
                        -> new FmtCustomNull(new Optional(new ParseEnumSet(EgressDirection.class, LIST_SEPARATOR)));
                case POSITION_EXUVIAE_FOUND_IN_COL_NAME
                        -> new FmtCustomNull(new Optional(new ParseEnumSet(ExuviaePosition.class, LIST_SEPARATOR)));
                case MOULTING_PHASE_COL_NAME
                        -> new FmtCustomNull(new Optional(new FmtStrLowerCase(
                        new IsElementOf(getObjects(MoultingPhase.class)))));
                case MOULTING_VARIABILITY_COL_NAME
                        -> new FmtCustomNull(new Optional(new FmtStrLowerCase(
                        new IsElementOf(getObjects(MoultingVariability.class)))));
                case CALCIFICATION_EVENT_COL_NAME
                        -> new FmtCustomNull(new Optional(new FmtStrLowerCase(
                        new IsElementOf(getObjects(Calcification.class)))));
                case HEAVY_METAL_REINFORCEMENT_COL_NAME
                        -> new FmtCustomNull(new Optional(new ParseEnumSet(HeavyMetalReinforcement.class, LIST_SEPARATOR)));
                case OTHER_BEHAVIOURS_ASSOCIATED_WITH_MOULTING_COL_NAME
                        -> new FmtCustomNull(new Optional(new ParseStringSet(LIST_SEPARATOR)));
                case EXUVIAE_CONSUMPTION_COL_NAME
                        -> new FmtCustomNull(new Optional(new FmtStrLowerCase(
                        new IsElementOf(getObjects(ExuviaeConsumption.class)))));
                case REABSORPTION_COL_NAME
                        -> new FmtCustomNull(new Optional(new FmtStrLowerCase(
                        new IsElementOf(getObjects(Reabsorption.class)))));
                case FOSSIL_EXUVIAE_QUALITY_COL_NAME
                        -> new FmtCustomNull(new Optional(new ParseStringSet(LIST_SEPARATOR)));
                case EVIDENCE_CODE_COL_NAME
                        -> new FmtCustomNull(new StrNotNullOrEmpty(new ParseFirstElement(" ", new StrRegEx(ECO_ID_REGEXP))));
                case CONFIDENCE_COL_NAME
                        -> new FmtCustomNull(new StrNotNullOrEmpty(new ParseFirstElement(" ", new StrRegEx(CIO_ID_REGEXP))));
                case GENERAL_COMMENTS_COL_NAME
                        -> new FmtCustomNull(new Optional(new Trim()));
                default -> throw new IllegalArgumentException("Unrecognized header: " + header[i] + " for TaxonAnnotationBean");
            };
        }
        logger.trace("processors: " + Arrays.stream(processors).toList());
        return processors;
    }
    
    private static <T extends Enum<T> & MoutldbEnum> List<Object> getObjects(Class<T> enumClass) {
        return new ArrayList<>(MoutldbEnum.getAllStringRepresentations(enumClass));
    }
    
    private String[] mapHeaderToAttributes(String[] header) {
        
        String[] mapping = new String[header.length];
        for (int i = 0; i < header.length; i++) {
            if (header[i] == null) {
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
                case FOSSIL_PRESERVATION_TYPE_COL_NAME -> "fossilPreservationTypes";
                case ENVIRONMENT_COL_NAME -> "environment";
                case BIOZONE_COL_NAME -> "biozone";
                case SAMPLE_SPECIMEN_COUNT_COL_NAME -> "sampleSpecimenCount";
                case ANNOT_SPECIMEN_COUNT_COL_NAME -> "annotSpecimenCount";
                case SEX_COL_NAME -> "sex";
                case PREVIOUS_REPRODUCTIVE_STATE_COL_NAME -> "previousReproductiveState";
                case PREVIOUS_DEV_STAGE_COL_NAME -> "previousDevStage";
                case REPRODUCTIVE_STATE_COL_NAME -> "reproductiveState";
                case OBSERVED_DEV_STAGE_COL_NAME -> "devStage";
                case SPECIMEN_TYPE_COL_NAME -> "specimenTypes";
                case LIFE_HISTORY_STYLE_COL_NAME -> "lifeHistoryStyle";
                case LIFE_MODES_COL_NAME -> "lifeModes";
                case JUVENILE_MOULT_COUNT_COL_NAME -> "juvenileMoultCount";
                case MAJOR_MORPHOLOGICAL_TRANSITION_COUNT_COL_NAME -> "majorMorphologicalTransitionCount";
                case ADULT_STAGE_MOULTING_COL_NAME -> "adultStageMoulting";
                case OBSERVED_MOULT_STAGES_COUNT_COL_NAME -> "observedMoultStagesCount";
                case ESTIMATED_MOULT_STAGES_COUNT_COL_NAME -> "estimatedMoultStagesCount";
                case SEGMENT_ADDITION_MODE_COL_NAME -> "segmentAdditionMode";
                case BODY_SEGMENT_COUNT_COL_NAME -> "bodySegmentCount";
                case BODY_SEGMENT_COUNT_IN_ADULTS_COL_NAME -> "bodySegmentCountInAdults";
                case BODY_LENGTH_AVERAGE_COL_NAME -> "bodyLengthAverage";
                case BODY_WIDTH_AVERAGE_COL_NAME -> "bodyWidthAverage";
                case BODY_LENGTH_INCREASE_AVERAGE_COL_NAME -> "bodyLengthIncreaseAverage";
                case BODY_WIDTH_INCREASE_AVERAGE_COL_NAME -> "bodyWidthIncreaseAverage";
                case BODY_MASS_INCREASE_AVERAGE_COL_NAME -> "bodyMassIncreaseAverage";
                case ONTOGENETIC_STAGE_PERIOD_COL_NAME -> "stagePeriod";
                case INTERMOULT_PERIOD_COL_NAME -> "intermoultPeriod";
                case PREMOULT_PERIOD_COL_NAME -> "premoultPeriod";
                case POSTMOULT_PERIOD_COL_NAME -> "postmoultPeriod";
                case VARIATION_WITHIN_COHORTS_COL_NAME -> "variationWithinCohorts";
                case MOULTING_SUTURE_LOCATION_COL_NAME -> "sutureLocations";
                case CEPHALIC_SUTURE_LOCATION_COL_NAME -> "cephalicSutureLocations";
                case POST_CEPHALIC_SUTURE_LOCATION_COL_NAME -> "postCephalicSutureLocations";
                case RESULTING_NAMED_MOULTING_CONFIGURATIONS_COL_NAME -> "resultingNamedMoultingConfigurations";
                case EGRESS_DIRECTION_DURING_MOULTING_COL_NAME -> "egressDirectionsDuringMoulting";
                case POSITION_EXUVIAE_FOUND_IN_COL_NAME -> "positionsExuviaeFoundIn";
                case MOULTING_PHASE_COL_NAME -> "moultingPhase";
                case MOULTING_VARIABILITY_COL_NAME -> "moultingVariability";
                case CALCIFICATION_EVENT_COL_NAME -> "calcificationEvent";
                case HEAVY_METAL_REINFORCEMENT_COL_NAME -> "heavyMetalReinforcements";
                case OTHER_BEHAVIOURS_ASSOCIATED_WITH_MOULTING_COL_NAME -> "otherBehaviours";
                case EXUVIAE_CONSUMPTION_COL_NAME -> "exuviaeConsumption";
                case REABSORPTION_COL_NAME -> "reabsorption";
                case FOSSIL_EXUVIAE_QUALITY_COL_NAME -> "fossilExuviaeQualities";
                case EVIDENCE_CODE_COL_NAME -> "evidenceCode";
                case CONFIDENCE_COL_NAME -> "confidence";
                case GENERAL_COMMENTS_COL_NAME -> "generalComments";
                default -> throw new IllegalArgumentException("Unrecognized header: '" + header[i] + "' for TaxonAnnotationBean");
            };
        }
        logger.trace("mapping: " + Arrays.stream(mapping).toList());
        return mapping;
    }
    
    public static class FmtCustomNull extends CellProcessorAdaptor {
        
        public FmtCustomNull() {
            super();
        }
        
        public FmtCustomNull(CellProcessor next) {
            // this constructor allows other processors to be chained after this processor
            super(next);
        }
        
        public Object execute(Object value, CsvContext context) {
    
            String stringValue = String.valueOf(value);
            if (value == null || stringValue.equals("NA") || stringValue.equals("?")) {
                return next.execute(null, context);
            }
    
            return next.execute(value, context);
        }
    }
    
    public static class FmtStrLowerCase extends CellProcessorAdaptor implements StringCellProcessor {
        
        public FmtStrLowerCase() {
            super();
        }
        
        public FmtStrLowerCase(CellProcessor next) {
            // this constructor allows other processors to be chained after this processor
            super(next);
        }
        
        public Object execute(Object value, CsvContext context) {
            
            // Throws an Exception if the input is null
            validateInputNotNull(value, context);
            
            return next.execute(String.valueOf(value).toLowerCase(), context);
        }
    }
    
    public static class ParseFirstElement extends CellProcessorAdaptor implements StringCellProcessor {
        
        private String separator;
        
        public ParseFirstElement() {
            super();
        }
        
        public ParseFirstElement(String separator) {
            super();
            this.separator = separator;
        }
        
        public ParseFirstElement(String separator, CellProcessor next) {
            // this constructor allows other processors to be chained after this processor
            super(next);
            this.separator = separator;
        }
        
        public Object execute(Object value, CsvContext context) {
            
            // Throws an Exception if the input is null
            validateInputNotNull(value, context);
            
            return next.execute(String.valueOf(value).split(this.separator)[0], context);
        }
    }
    
    public static class ParseEnumSet extends CellProcessorAdaptor implements StringCellProcessor {
        
        private final String separator;
        
        private final Set<Object> possibleValues = new HashSet<>();
        
        public <T extends Enum<T> & MoutldbEnum> ParseEnumSet(final Class<T> enumClass, final String separator) {
            super();
            this.separator = separator;
            this.possibleValues.addAll(MoutldbEnum.getAllStringRepresentations(enumClass));
        }
        
        public <T extends Enum<T> & MoutldbEnum> ParseEnumSet(final Class<T> enumClass, final String separator, final CellProcessor next) {
            // this constructor allows other processors to be chained after this processor
            super(next);
            this.separator = separator;
            this.possibleValues.addAll(MoutldbEnum.getAllStringRepresentations(enumClass));
        }
        
        public Object execute(Object value, CsvContext context) {
            
            // Throws an Exception if the input is null
            validateInputNotNull(value, context);
            
            Set<String> set = Arrays.stream(String.valueOf(value).split(this.separator))
                    .map(s -> s.trim().toLowerCase())
                    .collect(Collectors.toSet());
            
            if (!this.possibleValues.containsAll(set)) {
                throw new ParseException(String.format("'%s' does not only contain allowed set of values '%s'",
                        value, this.possibleValues));
            }
            
            return set;
        }
    }
    
    public static class ParseStringSet extends CellProcessorAdaptor implements StringCellProcessor {
        
        private final String separator;
        
        public <T extends Enum<T> & MoutldbEnum> ParseStringSet(final String separator) {
            super();
            this.separator = separator;
        }
        
        public <T extends Enum<T> & MoutldbEnum> ParseStringSet(final String separator, final CellProcessor next) {
            // this constructor allows other processors to be chained after this processor
            super(next);
            this.separator = separator;
        }
        
        public Object execute(Object value, CsvContext context) {
            
            // Throws an Exception if the input is null
            validateInputNotNull(value, context);
            
            return Arrays.stream(String.valueOf(value).split(this.separator))
                    .map(s -> s.trim().toLowerCase())
                    .collect(Collectors.toSet());
        }
    }
    
    public static class CustomDecimalFormat extends CellProcessorAdaptor implements StringCellProcessor {
        
        
        public <T extends Enum<T> & MoutldbEnum> CustomDecimalFormat() {
            super();
        }
        
        public <T extends Enum<T> & MoutldbEnum> CustomDecimalFormat(final CellProcessor next) {
            // this constructor allows other processors to be chained after this processor
            super(next);
        }
        
        public Object execute(Object value, CsvContext context) {
            
            // Throws an Exception if the input is null
            validateInputNotNull(value, context);
            
            final List<Pattern> rxs = Arrays.asList(BIG_DECIMAL_PATTERN, MULTIPLE_BIG_DECIMAL_PATTERN,
                    RANGE_PATTERN, MORE_OR_LESS_PATTERN, UNCERTAINTY_PATTERN);
            
            if (rxs.stream().noneMatch(rx -> rx.matcher(String.valueOf(value)).matches())) {
                throw new ParseException(String.format("'%s' does not match the decimal expression", value));
            }
            
            return value;
        }
    }
    
}
