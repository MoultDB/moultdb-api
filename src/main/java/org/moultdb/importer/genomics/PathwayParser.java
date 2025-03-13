package org.moultdb.importer.genomics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.model.moutldbenum.DatasourceEnum;
import org.moultdb.api.model.moutldbenum.MoutldbEnum;
import org.moultdb.api.repository.dao.ArticleDAO;
import org.moultdb.api.repository.dao.GeneDAO;
import org.moultdb.api.repository.dao.PathwayDAO;
import org.moultdb.api.repository.dto.*;
import org.moultdb.importer.taxonannotation.TaxonAnnotationParser;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.Trim;
import org.supercsv.cellprocessor.constraint.StrNotNullOrEmpty;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Valentine Rech de Laval
 * @since 2024-03-22
 */
public class PathwayParser {
    
    private final static Logger logger = LogManager.getLogger(PathwayParser.class.getName());
    
    private final static CsvPreference TSV_COMMENTED = new CsvPreference.Builder(CsvPreference.TAB_PREFERENCE).build();
    private final static String LIST_SEPARATOR = ",";
    
    private final static String ORTHOGROUP_ID_COL_NAME = "cluster_id";
    private final static String PROTEIN_ID_COL_NAME = "protein_id";
    private final static String TAXID_COL_NAME = "taxid";
    private final static String GENE_NAME_COL_NAME = "gene_name";
    private final static String FUNCTION_COL_NAME = "known_function";
    private final static String GENOME_ID_COL_NAME = "genome_id";
    private final static String PATHWAY_ID_COL_NAME = "identifier";
    private final static String PATHWAY_NAME_COL_NAME = "pathway";
    private final static String REFERENCE_COL_NAME = "reference";
    private final static String DESCRIPTION_COL_NAME = "description";
    private final static String FIGURE_COL_NAME = "figures";
    
    public static void main(String[] args) {
        logger.traceEntry(Arrays.toString(args));
        
        if (args.length != 2) {
            throw new IllegalArgumentException("Incorrect number of arguments provided, expected 2 arguments, " +
                    args.length + " provided");
        }
        
        PathwayParser parser = new PathwayParser();
        Set<PathwayBean> pathwayBeans = parser.parsePathwayFile(args[0]);
        Set<PathwayCvBean> pathwayCvBeans = parser.parsePathwayCvFile(args[1]);
//        parser.getPathwayTOs(pathwayBeans);
        
        logger.traceExit();
    }
    
    public Set<PathwayBean> parsePathwayFile(String fileName) {
        logger.info("Start parsing of pathway file " + fileName + "...");
        
        try (ICsvBeanReader pathwayReader = new CsvBeanReader(new FileReader(fileName), TSV_COMMENTED)) {
            
            logger.info("End parsing of pathway file");
            
            return logger.traceExit(getPathwayBeans(pathwayReader));
            
        } catch (SuperCsvException e) {
            throw new IllegalArgumentException("The provided file " + fileName + " could not be properly parsed", e);
        } catch (IOException e) {
            throw new UncheckedIOException("Can not read file " + fileName, e);
        }
    }
    
    public Set<PathwayCvBean> parsePathwayCvFile(String fileName) {
        logger.info("Start parsing of pathway CV file " + fileName + "...");
        
        try (ICsvBeanReader pathwayCvReader = new CsvBeanReader(new FileReader(fileName), TSV_COMMENTED)) {
            
            logger.info("End parsing of pathway CV file");
            
            return logger.traceExit(getPathwayCvBeans(pathwayCvReader));
            
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
    
    private Set<PathwayCvBean> getPathwayCvBeans(ICsvBeanReader pathwayCvReader) throws IOException {
        Set<PathwayCvBean> pathwayCvBeans = new HashSet<>();
        
        final String[] header = pathwayCvReader.getHeader(true);
        
        String[] attributeMapping = mapPathwayCvHeaderToAttributes(header);
        CellProcessor[] cellProcessorMapping = mapPathwayCvHeaderToCellProcessors(header);
        PathwayCvBean pathwayCvBean;
        
        while((pathwayCvBean = pathwayCvReader.read(PathwayCvBean.class, attributeMapping, cellProcessorMapping)) != null) {
            pathwayCvBeans.add(pathwayCvBean);
        }
        if (pathwayCvBeans.isEmpty()) {
            throw new IllegalArgumentException("The provided file did not allow to retrieve any pathwayCvBean");
        }
        
        return pathwayCvBeans;
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
                case DESCRIPTION_COL_NAME -> "description";
                default -> throw new IllegalArgumentException("Unrecognized header: '" + header[i] + "' for PathwayBean");
            };
        }
        return mapping;
    }
    
