package org.moultdb.api.model;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2022-04-11
 */
public class TimePeriod {
    
    private final GeologicalAge geologicalAgeFrom;
    private final GeologicalAge geologicalAgeTo;
    
    /**
     * Constructor providing only one {@code GeologicalAge}.
     * In this case, the time period concerns only one {@code GeologicalAge}.
     * Thus, the {@code geologicalAgeFrom} and {@code geologicalAgeTo} are equals.
     */
    public TimePeriod(GeologicalAge geologicalAge) {
        this(geologicalAge, geologicalAge);
    }

    public TimePeriod(GeologicalAge geologicalAgeFrom, GeologicalAge geologicalAgeTo) {
        this.geologicalAgeFrom = geologicalAgeFrom;
        this.geologicalAgeTo = geologicalAgeTo;
    }
    
    public GeologicalAge getGeologicalAgeFrom() {
        return geologicalAgeFrom;
    }
    
    public GeologicalAge getGeologicalAgeTo() {
        return geologicalAgeTo;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        TimePeriod that = (TimePeriod) o;
        return Objects.equals(geologicalAgeFrom, that.geologicalAgeFrom)
                && Objects.equals(geologicalAgeTo, that.geologicalAgeTo);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(geologicalAgeFrom, geologicalAgeTo);
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", TimePeriod.class.getSimpleName() + "[", "]")
                .add("geologicalAgeFrom=" + geologicalAgeFrom)
                .add("geologicalAgeTo=" + geologicalAgeTo)
                .toString();
    }
}
