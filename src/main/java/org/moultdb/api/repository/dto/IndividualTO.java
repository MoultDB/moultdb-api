package org.moultdb.api.repository.dto;

import java.io.Serial;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-11-22
 */
public class IndividualTO extends EntityTO {
    
    @Serial
    private static final long serialVersionUID = 5144072593844456844L;
    
    private final TaxonTO taxonTO;
    private final MoultingCharactersTO moultingCharactersTO;
    private final SampleTO sampleTO;
    private final ConditionTO conditionTO;
    private final Integer versionId;
    
    public IndividualTO(Integer id, TaxonTO taxonTO, MoultingCharactersTO moultingCharactersTO, SampleTO sampleTO,
                        ConditionTO conditionTO, Integer versionId) throws IllegalArgumentException {
        super(id);
        this.taxonTO = taxonTO;
        this.moultingCharactersTO = moultingCharactersTO;
        this.sampleTO = sampleTO;
        this.conditionTO = conditionTO;
        this.versionId = versionId;
    }
    
    public TaxonTO getTaxonTO() {
        return taxonTO;
    }
    
    public MoultingCharactersTO getMoultingCharactersTO() {
        return moultingCharactersTO;
    }
    
    public SampleTO getSampleTO() {
        return sampleTO;
    }
    
    public ConditionTO getConditionTO() {
        return conditionTO;
    }
    
    public Integer getVersionId() {
        return versionId;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", IndividualTO.class.getSimpleName() + "[", "]")
                .add("id=" + getId())
                .add("taxonTO=" + taxonTO)
                .add("moultingCharactersTO=" + moultingCharactersTO)
                .add("sampleTO=" + sampleTO)
                .add("conditionTO=" + conditionTO)
                .add("versionId=" + versionId)
                .toString();
    }
}
