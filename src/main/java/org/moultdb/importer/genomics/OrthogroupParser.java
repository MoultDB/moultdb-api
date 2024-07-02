package org.moultdb.importer.genomics;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.repository.dao.GeneDAO;
import org.moultdb.api.repository.dao.OrthogroupDAO;
import org.moultdb.api.repository.dto.GeneTO;
import org.moultdb.api.repository.dto.OrthogroupTO;
import org.moultdb.api.repository.dto.TaxonTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.cellprocessor.ParseDate;
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
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Valentine Rech de Laval
 * @since 2024-03-21
 */
public class OrthogroupParser {
    
    
    @Autowired GeneDAO geneDAO;
    
    @Autowired OrthogroupDAO orthogroupDAO;
    
    private final static Logger logger = LogManager.getLogger(OrthogroupParser.class.getName());
    
    private final static CsvPreference TSV_COMMENTED = new CsvPreference.Builder(CsvPreference.TAB_PREFERENCE).build();
    
    private final static String PROTEIN_ID_COL_NAME = "protein_id";
    private final static String ORTHOGROUP_ID_COL_NAME = "orthogroup_id";
    private final static String TAX_ID_COL_NAME = "taxid";
    private final static String VERSION_COL_NAME = "version";
    
    public static void main(String[] args) {
        logger.traceEntry(Arrays.toString(args));
        
        if (args.length != 1) {
            throw new IllegalArgumentException("Incorrect number of arguments provided, expected 1 argument, " +
                    args.length + " provided");
        }
        
        OrthogroupParser parser = new OrthogroupParser();
        Set<OrthogroupBean> orthogroupBeans = new HashSet<>(parser.getOrthogroupBeans(args[1]));
        parser.updatedGenes(orthogroupBeans);
        
        logger.traceExit();
    }
    
    public Set<GeneTO> updatedGenes(Set<OrthogroupBean> orthogroupBeans) {
        return updatedGenes(orthogroupBeans, geneDAO, orthogroupDAO);
    }
    
    public Set<GeneTO> updatedGenes(MultipartFile orthogroupFile, GeneDAO geneDAO, OrthogroupDAO orthogroupDAO) {
        Set<OrthogroupBean> orthogroupBeans = getOrthogroupBeans(orthogroupFile);
        logger.debug("orthogroup bean count: " + orthogroupBeans.size());
        return updatedGenes(orthogroupBeans, geneDAO, orthogroupDAO);
    }
    
    private Set<GeneTO> updatedGenes(Set<OrthogroupBean> orthogroupBeans, GeneDAO geneDAO, OrthogroupDAO orthogroupDAO) {
        
        Set<Integer> orthogroupIds = orthogroupBeans.stream()
                .map(OrthogroupBean::getOrthogroupId)
                .collect(Collectors.toSet());
        logger.debug("orthogroup count: " + orthogroupIds.size());
        
        Map<Integer, OrthogroupTO> dbOrthogroupTOs = orthogroupDAO.findByIds(orthogroupIds).stream()
                .collect(Collectors.toMap(OrthogroupTO::getId, Function.identity()));
        logger.debug("db orthogroup count: " + dbOrthogroupTOs.keySet().size());
        
        Map<String, OrthogroupTO> proteinIdToOrthogroupId = orthogroupBeans.stream()
                .collect(Collectors.toMap(OrthogroupBean::getProteinId, o -> dbOrthogroupTOs.get(o.getOrthogroupId())));
        logger.debug("protein count: " + proteinIdToOrthogroupId.keySet().size());
        
        List<GeneTO> dbGeneTOs = geneDAO.findByProteinIds(proteinIdToOrthogroupId.keySet());
        logger.debug("gene count: " + dbGeneTOs.size());
        
        Set<GeneTO> geneTOs = new HashSet<>();
        for (GeneTO geneTO: dbGeneTOs) {
            geneTOs.add(new GeneTO(geneTO.getId(), geneTO.getGeneId(), geneTO.getGeneName(), geneTO.getLocusTag(),
                    geneTO.getGenomeAcc(), null,
                    geneTO.getTranscriptId(), geneTO.getTranscriptUrlSuffix(), geneTO.getProteinId(),
                    geneTO.getProteinDescription(), geneTO.getProteinLength(), geneTO.getDataSourceTO(),
                    geneTO.getPathwayTO(), proteinIdToOrthogroupId.get(geneTO.getProteinId())));
        }
        return geneTOs;
    }
    
