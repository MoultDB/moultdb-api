package org.moultdb.api.repository.dto;

import java.io.Serial;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-11-22
 */
public class ConditionTO extends EntityTO<Integer> {
    
    @Serial
    private static final long serialVersionUID = -5175216293842504409L;
    
    private final DevStageTO devStageTO;
    private final Integer ageInDays;
    private final AnatEntityTO anatEntityTO;
    private final String sex;
    private final String reproductiveState;
    private final String moultingStep;
    
    public ConditionTO(Integer id, Integer ageInDays, AnatEntityTO anatEntityTO,
                       String sex, String reproductiveState, String moultingStep)
            throws IllegalArgumentException {
        this(id, null, ageInDays, anatEntityTO, sex, reproductiveState, moultingStep);
    }
    
    public ConditionTO(Integer id, DevStageTO devStageTO, AnatEntityTO anatEntityTO,
                       String sex, String reproductiveState, String moultingStep)
            throws IllegalArgumentException {
        this(id, devStageTO, null, anatEntityTO, sex, reproductiveState, moultingStep);
    }

    private ConditionTO(Integer id, DevStageTO devStageTO, Integer ageInDays, AnatEntityTO anatEntityTO,
                        String sex, String reproductiveState, String moultingStep)
            throws IllegalArgumentException {
        super(id);
        this.devStageTO = devStageTO;
        this.ageInDays = ageInDays;
        this.anatEntityTO = anatEntityTO;
        this.sex = sex;
        this.reproductiveState = reproductiveState;
        this.moultingStep = moultingStep;
    }
    
    public DevStageTO getDevStageTO() {
        return devStageTO;
    }
    
    public Integer getAgeInDays() {
        return ageInDays;
    }
    
    public AnatEntityTO getAnatEntityTO() {
        return anatEntityTO;
    }
    
    public String getSex() {
        return sex;
    }
    
    public String getReproductiveState() {
        return reproductiveState;
    }
    
    public String getMoultingStep() {
        return moultingStep;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", ConditionTO.class.getSimpleName() + "[", "]")
                .add("devStageTO=" + devStageTO)
                .add("anatEntityTO=" + anatEntityTO)
                .add("sex=" + sex)
                .add("reproductiveState=" + reproductiveState)
                .add("moultingStep=" + moultingStep)
                .toString();
    }
}
