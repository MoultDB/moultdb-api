package org.moultdb.importer.genomics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.repository.dao.GeneDAO;
import org.moultdb.api.repository.dto.GeneTO;
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
import java.util.stream.Collectors;

/**
 * @author Valentine Rech de Laval
 * @since 2024-03-21
 */
public class OrthogroupParser {
    
    
    @Autowired GeneDAO geneDAO;
    
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
                    args.length + " provided.");
        }
        
        OrthogroupParser parser = new OrthogroupParser();
        Set<OrthogroupBean> orthogroupBeans = new HashSet<>(parser.getOrthogroupBeans(args[1]));
        parser.getGeneToOrthogroup(orthogroupBeans);
        
        logger.traceExit();
    }
    
    public Map<String, Integer> getGeneToOrthogroup(Set<OrthogroupBean> orthogroupBeans) {
        return getGeneToOrthogroup(orthogroupBeans, geneDAO);
    }
    
    public Map<String, Integer> getGeneToOrthogroup(MultipartFile orthogroupFile, GeneDAO geneDAO) {
        Set<OrthogroupBean> orthogroupBeans = getOrthogroupBeans(orthogroupFile);
        return getGeneToOrthogroup(orthogroupBeans, geneDAO);
    }
    
    private Map<String, Integer> getGeneToOrthogroup(Set<OrthogroupBean> orthogroupBeans, GeneDAO geneDAO) {
        Map<String, Integer> proteinIdToOrthogroup = orthogroupBeans.stream()
                .collect(Collectors.toMap(OrthogroupBean::getProteinId, OrthogroupBean::getOrthogroupId));
        
        List<GeneTO> geneTOs = geneDAO.findByProteinIds(proteinIdToOrthogroup.keySet());
        
        Map<String, Integer> geneToOrthogroup = new HashMap<>();
        for (GeneTO geneTO: geneTOs) {
            geneToOrthogroup.put(geneTO.getGeneId(), proteinIdToOrthogroup.get(geneTO.getProteinId()));
        }
        return geneToOrthogroup;
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
}
