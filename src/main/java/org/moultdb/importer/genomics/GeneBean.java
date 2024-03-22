package org.moultdb.importer.genomics;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2024-03-21
 */
public class GeneBean {
    
    private String proteinId;
    private Integer proteinLength;
    private String transcriptId;
    private String transcriptUrlSuffix;
    private String geneId;
    private String geneName;
    private String locusTag;
    private String proteinDescription;
    private String origin;
    private String genomeAcc;
    
    public GeneBean() {
    }
    
    public String getProteinId() {
        return proteinId;
    }
    
    public void setProteinId(String proteinId) {
        this.proteinId = proteinId;
    }
    
    public Integer getProteinLength() {
        return proteinLength;
    }
    
    public void setProteinLength(Integer proteinLength) {
        this.proteinLength = proteinLength;
    }
    
    public String getTranscriptId() {
        return transcriptId;
    }
    
    public void setTranscriptId(String transcriptId) {
        this.transcriptId = transcriptId;
    }
    
    public String getTranscriptUrlSuffix() {
        return transcriptUrlSuffix;
    }
    
    public void setTranscriptUrlSuffix(String transcriptUrlSuffix) {
        this.transcriptUrlSuffix = transcriptUrlSuffix;
    }
    
    public String getGeneId() {
        return geneId;
    }
    
    public void setGeneId(String geneId) {
        this.geneId = geneId;
    }
    
    public String getGeneName() {
        return geneName;
    }
    
    public void setGeneName(String geneName) {
        this.geneName = geneName;
    }
    
    public String getLocusTag() {
        return locusTag;
    }
    
    public void setLocusTag(String locusTag) {
        this.locusTag = locusTag;
    }
    
    public String getProteinDescription() {
        return proteinDescription;
    }
    
    public void setProteinDescription(String proteinDescription) {
        this.proteinDescription = proteinDescription;
    }
    
    public String getOrigin() {
        return origin;
    }
    
    public void setOrigin(String origin) {
        this.origin = origin;
    }
    
    public String getGenomeAcc() {
        return genomeAcc;
    }
    
    public void setGenomeAcc(String genomeAcc) {
        this.genomeAcc = genomeAcc;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeneBean geneBean = (GeneBean) o;
        return Objects.equals(proteinId, geneBean.proteinId)
                && Objects.equals(proteinLength, geneBean.proteinLength)
                && Objects.equals(transcriptId, geneBean.transcriptId)
                && Objects.equals(transcriptUrlSuffix, geneBean.transcriptUrlSuffix)
                && Objects.equals(geneId, geneBean.geneId)
                && Objects.equals(geneName, geneBean.geneName)
                && Objects.equals(locusTag, geneBean.locusTag)
                && Objects.equals(proteinDescription, geneBean.proteinDescription)
                && Objects.equals(origin, geneBean.origin)
                && Objects.equals(genomeAcc, geneBean.genomeAcc);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(proteinId, proteinLength, transcriptId, transcriptUrlSuffix, geneId, geneName, locusTag,
                proteinDescription, origin, genomeAcc);
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", GeneBean.class.getSimpleName() + "[", "]")
                .add("proteinId='" + proteinId + "'")
                .add("proteinLength=" + proteinLength)
                .add("transcriptId='" + transcriptId + "'")
                .add("transcriptUrlSuffix='" + transcriptUrlSuffix + "'")
                .add("geneId='" + geneId + "'")
                .add("geneName='" + geneName + "'")
                .add("locusTag='" + locusTag + "'")
                .add("proteinDescription='" + proteinDescription + "'")
                .add("origin='" + origin + "'")
                .add("genomeAcc='" + genomeAcc + "'")
                .toString();
    }
}
