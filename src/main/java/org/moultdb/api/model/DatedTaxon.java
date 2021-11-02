package org.moultdb.api.model;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-20
 */
public class DatedTaxon {
    
    private final GeologicalAge geologicalAge;
    private final Taxon taxon;
    private final GeneralMoultingCharacters generalMoultingCharacters;
    private final Version version;
    
    public DatedTaxon(GeologicalAge geologicalAge, Taxon taxon, GeneralMoultingCharacters generalMoultingCharacters, Version version) {
        this.geologicalAge = geologicalAge;
        this.taxon = taxon;
        this.generalMoultingCharacters = generalMoultingCharacters;
        this.version = version;
    }
    
    public GeologicalAge getGeologicalAge() {
        return geologicalAge;
    }
    
    public Taxon getTaxon() {
        return taxon;
    }
    
    public GeneralMoultingCharacters getGeneralMoultingCharacters() {
        return generalMoultingCharacters;
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
        DatedTaxon that = (DatedTaxon) o;
        return Objects.equals(geologicalAge, that.geologicalAge)
                && Objects.equals(taxon, that.taxon);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(geologicalAge, taxon);
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", DatedTaxon.class.getSimpleName() + "[", "]")
                .add("geologicalAge=" + geologicalAge)
                .add("taxon=" + taxon)
                .add("version=" + version)
                .toString();
    }
}
