package org.moultdb.importer.genomics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.repository.dao.DomainDAO;
import org.moultdb.api.repository.dao.GeneDAO;
import org.moultdb.api.repository.dto.DomainTO;
import org.moultdb.api.repository.dto.GeneTO;
import org.moultdb.api.repository.dto.GeneToDomainTO;
import org.springframework.web.multipart.MultipartFile;
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
                    args.length + " provided");
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
    
    public Set<GeneToDomainTO> getGeneToDomainTOs(MultipartFile file, GeneDAO geneDAO) {
        Set<DomainBean> domainBeans = getDomainBeans(file);
        return getGeneToDomainTOs(domainBeans, geneDAO);
    }
    
    private Set<GeneToDomainTO> getGeneToDomainTOs(Set<DomainBean> domainBeans, GeneDAO geneDAO) {
        Map<String, List<DomainBean>> proteinToDomainBean = domainBeans.stream()
                .collect(Collectors.groupingBy(DomainBean::getProteinId));
        Map<String, GeneTO> proteinToGeneTO = geneDAO.findByProteinIds(proteinToDomainBean.keySet()).stream()
                .collect(Collectors.toMap(GeneTO::getProteinId, Function.identity()));
        if (proteinToDomainBean.keySet().size() != proteinToGeneTO.keySet().size()) {
            Set<String> proteinIds = new HashSet<>(proteinToDomainBean.keySet());
            proteinIds.removeAll(proteinToGeneTO.keySet());
            logger.warn("Unknown protein ID(s): " + proteinIds);
            // We continue without unknown proteins
            proteinToDomainBean.keySet().removeAll(proteinIds);
        }
        
        Set<GeneToDomainTO> geneToDomainTOs = new HashSet<>();
        for (Map.Entry<String, List<DomainBean>> entry : proteinToDomainBean.entrySet()) {
            GeneTO geneTO = proteinToGeneTO.get(entry.getKey());
            assert geneTO != null;
            Map<String, List<DomainBean>> domainToBean = entry.getValue().stream()
                    .collect(Collectors.groupingBy(DomainBean::getDomainId));
            
            for (Map.Entry<String, List<DomainBean>> cluster : domainToBean.entrySet()) {
                Integer start = cluster.getValue().stream()
                        .map(DomainBean::getDomainStart)
                        .min(Integer::compareTo)
                        .orElseThrow((() -> new IllegalStateException("Missing start for domain: " + cluster.getKey())));
                Integer end = cluster.getValue().stream()
                        .map(DomainBean::getDomainEnd)
                        .max(Integer::compareTo)
                        .orElseThrow((() -> new IllegalStateException("Missing end for domain: " + cluster.getKey())));
                geneToDomainTOs.add(new GeneToDomainTO(geneTO.getId(),
                        new DomainTO(cluster.getKey(), cluster.getValue().get(0).getDomainDescription()),
                        start, end));
            }
        }
        return geneToDomainTOs;
    }
    
    private Set<DomainBean> getDomainBeans(MultipartFile domainFile) {
        try (ICsvBeanReader geneToPathwayReader = new CsvBeanReader(new InputStreamReader(domainFile.getInputStream()), TSV_COMMENTED)) {
            return logger.traceExit(getDomainBeans(geneToPathwayReader));
        } catch (SuperCsvException e) {
            throw new IllegalArgumentException("The provided file " + domainFile.getOriginalFilename()
                    + " could not be properly parsed", e);
        } catch (IOException e) {
            throw new UncheckedIOException("Can not read file " + domainFile.getOriginalFilename(), e);
        }
    }
    
    public Set<DomainTO> getDomainTOs(MultipartFile file) {
        Set<DomainBean> domainBeans = new HashSet<>();
        try (ICsvBeanReader geneToPathwayReader = new CsvBeanReader(new InputStreamReader(file.getInputStream()), TSV_COMMENTED)) {
            domainBeans.addAll(getDomainBeans(geneToPathwayReader));
        } catch (SuperCsvException e) {
            throw new IllegalArgumentException("The provided file " + file.getOriginalFilename()
                    + " could not be properly parsed", e);
        } catch (IOException e) {
            throw new UncheckedIOException("Can not read file " + file.getOriginalFilename(), e);
        }
        
        // Here we keep only one (random) DomainBean by domain ID 
        Map<String, DomainBean> proteinToGeneTO = domainBeans.stream()
                .collect(Collectors.toMap(DomainBean::getDomainId, Function.identity(),
                        (existingValue, newValue) -> existingValue));
        
        Set<DomainTO> domainTOs = new HashSet<>();
        for (DomainBean domainBean: proteinToGeneTO.values()) {
            domainTOs.add(new DomainTO(domainBean.getDomainId(), domainBean.getDomainDescription()));
        }
        return domainTOs;
    }
}