    private Set<OrthogroupBean> getOrthogroupBeans(MultipartFile orthogroupFile) {
        try (ICsvBeanReader orthogroupBeans = new CsvBeanReader(new InputStreamReader(orthogroupFile.getInputStream()), TSV_COMMENTED)) {
            return logger.traceExit(getOrthogroupBeans(orthogroupBeans));
        } catch (SuperCsvException e) {
            throw new IllegalArgumentException("The provided file " + orthogroupFile.getOriginalFilename()
                    + " could not be properly parsed", e);
        } catch (IOException e) {
            throw new UncheckedIOException("Can not read file " + orthogroupFile.getOriginalFilename(), e);
        }
    }
    
    public Set<OrthogroupBean> getOrthogroupBeans(String fileName) {
        logger.info("Start parsing of orthogroup file " + fileName + "...");
        
        try (ICsvBeanReader geneReader = new CsvBeanReader(new FileReader(fileName), TSV_COMMENTED)) {
            
            logger.info("End parsing of gene file");
            
            return logger.traceExit(getOrthogroupBeans(geneReader));
            
        } catch (SuperCsvException e) {
            throw new IllegalArgumentException("The provided file " + fileName + " could not be properly parsed", e);
        } catch (IOException e) {
            throw new UncheckedIOException("Can not read file " + fileName, e);
        }
    }
    
    private Set<OrthogroupBean> getOrthogroupBeans(ICsvBeanReader orthogroupReader) throws IOException {
        Set<OrthogroupBean> orthogroupBeans = new HashSet<>();
        
        final String[] header = orthogroupReader.getHeader(true);
        
        String[] attributeMapping = mapOrthogroupHeaderToAttributes(header);
        CellProcessor[] cellProcessorMapping = mapOrthogroupHeaderToCellProcessors(header);
        OrthogroupBean orthogroupBean;
        
        while((orthogroupBean = orthogroupReader.read(OrthogroupBean.class, attributeMapping, cellProcessorMapping)) != null) {
            orthogroupBeans.add(orthogroupBean);
        }
        if (orthogroupBeans.isEmpty()) {
            throw new IllegalArgumentException("The provided file did not allow to retrieve any orthogroupBean");
        }
        
        return orthogroupBeans;
    }
    
    private String[] mapOrthogroupHeaderToAttributes(String[] header) {
        String[] mapping = new String[header.length];
        for (int i = 0; i < header.length; i++) {
            mapping[i] = switch (header[i]) {
                case ORTHOGROUP_ID_COL_NAME -> "orthogroupId";
                case TAX_ID_COL_NAME -> "taxonId";
                case PROTEIN_ID_COL_NAME -> "proteinId";
                case VERSION_COL_NAME -> "version";
                default -> throw new IllegalArgumentException("Unrecognized header: '" + header[i] + "' for OrthogroupBean");
            };
        }
        return mapping;
    }
    
    private CellProcessor[] mapOrthogroupHeaderToCellProcessors(String[] header) {
        CellProcessor[] processors = new CellProcessor[header.length];
        String dateFormat = "yyyy-MM-dd";
        
        for (int i = 0; i < header.length; i++) {
            processors[i] = switch (header[i]) {
                case ORTHOGROUP_ID_COL_NAME -> new ParseInt();
                case TAX_ID_COL_NAME -> new ParseInt();
                case PROTEIN_ID_COL_NAME -> new StrNotNullOrEmpty(new Trim());
                case VERSION_COL_NAME -> new ParseDate(dateFormat);
                default -> throw new IllegalArgumentException("Unrecognized header: '" + header[i] + "' for OrthogroupBean");
            };
        }
        return processors;
    }
    
    public Set<OrthogroupTO> getOrthogroups(MultipartFile orthogroupFile, MultipartFile pathwayFile) {
        Set<PathwayBean> pathwayBeans = new PathwayParser().getPathwayBeans(pathwayFile);
        Map<Integer, String> moultingOrthogroups = pathwayBeans.stream()
                .collect(Collectors.toMap(PathwayBean::getOrthogroupId, PathwayBean::getGeneName, (v1, v2) -> v1));
        return (getOrthogroupBeans(orthogroupFile).stream()
                .map(b -> new OrthogroupTO(b.getOrthogroupId(), moultingOrthogroups.get(b.getOrthogroupId())))
                .collect(Collectors.toSet()));
    }
}