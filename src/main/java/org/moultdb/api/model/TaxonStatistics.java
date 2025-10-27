package org.moultdb.api.model;

import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2025-10-13
 */
public class TaxonStatistics extends Entity<String> {
    
    private final Integer depth;
    private final Boolean isLeaf;
    private final Integer speciesCount;
    private final Integer genomeCount;
    private final Integer taxonAnnotationCount;
    private final String orthogroupRange;
    
    public TaxonStatistics(String taxonPath, Integer depth, Boolean isLeaf, Integer speciesCount, Integer genomeCount,
                           Integer taxonAnnotationCount, String orthogroupRange) {
        super(taxonPath);
        this.depth = depth;
        this.isLeaf = isLeaf;
        this.speciesCount = speciesCount;
        this.genomeCount = genomeCount;
        this.taxonAnnotationCount = taxonAnnotationCount;
        this.orthogroupRange = orthogroupRange;
    }
    
    public String getTaxonPath() {
        return getId();
    }
    
    public Integer getDepth() {
        return depth;
    }
    
    public Boolean isLeaf() {
        return isLeaf;
    }
    
    public Integer getSpeciesCount() {
        return speciesCount;
    }
    
    public Integer getGenomeCount() {
        return genomeCount;
    }
    
    public Integer getTaxonAnnotationCount() {
        return taxonAnnotationCount;
    }
    
    public String getOrthogroupRange() {
        return orthogroupRange;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", TaxonStatistics.class.getSimpleName() + "[", "]")
                .add("taxonPath=" + getTaxonPath())
                .add("depth=" + depth)
                .add("isLeaf=" + isLeaf)
                .add("speciesCount=" + speciesCount)
                .add("genomeCount=" + genomeCount)
                .add("taxonAnnotationCount=" + taxonAnnotationCount)
                .add("orthogroupRange=" + orthogroupRange)
                .toString();
    }
}
