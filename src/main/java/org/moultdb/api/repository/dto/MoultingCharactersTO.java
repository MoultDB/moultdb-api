package org.moultdb.api.repository.dto;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
public class MoultingCharactersTO extends EntityTO<Integer> {
    
    @Serial
    private static final long serialVersionUID = -558466073558777076L;
    
    private final String lifeHistoryStyle;
    private final Boolean hasTerminalAdultStage;
    private final Integer observedMoultStageCount;
    private final Integer estimatedMoultStageCount;
    private final Integer specimenCount;
    private final String segmentAdditionMode;
    private final String bodySegmentsCountPerMoultStage;
    private final String bodySegmentsCountInAdults;
    private final BigDecimal bodyLengthAverage;
    private final String bodyLengthIncreaseAverage;
    private final String measurementUnit;
    private final String sutureLocation;
    private final String cephalicSutureLocation;
    private final String postCephalicSutureLocation;
    private final String resultingNamedMoultingConfiguration;
    private final String egressDirection;
    private final String positionExuviaeFoundIn;
    private final String moultingPhase;
    private final String moultingVariability;
    private final String otherBehaviour;
    private final String exuviaeConsumed;
    private final String exoskeletalMaterialReabsorption;
    private final String fossilExuviaeQuality;
    
    public MoultingCharactersTO(Integer id, String lifeHistoryStyle, Boolean hasTerminalAdultStage,
                                Integer observedMoultStageCount, Integer estimatedMoultStageCount, Integer specimenCount,
                                String segmentAdditionMode, String bodySegmentsCountPerMoultStage,
                                String bodySegmentsCountInAdults, BigDecimal bodyLengthAverage,
                                String bodyLengthIncreaseAverage,
                                String measurementUnit, String sutureLocation, String cephalicSutureLocation,
                                String postCephalicSutureLocation, String resultingNamedMoultingConfiguration,
                                String egressDirection, String positionExuviaeFoundIn, String moultingPhase,
                                String moultingVariability, String otherBehaviour,
                                String exuviaeConsumed, String exoskeletalMaterialReabsorption, String fossilExuviaeQuality)
            throws IllegalArgumentException {
        super(id);
        this.lifeHistoryStyle = lifeHistoryStyle;
        this.hasTerminalAdultStage = hasTerminalAdultStage;
        this.observedMoultStageCount = observedMoultStageCount;
        this.estimatedMoultStageCount = estimatedMoultStageCount;
        this.specimenCount = specimenCount;
        this.segmentAdditionMode = segmentAdditionMode;
        this.bodySegmentsCountPerMoultStage = bodySegmentsCountPerMoultStage;
        this.bodySegmentsCountInAdults = bodySegmentsCountInAdults;
        this.bodyLengthAverage = bodyLengthAverage;
        this.bodyLengthIncreaseAverage = bodyLengthIncreaseAverage;
        this.measurementUnit = measurementUnit;
        this.sutureLocation = sutureLocation;
        this.cephalicSutureLocation = cephalicSutureLocation;
        this.postCephalicSutureLocation = postCephalicSutureLocation;
        this.resultingNamedMoultingConfiguration = resultingNamedMoultingConfiguration;
        this.egressDirection = egressDirection;
        this.positionExuviaeFoundIn = positionExuviaeFoundIn;
        this.moultingPhase = moultingPhase;
        this.moultingVariability = moultingVariability;
        this.otherBehaviour = otherBehaviour;
        this.exuviaeConsumed = exuviaeConsumed;
        this.exoskeletalMaterialReabsorption = exoskeletalMaterialReabsorption;
        this.fossilExuviaeQuality = fossilExuviaeQuality;
    }
    
    public String getLifeHistoryStyle() {
        return lifeHistoryStyle;
    }
    
    public Boolean getHasTerminalAdultStage() {
        return hasTerminalAdultStage;
    }
    
    public Integer getObservedMoultStageCount() {
        return observedMoultStageCount;
    }
    
    public Integer getEstimatedMoultStageCount() {
        return estimatedMoultStageCount;
    }
    
    public Integer getSpecimenCount() {
        return specimenCount;
    }
    
    public String getSegmentAdditionMode() {
        return segmentAdditionMode;
    }
    
    public String getBodySegmentsCountPerMoultStage() {
        return bodySegmentsCountPerMoultStage;
    }
    
    public String getBodySegmentsCountInAdults() {
        return bodySegmentsCountInAdults;
    }
    
    public BigDecimal getBodyLengthAverage() {
        return bodyLengthAverage;
    }
    
    public String getBodyLengthIncreaseAverage() {
        return bodyLengthIncreaseAverage;
    }
    
    public String getMeasurementUnit() {
        return measurementUnit;
    }
    
    public String getSutureLocation() {
        return sutureLocation;
    }
    
    public String getCephalicSutureLocation() {
        return cephalicSutureLocation;
    }
    
    public String getPostCephalicSutureLocation() {
        return postCephalicSutureLocation;
    }
    
    public String getResultingNamedMoultingConfiguration() {
        return resultingNamedMoultingConfiguration;
    }
    
    public String getEgressDirection() {
        return egressDirection;
    }
    
    public String getPositionExuviaeFoundIn() {
        return positionExuviaeFoundIn;
    }
    
    public String getMoultingPhase() {
        return moultingPhase;
    }
    
    public String getMoultingVariability() {
        return moultingVariability;
    }
    
    public String getOtherBehaviour() {
        return otherBehaviour;
    }
    
    public String getExuviaeConsumed() {
        return exuviaeConsumed;
    }
    
    public String getExoskeletalMaterialReabsorption() {
        return exoskeletalMaterialReabsorption;
    }
    
    public String getFossilExuviaeQuality() {
        return fossilExuviaeQuality;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", MoultingCharactersTO.class.getSimpleName() + "[", "]")
                .add("id=" + getId())
                .add("lifeHistoryStyle='" + lifeHistoryStyle + "'")
                .add("hasTerminalAdultStage=" + hasTerminalAdultStage)
                .add("observedMoultStageCount=" + observedMoultStageCount)
                .add("estimatedMoultStageCount=" + estimatedMoultStageCount)
                .add("specimenCount=" + specimenCount)
                .add("segmentAdditionMode='" + segmentAdditionMode + "'")
                .add("bodySegmentsCountPerMoultStage=" + bodySegmentsCountPerMoultStage)
                .add("bodySegmentsCountInAdults=" + bodySegmentsCountInAdults)
                .add("bodyLengthAverage=" + bodyLengthAverage)
                .add("bodyLengthIncreaseAverage=" + bodyLengthIncreaseAverage)
                .add("measurementUnit='" + measurementUnit + "'")
                .add("sutureLocation='" + sutureLocation + "'")
                .add("cephalicSutureLocation='" + cephalicSutureLocation + "'")
                .add("postCephalicSutureLocation='" + postCephalicSutureLocation + "'")
                .add("resultingNamedMoultingConfiguration='" + resultingNamedMoultingConfiguration + "'")
                .add("egressDirection='" + egressDirection + "'")
                .add("positionExuviaeFoundIn='" + positionExuviaeFoundIn + "'")
                .add("moultingPhase='" + moultingPhase + "'")
                .add("moultingVariability='" + moultingVariability + "'")
                .add("otherBehaviour='" + otherBehaviour + "'")
                .add("exuviaeConsumed='" + exuviaeConsumed + "'")
                .add("exoskeletalMaterialReabsorption='" + exoskeletalMaterialReabsorption + "'")
                .add("fossilExuviaeQuality='" + fossilExuviaeQuality + "'")
                .toString();
    }
}
