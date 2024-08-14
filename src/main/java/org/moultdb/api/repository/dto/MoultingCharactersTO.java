package org.moultdb.api.repository.dto;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Set;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
public class MoultingCharactersTO extends EntityTO<Integer> {
    
    @Serial
    private static final long serialVersionUID = -558466073558777076L;
    
    private final String lifeHistoryStyle;
    private final Set<String> lifeModes;
    private final String juvenileMoultCount;
    private final Integer majorMorphologicalTransitionCount;
    private final Boolean hasTerminalAdultStage;
    private final Integer observedMoultStageCount;
    private final Integer estimatedMoultStageCount;
    private final String segmentAdditionMode;
    private final String bodySegmentCount;
    private final String bodySegmentCountInAdults;
    private final BigDecimal bodyLengthAverage;
    private final BigDecimal bodyLengthIncreaseAverage;
    private final BigDecimal bodyMassIncreaseAverage;
    private final String intermoultPeriod;
    private final String premoultPeriod;
    private final String postmoultPeriod;
    private final String variationWithinCohorts;
    private final String sutureLocation;
    private final String cephalicSutureLocation;
    private final String postCephalicSutureLocation;
    private final String resultingNamedMoultingConfiguration;
    private final String egressDirection;
    private final String positionExuviaeFoundIn;
    private final String moultingPhase;
    private final String moultingVariability;
    private final String calcificationEvent;
    private final String heavyMetalReinforcement;
    private final String otherBehaviour;
    private final String exuviaeConsumed;
    private final String exoskeletalMaterialReabsorption;
    private final String fossilExuviaeQuality;
    private final String generalComments;
    
    public MoultingCharactersTO(Integer id, String lifeHistoryStyle, Set<String> lifeModes, String juvenileMoultCount,
                                Integer majorMorphologicalTransitionCount, Boolean hasTerminalAdultStage,
                                Integer observedMoultStageCount, Integer estimatedMoultStageCount,
                                String segmentAdditionMode, String bodySegmentCount,
                                String bodySegmentCountInAdults, BigDecimal bodyLengthAverage,
                                BigDecimal bodyLengthIncreaseAverage, BigDecimal bodyMassIncreaseAverage,
                                String intermoultPeriod, String premoultPeriod, String postmoultPeriod,
                                String variationWithinCohorts, String sutureLocation, String cephalicSutureLocation,
                                String postCephalicSutureLocation, String resultingNamedMoultingConfiguration,
                                String egressDirection, String positionExuviaeFoundIn, String moultingPhase,
                                String moultingVariability, String calcificationEvent, String heavyMetalReinforcement,
                                String otherBehaviour, String exuviaeConsumed, String exoskeletalMaterialReabsorption,
                                String fossilExuviaeQuality, String generalComments)
            throws IllegalArgumentException {
        super(id);
        this.lifeHistoryStyle = lifeHistoryStyle;
        this.lifeModes = lifeModes;
        this.juvenileMoultCount = juvenileMoultCount;
        this.majorMorphologicalTransitionCount = majorMorphologicalTransitionCount;
        this.hasTerminalAdultStage = hasTerminalAdultStage;
        this.observedMoultStageCount = observedMoultStageCount;
        this.estimatedMoultStageCount = estimatedMoultStageCount;
        this.segmentAdditionMode = segmentAdditionMode;
        this.bodySegmentCount = bodySegmentCount;
        this.bodySegmentCountInAdults = bodySegmentCountInAdults;
        this.bodyLengthAverage = bodyLengthAverage;
        this.bodyLengthIncreaseAverage = bodyLengthIncreaseAverage;
        this.bodyMassIncreaseAverage = bodyMassIncreaseAverage;
        this.intermoultPeriod = intermoultPeriod;
        this.premoultPeriod = premoultPeriod;
        this.postmoultPeriod = postmoultPeriod;
        this.variationWithinCohorts = variationWithinCohorts;
        this.sutureLocation = sutureLocation;
        this.cephalicSutureLocation = cephalicSutureLocation;
        this.postCephalicSutureLocation = postCephalicSutureLocation;
        this.resultingNamedMoultingConfiguration = resultingNamedMoultingConfiguration;
        this.egressDirection = egressDirection;
        this.positionExuviaeFoundIn = positionExuviaeFoundIn;
        this.moultingPhase = moultingPhase;
        this.moultingVariability = moultingVariability;
        this.calcificationEvent = calcificationEvent;
        this.heavyMetalReinforcement = heavyMetalReinforcement;
        this.otherBehaviour = otherBehaviour;
        this.exuviaeConsumed = exuviaeConsumed;
        this.exoskeletalMaterialReabsorption = exoskeletalMaterialReabsorption;
        this.fossilExuviaeQuality = fossilExuviaeQuality;
        this.generalComments = generalComments;
    }
    
