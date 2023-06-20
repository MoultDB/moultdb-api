package org.moultdb.api.model;

import org.moultdb.api.model.moutldbenum.MoultingStep;

import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-11-23
 */
public class Condition {
    
    private final DevStage devStage;
    private final AnatEntity anatomicalEntity;
    private final String sex;
    private final MoultingStep moultingStep;
    
    public Condition(DevStage devStage, AnatEntity anatomicalEntity, String sex, MoultingStep moultingStep) {
        this.devStage = devStage;
        this.anatomicalEntity = anatomicalEntity;
        this.sex = sex;
        this.moultingStep = moultingStep;
    }
    
    public DevStage getDevStage() {
        return devStage;
    }
    
    public AnatEntity getAnatomicalEntity() {
        return anatomicalEntity;
    }
    
    public String getSex() {
        return sex;
    }
    
    public MoultingStep getMoultingStep() {
        return moultingStep;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", Condition.class.getSimpleName() + "[", "]")
                .add("devStage=" + devStage)
                .add("anatomicalEntity=" + anatomicalEntity)
                .add("sex=" + sex)
                .add("moultingStep=" + moultingStep)
                .toString();
    }
}
