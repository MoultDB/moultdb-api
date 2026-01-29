package org.moultdb.api.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.moultdb.api.model.Taxon;
import org.moultdb.api.model.TaxonStatistics;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.moultdb.api.service.impl.TaxonServiceImpl.SPECIES_TAG;

/**
 * @author Valentine Rech de Laval
 * @since 2025-10-22
 */
public class StatisticsResponseBuilder {
    
    public List<StatisticsNode> buildChildrenList(List<Taxon> childrenTaxa, Map<String, TaxonStatistics> statisticsMap) {
        return childrenTaxa.stream()
                .map(child -> buildTreeNode(child, statisticsMap.get(child.getPath())))
                .collect(Collectors.toList());
    }
    
    public StatisticsNode buildTreeNode(Taxon taxon, TaxonStatistics stats) {
        if (taxon == null) {
            return null;
        }
        return new StatisticsNode(taxon.getPath(), taxon.getScientificName(), !stats.isLeaf(),
                SPECIES_TAG.equals(taxon.getRank()), taxon.getAccession(), stats);
    }
    
    public static class StatisticsNode {
        
        @JsonProperty
        // Taxon path in our case
        private String id;
        @JsonProperty
        private String name;
        // Determine whether this node is a branch (if it has children)
        @JsonProperty
        private boolean isBranch;
        @JsonProperty
        private boolean isSpecies;
        // Taxon accession to build URL
        @JsonProperty
        private String accession;
        @JsonProperty
        private TaxonStatistics statistics;
        
        public StatisticsNode(String id, String name, boolean isBranch, boolean isSpecies, String accession, TaxonStatistics statistics) {
            this.id = id;
            this.name = name;
            this.isBranch = isBranch;
            this.isSpecies = isSpecies;
            this.accession = accession;
            this.statistics = statistics;
        }
    }
}