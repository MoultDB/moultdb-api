package org.moultdb.api.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.exception.MoultDBException;
import org.moultdb.api.model.Taxon;
import org.moultdb.api.model.moutldbenum.DatasourceEnum;
import org.moultdb.api.repository.dao.DataSourceDAO;
import org.moultdb.api.repository.dao.DbXrefDAO;
import org.moultdb.api.repository.dao.TaxonDAO;
import org.moultdb.api.repository.dto.DataSourceTO;
import org.moultdb.api.repository.dto.DbXrefTO;
import org.moultdb.api.repository.dto.TaxonTO;
import org.moultdb.api.service.ServiceUtils;
import org.moultdb.api.service.TaxonService;
import org.moultdb.importer.taxon.TaxonBean;
import org.moultdb.importer.taxon.TaxonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaxonServiceImpl implements TaxonService {
    
    private final static Logger logger = LogManager.getLogger(TaxonServiceImpl.class.getName());
    private final static int TAXON_SUBSET_SIZE = 30000;
    
    @Autowired
    DataSourceDAO dataSourceDAO;
    @Autowired
    private TaxonDAO taxonDAO;
    @Autowired
    private DbXrefDAO dbXrefDAO;
    
    @Override
    public List<Taxon> getAllTaxa() {
        return getTaxons(taxonDAO.findAll());
    }
    
    @Override
    public List<Taxon> getTaxonByText(String searchedText) {
        return getTaxons(taxonDAO.findByText(searchedText));
    }
    
    @Override
    public Taxon getTaxonByScientificName(String scientificName) {
        TaxonTO taxonTO = taxonDAO.findByScientificName(scientificName);
        if (taxonTO != null) {
            return getTaxons(Collections.singletonList(taxonTO)).get(0);
        }
        return null;
    }
    
    @Override
    public Taxon getTaxonByDbXref(String datasource, String accession) {
        DatasourceEnum datasourceEnum = DatasourceEnum.valueOfByStringRepresentation(datasource);
        TaxonTO taxonTO = taxonDAO.findByAccession(accession, datasourceEnum.getStringRepresentation());
        if (taxonTO != null) {
            return getTaxons(Collections.singletonList(taxonTO)).get(0);
        }
        return null;
    }
    
    @Override
    public List<Taxon> getTaxonLineage(String taxonPath) {
        return getTaxons(taxonDAO.findLineageByPath(taxonPath));
    }
    
    @Override
    public List<Taxon> getTaxonChildren(String taxonPath) {
        return getTaxons(taxonDAO.findChildrenByPath(taxonPath));
    }
    
    private List<Taxon> getTaxons(List<TaxonTO> taxonTOs) {
        return taxonTOs.stream()
                       .map(ServiceUtils::mapFromTO)
                       .collect(Collectors.toList());
    }
    
    @Override
    public void insertTaxon(Taxon taxon) {
        taxonDAO.insert(convertToDto(taxon));
    }
    
    List<String> listDuplicateUsingFilterAndSetAdd(List<String> list) {
        Set<String> elements = new HashSet<>();
        return list.stream()
                .filter(n -> !elements.add(n))
                .collect(Collectors.toList());
    }
    
    @Override
    public Integer insertTaxa(MultipartFile file) {
        logger.info("Start taxon import...");
        long startImportTimePoint = System.currentTimeMillis();
        
        TaxonParser parser = new TaxonParser();
        
        logger.info("# Start reading taxon file " + file.getOriginalFilename() + "...");
        Set<TaxonBean> taxonBeans = parser.getTaxonBeans(file);
        logger.info("# End reading taxon file");
        
        List<String> ncbiIds = taxonBeans.stream()
                .map(TaxonBean::getNcbiId)
                .filter(Objects::nonNull)
                .toList();
        List<String> gbifIds = taxonBeans.stream()
                .map(TaxonBean::getGbifId)
                .filter(Objects::nonNull)
                .toList();
        List<String> syno = taxonBeans.stream()
                .filter(b -> b.getSynonymGbifIds() != null)
                .map(b -> List.of(b.getSynonymGbifIds().split(", ")))
                .flatMap(List::stream).toList();
        List<String> inatIds = taxonBeans.stream()
                .map(TaxonBean::getInatId)
                .filter(Objects::nonNull)
                .toList();

        boolean hasDuplicatedIDs = false;
        HashSet<String> uniqNcbiIds = new HashSet<>(ncbiIds);
        if (ncbiIds.size() != uniqNcbiIds.size()) {
            hasDuplicatedIDs = true;
            logger.error("NCBI IDs are not uniq: " + listDuplicateUsingFilterAndSetAdd(ncbiIds));
        }
        HashSet<String> uniqGbifIds = new HashSet<>(gbifIds);
        if (gbifIds.size() != uniqGbifIds.size()) {
            hasDuplicatedIDs = true;
            logger.error("GBIF IDs are not uniq: " + listDuplicateUsingFilterAndSetAdd(gbifIds));
        }
        HashSet<String> uniqSyno = new HashSet<>(syno);
        if (syno.size() != uniqSyno.size()) {
            hasDuplicatedIDs = true;
            logger.error("GBIF synonym IDs are not uniq: " + listDuplicateUsingFilterAndSetAdd(syno));
        }
        HashSet<String> uniq = new HashSet<>(uniqGbifIds);
        uniq.addAll(uniqSyno);
        if ((uniqGbifIds.size() + uniqSyno.size()) != uniq.size()) {
            hasDuplicatedIDs = true;
            List<String> list = new ArrayList<>(uniqGbifIds);
            list.addAll(uniqSyno);
            logger.error("All GBIF IDs (main + synonyms) are not uniq: " +
                    listDuplicateUsingFilterAndSetAdd(list));
        }
        HashSet<String> uniqInatIds = new HashSet<>(inatIds);
        if (inatIds.size() != uniqInatIds.size()) {
            hasDuplicatedIDs = true;
            logger.error("INaturalist IDs are not uniq: " + listDuplicateUsingFilterAndSetAdd(inatIds));
        }
        if (hasDuplicatedIDs) {
            throw new MoultDBException("There are duplicates in the input file");
        }
        
        // Divide set in several sets to avoid memory errors
        Set<Set<TaxonBean>> taxonBeanSubsets = splitBeans(taxonBeans);
        int sum = 0;
        
        int idx = 1;
        for (Set<TaxonBean> taxonBeanSubset : taxonBeanSubsets) {
            logger.info("# Start subset taxon import " + idx + "/" + taxonBeanSubsets.size() + "...");
            logger.debug("# Start parsing taxon beans...");
            long startTimePoint1 = System.currentTimeMillis();
            Set<TaxonTO> taxonTOs = parser.getTaxonTOs(taxonBeanSubset, taxonDAO, dataSourceDAO, dbXrefDAO);
            long endTimePoint = System.currentTimeMillis();
            logger.debug("# End parsing taxon beans. " + getExecutionTime(startTimePoint1, endTimePoint));
            
            logger.debug("# Start of taxon insertion...");
            long startTimePoint2 = System.currentTimeMillis();
            sum += taxonDAO.batchUpdate(taxonTOs);
            endTimePoint = System.currentTimeMillis();
            logger.debug("# End of taxon insertion. " + getExecutionTime(startTimePoint2, endTimePoint));
            logger.info("# End of subset taxon import. " + getExecutionTime(startTimePoint1, endTimePoint));
            idx++;
        }
        long endImportTimePoint = System.currentTimeMillis();
        
        logger.info("End taxon import." + getExecutionTime(startImportTimePoint, endImportTimePoint));
        
        return sum;
    }
    
    private static Set<Set<TaxonBean>> splitBeans(Set<TaxonBean> originalSet) {
        
        Set<Set<TaxonBean>> allSubsets = new HashSet<>();
        Set<TaxonBean> currentSubset = new HashSet<>();
        
        for (TaxonBean element : originalSet) {
            currentSubset.add(element);
            
            if (currentSubset.size() == TAXON_SUBSET_SIZE) {
                allSubsets.add(currentSubset);
                currentSubset = new HashSet<>(); // Reinitialize current subset
            }
        }
        
        // Add last subset if not complete
        if (!currentSubset.isEmpty()) {
            allSubsets.add(currentSubset);
        }
        return allSubsets;
    }
    
    private static String getExecutionTime(long startPoint, long endPoint) {
        long executionTimeInMs = endPoint - startPoint;

        long seconds = (executionTimeInMs / 1000) % 60;
        long minutes = (executionTimeInMs / (1000 * 60)) % 60;
        long hours = (executionTimeInMs / (1000 * 60 * 60));
        
        return "Execution time: " + hours + " hours, " + minutes + " minutes, " + seconds + " seconds.";
    }
    
    private TaxonTO convertToDto(Taxon taxon) {
        Set<DbXrefTO> dbXrefTOs = taxon.getDbXrefs().stream()
                                       .map(xref -> {
                                           DataSourceTO dataSourceTO = dataSourceDAO.findByName(xref.dataSource().name());
                                           return new DbXrefTO(null, xref.accession(), xref.name(), dataSourceTO);
                                       })
                                       .collect(Collectors.toSet());
        
        return new TaxonTO(null, taxon.getScientificName(), taxon.getCommonName(), taxon.isExtinct(), dbXrefTOs);
    }
}
