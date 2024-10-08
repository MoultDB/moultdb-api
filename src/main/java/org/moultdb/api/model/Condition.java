package org.moultdb.api.model;

import org.moultdb.api.model.moutldbenum.MoultingStep;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-11-23
 */
public class Condition {
    
    private final DevStage devStage;
    private final AnatEntity anatomicalEntity;
    private final String sex;
    private final String reproductiveState;
    private final MoultingStep moultingStep;
    
    public Condition(DevStage devStage, AnatEntity anatomicalEntity, String sex, String reproductiveState,
                     MoultingStep moultingStep) {
        this.devStage = devStage;
        this.anatomicalEntity = anatomicalEntity;
        this.sex = sex;
        this.reproductiveState = reproductiveState;
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
    
    public String getReproductiveState() {
        return reproductiveState;
    }
    
    public MoultingStep getMoultingStep() {
        return moultingStep;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Condition condition = (Condition) o;
        return Objects.equals(devStage, condition.devStage)
                && Objects.equals(anatomicalEntity, condition.anatomicalEntity)
                && Objects.equals(sex, condition.sex)
                && Objects.equals(reproductiveState, condition.reproductiveState)
                && moultingStep == condition.moultingStep;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(devStage, anatomicalEntity, sex, reproductiveState, moultingStep);
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", Condition.class.getSimpleName() + "[", "]")
                .add("devStage=" + devStage)
                .add("anatomicalEntity=" + anatomicalEntity)
                .add("sex=" + sex)
                .add("reproductiveState=" + reproductiveState)
                .add("moultingStep=" + moultingStep)
                .toString();
    }
}
