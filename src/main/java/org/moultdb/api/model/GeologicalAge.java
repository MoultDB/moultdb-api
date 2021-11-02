package org.moultdb.api.model;

import java.math.BigDecimal;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-20
 */
public class GeologicalAge extends NamedEntity {

    private final String symbol;
    private final BigDecimal youngerBound;
    private final BigDecimal olderBound;
    
    public GeologicalAge(String name, String symbol, BigDecimal youngerBound, BigDecimal olderBound) {
        super(name);
        this.symbol = symbol;
        this.youngerBound = youngerBound;
        this.olderBound = olderBound;
    }
    
    public String getSymbol() {
        return symbol;
    }
    
    public BigDecimal getYoungerBound() {
        return youngerBound;
    }
    
    public BigDecimal getOlderBound() {
        return olderBound;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", GeologicalAge.class.getSimpleName() + "[", "]")
                .add("name='" + getName() + "'")
                .add("symbol='" + symbol + "'")
                .add("youngerBound=" + youngerBound)
                .add("olderBound=" + olderBound)
                .toString();
    }
}
