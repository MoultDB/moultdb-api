package org.moultdb.api.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.model.TaxonStatistics;
import org.moultdb.api.repository.dao.*;
import org.moultdb.api.repository.dto.*;
import org.moultdb.api.service.ServiceUtils;
import org.moultdb.api.service.TaxonStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.moultdb.api.service.ServiceUtils.getExecutionTime;
import static org.moultdb.api.service.ServiceUtils.mapFromTO;

/**
 * @author Valentine Rech de Laval
 * @since 2025-10-13
 */
@Service
public class TaxonStatisticsServiceImpl implements TaxonStatisticsService {
    
    private final static Logger logger = LogManager.getLogger(TaxonStatisticsServiceImpl.class.getName());
    private static final int UNKNOWN_OG_COUNT = -1;
    
    @Autowired GenomeDAO genomeDAO;
    @Autowired OrthogroupDAO orthogroupDAO;
    @Autowired TaxonDAO taxonDAO;
    @Autowired TaxonAnnotationDAO taxonAnnotationDAO;
    @Autowired TaxonStatisticsDAO taxonStatisticsDAO;
    
    @Override
    public List<TaxonStatistics> getAllStats() {
        return List.of();
    }
    
    @Override
    public TaxonStatistics getTaxonStatsByPath(String path) {
        return mapFromTO(taxonStatisticsDAO.findByPath(path));
    }
    
    @Override
    public Map<String, TaxonStatistics> getTaxonStatsByPathWithChildren(String path) {
        return taxonStatisticsDAO.findByPathWithChildren(path).stream()
                .map(ServiceUtils::mapFromTO)
                .collect(Collectors.toMap(TaxonStatistics::getTaxonPath, Function.identity()));
    }
    
    @Override
    public Integer updateTaxonStatistics() {
        
        logger.info("Start computation of taxon statistics...");
        long startComputationTimePoint = System.currentTimeMillis();
        
        logger.debug("# Retrieving genomes...");
        List<GenomeTO> genomeTOs = genomeDAO.findAll();
        
        logger.debug("# Retrieving taxon annotations...");
        List<TaxonAnnotationTO> taxonAnnotTOs = taxonAnnotationDAO.findAll();
        
        logger.debug("# Retrieving parent taxa...");
        Set<String> taxonPathsWithChildren = new HashSet<>(taxonDAO.findAllPathsHavingChildren());
        
        logger.debug("# Retrieving all taxon paths...");
        Set<String> taxonPaths = new HashSet<>(taxonDAO.findAllPaths());
        
        logger.debug("# Retrieving and processing taxa...");
        AtomicInteger totalUpdateCount = new AtomicInteger(0); // thread-safe if needed

        taxonDAO.processAllInBatches(TaxonServiceImpl.TAXON_SUBSET_SIZE, taxonTOs -> {
            Set<TaxonStatisticsTO> taxonStatsTOs = new HashSet<>();
            taxonTOs.forEach(taxonTO -> {
                String prefix = taxonTO.getPath() + ".";
                
                int depth = taxonTO.getPath().split("\\.").length;
                
                boolean isLeaf = !taxonPathsWithChildren.contains(taxonTO.getPath());
                
                // Find all descendants
                Set<String> descendantPaths = taxonPaths.stream()
                        .filter(path2 -> path2.startsWith(prefix))
                        .collect(Collectors.toSet());
                
                // Among all descendants, count leaves to get species number
                int speciesCount = (int) descendantPaths.stream()
                        .filter(descendantPath -> !taxonPathsWithChildren.contains(descendantPath))
                        .count();
                
                int genomeCount = (int) genomeTOs.stream()
                        .filter(g -> g.getTaxonTO().getPath().equals(taxonTO.getPath())
                                || g.getTaxonTO().getPath().startsWith(prefix))
                        .count();
                
                int annotationCount = (int) taxonAnnotTOs.stream()
                        .filter(ta -> ta.getTaxonTO().getPath().equals(taxonTO.getPath()) || ta.getTaxonTO().getPath().startsWith(prefix))
                        .count();
                
                int ogCount = UNKNOWN_OG_COUNT; 
                if (genomeCount == 1) {
                    List<OrthogroupTO> moultingOrthogroupsByTaxon = orthogroupDAO.findMoultingOrthogroupsByTaxon(taxonTO.getPath());
                    ogCount = moultingOrthogroupsByTaxon.size();
                }
                
                taxonStatsTOs.add(new TaxonStatisticsTO(taxonTO.getPath(), depth, isLeaf, speciesCount, genomeCount,
                        annotationCount, ogCount, null));
            });
            
            int updateCount = taxonStatisticsDAO.batchUpdate(taxonStatsTOs);
            totalUpdateCount.addAndGet(updateCount);
        });
        
        logger.debug("# Updating orthogroup range...");
        Set<TaxonStatisticsTO> updatedTaxonStatsTOs = new HashSet<>();
        List<TaxonStatisticsTO> all = taxonStatisticsDAO.findAll();
        all.forEach(tsTO -> {
            String prefix = tsTO.getPath() + ".";
            
            Set<Integer> ogCounts = all.stream()
                    .filter(tsTO2 -> tsTO2.getPath().equals(tsTO.getPath()) || tsTO2.getPath().startsWith(prefix))
                    .map(TaxonStatisticsTO::getOrthogroupCount)
                    .filter(ogCount -> ogCount != UNKNOWN_OG_COUNT)
                    .collect(Collectors.toSet());
            
            String range = "";
            if (!ogCounts.isEmpty()) {
                if (tsTO.isLeaf()) {
                    range = String.valueOf(tsTO.getOrthogroupCount());
                } else {
                    int min = Collections.min(ogCounts);
                    int max = Collections.max(ogCounts);
                    range = String.valueOf(min);
                    if (min != max) range = range + "-" + Collections.max(ogCounts);
                }
            }
            
            updatedTaxonStatsTOs.add(new TaxonStatisticsTO(tsTO.getPath(), tsTO.getDepth(), tsTO.isLeaf(),
                    tsTO.getSpeciesCount(), tsTO.getGenomeCount(), tsTO.getTaxonAnnotationCount(),
                    tsTO.getOrthogroupCount(), range));
        });
        int updateCount2 = taxonStatisticsDAO.batchUpdate(updatedTaxonStatsTOs);
        
        assert updateCount2 == totalUpdateCount.get();
        
        
        logger.info("Total updated taxon statistics entries: {}", totalUpdateCount.get());
        logger.info("End computation of taxon statistics - {}", getExecutionTime(startComputationTimePoint, System.currentTimeMillis()));
        
        return totalUpdateCount.get();
    }
}


