package org.moultdb.api.model;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2022-05-02
 */
public class DevStage extends NamedEntity<String> {
    
    private final String taxon;
    
    private final Integer leftBound;
    
    private final Integer rightBound;
    
    public DevStage(String accession, String name, String description, String taxon, Integer leftBound, Integer rightBound)
            throws IllegalArgumentException {
        super(accession, name, description);
        this.taxon = taxon;
        if (leftBound != null && leftBound < 1) {
            throw new IllegalArgumentException("The provided left bound cannot be less than 1");
        }
        if (leftBound!= null && rightBound != null && rightBound <= leftBound) {
            throw new IllegalArgumentException("The provided right bound cannot be less than or equal to the provided left bound");
        }
        this.leftBound = leftBound;
        this.rightBound = rightBound;
    }
    
    public String getTaxon() {
        return taxon;
    }
    
    public Integer getLeftBound() {
        return leftBound;
    }
    
    public Integer getRightBound() {
        return rightBound;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        DevStage that = (DevStage) o;
        return Objects.equals(leftBound, that.leftBound)
                && Objects.equals(rightBound, that.rightBound)
                && Objects.equals(taxon, that.taxon);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), taxon, leftBound, rightBound);
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", DevStage.class.getSimpleName() + "[", "]")
                .add("id=" + getId())
                .add("name=" + getName())
                .add("description=" + getDescription())
                .add("taxon='" + taxon + "'")
                .add("leftBound=" + leftBound)
                .add("rightBound=" + rightBound)
                .toString();
    }
}
