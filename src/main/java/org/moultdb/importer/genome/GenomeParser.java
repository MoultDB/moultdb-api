package org.moultdb.importer.genome;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.repository.dao.DbXrefDAO;
import org.moultdb.api.repository.dao.TaxonDAO;
import org.moultdb.api.repository.dto.GenomeTO;
import org.moultdb.api.repository.dto.TaxonTO;
import org.moultdb.api.model.moutldbenum.DatasourceEnum;
import org.moultdb.importer.taxon.TaxonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.cellprocessor.*;
import org.supercsv.cellprocessor.constraint.StrNotNullOrEmpty;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.CsvContext;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Valentine Rech de Laval
 * @since 2021-12-13
 */
public class GenomeParser {
    
    @Autowired TaxonDAO taxonDAO;
    @Autowired DbXrefDAO dbXrefDAO;
    
    private final static Logger logger = LogManager.getLogger(TaxonParser.class.getName());
    
    private final static CsvPreference CSV_COMMENTED = new CsvPreference.Builder(CsvPreference.STANDARD_PREFERENCE).build();
    
    private final static String GENBANK_ACC_COL_NAME = "GenBank";
    private final static String SUBMISSION_DATE_COL_NAME = "Submission Date";
    private final static String TAXID_COL_NAME = "TaxId";
    private final static String SUBPHYLUM_COL_NAME = "SubPhylum";
    private final static String ORDER_COL_NAME = "Order";
    private final static String GENUS_COL_NAME = "Genus";
    private final static String SPECIES_COL_NAME = "Species";
    private final static String LENGTH_COL_NAME = "Length";
    private final static String SCAFFOLDS_COL_NAME = "Scaffolds";
    private final static String SCAFFOLD_L50_COL_NAME = "Scaffold L50";
    private final static String SCAFFOLD_N50_COL_NAME = "Scaffold N50";
    private final static String ANNOTATION_DATE_COL_NAME = "Annotation Date";
    private final static String TOTAL_GENES_COL_NAME = "Total genes";
    private final static String ARTHROPODA_COMPLETE_COL_NAME = "Arthropoda Complete";
    private final static String ARTHROPODA_SINGLE_COL_NAME = "Arthropoda Single";
    private final static String ARTHROPODA_DUPLICATED_COL_NAME = "Arthropoda Duplicated";
    private final static String ARTHROPODA_FRAGMENTED_COL_NAME = "Arthropoda Fragmented";
    private final static String ARTHROPODA_MISSING_COL_NAME = "Arthropoda Missing";
    
    public static void main(String[] args) {
        logger.traceEntry(Arrays.toString(args));
        
        if (args.length != 1) {
            throw new IllegalArgumentException("Incorrect number of arguments provided, expected 1 argument, " +
                    args.length + " provided.");
        }
        
        GenomeParser parser = new GenomeParser();
        Set<GenomeBean> genomeBeans = parser.parseGenomeFile(args[0]);
        parser.getGenomeTOs(genomeBeans);
        
        logger.traceExit();
    }
    
    public Set<GenomeBean> parseGenomeFile(String fileName) {
        logger.info("Start parsing of genome file " + fileName + "...");
        
        try (ICsvBeanReader genomeReader = new CsvBeanReader(new FileReader(fileName), CSV_COMMENTED)) {
            
            logger.info("End parsing of genome file");
            
            return logger.traceExit(getGenomeBeans(genomeReader));
            
        } catch (SuperCsvException e) {
            throw new IllegalArgumentException("The provided file " + fileName + " could not be properly parsed", e);
        } catch (IOException e) {
            throw new UncheckedIOException("Can not read file " + fileName, e);
        }
    }
    
    public Set<GenomeTO> getGenomeTOs(MultipartFile uploadedFile, TaxonDAO taxonDAO) {
        Set<GenomeBean> genomeBeans = getGenomeBeans(uploadedFile);
        return getGenomeTOs(genomeBeans, taxonDAO);
    }
    
    public Set<GenomeTO> getGenomeTOs(Set<GenomeBean> genomeBeans) {
        return getGenomeTOs(genomeBeans, taxonDAO);
    }
    
