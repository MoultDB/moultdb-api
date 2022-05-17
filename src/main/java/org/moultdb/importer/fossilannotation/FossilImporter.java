package org.moultdb.importer.fossilannotation;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.model.moutldbenum.EgressDirection;
import org.moultdb.api.model.moutldbenum.ExuviaeConsumption;
import org.moultdb.api.model.moutldbenum.ExuviaePosition;
import org.moultdb.api.model.moutldbenum.LifeHistoryStyle;
import org.moultdb.api.model.moutldbenum.MoultingPhase;
import org.moultdb.api.model.moutldbenum.MoultingVariability;
import org.moultdb.api.model.moutldbenum.Reabsorption;
import org.moultdb.api.repository.dao.GeologicalAgeDAO;
import org.moultdb.api.repository.dao.TaxonDAO;
import org.moultdb.api.repository.dto.FossilAnnotationImportTO;
import org.moultdb.api.repository.dto.GeologicalAgeTO;
import org.moultdb.api.repository.dto.TaxonTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ParseBigDecimal;
import org.supercsv.cellprocessor.ParseBool;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.Trim;
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
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Valentine Rech de Laval
 * @since 2021-12-13
 */
public class FossilImporter {
    
    @Autowired TaxonDAO taxonDAO;
    @Autowired GeologicalAgeDAO geologicalAgeDAO;
    
    private final static Logger logger = LogManager.getLogger(FossilImporter.class.getName());
    
    private final static String ORDER_COL_NAME = "Order";
    private final static String TAXON_COL_NAME = "Taxon";
    private final static String PUBLISHED_REFERENCE_TEXT_COL_NAME = "Published reference";
    private final static String PUBLISHED_REFERENCE_ACC_COL_NAME = "DOI/PMID/URL";
    private final static String MUSEUM_COLLECTION_COL_NAME = "Museum collection";
    private final static String MUSEUM_ACCESSION_COL_NAME = "Museum accession # in the study";
    private final static String LOCATION_COL_NAME = "Location";
    private final static String GEOLOGICAL_FORMATION_COL_NAME = "Geological formation";
    private final static String GEOLOGICAL_AGE_COL_NAME = "Geological age";
    private final static String FOSSIL_PRESERVATION_TYPE_COL_NAME = "Fossil preservation type";
    private final static String ENVIRONMENT_COL_NAME = "Environment";
    private final static String SPECIMEN_COUNT_COL_NAME = "Number of specimens";
    private final static String SPECIMEN_COUNT_PER_MOULT_STAGE_COL_NAME = "Number of specimen at each moult stage";
    private final static String SPECIMEN_TYPE_COL_NAME = "Type of specimens of interest";
    private final static String LIFE_HISTORY_STYLE_COL_NAME = "Life history style";
    private final static String ADULT_STAGE_MOULTING_COL_NAME = "Adult stage moulting";
    private final static String OBSERVED_MOULT_STAGES_COUNT_COL_NAME = "Observed # total moult stages";
    private final static String OBSERVED_MOULT_STAGES_COL_NAME = "Observed moult stages";
    private final static String ESTIMATED_MOULT_STAGES_COUNT_COL_NAME = "Estimated # moult stages";
    private final static String SEGMENT_ADDITION_MODE_COL_NAME = "Segment addition mode";
    private final static String BODY_SEGMENTS_COUNT_PER_MOULT_STAGE_COL_NAME = "# body segments per moult stage";
    private final static String BODY_SEGMENTS_COUNT_IN_ADULT_COL_NAME = "# body segments in adult individuals";
    private final static String BODY_LENGTH_INCREASE_AVERAGE_PER_MOULT_COL_NAME = "Average body length increase per moult";
    private final static String BODY_LENGTH_AVERAGE_AT_EACH_MOULT_STAGE_COL_NAME = "Average body length at each moult stage";
    private final static String UNIT_OF_MEASUREMENT = "Unit of Measurement";
    private final static String MOULTING_SUTURE_LOCATION_COL_NAME = "Location of moulting suture";
    private final static String CEPHALIC_SUTURE_LOCATION_COL_NAME = "Cephalic suture location";
    private final static String POST_CEPHALIC_SUTURE_LOCATION_COL_NAME = "Post-cephalic suture location";
    private final static String RESULTING_NAMED_MOULTING_CONFIGURATIONS_COL_NAME = "Resulting named moulting configurations";
    private final static String EGRESS_DIRECTION_DURING_MOULTING_COL_NAME = "Direction of egress during moulting";
    private final static String POSITION_EXUVIAE_FOUND_IN_COL_NAME = "Position exuviae found in";
    private final static String MOULTING_PHASE_COL_NAME = "Moulting phase";
    private final static String MOULTING_VARIABILITY_COL_NAME = "Level of intraspecific variability in moulting mode";
    private final static String JUVENILE_MOULTING_BEHAVIOURS_COL_NAME = "Juvenile moulting behaviours";
    private final static String JUVENILE_MOULTING_SUTURE_LOCATION_COL_NAME = "Juvenile location of moulting suture";
    private final static String JUVENILE_CEPHALIC_SUTURE_LOCATION_COL_NAME = "Juvenile Cephalic suture location";
    private final static String JUVENILE_POST_CEPHALIC_SUTURE_LOCATION_COL_NAME = "Juvenile Post-cephalic suture location";
    private final static String JUVENILE_RESULTING_NAMED_MOULTING_CONFIGURATIONS_COL_NAME = "Juvenile Resulting named moulting configurations";
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
        
