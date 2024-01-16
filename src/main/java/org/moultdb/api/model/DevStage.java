package org.moultdb.api.model;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2022-05-02
 */
public class DevStage extends NamedEntity<String> {
    
    private final int leftBound;
    
    private final int rightBound;
    
    public DevStage(String accession, String name, String description, int leftBound, int rightBound)
            throws IllegalArgumentException {
        super(accession, name, description);
        if (leftBound < 1) {
            throw new IllegalArgumentException("the provided left bound cannot be less than 1.");
        }
        if (rightBound <= leftBound) {
            throw new IllegalArgumentException("the provided right bound cannot be less than or equal to the provided left bound.");
        }
        this.leftBound = leftBound;
        this.rightBound = rightBound;
    }
    
    public int getLeftBound() {
        return leftBound;
    }
    
    public int getRightBound() {
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
        return leftBound == that.leftBound && rightBound == that.rightBound;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), leftBound, rightBound);
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", DevStage.class.getSimpleName() + "[", "]")
                .add("id=" + getId())
                .add("name=" + getName())
                .add("description=" + getDescription())
                .add("leftBound=" + leftBound)
                .add("rightBound=" + rightBound)
                .toString();
    }
}