    public Set<GenomeTO> getGenomeTOs(Set<GenomeBean> genomeBeans, TaxonDAO taxonDAO) {

        Map<String, List<GenomeBean>> multipleGenomes = genomeBeans.stream().collect(Collectors.groupingBy(GenomeBean::getTaxonId));
        if (!multipleGenomes.isEmpty()) {
            throw new IllegalArgumentException(multipleGenomes.entrySet().stream()
                    .filter(e -> e.getValue().size() > 1)
                    .map(e-> e.getKey() + " " + e.getValue().stream().map(GenomeBean::getGenbankAcc).toList())
                    .collect(Collectors.joining(" ; ")));
        }
        
        Set<GenomeTO> genomeTOs = new HashSet<>();
        Set<String> notFoundTaxa = new HashSet<>();
        Set<String> subspecies = new HashSet<>();
        for (GenomeBean bean: genomeBeans) {
            TaxonTO taxonTO = taxonDAO.findByAccession(bean.getTaxonId(), DatasourceEnum.NCBI.getStringRepresentation());
            if (taxonTO == null) {
                notFoundTaxa.add(bean.getTaxonId() + " - " + bean.getSpecies());
                continue;
            } else {
                if (!taxonTO.getScientificName().equals(bean.getSpecies())) {
                    if (!bean.getSpecies().startsWith(taxonTO.getScientificName())) {
                        subspecies.add(bean.getSpecies() + " - " +taxonTO.getScientificName());
                    } else {
                        throw new IllegalArgumentException("Provided species name [" + bean.getSpecies()
                                + "] is different from the taxon in the database: " + taxonTO);
                    }
                }
            }
            
            genomeTOs.add(new GenomeTO(bean.getGenbankAcc(), taxonTO, convertToLocalDate(bean.getSubmissionDate()),
                    bean.getLength(), bean.getScaffolds(), bean.getScaffoldL50(), bean.getScaffoldN50(),
                    convertToLocalDate(bean.getAnnotationDate()), bean.getTotalGenes(),
                    convertDoubleToFloat(bean.getCompleteBusco()), convertDoubleToFloat(bean.getSingleBusco()),
                    convertDoubleToFloat(bean.getDuplicatedBusco()), convertDoubleToFloat(bean.getFragmentedBusco()),
                    convertDoubleToFloat(bean.getMissingBusco())));
        }
        logger.trace("Found " + subspecies.size() + " subspecies: " + String.join(" ; ", subspecies));
        
        if (!notFoundTaxa.isEmpty()) {
            throw new IllegalArgumentException(notFoundTaxa.size() + " taxon IDs  not found in database: " +
                    String.join(" ; ", notFoundTaxa));
        }
        return genomeTOs;
    }
    
    private static Float convertDoubleToFloat(Double aDouble) {
        return (aDouble != null) ? aDouble.floatValue() : null;
    }
    
    private static LocalDate convertToLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
    
    public Set<GenomeBean> getGenomeBeans(MultipartFile uploadedFile) {
        try (ICsvBeanReader genomeReader = new CsvBeanReader(new InputStreamReader(uploadedFile.getInputStream()), CSV_COMMENTED)) {
            return logger.traceExit(getGenomeBeans(genomeReader));
        } catch (SuperCsvException e) {
            throw new IllegalArgumentException("The provided file " + uploadedFile.getOriginalFilename()
                    + " could not be properly parsed", e);
        } catch (IOException e) {
            throw new UncheckedIOException("Can not read file " + uploadedFile.getOriginalFilename(), e);
        }
    }
    
    private Set<GenomeBean> getGenomeBeans(ICsvBeanReader genomeReader) throws IOException {
        Set<GenomeBean> genomeBeans = new HashSet<>();
        
        final String[] header = genomeReader.getHeader(true);
        
        String[] attributeMapping = mapGenomeHeaderToAttributes(header);
        CellProcessor[] cellProcessorMapping = mapGenomeHeaderToCellProcessors(header);
        GenomeBean genomeBean;
        
        while((genomeBean = genomeReader.read(GenomeBean.class, attributeMapping, cellProcessorMapping)) != null) {
            genomeBeans.add(genomeBean);
        }
        if (genomeBeans.isEmpty()) {
            throw new IllegalArgumentException("The provided file did not allow to retrieve any genomeBean");
        }
        
        return genomeBeans;
    }
    
