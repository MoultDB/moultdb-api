package org.moultdb.importer.genomics;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2024-03-22
 */
public class PathwayBean {
    
    private String id;
    private String function;
    private String description;
    private int orthogroupId;
    private String proteinId;
    private int taxonId;
    private String geneName;
    private String genomeAcc;
    private String reference;
    
    public PathwayBean() {
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getFunction() {
        return function;
    }
    
    public void setFunction(String function) {
        this.function = function;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public int getOrthogroupId() {
        return orthogroupId;
    }
    
    public void setOrthogroupId(int orthogroupId) {
        this.orthogroupId = orthogroupId;
    }
    
    public String getProteinId() {
        return proteinId;
    }
    
    public void setProteinId(String proteinId) {
        this.proteinId = proteinId;
    }
    
    public int getTaxonId() {
        return taxonId;
    }
    
    public void setTaxonId(int taxonId) {
        this.taxonId = taxonId;
    }
    
    public String getGeneName() {
        return geneName;
    }
    
    public void setGeneName(String geneName) {
        this.geneName = geneName;
    }
    
    public String getGenomeAcc() {
        return genomeAcc;
    }
    
    public void setGenomeAcc(String genomeAcc) {
        this.genomeAcc = genomeAcc;
    }
    
    public String getReference() {
        return reference;
    }
    
    public void setReference(String reference) {
        this.reference = reference;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PathwayBean that = (PathwayBean) o;
        return Objects.equals(id, that.id)
                && Objects.equals(function, that.function)
                && Objects.equals(description, that.description)
                && orthogroupId == that.orthogroupId
                && Objects.equals(proteinId, that.proteinId)
                && taxonId == that.taxonId
                && Objects.equals(geneName, that.geneName)
                && Objects.equals(genomeAcc, that.genomeAcc)
                && Objects.equals(reference, that.reference);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, function, description, orthogroupId, proteinId, taxonId, geneName, genomeAcc, reference);
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", PathwayBean.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("function='" + function + "'")
                .add("description='" + description + "'")
                .add("orthogroupId=" + orthogroupId)
                .add("proteinId='" + proteinId + "'")
                .add("taxonId=" + taxonId)
                .add("geneName='" + geneName + "'")
                .add("genomeAcc='" + genomeAcc + "'")
                .add("reference='" + reference + "'")
                .toString();
    }
}
