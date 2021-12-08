package org.moultdb.api.repository.dto;

import java.io.Serial;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-11-22
 */
public class ConditionTO extends EntityTO {
    
    @Serial
    private static final long serialVersionUID = -5175216293842504409L;
    
    private final Integer devStageId;
    private final Integer anatomicalEntityId;
    private final Integer sexId;
    private final String moultingStep;
    
    public ConditionTO(Integer id, Integer devStageId, Integer anatomicalEntityId, Integer sexId, String moultingStep)
            throws IllegalArgumentException {
        super(id);
        this.devStageId = devStageId;
        this.anatomicalEntityId = anatomicalEntityId;
        this.sexId = sexId;
        this.moultingStep = moultingStep;
    }
    
    public Integer getDevStageId() {
        return devStageId;
    }
    
    public Integer getAnatomicalEntityId() {
        return anatomicalEntityId;
    }
    
    public Integer getSexId() {
        return sexId;
    }
    
    public String getMoultingStep() {
        return moultingStep;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", ConditionTO.class.getSimpleName() + "[", "]")
                .add("devStageId=" + devStageId)
                .add("anatomicalEntityId=" + anatomicalEntityId)
                .add("sexId=" + sexId)
                .add("moultingStep=" + moultingStep)
                .toString();
    }
}