        if (args.length != 1){
            throw new IllegalArgumentException("Incorrect number of arguments provided, expected 1 argument, " +
                    args.length + " provided.");
        }
    
        FossilImporter parser = new FossilImporter();
        List<FossilAnnotationBean> fossilAnnotationBeans = parser.parseFossilAnnotation(args[0]);
    
        FossilAnnotationImportTO importTO = parser.transformFossilAnnotation(fossilAnnotationBeans);
    
        logger.traceExit(importTO);
        logger.traceExit();
    }
    
    public FossilAnnotationImportTO transformFossilAnnotation(List<FossilAnnotationBean> fossilAnnotationBeans) {
    
        for (FossilAnnotationBean bean: fossilAnnotationBeans) {
            TaxonTO taxonTOs = taxonDAO.findByScientificName(bean.getScientificName());
            String taxonPath = taxonTOs.getPath();
    
            GeologicalAgeTO geologicalAgeTO = geologicalAgeDAO.findByLabelOrSynonym(bean.getGeologicalAge());
        }
        return null;
    }
    
    public List<FossilAnnotationBean> parseFossilAnnotation(String fileName) {
        logger.info("Start parsing of fossil annotation file " + fileName + "...");
    
        CsvPreference TSVCOMMENTED = new CsvPreference.Builder(CsvPreference.TAB_PREFERENCE).build();

        try (ICsvBeanReader fossilReader = new CsvBeanReader(new FileReader(fileName), TSVCOMMENTED)) {

            List<FossilAnnotationBean> annots = new ArrayList<>();
            final String[] header = fossilReader.getHeader(true);

            String[] attributeMapping = mapFossilAnnotationHeaderToAttributes(header);
            CellProcessor[] cellProcessorMapping = mapFossilAnnotationHeaderToCellProcessors(header);
            FossilAnnotationBean fossil;

            while((fossil = fossilReader.read(FossilAnnotationBean.class, attributeMapping, cellProcessorMapping)) != null) {
                annots.add(fossil);
            }
            if (annots.isEmpty()) {
                throw new IllegalArgumentException("The provided file " + fileName + " did not allow to retrieve any fossil");
            }
            logger.info("End parsing of fossil annotation file");
    
            return logger.traceExit(annots);

        } catch (SuperCsvException e) {
            //hide implementation details
            throw new IllegalArgumentException("The provided file " + fileName + " could not be properly parsed", e);
        } catch (IOException e) {
            throw new UncheckedIOException("Can not read file " + fileName, e);
        }
    }
    
    private CellProcessor[] mapFossilAnnotationHeaderToCellProcessors(String[] header) {
        CellProcessor[] processors = new CellProcessor[header.length];
        for (int i = 0; i < header.length; i++) {
            if (header[i] == null || header[i].equals(GENERAL_COMMENTS_COL_NAME)) {
                continue;
            }
            processors[i] = switch (header[i]) {
                case ORDER_COL_NAME, TAXON_COL_NAME, LOCATION_COL_NAME, EVIDENCE_CODE_COL_NAME, CONFIDENCE_COL_NAME
                        -> new StrNotNullOrEmpty(new Trim());
                case PUBLISHED_REFERENCE_TEXT_COL_NAME, PUBLISHED_REFERENCE_ACC_COL_NAME, MUSEUM_COLLECTION_COL_NAME, MUSEUM_ACCESSION_COL_NAME,
                        GEOLOGICAL_FORMATION_COL_NAME, GEOLOGICAL_AGE_COL_NAME, OBSERVED_MOULT_STAGES_COL_NAME,
                        // TODO check if we post-treat them?
                        MOULTING_SUTURE_LOCATION_COL_NAME,
                        JUVENILE_MOULTING_SUTURE_LOCATION_COL_NAME, JUVENILE_CEPHALIC_SUTURE_LOCATION_COL_NAME,
                        JUVENILE_RESULTING_NAMED_MOULTING_CONFIGURATIONS_COL_NAME, CEPHALIC_SUTURE_LOCATION_COL_NAME,
                        RESULTING_NAMED_MOULTING_CONFIGURATIONS_COL_NAME, FOSSIL_PRESERVATION_TYPE_COL_NAME,
                        ENVIRONMENT_COL_NAME, SEGMENT_ADDITION_MODE_COL_NAME,
                        POST_CEPHALIC_SUTURE_LOCATION_COL_NAME, JUVENILE_POST_CEPHALIC_SUTURE_LOCATION_COL_NAME,
                        OTHER_BEHAVIOURS_ASSOCIATED_WITH_MOULTING_COL_NAME, FOSSIL_EXUVIAE_QUALITY_COL_NAME,
                        SPECIMEN_TYPE_COL_NAME, UNIT_OF_MEASUREMENT, BODY_LENGTH_INCREASE_AVERAGE_PER_MOULT_COL_NAME
                        -> new ParseCustomOptional(new Trim());
                case SPECIMEN_COUNT_COL_NAME, SPECIMEN_COUNT_PER_MOULT_STAGE_COL_NAME,
                        OBSERVED_MOULT_STAGES_COUNT_COL_NAME, ESTIMATED_MOULT_STAGES_COUNT_COL_NAME,
                        BODY_SEGMENTS_COUNT_IN_ADULT_COL_NAME
                        -> new ParseCustomOptional(new ParseInt());
                case BODY_SEGMENTS_COUNT_PER_MOULT_STAGE_COL_NAME
                        -> new ParseCustomOptional(new ParseTwoInt());
                case BODY_LENGTH_AVERAGE_AT_EACH_MOULT_STAGE_COL_NAME
                        -> new ParseCustomOptional(new ParseBigDecimal());
                case LIFE_HISTORY_STYLE_COL_NAME
                        -> new ParseCustomOptional(new ParseStrIgnoringCase(
                        new IsElementOf(Arrays.asList(LifeHistoryStyle.values()))));
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
                case EXUVIAE_CONSUMPTION_COL_NAME
                        -> new ParseCustomOptional(new ParseStrIgnoringCase(
                        new IsElementOf(Arrays.asList(ExuviaeConsumption.values()))));
                case REABSORPTION_COL_NAME
                        -> new ParseCustomOptional(new ParseStrIgnoringCase(
                        new IsElementOf(Arrays.asList(Reabsorption.values()))));
                case ADULT_STAGE_MOULTING_COL_NAME// TODO yes/true?
                        -> new ParseCustomOptional(new ParseBool("Continued moulting", "no"));
                case JUVENILE_MOULTING_BEHAVIOURS_COL_NAME
                        -> new ParseCustomOptional(new ParseBool("same as in adults", "different to in adults"));
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
//            DOI/PMID/URL
//            Museum collection
//            Museum accession #
//            Location
//            Geological formation
//            Geological age
//            Fossil preservation type
//                    Environment
//            Number of specimens
//            Type of specimens of interest
//            Life history style
//            Adult stage moulting
//            Observed # total moult stages
//            Observed moult stages
//            Estimated # moult stages
//            Segment addition mode
//# body segments per moult stage
//# body segments in adult individuals
//            Average body length increase per moult
//            Average body length at each moult stage
//            Location of moulting suture
//            Cephalic suture location
//            Post-cephalic suture location
//            Resulting named moulting configurations
//            Direction of egress during moulting
//            Position exuviae found in
//            Moulting phase
//            Level of intraspecific variability in moulting mode
//            Juvenile moulting behaviours
//            Juvenile location of moulting suture
//            Juvenile Cephalic suture location
//            Juvenile Post-cephalic suture location
//            Juvenile Resulting named moulting configurations
//            Other behaviours associated with moulting
//            Consumption of exuviae
//            Reabsorption during moulting
//            General Comments
//            Quality of fossil exuviae
//            Evidence code
//            Confidence
            mapping[i] = switch (header[i]) {
                case ORDER_COL_NAME -> "order";
                case TAXON_COL_NAME -> "taxon";
                case PUBLISHED_REFERENCE_TEXT_COL_NAME -> "publishedReferenceText";
                case PUBLISHED_REFERENCE_ACC_COL_NAME -> "publishedReferenceAcc";
                case MUSEUM_COLLECTION_COL_NAME -> "museumCollection";
                case MUSEUM_ACCESSION_COL_NAME -> "museumAccession";
                case LOCATION_COL_NAME -> "location";
                case GEOLOGICAL_FORMATION_COL_NAME -> "geologicalFormation";
                case GEOLOGICAL_AGE_COL_NAME -> "geologicalAge";
                case FOSSIL_PRESERVATION_TYPE_COL_NAME -> "fossilPreservationType";
                case ENVIRONMENT_COL_NAME -> "environment";
                case SPECIMEN_COUNT_COL_NAME -> "specimenCount";
                case SPECIMEN_COUNT_PER_MOULT_STAGE_COL_NAME -> "specimenCountPerMoultStage";
                case SPECIMEN_TYPE_COL_NAME -> "specimenType";
                case LIFE_HISTORY_STYLE_COL_NAME -> "lifeHistoryStyle";
                case ADULT_STAGE_MOULTING_COL_NAME -> "adultStageMoulting";
                case OBSERVED_MOULT_STAGES_COUNT_COL_NAME -> "observedMoultStagesCount";
                case OBSERVED_MOULT_STAGES_COL_NAME -> "observedMoultStages";
                case ESTIMATED_MOULT_STAGES_COUNT_COL_NAME -> "estimatedMoultStagesCount";
                case SEGMENT_ADDITION_MODE_COL_NAME -> "segmentAdditionMode";
                case BODY_SEGMENTS_COUNT_PER_MOULT_STAGE_COL_NAME -> "bodySegmentsCountPerMoultStage";
                case BODY_SEGMENTS_COUNT_IN_ADULT_COL_NAME -> "bodySegmentsCountInAdult";
                case BODY_LENGTH_INCREASE_AVERAGE_PER_MOULT_COL_NAME -> "bodyLengthIncreaseAveragePerMoult";
                case BODY_LENGTH_AVERAGE_AT_EACH_MOULT_STAGE_COL_NAME -> "bodyLengthAverageAtEachMoultStage";
                case UNIT_OF_MEASUREMENT -> "unitOfMeasurement";
                case MOULTING_SUTURE_LOCATION_COL_NAME -> "moultingSutureLocation";
                case CEPHALIC_SUTURE_LOCATION_COL_NAME -> "cephalicSutureLocation";
                case POST_CEPHALIC_SUTURE_LOCATION_COL_NAME -> "postCephalicSutureLocation";
                case RESULTING_NAMED_MOULTING_CONFIGURATIONS_COL_NAME -> "resultingNamedMoultingConfigurations";
                case EGRESS_DIRECTION_DURING_MOULTING_COL_NAME -> "egressDirectionDuringMoulting";
                case POSITION_EXUVIAE_FOUND_IN_COL_NAME -> "positionExuviaeFoundIn";
                case MOULTING_PHASE_COL_NAME -> "moultingPhase";
                case MOULTING_VARIABILITY_COL_NAME -> "moultingVariability";
                case JUVENILE_MOULTING_BEHAVIOURS_COL_NAME -> "juvenileMoultingBehaviours";
                case JUVENILE_MOULTING_SUTURE_LOCATION_COL_NAME -> "juvenileMoultingSutureLocation";
                case JUVENILE_CEPHALIC_SUTURE_LOCATION_COL_NAME -> "juvenileCephalicSutureLocation";
                case JUVENILE_POST_CEPHALIC_SUTURE_LOCATION_COL_NAME -> "juvenilePostCephalicSutureLocation";
                case JUVENILE_RESULTING_NAMED_MOULTING_CONFIGURATIONS_COL_NAME -> "juvenileResultingNamedMoultingConfigurations";
                case OTHER_BEHAVIOURS_ASSOCIATED_WITH_MOULTING_COL_NAME -> "otherBehavioursAssociatedWithMoulting";
                case EXUVIAE_CONSUMPTION_COL_NAME -> "exuviaeConsumption";
                case REABSORPTION_COL_NAME -> "reabsorption";
                case FOSSIL_EXUVIAE_QUALITY_COL_NAME -> "fossilExuviaeQuality";
                case EVIDENCE_CODE_COL_NAME -> "evidenceCode";
                case CONFIDENCE_COL_NAME -> "confidence";
                default -> throw new IllegalArgumentException("Unrecognized header: " + header[i] + " for FossilAnnotationBean");
            };
        }
        return mapping;
    }
    
//    public static class ParseObservedMoultStages extends CellProcessorAdaptor {
//
//        // TODO letters are not in requirements
//        private final String ONE_VALUE_PATTERN = "[A-Z0-9]+ \\(n=[0-9]+\\)";
//        public ParseObservedMoultStages() {
//            super();
//        }
//
//        public ParseObservedMoultStages(CellProcessor next) {
//            // this constructor allows other processors to be chained after this processor
//            super(next);
//        }
//
//        public Object execute(Object value, CsvContext context) {
//
//            // Throws an Exception if the input is null
//            validateInputNotNull(value, context);
//
//            // Try to match free number-only field, but consisting of several numbers comma-delimited
//            String s = String.valueOf(value);
//            s = s.replaceAll(" *; *", ";");
//            Matcher m = Pattern.compile("^" + ONE_VALUE_PATTERN + "(;" + ONE_VALUE_PATTERN + ")*$").matcher(s);
//            if (m.matches()) {
//                return next.execute(value, context);
//            }
//
//            throw new SuperCsvCellProcessorException(
//                    String.format("Could not parse '%s' as a ObservedMoultStages", value), context, this);
//        }
//    }
    
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
            if (StringUtils.isBlank(stringValue) || stringValue.equals("NA") || stringValue.equals("?")) {
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
            
            // Try to match free number field, but might need the ability to put two, such as '0/1'
            Matcher m = Pattern.compile("^[0-9]+(/[0-9]+)*$").matcher(String.valueOf(value));
            if (m.matches()) {
                return next.execute(value, context);
            }
            
            throw new SuperCsvCellProcessorException(
                    String.format("Could not parse '%s' as a TwoInt", value), context, this);
        }
    }
    
    public static class ParseIntRange extends CellProcessorAdaptor {
        
        public ParseIntRange() {
            super();
        }
        
        public ParseIntRange(CellProcessor next) {
            // this constructor allows other processors to be chained after this processor
            super(next);
        }
        
        public Object execute(Object value, CsvContext context) {
            
            // Throws an Exception if the input is null
            validateInputNotNull(value, context);
            
            // Try to match free number field, but might need the ability to put two, or a range
            Matcher m = Pattern.compile("^[0-9]+(-[0-9]+)*$").matcher(String.valueOf(value));
            if (m.matches()) {
                return next.execute(value, context);
            }
            
            throw new SuperCsvCellProcessorException(
                    String.format("Could not parse '%s' as a IntRange", value), context, this);
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
