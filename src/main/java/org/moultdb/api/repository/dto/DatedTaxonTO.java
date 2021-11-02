package org.moultdb.api.repository.dto;

import java.io.Serial;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
public class DatedTaxonTO extends EntityTO {
    
    @Serial
    private static final long serialVersionUID = 9090994360207976303L;
    
    private final GeologicalAgeTO geologicalAgeTO;
    private final Integer taxonId;
    private final Integer versionId;
    
    public DatedTaxonTO(Integer id, GeologicalAgeTO geologicalAgeTO, Integer taxonId, Integer versionId) {
        super(id);
        this.geologicalAgeTO = geologicalAgeTO;
        this.taxonId = taxonId;
        this.versionId = versionId;
    }
    
    public GeologicalAgeTO getGeologicalAgeTO() {
        return geologicalAgeTO;
    }
    
    public Integer getTaxonId() {
        return taxonId;
    }
    
    public Integer getVersionId() {
        return versionId;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", DatedTaxonTO.class.getSimpleName() + "[", "]")
                .add("id=" + getId())
                .add("geologicalAgeTO=" + geologicalAgeTO)
                .add("taxonId=" + taxonId)
                .add("versionId=" + versionId)
                .toString();
    }
}
