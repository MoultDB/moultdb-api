package org.moultdb.api.model;

import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-11-23
 */
public class Condition {
    
    private final Integer devStageId;
    private final Integer anatomicalEntityId;
    private final Integer sexId;
    private final MoultingStep moultingStep;
    
    public Condition(Integer devStageId, Integer anatomicalEntityId, Integer sexId, MoultingStep moultingStep) {
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
    
    public MoultingStep getMoultingStep() {
        return moultingStep;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", Condition.class.getSimpleName() + "[", "]")
                .add("devStageId=" + devStageId)
                .add("anatomicalEntityId=" + anatomicalEntityId)
                .add("sexId=" + sexId)
                .add("moultingStep=" + moultingStep)
                .toString();
    }
}
