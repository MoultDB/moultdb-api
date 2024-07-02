package org.moultdb.importer.genomics;

import java.util.Date;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2024-03-22
 */
public class OrthogroupBean {
    
    private int orthogroupId;
    private int taxonId;
    private String proteinId;
    private Date version;
    
    public OrthogroupBean() {
    }
    
    public int getOrthogroupId() {
        return orthogroupId;
    }
    
    public void setOrthogroupId(int orthogroupId) {
        this.orthogroupId = orthogroupId;
    }
    
    public int getTaxonId() {
        return taxonId;
    }
    
    public void setTaxonId(int taxonId) {
        this.taxonId = taxonId;
    }
    
    public String getProteinId() {
        return proteinId;
    }
    
    public void setProteinId(String proteinId) {
        this.proteinId = proteinId;
    }
    
    public Date getVersion() {
        return version;
    }
    
    public void setVersion(Date version) {
        this.version = version;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrthogroupBean that = (OrthogroupBean) o;
        return orthogroupId == that.orthogroupId
                && taxonId == that.taxonId
                && Objects.equals(proteinId, that.proteinId)
                && Objects.equals(version, that.version);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(orthogroupId, taxonId, proteinId, version);
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", OrthogroupBean.class.getSimpleName() + "[", "]")
                .add("orthogroupId=" + orthogroupId)
                .add("taxonId=" + taxonId)
                .add("proteinId='" + proteinId + "'")
                .add("version=" + version)
                .toString();
    }
}
