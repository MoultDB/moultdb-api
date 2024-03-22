package org.moultdb.importer.genomics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.Trim;
import org.supercsv.cellprocessor.constraint.StrNotNullOrEmpty;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2024-03-22
 */
public class PathwayParser {
    
    private final static Logger logger = LogManager.getLogger(PathwayParser.class.getName());
    
    private final static CsvPreference TSV_COMMENTED = new CsvPreference.Builder(CsvPreference.TAB_PREFERENCE).build();
    
    private final static String ORTHOGROUP_ID_COL_NAME = "cluster_id";
    private final static String PROTEIN_ID_COL_NAME = "protein_id";
    private final static String TAXID_COL_NAME = "taxid";
    private final static String GENE_NAME_COL_NAME = "gene_name";
    private final static String FUNCTION_COL_NAME = "known_function";
    private final static String GENOME_ID_COL_NAME = "genome_id";
    private final static String PATHWAY_ID_COL_NAME = "identifier";
    private final static String REFERENCE_COL_NAME = "reference";
    private final static String PATHWAY_DESCRIPTION_COL_NAME = "description";
    
    public static void main(String[] args) {
        logger.traceEntry(Arrays.toString(args));
        
        if (args.length != 1) {
            throw new IllegalArgumentException("Incorrect number of arguments provided, expected 1 argument, " +
                    args.length + " provided.");
        }
        
        PathwayParser parser = new PathwayParser();
        Set<PathwayBean> pathwayBeans = parser.parsePathwayFile(args[0]);
//        parser.getPathwayTOs(pathwayBeans);
        
        logger.traceExit();
    }
    
    public Set<PathwayBean> parsePathwayFile(String fileName) {
        logger.info("Start parsing of gene file " + fileName + "...");
        
        try (ICsvBeanReader pathwayReader = new CsvBeanReader(new FileReader(fileName), TSV_COMMENTED)) {
            
            logger.info("End parsing of gene file");
            
            return logger.traceExit(getPathwayBeans(pathwayReader));
            
        } catch (SuperCsvException e) {
            throw new IllegalArgumentException("The provided file " + fileName + " could not be properly parsed", e);
        } catch (IOException e) {
            throw new UncheckedIOException("Can not read file " + fileName, e);
        }
    }
    
    private Set<PathwayBean> getPathwayBeans(ICsvBeanReader pathwayReader) throws IOException {
        Set<PathwayBean> pathwayBeans = new HashSet<>();
        
        final String[] header = pathwayReader.getHeader(true);
        
        String[] attributeMapping = mapPathwayHeaderToAttributes(header);
        CellProcessor[] cellProcessorMapping = mapPathwayHeaderToCellProcessors(header);
        PathwayBean pathwayBean;
        
        while((pathwayBean = pathwayReader.read(PathwayBean.class, attributeMapping, cellProcessorMapping)) != null) {
            pathwayBeans.add(pathwayBean);
        }
        if (pathwayBeans.isEmpty()) {
            throw new IllegalArgumentException("The provided file did not allow to retrieve any pathwayBean");
        }
        
        return pathwayBeans;
    }
    
    private String[] mapPathwayHeaderToAttributes(String[] header) {
        String[] mapping = new String[header.length];
        for (int i = 0; i < header.length; i++) {
            mapping[i] = switch (header[i]) {
                case ORTHOGROUP_ID_COL_NAME -> "orthogroupId";
                case PROTEIN_ID_COL_NAME -> "proteinId";
                case TAXID_COL_NAME -> "taxonId";
                case GENE_NAME_COL_NAME -> "geneName";
                case FUNCTION_COL_NAME -> "function";
                case GENOME_ID_COL_NAME -> "genomeAcc";
                case PATHWAY_ID_COL_NAME -> "id";
                case REFERENCE_COL_NAME -> "reference";
                case PATHWAY_DESCRIPTION_COL_NAME -> "description";
                default -> throw new IllegalArgumentException("Unrecognized header: '" + header[i] + "' for PathwayBean");
            };
        }
        return mapping;
    }
    
    private CellProcessor[] mapPathwayHeaderToCellProcessors(String[] header) {
        CellProcessor[] processors = new CellProcessor[header.length];
        
        for (int i = 0; i < header.length; i++) {
            processors[i] = switch (header[i]) {
                case ORTHOGROUP_ID_COL_NAME -> new ParseInt();
                case PROTEIN_ID_COL_NAME -> new StrNotNullOrEmpty(new Trim());
                case TAXID_COL_NAME -> new ParseInt();
                case GENE_NAME_COL_NAME -> new StrNotNullOrEmpty(new Trim());
                case FUNCTION_COL_NAME -> new StrNotNullOrEmpty(new Trim());
                case GENOME_ID_COL_NAME -> new StrNotNullOrEmpty(new Trim());
                case PATHWAY_ID_COL_NAME -> new StrNotNullOrEmpty(new Trim());
                case REFERENCE_COL_NAME -> new StrNotNullOrEmpty(new Trim());
                case PATHWAY_DESCRIPTION_COL_NAME -> new Optional(new Trim());
                default -> throw new IllegalArgumentException("Unrecognized header: '" + header[i] + "' for PathwayBean");
            };
        }
        return processors;
    }
}
