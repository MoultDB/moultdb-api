package org.moultdb.importer.genomics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.supercsv.cellprocessor.*;
import org.supercsv.cellprocessor.constraint.IsElementOf;
import org.supercsv.cellprocessor.constraint.StrNotNullOrEmpty;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.CsvContext;

import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Valentine Rech de Laval
 * @since 2024-03-21
 */
public class GeneParser {
    
    private final static Logger logger = LogManager.getLogger(GeneParser.class.getName());
    
    private final static CsvPreference TSV_COMMENTED = new CsvPreference.Builder(CsvPreference.TAB_PREFERENCE).build();
    
    private final static String PROTEIN_ID_COL_NAME = "protein_id";
    private final static String PROTEIN_LENGTH_COL_NAME = "prot_len";
    private final static String TRANSCRIPT_ID_COL_NAME = "transcript_id";
    private final static String TRANSCRIPT_URL_SUFFIX_COL_NAME = "transcript_url_suffix";
    private final static String GENE_ID_COL_NAME = "gene_id";
    private final static String GENE_NAME_COL_NAME = "gene_name";
    private final static String LOCUS_TAG_COL_NAME = "locus_tag";
    private final static String PROTEIN_DESCRIPTION_COL_NAME = "protein_description";
    private final static String ORIGIN_COL_NAME = "origin";
    private final static String GENOME_ID_COL_NAME = "genome_id";
    
    private final static String ORIGIN_REFSEQ = "NCBI RefSeq assembly";
    private final static String ORIGIN_GENBANK = "Submitted GenBank assembly";
    
    public static void main(String[] args) {
        logger.traceEntry(Arrays.toString(args));
        
        if (args.length != 1) {
            throw new IllegalArgumentException("Incorrect number of arguments provided, expected 1 argument, " +
                    args.length + " provided.");
        }
        
        GeneParser parser = new GeneParser();
        Set<GeneBean> geneBeans = parser.parseGeneFile(args[0]);
//        parser.getGeneTOs(geneBeans);
        
        logger.traceExit();
    }
    
    public Set<GeneBean> parseGeneFile(String fileName) {
        logger.info("Start parsing of gene file " + fileName + "...");
        
        try (ICsvBeanReader geneReader = new CsvBeanReader(new FileReader(fileName), TSV_COMMENTED)) {
            
            logger.info("End parsing of gene file");
            
            return logger.traceExit(getGeneBeans(geneReader));
            
        } catch (SuperCsvException e) {
            throw new IllegalArgumentException("The provided file " + fileName + " could not be properly parsed", e);
        } catch (IOException e) {
            throw new UncheckedIOException("Can not read file " + fileName, e);
        }
    }
    
    private Set<GeneBean> getGeneBeans(ICsvBeanReader geneReader) throws IOException {
        Set<GeneBean> geneBeans = new HashSet<>();
        
        final String[] header = geneReader.getHeader(true);
        
        String[] attributeMapping = mapGeneHeaderToAttributes(header);
        CellProcessor[] cellProcessorMapping = mapGeneHeaderToCellProcessors(header);
        GeneBean geneBean;
        
        while((geneBean = geneReader.read(GeneBean.class, attributeMapping, cellProcessorMapping)) != null) {
            geneBeans.add(geneBean);
        }
        if (geneBeans.isEmpty()) {
            throw new IllegalArgumentException("The provided file did not allow to retrieve any genomeBean");
        }
        
        return geneBeans;
    }
    
    private String[] mapGeneHeaderToAttributes(String[] header) {
        String[] mapping = new String[header.length];
        for (int i = 0; i < header.length; i++) {
            mapping[i] = switch (header[i]) {
                case PROTEIN_ID_COL_NAME -> "proteinId";
                case PROTEIN_LENGTH_COL_NAME -> "proteinLength";
                case TRANSCRIPT_ID_COL_NAME -> "transcriptId";
                case TRANSCRIPT_URL_SUFFIX_COL_NAME -> "transcriptUrlSuffix";
                case GENE_ID_COL_NAME -> "geneId";
                case GENE_NAME_COL_NAME -> "geneName";
                case LOCUS_TAG_COL_NAME -> "locusTag";
                case PROTEIN_DESCRIPTION_COL_NAME -> "proteinDescription";
                case ORIGIN_COL_NAME -> "origin";
                case GENOME_ID_COL_NAME -> "genomeAcc";
                default -> throw new IllegalArgumentException("Unrecognized header: '" + header[i] + "' for GeneBean");
            };
        }
        return mapping;
    }
    
    private CellProcessor[] mapGeneHeaderToCellProcessors(String[] header) {
        CellProcessor[] processors = new CellProcessor[header.length];
        
        for (int i = 0; i < header.length; i++) {
            processors[i] = switch (header[i]) {
                case PROTEIN_ID_COL_NAME -> new StrNotNullOrEmpty(new Trim());
                case PROTEIN_LENGTH_COL_NAME -> new ParseInt();
                case TRANSCRIPT_ID_COL_NAME -> new ParseCustomOptional(new Trim());
                case TRANSCRIPT_URL_SUFFIX_COL_NAME -> new StrNotNullOrEmpty(new Trim());
                case GENE_ID_COL_NAME -> new ParseCustomOptional(new Trim());
                case GENE_NAME_COL_NAME -> new ParseCustomOptional(new Trim());
                case LOCUS_TAG_COL_NAME -> new ParseCustomOptional(new Trim());
                case PROTEIN_DESCRIPTION_COL_NAME -> new StrNotNullOrEmpty(new Trim());
                case ORIGIN_COL_NAME -> new IsElementOf(Stream.of(ORIGIN_REFSEQ, ORIGIN_GENBANK)
                        .collect(Collectors.toSet()));
                case GENOME_ID_COL_NAME -> new StrNotNullOrEmpty(new Trim());
                default -> throw new IllegalArgumentException("Unrecognized header: '" + header[i] + "' for GenomeBean");
            };
        }
        return processors;
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
            if (value == null || stringValue.equals("NA") || stringValue.equals("NaN")) {
                return null;
            }
            
            return next.execute(value, context);
        }
    }
}
