package org.moultdb.importer.genomics;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2024-03-22
 */
public class DomainBean {

    private String proteinId;
    private String domainId;
    private int domainStart;
    private int domainEnd;
    private String domainDescription;
    
    public DomainBean() {
    }
    
    public String getProteinId() {
        return proteinId;
    }
    
    public void setProteinId(String proteinId) {
        this.proteinId = proteinId;
    }
    
    public String getDomainId() {
        return domainId;
    }
    
    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }
    
    public int getDomainStart() {
        return domainStart;
    }
    
    public void setDomainStart(int domainStart) {
        this.domainStart = domainStart;
    }
    
    public int getDomainEnd() {
        return domainEnd;
    }
    
    public void setDomainEnd(int domainEnd) {
        this.domainEnd = domainEnd;
    }
    
    public String getDomainDescription() {
        return domainDescription;
    }
    
    public void setDomainDescription(String domainDescription) {
        this.domainDescription = domainDescription;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DomainBean that = (DomainBean) o;
        return Objects.equals(proteinId, that.proteinId)
                && Objects.equals(domainId, that.domainId)
                && domainStart == that.domainStart
                && domainEnd == that.domainEnd
                && Objects.equals(domainDescription, that.domainDescription);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(proteinId, domainId, domainStart, domainEnd, domainDescription);
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", DomainBean.class.getSimpleName() + "[", "]")
                .add("proteinId='" + proteinId + "'")
                .add("domainId='" + domainId + "'")
                .add("domainStart=" + domainStart)
                .add("domainEnd=" + domainEnd)
                .add("domainDescription='" + domainDescription + "'")
                .toString();
    }
}
