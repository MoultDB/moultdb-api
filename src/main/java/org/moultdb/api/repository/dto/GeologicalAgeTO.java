package org.moultdb.api.repository.dto;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
public class GeologicalAgeTO extends NamedEntityTO<String> {
    
    @Serial
    private static final long serialVersionUID = 1080691858915012455L;
    
    private final String rank;
    private final BigDecimal youngerBound;
    private final BigDecimal youngerBoundImprecision;
    private final BigDecimal olderBound;
    private final BigDecimal olderBoundImprecision;
    private final Set<String> synonyms;
    
    public GeologicalAgeTO(String notation, String name, String rank, BigDecimal youngerBound, BigDecimal youngerBoundImprecision,
                           BigDecimal olderBound, BigDecimal olderBoundImprecision, Collection<String> synonyms) {
        super(notation, name, null);
        this.rank = rank;
        this.youngerBound = youngerBound;
        this.youngerBoundImprecision = youngerBoundImprecision;
        this.olderBound = olderBound;
        this.olderBoundImprecision = olderBoundImprecision;
        this.synonyms = Collections.unmodifiableSet(synonyms == null ? new HashSet<>(): new HashSet<>(synonyms));
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
        return new StringJoiner(", ", GeologicalAgeTO.class.getSimpleName() + "[", "]")
                .add("notation=" + getNotation())
                .add("name=" + getName())
                .add("rank='" + rank + "'")
                .add("youngerBound=" + youngerBound)
                .add("youngerBoundImprecision=" + youngerBoundImprecision)
                .add("olderBound=" + olderBound)
                .add("olderBoundImprecision=" + olderBoundImprecision)
                .add("synonyms=" + synonyms)
                .toString();
    }
}
