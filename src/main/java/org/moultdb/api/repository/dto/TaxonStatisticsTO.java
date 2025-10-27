package org.moultdb.api.repository.dto;

import java.io.Serial;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2025-10-13
 */
public class TaxonStatisticsTO extends EntityTO<String> {
    
    @Serial
    private static final long serialVersionUID = -6545761622277960336L;
    
    Integer depth;
    Boolean isLeaf;
    Integer speciesCount;
    Integer genomeCount;
    Integer taxonAnnotationCount;
    Integer orthogroupCount;
    String orthogroupRange;
    
    public TaxonStatisticsTO(String path, Integer depth, Boolean isLeaf, Integer speciesCount, Integer genomeCount,
                             Integer taxonAnnotationCount, Integer orthogroupCount, String orthogroupRange)
            throws IllegalArgumentException {
        super(path);
        this.depth = depth;
        this.isLeaf = isLeaf;
        this.speciesCount = speciesCount;
        this.genomeCount = genomeCount;
        this.taxonAnnotationCount = taxonAnnotationCount;
        this.orthogroupCount = orthogroupCount;
        this.orthogroupRange = orthogroupRange;
    }
    
    public String getPath() {
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
    
    public Integer getOrthogroupCount() {
        return orthogroupCount;
    }
    
    public String getOrthogroupRange() {
        return orthogroupRange;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", TaxonStatisticsTO.class.getSimpleName() + "[", "]")
                .add("path=" + getPath())
                .add("depth=" + depth)
                .add("isLeaf=" + isLeaf)
                .add("speciesCount=" + speciesCount)
                .add("genomeCount=" + genomeCount)
                .add("taxonAnnotationCount=" + taxonAnnotationCount)
                .add("orthogroupCount=" + orthogroupCount)
                .add("orthogroupRange=" + orthogroupRange)
                .toString();
    }
}