    private CellProcessor[] mapGenomeHeaderToCellProcessors(String[] header) {
        CellProcessor[] processors = new CellProcessor[header.length];
        String dateFormat = "yyyy-MM-dd";
        
        for (int i = 0; i < header.length; i++) {
            processors[i] = switch (header[i]) {
                case GENBANK_ACC_COL_NAME -> new StrNotNullOrEmpty(new Trim());
                case TAXID_COL_NAME -> new StrNotNullOrEmpty(new Trim());
                case SUBMISSION_DATE_COL_NAME -> new ParseDate(dateFormat);
                case SUBPHYLUM_COL_NAME -> new StrNotNullOrEmpty(new Trim());
                case ORDER_COL_NAME -> new StrNotNullOrEmpty(new Trim());
                case GENUS_COL_NAME -> new StrNotNullOrEmpty(new Trim());
                case SPECIES_COL_NAME -> new StrNotNullOrEmpty(new Trim());
                case LENGTH_COL_NAME -> new StrReplace(",", "", new ParseLong());
                case SCAFFOLDS_COL_NAME -> new StrReplace(",", "", new ParseInt());
                case SCAFFOLD_L50_COL_NAME -> new StrReplace(",", "", new ParseInt());
                case SCAFFOLD_N50_COL_NAME -> new StrReplace(",", "", new ParseInt());
                case ANNOTATION_DATE_COL_NAME -> new ParseCustomOptional(new ParseDate(dateFormat));
                case TOTAL_GENES_COL_NAME -> new ParseCustomOptional(new StrReplace(",", "", new ParseInt()));
                case ARTHROPODA_COMPLETE_COL_NAME -> new ParseCustomOptional(new ParseDouble());
                case ARTHROPODA_SINGLE_COL_NAME -> new ParseCustomOptional(new ParseDouble());
                case ARTHROPODA_DUPLICATED_COL_NAME -> new ParseCustomOptional(new ParseDouble());
                case ARTHROPODA_FRAGMENTED_COL_NAME -> new ParseCustomOptional(new ParseDouble());
                case ARTHROPODA_MISSING_COL_NAME -> new ParseCustomOptional(new ParseDouble());
                default -> throw new IllegalArgumentException("Unrecognized header: " + header[i] + " for GenomeBean");
            };
        }
        return processors;
    }
    
    private String[] mapGenomeHeaderToAttributes(String[] header) {
        String[] mapping = new String[header.length];
        for (int i = 0; i < header.length; i++) {
            mapping[i] = switch (header[i]) {
                case GENBANK_ACC_COL_NAME -> "genbankAcc";
                case TAXID_COL_NAME -> "taxonId";
                case SUBMISSION_DATE_COL_NAME -> "submissionDate";
                case SUBPHYLUM_COL_NAME -> "subphylum";
                case ORDER_COL_NAME -> "order";
                case GENUS_COL_NAME -> "genus";
                case SPECIES_COL_NAME -> "species";
                case LENGTH_COL_NAME -> "length";
                case SCAFFOLDS_COL_NAME -> "scaffolds";
                case SCAFFOLD_L50_COL_NAME -> "scaffoldL50";
                case SCAFFOLD_N50_COL_NAME -> "scaffoldN50";
                case ANNOTATION_DATE_COL_NAME -> "annotationDate";
                case TOTAL_GENES_COL_NAME -> "totalGenes";
                case ARTHROPODA_COMPLETE_COL_NAME -> "completeBusco";
                case ARTHROPODA_SINGLE_COL_NAME -> "singleBusco";
                case ARTHROPODA_DUPLICATED_COL_NAME -> "duplicatedBusco";
                case ARTHROPODA_FRAGMENTED_COL_NAME -> "fragmentedBusco";
                case ARTHROPODA_MISSING_COL_NAME -> "missingBusco";
                default -> throw new IllegalArgumentException("Unrecognized header: " + header[i] + " for GenomeBean");
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
            if (value == null || stringValue.equals("NA")) {
                return null;
            }
            return next.execute(value, context);
        }
    }
}
