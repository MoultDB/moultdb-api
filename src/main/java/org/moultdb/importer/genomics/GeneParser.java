package org.moultdb.importer.genomics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.repository.dao.DataSourceDAO;
import org.moultdb.api.repository.dao.GeneDAO;
import org.moultdb.api.repository.dao.GenomeDAO;
import org.moultdb.api.repository.dto.DataSourceTO;
import org.moultdb.api.repository.dto.GeneTO;
import org.moultdb.api.repository.dto.GenomeTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.Trim;
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
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Valentine Rech de Laval
 * @since 2024-03-21
 */
public class GeneParser {
    
    @Autowired DataSourceDAO dataSourceDAO;
    @Autowired GeneDAO geneDAO;
    @Autowired GenomeDAO genomeDAO;
    
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
    
    private final static Map<String, String> originToDatasourceName;
    
    static {
        Map<String, String> originToDatasourceNameMap = new HashMap<>();
        originToDatasourceNameMap.put("NCBI RefSeq assembly", "RefSeq");
        originToDatasourceNameMap.put("Submitted GenBank assembly", "GenBank");
        
        originToDatasourceName = Collections.unmodifiableMap(originToDatasourceNameMap);
    }
    
    public static void main(String[] args) {
        logger.traceEntry(Arrays.toString(args));
        
        if (args.length != 1) {
            throw new IllegalArgumentException("Incorrect number of arguments provided, expected 1 argument, " +
                    args.length + " provided");
        }
        
        GeneParser parser = new GeneParser();
        Set<GeneBean> geneBeans = parser.getGeneBeans(args[0]);
        parser.getGeneTOs(geneBeans);
        
        logger.traceExit();
    }
    
    public Set<GeneTO> getGeneTOs(MultipartFile geneFile, boolean throwException,
                                  DataSourceDAO dataSourceDAO, GeneDAO geneDAO, GenomeDAO genomeDAO) {
        Set<GeneBean> geneBeans = getGeneBeans(geneFile);
        return getGeneTOs(geneBeans, throwException, dataSourceDAO, geneDAO, genomeDAO);
    }
    
    private Set<GeneTO> getGeneTOs(Set<GeneBean> geneBeans) {
        return getGeneTOs(geneBeans, false, dataSourceDAO, geneDAO, genomeDAO);
    }
    
    private Set<GeneTO> getGeneTOs(Set<GeneBean> geneBeans, boolean throwException,
                                   DataSourceDAO dataSourceDAO, GeneDAO geneDAO, GenomeDAO genomeDAO) {
        
        // Sanity checks
        Set<String> origins = geneBeans.stream().map(GeneBean::getOrigin).collect(Collectors.toSet());
        if (origins.size() != 1) {
            throw new IllegalArgumentException("One file should contains only one origin");
        }
        String origin = origins.iterator().next();
        DataSourceTO dataSourceTO = dataSourceDAO.findByName(originToDatasourceName.get(origin));
        if (dataSourceTO == null) {
            throw new IllegalArgumentException("Unknown origin '" + origin + "'");
        }
        
        Set<String> genomeAccs = geneBeans.stream().map(GeneBean::getGenomeAcc).collect(Collectors.toSet());
        if (genomeAccs.size() != 1) {
            throw new IllegalArgumentException("One file should contains only one genome accession");
        }
        String genomeAcc = genomeAccs.iterator().next();
        GenomeTO genomeTO = genomeDAO.findByGenbankAcc(genomeAcc);
        if (genomeTO == null) {
            logger.warn("Unknown genome accession '" + genomeAcc + "': no genes imported");
            return new HashSet<>();
        }
        
        List<GeneBean> beansWithMissingData = geneBeans.stream().filter(b -> b.getGeneId() == null && b.getLocusTag() == null).toList();
        filterErrors(geneBeans, beansWithMissingData, Function.identity(), throwException,
                "All genes should have a gene ID and/or a locus tag: " + beansWithMissingData);
        
        Set<String> duplicatedGeneIds = getDuplicates(geneBeans, GeneBean::getGeneId);
        filterErrors(geneBeans, duplicatedGeneIds, GeneBean::getGeneId, throwException,
                "A gene ID must refer to a single protein only: " + duplicatedGeneIds);
        
        Set<String> duplicatedLocusTags = getDuplicates(geneBeans, GeneBean::getLocusTag);
        filterErrors(geneBeans, duplicatedLocusTags, GeneBean::getLocusTag, throwException,
                "A locus tag must refer to a single protein only: " + duplicatedLocusTags);
        
        // Generate GeneTOs
        Integer geneLastId = geneDAO.getLastId();
        Integer geneNextId = geneLastId == null ? 1 : geneLastId + 1;
        Set<GeneTO> geneTOs = new HashSet<>();
        for (GeneBean geneBean: geneBeans) {
            geneTOs.add(new GeneTO(geneNextId, geneBean.getGeneId(), geneBean.getGeneName(), geneBean.getLocusTag(),
                    genomeTO.getGeneBankAcc(), null, null, 
                    geneBean.getTranscriptId(), geneBean.getTranscriptUrlSuffix(), geneBean.getProteinId(),
                    geneBean.getProteinDescription(), geneBean.getProteinLength(), dataSourceTO, null, null));
            geneNextId++;
        }
        return geneTOs;
    }
    
    private static <T> Set<GeneBean> filterErrors(Set<GeneBean> geneBeans, Collection<T> dataToExclude,
                                                  Function<GeneBean, T> fun, boolean throwException, String message) {
        if (!dataToExclude.isEmpty()) {
            if (throwException) {
                throw new IllegalArgumentException(message);
            } else {
                logger.error(message);
            }
        }
        return geneBeans.stream().filter(b -> !dataToExclude.contains(fun.apply(b))).collect(Collectors.toSet());
    }
    
    private static Set<String> getDuplicates(Set<GeneBean> geneBeans, Function<GeneBean, String> func) {
        return geneBeans.stream()
                .filter(b -> func.apply(b) != null)
                .collect(Collectors.groupingBy(func)).entrySet().stream()
                .filter(e -> e.getValue().size() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }
    
    private Set<GeneBean> getGeneBeans(MultipartFile geneFile) {
        try (ICsvBeanReader geneReader = new CsvBeanReader(new InputStreamReader(geneFile.getInputStream()), TSV_COMMENTED)) {
            return logger.traceExit(getGeneBeans(geneReader));
        } catch (SuperCsvException e) {
            throw new IllegalArgumentException("The provided file " + geneFile.getOriginalFilename()
                    + " could not be properly parsed", e);
        } catch (IOException e) {
            throw new UncheckedIOException("Can not read file " + geneFile.getOriginalFilename(), e);
        }
    }
    
    public Set<GeneBean> getGeneBeans(String fileName) {
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
            throw new IllegalArgumentException("The provided file did not allow to retrieve any geneBean");
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
                default -> throw new IllegalArgumentException("Unrecognized header: '" + header[i] + "' for GeneBean");
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