    private String[] mapPathwayCvHeaderToAttributes(String[] header) {
        String[] mapping = new String[header.length];
        for (int i = 0; i < header.length; i++) {
            mapping[i] = switch (header[i]) {
                case PATHWAY_ID_COL_NAME -> "id";
                case PATHWAY_NAME_COL_NAME -> "name";
                case REFERENCE_COL_NAME -> "reference";
                case DESCRIPTION_COL_NAME -> "description";
                case FIGURE_COL_NAME -> "figureIds";
                default -> throw new IllegalArgumentException("Unrecognized header: '" + header[i] + "' for PathwayCvBean");
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
                case DESCRIPTION_COL_NAME -> new Optional(new Trim());
                default -> throw new IllegalArgumentException("Unrecognized header: '" + header[i] + "' for PathwayBean");
            };
        }
        return processors;
    }
    
    private CellProcessor[] mapPathwayCvHeaderToCellProcessors(String[] header) {
        CellProcessor[] processors = new CellProcessor[header.length];
        
        for (int i = 0; i < header.length; i++) {
            processors[i] = switch (header[i]) {
                case PATHWAY_ID_COL_NAME -> new StrNotNullOrEmpty(new Trim());
                case PATHWAY_NAME_COL_NAME -> new StrNotNullOrEmpty(new Trim());
                case REFERENCE_COL_NAME -> new StrNotNullOrEmpty(new Trim());
                case DESCRIPTION_COL_NAME -> new Optional(new Trim());
                case FIGURE_COL_NAME -> new Optional(new ParseIntSet(LIST_SEPARATOR));
                default -> throw new IllegalArgumentException("Unrecognized header: '" + header[i] + "' for PathwayCvBean");
            };
        }
        return processors;
    }
    
    public static class ParseIntSet extends CellProcessorAdaptor implements StringCellProcessor {
        
        private final String separator;
        
        public <T extends Enum<T> & MoutldbEnum> ParseIntSet(final String separator) {
            super();
            this.separator = separator;
        }
        
        public <T extends Enum<T> & MoutldbEnum> ParseIntSet(final String separator, final CellProcessor next) {
            // this constructor allows other processors to be chained after this processor
            super(next);
            this.separator = separator;
        }
        
        public Object execute(Object value, CsvContext context) {
            
            // Throws an Exception if the input is null
            validateInputNotNull(value, context);
            
            return Arrays.stream(String.valueOf(value).split(this.separator))
                    .map(s -> Integer.valueOf(s.trim()))
                    .collect(Collectors.toSet());
        }
    }
    
    public Set<PathwayTO> getPathwayTOs(MultipartFile pathwayCvFile, ArticleDAO articleDAO) {
        Set<PathwayCvBean> pathwayCvBeans = getPathwayCvBeans(pathwayCvFile);
        return getPathwayTOs(pathwayCvBeans, articleDAO);
    }
    
    private Set<PathwayCvBean> getPathwayCvBeans(MultipartFile pathwayCvFile) {
        try (ICsvBeanReader pathwayReader = new CsvBeanReader(new InputStreamReader(pathwayCvFile.getInputStream()), TSV_COMMENTED)) {
            return logger.traceExit(getPathwayCvBeans(pathwayReader));
        } catch (SuperCsvException e) {
            throw new IllegalArgumentException("The provided file " + pathwayCvFile.getOriginalFilename()
                    + " could not be properly parsed", e);
        } catch (IOException e) {
            throw new UncheckedIOException("Can not read file " + pathwayCvFile.getOriginalFilename(), e);
        }
    }
    