    public String getLifeHistoryStyle() {
        return lifeHistoryStyle;
    }
    
    public Set<String> getLifeModes() {
        return lifeModes;
    }
    
    public String getJuvenileMoultCount() {
        return juvenileMoultCount;
    }
    
    public Integer getMajorMorphologicalTransitionCount() {
        return majorMorphologicalTransitionCount;
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
    
    public String getSegmentAdditionMode() {
        return segmentAdditionMode;
    }
    
    public String getBodySegmentCount() {
        return bodySegmentCount;
    }
    
    public String getBodySegmentCountInAdults() {
        return bodySegmentCountInAdults;
    }
    
    public BigDecimal getBodyLengthAverage() {
        return bodyLengthAverage;
    }
    
    public BigDecimal getBodyLengthIncreaseAverage() {
        return bodyLengthIncreaseAverage;
    }
    
    public BigDecimal getBodyMassIncreaseAverage() {
        return bodyMassIncreaseAverage;
    }
    
    public String getIntermoultPeriod() {
        return intermoultPeriod;
    }
    
    public String getPremoultPeriod() {
        return premoultPeriod;
    }
    
    public String getPostmoultPeriod() {
        return postmoultPeriod;
    }
    
    public String getVariationWithinCohorts() {
        return variationWithinCohorts;
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
    
    public String getCalcificationEvent() {
        return calcificationEvent;
    }
    
    public String getHeavyMetalReinforcement() {
        return heavyMetalReinforcement;
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
    
    public String getGeneralComments() {
        return generalComments;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", MoultingCharactersTO.class.getSimpleName() + "[", "]")
                .add("lifeHistoryStyle='" + lifeHistoryStyle + "'")
                .add("lifeMode='" + lifeModes + "'")
                .add("juvenileMoultCount=" + juvenileMoultCount)
                .add("majorMorphologicalTransitionCount=" + majorMorphologicalTransitionCount)
                .add("hasTerminalAdultStage=" + hasTerminalAdultStage)
                .add("observedMoultStageCount=" + observedMoultStageCount)
                .add("estimatedMoultStageCount=" + estimatedMoultStageCount)
                .add("segmentAdditionMode='" + segmentAdditionMode + "'")
                .add("bodySegmentCount='" + bodySegmentCount + "'")
                .add("bodySegmentCountInAdults='" + bodySegmentCountInAdults + "'")
                .add("bodyLengthAverage=" + bodyLengthAverage)
                .add("bodyLengthIncreaseAverage=" + bodyLengthIncreaseAverage)
                .add("bodyMassIncreaseAverage=" + bodyMassIncreaseAverage)
                .add("intermoultPeriod='" + intermoultPeriod + "'")
                .add("premoultPeriod='" + premoultPeriod + "'")
                .add("postmoultPeriod='" + postmoultPeriod + "'")
                .add("variationWithinCohorts='" + variationWithinCohorts + "'")
                .add("sutureLocation='" + sutureLocation + "'")
                .add("cephalicSutureLocation='" + cephalicSutureLocation + "'")
                .add("postCephalicSutureLocation='" + postCephalicSutureLocation + "'")
                .add("resultingNamedMoultingConfiguration='" + resultingNamedMoultingConfiguration + "'")
                .add("egressDirection='" + egressDirection + "'")
                .add("positionExuviaeFoundIn='" + positionExuviaeFoundIn + "'")
                .add("moultingPhase='" + moultingPhase + "'")
                .add("moultingVariability='" + moultingVariability + "'")
                .add("calcificationEvent='" + calcificationEvent + "'")
                .add("heavyMetalReinforcement='" + heavyMetalReinforcement + "'")
                .add("otherBehaviour='" + otherBehaviour + "'")
                .add("exuviaeConsumed='" + exuviaeConsumed + "'")
                .add("exoskeletalMaterialReabsorption='" + exoskeletalMaterialReabsorption + "'")
                .add("fossilExuviaeQuality='" + fossilExuviaeQuality + "'")
                .add("generalComments='" + generalComments + "'")
                .toString();
    }
}
