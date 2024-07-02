package org.moultdb.api.repository.dto;

import java.io.Serial;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2024-04-02
 */
public class GeneToDomainTO extends TransfertObject {
    
    @Serial
    private static final long serialVersionUID = 3609551172788237633L;
    
    private final Integer geneId;
    private final DomainTO domainTO;
    private final Integer start;
    private final Integer end;

    public GeneToDomainTO(Integer geneId, DomainTO domainTO, Integer start, Integer end) {
        this.geneId = geneId;
        this.domainTO = domainTO;
        this.start = start;
        this.end = end;
    }

    public Integer getGeneId() {
        return geneId;
    }
    
    public DomainTO getDomainTO() {
        return domainTO;
    }
    
    public Integer getStart() {
        return start;
    }
    
    public Integer getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", GeneToDomainTO.class.getSimpleName() + "[", "]")
                .add("geneId=" + geneId)
                .add("domainTO='" + domainTO + "'")
                .add("start=" + start)
                .add("end=" + end)
                .toString();
    }
}
