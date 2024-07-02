package org.moultdb.api.repository.dto;

import java.io.Serial;
import java.util.*;

/**
 * @author Valentine Rech de Laval
 * @since 2024-03-22
 */
public class GeneTO extends EntityTO<Integer> {
    
    @Serial
    private static final long serialVersionUID = -8068259646168632939L;
    
    private final String geneId;
    private final String geneName;
    private final String locusTag;
    private final String genomeAcc;
    private final String taxonPath;
    private final OrthogroupTO orthogroupTO;
    private final String transcriptId;
    private final String transcriptUrlSuffix;
    private final String proteinId;
    private final String proteinDescription;
    private final Integer proteinLength;
    private final DataSourceTO dataSourceTO;
    private final PathwayTO pathwayTO;
    
    public GeneTO(Integer id, String geneId, String geneName, String locusTag, String genomeAcc, String taxonPath,
                  String transcriptId, String transcriptUrlSuffix, String proteinId, String proteinDescription,
                  Integer proteinLength, DataSourceTO dataSourceTO, PathwayTO pathwayTO, OrthogroupTO orthogroupTO)
            throws IllegalArgumentException {
        super(id);
        this.geneId = geneId;
        this.geneName = geneName;
        this.locusTag = locusTag;
        this.genomeAcc = genomeAcc;
        this.taxonPath = taxonPath;
        this.transcriptId = transcriptId;
        this.transcriptUrlSuffix = transcriptUrlSuffix;
        this.proteinId = proteinId;
        this.proteinDescription = proteinDescription;
        this.proteinLength = proteinLength;
        this.dataSourceTO = dataSourceTO;
        this.pathwayTO = pathwayTO;
        this.orthogroupTO = orthogroupTO;
    }
    
    public String getGeneId() {
        return geneId;
    }
    
    public String getGeneName() {
        return geneName;
    }
    
    public String getLocusTag() {
        return locusTag;
    }
    
    public String getGenomeAcc() {
        return genomeAcc;
    }
    
    public String getTaxonPath() {
        return taxonPath;
    }
    
    public String getTranscriptId() {
        return transcriptId;
    }
    
    public String getTranscriptUrlSuffix() {
        return transcriptUrlSuffix;
    }
    
    public String getProteinId() {
        return proteinId;
    }
    
    public String getProteinDescription() {
        return proteinDescription;
    }
    
    public Integer getProteinLength() {
        return proteinLength;
    }
    
    public DataSourceTO getDataSourceTO() {
        return dataSourceTO;
    }
    
    public PathwayTO getPathwayTO() {
        return pathwayTO;
    }
    
    public OrthogroupTO getOrthogroupTO() {
        return orthogroupTO;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", GeneTO.class.getSimpleName() + "[", "]")
                .add("geneId='" + geneId + "'")
                .add("geneName='" + geneName + "'")
                .add("locusTag='" + locusTag + "'")
                .add("genomeAcc=" + genomeAcc)
                .add("taxonPath=" + taxonPath)
                .add("transcriptId='" + transcriptId + "'")
                .add("transcriptUrlSuffix='" + transcriptUrlSuffix + "'")
                .add("proteinId='" + proteinId + "'")
                .add("proteinDescription='" + proteinDescription + "'")
                .add("proteinLength=" + proteinLength)
                .add("dataSourceTO=" + dataSourceTO)
                .add("pathwayTO=" + pathwayTO)
                .add("orthogroupTO=" + orthogroupTO)
                .toString();
    }
}
