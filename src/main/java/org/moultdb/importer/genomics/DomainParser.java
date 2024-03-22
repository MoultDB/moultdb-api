package org.moultdb.importer.genomics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
public class DomainParser {
    
    private final static Logger logger = LogManager.getLogger(DomainParser.class.getName());
    
    private final static CsvPreference TSV_COMMENTED = new CsvPreference.Builder(CsvPreference.TAB_PREFERENCE).build();
    
    private final static String PROTEIN_ID_COL_NAME = "Prot_IDs";
    private final static String DOMAIN_ID_COL_NAME = "IPR_ID";
    private final static String DOMAIN_START_COL_NAME = "Start";
    private final static String DOMAIN_END_COL_NAME = "End";
    private final static String DOMAIN_DESCRIPTION_COL_NAME = "description";
    
    public static void main(String[] args) {
        logger.traceEntry(Arrays.toString(args));
        
        if (args.length != 1) {
            throw new IllegalArgumentException("Incorrect number of arguments provided, expected 1 argument, " +
                    args.length + " provided.");
        }
        
        DomainParser parser = new DomainParser();
        Set<DomainBean> domainBeans = parser.parseDomainFile(args[0]);
//        parser.getDomainTOs(domainBeans);
        
        logger.traceExit();
    }
    
    public Set<DomainBean> parseDomainFile(String fileName) {
        logger.info("Start parsing of domain file " + fileName + "...");
        
        try (ICsvBeanReader domainReader = new CsvBeanReader(new FileReader(fileName), TSV_COMMENTED)) {
            
            logger.info("End parsing of domain file");
            
            return logger.traceExit(getDomainBeans(domainReader));
            
        } catch (SuperCsvException e) {
            throw new IllegalArgumentException("The provided file " + fileName + " could not be properly parsed", e);
        } catch (IOException e) {
            throw new UncheckedIOException("Can not read file " + fileName, e);
        }
    }
    
    private Set<DomainBean> getDomainBeans(ICsvBeanReader domainReader) throws IOException {
        Set<DomainBean> domainBeans = new HashSet<>();
        
        final String[] header = domainReader.getHeader(true);
        
        String[] attributeMapping = mapDomainHeaderToAttributes(header);
        CellProcessor[] cellProcessorMapping = mapDomainHeaderToCellProcessors(header);
        DomainBean domainBean;
        
        while((domainBean = domainReader.read(DomainBean.class, attributeMapping, cellProcessorMapping)) != null) {
            domainBeans.add(domainBean);
        }
        if (domainBeans.isEmpty()) {
            throw new IllegalArgumentException("The provided file did not allow to retrieve any domainBean");
        }
        
        return domainBeans;
    }
    
    private String[] mapDomainHeaderToAttributes(String[] header) {
        String[] mapping = new String[header.length];
        for (int i = 0; i < header.length; i++) {
            mapping[i] = switch (header[i]) {
                case PROTEIN_ID_COL_NAME -> "proteinId";
                case DOMAIN_ID_COL_NAME -> "domainId";
                case DOMAIN_START_COL_NAME -> "domainStart";
                case DOMAIN_END_COL_NAME -> "domainEnd";
                case DOMAIN_DESCRIPTION_COL_NAME -> "domainDescription";
                default -> throw new IllegalArgumentException("Unrecognized header: '" + header[i] + "' for DomainBean");
            };
        }
        return mapping;
    }
    
    private CellProcessor[] mapDomainHeaderToCellProcessors(String[] header) {
        CellProcessor[] processors = new CellProcessor[header.length];
        
        for (int i = 0; i < header.length; i++) {
            processors[i] = switch (header[i]) {
                case PROTEIN_ID_COL_NAME -> new StrNotNullOrEmpty(new Trim());
                case DOMAIN_ID_COL_NAME -> new StrNotNullOrEmpty(new Trim());
                case DOMAIN_START_COL_NAME -> new ParseInt();
                case DOMAIN_END_COL_NAME -> new ParseInt();
                case DOMAIN_DESCRIPTION_COL_NAME -> new StrNotNullOrEmpty(new Trim());
                default -> throw new IllegalArgumentException("Unrecognized header: '" + header[i] + "' for DomainBean");
            };
        }
        return processors;
    }
}
