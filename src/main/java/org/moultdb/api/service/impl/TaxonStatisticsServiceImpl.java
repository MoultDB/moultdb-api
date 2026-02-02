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
import static org.moultdb.api.service.impl.TaxonServiceImpl.SPECIES_TAG;
import static org.moultdb.api.service.impl.TaxonServiceImpl.TAXON_SUBSET_SIZE;

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
        return taxonStatisticsDAO.findByPathWithDirectChildren(path).stream()
                .map(ServiceUtils::mapFromTO)
                .collect(Collectors.toMap(TaxonStatistics::getTaxonPath, Function.identity()));
    }
    
    @Override
    public Map<String, TaxonStatistics> getTaxonStatsByPaths(Set<String> paths) {
        return taxonStatisticsDAO.findByPaths(paths).stream()
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
        
        logger.debug("# Calculating main taxon statistics...");
        AtomicInteger totalUpdateCount = new AtomicInteger(0); // thread-safe if needed
        
        taxonDAO.processAllInBatches(TAXON_SUBSET_SIZE, taxonTOs -> {
            Set<TaxonStatisticsTO> taxonStatsTOs = new HashSet<>();
            taxonTOs.forEach(taxonTO -> {

                TaxonStatisticsTO taxonStatisticsTO = getTaxonStatisticsTO(taxonTO, taxonPathsWithChildren, genomeTOs, taxonAnnotTOs);
                taxonStatsTOs.add(taxonStatisticsTO);
            });

            int updateCount = taxonStatisticsDAO.batchUpdate(taxonStatsTOs);
            totalUpdateCount.addAndGet(updateCount);
            logger.debug("## Count of taxon statistics: {}", totalUpdateCount.get());
        });

//        TODO Optimize orthogroup range computation to avoid out of memory exception
//        logger.debug("# Calculating orthogroup ranges...");
//        Set<TaxonStatisticsTO> updatedTaxonStatsTOs = new HashSet<>();
//        Set<TaxonStatisticsTO> allStats = new HashSet<>(taxonStatisticsDAO.findAll());
//        Set<Set<TaxonStatisticsTO>> statsSubsets = ServiceUtils.splitSet(allStats, TAXON_SUBSET_SIZE);
//        
//        int idx = 1;
//        int rangeCount = 0;
//        for (Set<TaxonStatisticsTO> subset : statsSubsets) {
//            logger.debug("## Calculating orthogroup ranges of subset {}/{}...", idx, statsSubsets.size());
//            
//            subset.forEach(tsTO -> {
//                TaxonStatisticsTO taxonStatisticsTO = getUpdatedTaxonStatisticsTO(tsTO);
//                updatedTaxonStatsTOs.add(taxonStatisticsTO);
//            });
//            rangeCount += taxonStatisticsDAO.batchUpdate(updatedTaxonStatsTOs);
//            idx++;
//        }
//        
//        assert totalUpdateCount.get() == rangeCount;
//        logger.debug ("Range count: {}", rangeCount);
        
        logger.info("Total updated taxon statistics entries: {}", totalUpdateCount.get());
        logger.info("End computation of taxon statistics - {}", getExecutionTime(startComputationTimePoint, System.currentTimeMillis()));
        
        return totalUpdateCount.get();
    }
    private TaxonStatisticsTO getTaxonStatisticsTO(TaxonTO taxonTO, Set<String> taxonPathsWithChildren,
                                                   List<GenomeTO> genomeTOs, List<TaxonAnnotationTO> taxonAnnotTOs) {
        String prefix = taxonTO.getPath() + ".";
        
        int depth = taxonTO.getPath().split("\\.").length;
        
        boolean isLeaf = !taxonPathsWithChildren.contains(taxonTO.getPath());
        
        Integer speciesCount = taxonDAO.findSpeciesCount(taxonTO.getPath());
        
        int genomeCount = (int) genomeTOs.stream()
                .filter(g -> g.getTaxonTO().getPath().equals(taxonTO.getPath())
                        || g.getTaxonTO().getPath().startsWith(prefix))
                .count();
        
        int annotationCount = (int) taxonAnnotTOs.stream()
                .filter(ta -> ta.getTaxonTO().getPath().equals(taxonTO.getPath()) || ta.getTaxonTO().getPath().startsWith(prefix))
                .count();
        
        int ogCount = UNKNOWN_OG_COUNT;
        if (genomeCount == 1 && SPECIES_TAG.equals(taxonTO.getRank())) {
            List<OrthogroupTO> moultingOrthogroupsByTaxon = orthogroupDAO.findMoultingOrthogroupsByTaxon(taxonTO.getPath());
            ogCount = moultingOrthogroupsByTaxon.size();
        }
        
        return new TaxonStatisticsTO(taxonTO.getPath(), depth, isLeaf, speciesCount, genomeCount,
                annotationCount, ogCount, null);
    }
    
    private TaxonStatisticsTO getUpdatedTaxonStatisticsTO(TaxonStatisticsTO tsTO) {
        Set<Integer> ogCounts = taxonStatisticsDAO.findByPathWithDirectChildren(tsTO.getPath()).stream()
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
        
        return new TaxonStatisticsTO(tsTO.getPath(), tsTO.getDepth(), tsTO.isLeaf(),
                tsTO.getSpeciesCount(), tsTO.getGenomeCount(), tsTO.getTaxonAnnotationCount(),
                tsTO.getOrthogroupCount(), range);
    }
}