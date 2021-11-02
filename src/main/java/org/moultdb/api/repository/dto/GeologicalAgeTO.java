package org.moultdb.api.repository.dto;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
public class GeologicalAgeTO extends NamedEntityTO {
    
    @Serial
    private static final long serialVersionUID = 1080691858915012455L;
    
    private final String symbol;
    private final BigDecimal youngerBound;
    private final BigDecimal olderBound;
    
    public GeologicalAgeTO(Integer id, String name, String symbol, BigDecimal youngerBound, BigDecimal olderBound) {
        super(id, name);
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
        return new StringJoiner(", ", GeologicalAgeTO.class.getSimpleName() + "[", "]")
                .add("id='" + getId() + "'")
                .add("name='" + getName() + "'")
                .add("symbol='" + symbol + "'")
                .add("youngerBound=" + youngerBound)
                .add("olderBound=" + olderBound)
                .toString();
    }
}
