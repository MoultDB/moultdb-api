package org.moultdb.api.model;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-20
 */
public class Individual {
    
    private final Taxon taxon;
    private final Sample sample;
    private final Condition condition;
    private final MoultingCharacters moultingCharacters;
    private final Version version;
    
    public Individual(Taxon taxon, Sample sample, Condition condition, MoultingCharacters moultingCharacters, Version version) {
        this.taxon = taxon;
        this.moultingCharacters = moultingCharacters;
        this.sample = sample;
        this.condition = condition;
        this.version = version;
    }
    
    public Taxon getTaxon() {
        return taxon;
    }
    
    public Sample getSample() {
        return sample;
    }
    
    public Condition getCondition() {
        return condition;
    }
    
    public MoultingCharacters getMoultingCharacters() {
        return moultingCharacters;
    }
    
    public MoultingCharacters getGeneralMoultingCharacters() {
        return moultingCharacters;
    }
    
    public Version getVersion() {
        return version;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Individual that = (Individual) o;
        return Objects.equals(taxon, that.taxon)
                && Objects.equals(moultingCharacters, that.moultingCharacters)
                && Objects.equals(sample, that.sample)
                && Objects.equals(condition, that.condition);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(taxon, moultingCharacters, sample, condition);
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", Individual.class.getSimpleName() + "[", "]")
                .add("taxon=" + taxon)
                .add("sample=" + sample)
                .add("condition=" + condition)
                .add("moultingCharacters=" + moultingCharacters)
                .add("version=" + version)
                .toString();
    }
}
