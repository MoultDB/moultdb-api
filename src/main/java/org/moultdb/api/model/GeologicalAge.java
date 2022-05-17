package org.moultdb.api.model;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-20
 */
public class GeologicalAge extends NamedEntity<String> {
    
    private final String rank;
    private final BigDecimal youngerBound;
    private final BigDecimal youngerBoundImprecision;
    private final BigDecimal olderBound;
    private final BigDecimal olderBoundImprecision;
    private final Set<String> synonyms;
    
    public GeologicalAge(String name, String notation, String rank, BigDecimal youngerBound,
                         BigDecimal youngerBoundImprecision, BigDecimal olderBound,
                         BigDecimal olderBoundImprecision, Collection<String> synonyms) {
        super(notation, name);
        this.rank = rank;
        this.youngerBound = youngerBound;
        this.youngerBoundImprecision = youngerBoundImprecision;
        this.olderBound = olderBound;
        this.olderBoundImprecision = olderBoundImprecision;
        this.synonyms = Collections.unmodifiableSet(synonyms == null ? new HashSet<>(): new HashSet<>(synonyms));;
    }
    
    public String getNotation() {
        return getId();
    }
    
    
    public String getRank() {
        return rank;
    }
    
    public BigDecimal getYoungerBound() {
        return youngerBound;
    }
    
    public BigDecimal getYoungerBoundImprecision() {
        return youngerBoundImprecision;
    }
    
    public BigDecimal getOlderBound() {
        return olderBound;
    }
    
    public BigDecimal getOlderBoundImprecision() {
        return olderBoundImprecision;
    }
    
    public Set<String> getSynonyms() {
        return synonyms;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", GeologicalAge.class.getSimpleName() + "[", "]")
                .add("notation='" + getId() + "'")
                .add("name='" + getName() + "'")
                .add("rank='" + rank + "'")
                .add("youngerBound=" + youngerBound)
                .add("youngerBoundImprecision=" + youngerBoundImprecision)
                .add("olderBound=" + olderBound)
                .add("olderBoundImprecision=" + olderBoundImprecision)
                .add("synonyms=" + synonyms)
                .toString();
    }
}
