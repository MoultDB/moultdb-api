package org.moultdb.api.repository.dto;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
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
    private final String majorMorphologicalTransitionCount;
    private final Boolean hasTerminalAdultStage;
    private final Integer observedMoultStageCount;
    private final String estimatedMoultStageCount;
    private final String segmentAdditionMode;
    private final String bodySegmentCount;
    private final String bodySegmentCountInAdults;
    private final BigDecimal bodyLengthAverage;
    private final BigDecimal bodyWidthAverage;
    private final BigDecimal bodyLengthIncreaseAverage;
    private final BigDecimal bodyWidthIncreaseAverage;
    private final BigDecimal bodyMassIncreaseAverage;
    private final String devStagePeriod;
    private final String intermoultPeriod;
    private final String premoultPeriod;
    private final String postmoultPeriod;
    private final String variationWithinCohorts;
    private final Set<String> sutureLocations;
    private final Set<String> cephalicSutureLocations;
    private final Set<String> postCephalicSutureLocations;
    private final Set<String> resultingNamedMoultingConfigurations;
    private final Set<String> egressDirections;
    private final Set<String> positionsExuviaeFoundIn;
    private final String moultingPhase;
    private final String moultingVariability;
    private final String calcificationEvent;
    private final Set<String> heavyMetalReinforcements;
    private final Set<String> otherBehaviours;
    private final String exuviaeConsumed;
    private final String exoskeletalMaterialReabsorption;
    private final Set<String> fossilExuviaeQualities;
    private final String generalComments;
    
    public MoultingCharactersTO(Integer id, String generalComments)
            throws IllegalArgumentException {
        this(id, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                generalComments);
    }

    public MoultingCharactersTO(Integer id, String lifeHistoryStyle, Set<String> lifeModes,
                                String juvenileMoultCount, String majorMorphologicalTransitionCount,
                                Boolean hasTerminalAdultStage, Integer observedMoultStageCount,
                                String estimatedMoultStageCount, String segmentAdditionMode, String bodySegmentCount,
                                String bodySegmentCountInAdults, BigDecimal bodyLengthAverage, BigDecimal bodyWidthAverage,
                                BigDecimal bodyLengthIncreaseAverage, BigDecimal bodyWidthIncreaseAverage,
                                BigDecimal bodyMassIncreaseAverage, String devStagePeriod, String intermoultPeriod,
                                String premoultPeriod, String postmoultPeriod, String variationWithinCohorts,
                                Set<String> sutureLocations, Set<String> cephalicSutureLocations,
                                Set<String> postCephalicSutureLocations, Set<String> resultingNamedMoultingConfigurations,
                                Set<String> egressDirections, Set<String> positionsExuviaeFoundIn, String moultingPhase,
                                String moultingVariability, String calcificationEvent, Set<String> heavyMetalReinforcements,
                                Set<String> otherBehaviours, String exuviaeConsumed, String exoskeletalMaterialReabsorption,
                                Set<String> fossilExuviaeQualities, String generalComments)
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
        this.bodyWidthAverage = bodyWidthAverage;
        this.bodyLengthIncreaseAverage = bodyLengthIncreaseAverage;
        this.bodyWidthIncreaseAverage = bodyWidthIncreaseAverage;
        this.bodyMassIncreaseAverage = bodyMassIncreaseAverage;
        this.devStagePeriod = devStagePeriod;
        this.intermoultPeriod = intermoultPeriod;
        this.premoultPeriod = premoultPeriod;
        this.postmoultPeriod = postmoultPeriod;
        this.variationWithinCohorts = variationWithinCohorts;
        this.sutureLocations = Collections.unmodifiableSet(sutureLocations == null ? new HashSet<>(): new HashSet<>(sutureLocations));
        this.cephalicSutureLocations = Collections.unmodifiableSet(cephalicSutureLocations == null ? new HashSet<>(): new HashSet<>(cephalicSutureLocations));
        this.postCephalicSutureLocations = Collections.unmodifiableSet(postCephalicSutureLocations == null ? new HashSet<>(): new HashSet<>(postCephalicSutureLocations));
        this.resultingNamedMoultingConfigurations = Collections.unmodifiableSet(resultingNamedMoultingConfigurations == null ? new HashSet<>(): new HashSet<>(resultingNamedMoultingConfigurations));
        this.egressDirections = Collections.unmodifiableSet(egressDirections == null ? new HashSet<>(): new HashSet<>(egressDirections));
        this.positionsExuviaeFoundIn = Collections.unmodifiableSet(positionsExuviaeFoundIn == null ? new HashSet<>(): new HashSet<>(positionsExuviaeFoundIn));
        this.moultingPhase = moultingPhase;
        this.moultingVariability = moultingVariability;
        this.calcificationEvent = calcificationEvent;
        this.heavyMetalReinforcements = Collections.unmodifiableSet(heavyMetalReinforcements == null ? new HashSet<>(): new HashSet<>(heavyMetalReinforcements));
        this.otherBehaviours = Collections.unmodifiableSet(otherBehaviours == null ? new HashSet<>(): new HashSet<>(otherBehaviours));
        this.exuviaeConsumed = exuviaeConsumed;
        this.exoskeletalMaterialReabsorption = exoskeletalMaterialReabsorption;
        this.fossilExuviaeQualities = Collections.unmodifiableSet(fossilExuviaeQualities == null ? new HashSet<>(): new HashSet<>(fossilExuviaeQualities));
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
    
    public String getMajorMorphologicalTransitionCount() {
        return majorMorphologicalTransitionCount;
    }
    
    public Boolean getHasTerminalAdultStage() {
        return hasTerminalAdultStage;
    }
    
    public Integer getObservedMoultStageCount() {
        return observedMoultStageCount;
    }
    
    public String getEstimatedMoultStageCount() {
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
    
    public BigDecimal getBodyWidthAverage() {
        return bodyWidthAverage;
    }
    
    public BigDecimal getBodyLengthIncreaseAverage() {
        return bodyLengthIncreaseAverage;
    }
    
    public BigDecimal getBodyWidthIncreaseAverage() {
        return bodyWidthIncreaseAverage;
    }
    
    public BigDecimal getBodyMassIncreaseAverage() {
        return bodyMassIncreaseAverage;
    }
    
    public String getDevStagePeriod() {
        return devStagePeriod;
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
    
    public Set<String> getSutureLocations() {
        return sutureLocations;
    }
    
    public Set<String> getCephalicSutureLocations() {
        return cephalicSutureLocations;
    }
    
    public Set<String> getPostCephalicSutureLocations() {
        return postCephalicSutureLocations;
    }
    
    public Set<String> getResultingNamedMoultingConfigurations() {
        return resultingNamedMoultingConfigurations;
    }
    
    public Set<String> getEgressDirections() {
        return egressDirections;
    }
    
    public Set<String> getPositionsExuviaeFoundIn() {
        return positionsExuviaeFoundIn;
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
    
    public Set<String> getHeavyMetalReinforcements() {
        return heavyMetalReinforcements;
    }
    
    public Set<String> getOtherBehaviours() {
        return otherBehaviours;
    }
    
    public String getExuviaeConsumed() {
        return exuviaeConsumed;
    }
    
    public String getExoskeletalMaterialReabsorption() {
        return exoskeletalMaterialReabsorption;
    }
    
    public Set<String> getFossilExuviaeQualities() {
        return fossilExuviaeQualities;
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
                .add("bodyLengthAverage=" + bodyWidthAverage)
                .add("bodyLengthIncreaseAverage=" + bodyLengthIncreaseAverage)
                .add("bodyLengthIncreaseAverage=" + bodyWidthIncreaseAverage)
                .add("bodyMassIncreaseAverage=" + bodyMassIncreaseAverage)
                .add("devStagePeriod='" + devStagePeriod + "'")
                .add("intermoultPeriod='" + intermoultPeriod + "'")
                .add("premoultPeriod='" + premoultPeriod + "'")
                .add("postmoultPeriod='" + postmoultPeriod + "'")
                .add("variationWithinCohorts='" + variationWithinCohorts + "'")
                .add("sutureLocations='" + sutureLocations + "'")
                .add("cephalicSutureLocations='" + cephalicSutureLocations + "'")
                .add("postCephalicSutureLocations='" + postCephalicSutureLocations + "'")
                .add("resultingNamedMoultingConfigurations='" + resultingNamedMoultingConfigurations + "'")
                .add("egressDirections='" + egressDirections + "'")
                .add("positionsExuviaeFoundIn='" + positionsExuviaeFoundIn + "'")
                .add("moultingPhase='" + moultingPhase + "'")
                .add("moultingVariability='" + moultingVariability + "'")
                .add("calcificationEvent='" + calcificationEvent + "'")
                .add("heavyMetalReinforcements='" + heavyMetalReinforcements + "'")
                .add("otherBehaviours='" + otherBehaviours + "'")
                .add("exuviaeConsumed='" + exuviaeConsumed + "'")
                .add("exoskeletalMaterialReabsorption='" + exoskeletalMaterialReabsorption + "'")
                .add("fossilExuviaeQualities='" + fossilExuviaeQualities + "'")
                .add("generalComments='" + generalComments + "'")
                .toString();
    }
}