    private Set<PathwayTO> getPathwayTOs(Set<PathwayCvBean> pathwayCvBeans, ArticleDAO articleDAO) {
        Set<PathwayTO> pathwayTOs = new HashSet<>();
        for (PathwayCvBean pathwayCvBean: pathwayCvBeans) {
            // FIXME replace 2 with the dataSourceId found in db
            ArticleTO articleTO = articleDAO.findByDbXref(pathwayCvBean.getReference(), 2);
            pathwayTOs.add(new PathwayTO(pathwayCvBean.getId(), pathwayCvBean.getName(),
                    pathwayCvBean.getDescription(), articleTO, pathwayCvBean.getFigureIds()));
        }
        return pathwayTOs;
    }
    
    public Set<GeneTO> getUpdatedGeneTOs(MultipartFile geneToPathwayFile, GeneDAO geneDAO, PathwayDAO pathwayDAO) {
        Set<PathwayBean> pathwayBeans = getPathwayBeans(geneToPathwayFile);
        return getGeneTOs(pathwayBeans, geneDAO, pathwayDAO);
    }
    
    protected Set<PathwayBean> getPathwayBeans(MultipartFile geneToPathwayFile) {
        try (ICsvBeanReader geneToPathwayReader = new CsvBeanReader(new InputStreamReader(geneToPathwayFile.getInputStream()), TSV_COMMENTED)) {
            return logger.traceExit(getPathwayBeans(geneToPathwayReader));
        } catch (SuperCsvException e) {
            throw new IllegalArgumentException("The provided file " + geneToPathwayFile.getOriginalFilename()
                    + " could not be properly parsed", e);
        } catch (IOException e) {
            throw new UncheckedIOException("Can not read file " + geneToPathwayFile.getOriginalFilename(), e);
        }
    }
    
    private Set<GeneTO> getGeneTOs(Set<PathwayBean> pathwayBeans, GeneDAO geneDAO, PathwayDAO pathwayDAO) {
        Map<String, PathwayBean> proteinToPathwayBean = pathwayBeans.stream()
                .collect(Collectors.toMap(PathwayBean::getProteinId, Function.identity()));
        
        Map<String, GeneTO> proteinToGeneTO = geneDAO.findByProteinIds(proteinToPathwayBean.keySet()).stream()
                .collect(Collectors.toMap(GeneTO::getProteinId, Function.identity()));
        if (proteinToPathwayBean.keySet().size() != proteinToGeneTO.keySet().size()) {
            Set<String> proteinIds = new HashSet<>(proteinToPathwayBean.keySet());
            proteinIds.removeAll(proteinToGeneTO.keySet());
            logger.warn("Unknown protein ID(s): " + proteinIds);
            // We continue without unknown proteins
            proteinToPathwayBean.keySet().removeAll(proteinIds);
        }
        Set<String> pathwaysIds = proteinToPathwayBean.values().stream().map(PathwayBean::getId).collect(Collectors.toSet());
        
        Map<String, PathwayTO> pathwayTOMap = pathwayDAO.findByIds(pathwaysIds).stream()
                .collect(Collectors.toMap(PathwayTO::getId, Function.identity()));
        if (pathwaysIds.size() != pathwayTOMap.keySet().size()) {
            pathwaysIds.removeAll(pathwayTOMap.keySet());
            throw new IllegalArgumentException("Unknown pathway(s): " + pathwaysIds);
        }
        
        Set<GeneTO> geneTOs = new HashSet<>();
        for (PathwayBean pathwayBean: proteinToPathwayBean.values()) {
            GeneTO geneTO = proteinToGeneTO.get(pathwayBean.getProteinId());
            PathwayTO pathwayTO = pathwayTOMap.get(pathwayBean.getId());
            assert geneTO != null && pathwayTO != null;
            geneTOs.add(new GeneTO(geneTO.getId(), geneTO.getGeneId(), geneTO.getGeneName(), geneTO.getLocusTag(),
                    geneTO.getGenomeAcc(), null, geneTO.getTranscriptId(),
                    geneTO.getTranscriptUrlSuffix(), geneTO.getProteinId(), geneTO.getProteinDescription(),
                    geneTO.getProteinLength(), geneTO.getDataSourceTO(), pathwayTO, geneTO.getOrthogroupTO()));
        }
        logger.debug("Number of genes to be updated: " + geneTOs.size());
        return geneTOs;
    }
}
