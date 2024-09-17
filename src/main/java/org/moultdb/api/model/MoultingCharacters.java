package org.moultdb.api.model;

import org.moultdb.api.model.moutldbenum.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-20
 */
public class MoultingCharacters {
    
    /**
     * Define whether individuals of this species show direct development from juvenile to adulthood,
     * or whether this development is punctuated by metamorphosis
     */
    private final LifeHistoryStyle lifeHistoryStyle;
    
    private final Set<LifeMode> lifeModes;
    
    private final String juvenileMoultCount;
    
    private final String majorMorphologicalTransitionCount;
    
    /**
     * Define whether there is a terminal adult moult (=true) or it's a continued moulting (=false) during adult stage
     */
    private final Boolean hasTerminalAdultStage;
    
    /**
     * Define the number of separate moult stages observed and described for this species' development (indefinite/variable = -1)
     */
    private final Integer observedMoultCount;
    
    /**
     * Define the number of separate moult stages estimated for this species' development, if not all observed and described
     */
    private final String estimatedMoultCount;

    /**
     * Define the mode in which the species adds segments through its life history
     */
    private final String segmentAdditionMode;
    
    /**
     * Define the number of body segments added by individuals of the species at each moult stage (can be two values)
     */
    private final String bodySegmentCount;
    
    /**
     * Define the number of body segments in adult individuals of the species (can be two values ar a range)
     */
    private final String bodySegmentCountInAdults;
    
    /**
     * Define the average body length of individuals at this defined and observed moult stage
     */
    private final BigDecimal bodyLengthAverage;
    
    /**
     * Define the average body length increase with the previous moult stage (can be a range)
     */
    private final BigDecimal bodyLengthIncreaseAverage;
    
    /**
     * Define the average body width of individuals at this defined and observed moult stage
     */
    private final BigDecimal bodyWidthAverage;
    
    /**
     * Define the average body Width increase with the previous moult stage (can be a range)
     */
    private final BigDecimal bodyWidthIncreaseAverage;
    
    /**
     * Define the average body mass increase with the previous moult stage (can be a range)
     */
    private final BigDecimal bodyMassIncreaseAverage;
    
    private final String devStagePeriod;
    
    private final String intermoultPeriod;
    
    private final String premoultPeriod;
    
    private final String postmoultPeriod;
    
    private final String variationWithinCohorts;
    
    /**
     * Define location of moulting suture opened in adult specimens of this species in order to moult
     */
    private final Set<String> sutureLocations;
    
    /**
     * Define the locations of cephalic sutures if cephalic moulting sutures are used
     */
    private final Set<String> cephalicSutureLocations;
    
    /**
     * Define the locations of post-cephalic sutures if post-cephalic moulting is used
     */
    private final Set<String> postCephalicSutureLocations;
    
    /**
     * Define the moult configurations resulting from a moulting event in the species formally described and named
     */
    private final Set<String> resultingNamedMoultingConfigurations;
    
    /**
     * Define the direction in which individuals of this species exist their exuviae
     */
    private final Set<EgressDirection> egressDirections;
    
    /**
     * Define the position in which moults of this species are found in - prone (dorsum-up) or supine (ventrum-up), or both
     */
    private final Set<ExuviaePosition> positionsExuviaeFoundIn;
    
    /**
     * Define whether individuals of this species moult all parts of the exoskeleton in one go, or in multiple phases
     */
    private final MoultingPhase moultingPhase;
    
    /**
     * Define whether individuals of this species vary in the ways they moult their exoskeletons? Such as whether different individuals use different moulting sutures, or produce a variety of resulting moulting configurations
     */
    private final MoultingVariability moultingVariability;
    
    private final Calcification calcificationEvent;
    
    private final Set<HeavyMetalReinforcement> heavyMetalReinforcements;
    
    /**
     * Define other notable behaviours, excepting methods of moulting itself, that the species shows in conjunction with individual moulting events
     */
    private final Set<String> otherBehaviours;
    
    /**
     * Define whether individuals of this species are known to consume any of their moulted exoskeletons
     */
    private final ExuviaeConsumption exuviaeConsumption;
    /**
     * Define whether  individuals of this species are known to reabsorb any of their previous exoskeleton during moulting
     */
    private final Reabsorption reabsorption;
    
    /**
     * Define preservational quality of fossil moults for the sample
     */
    private final Set<String> fossilExuviaeQualities;
    
    private final String generalComments;
    
    public MoultingCharacters(LifeHistoryStyle lifeHistoryStyle, Set<LifeMode> lifeModes, String juvenileMoultCount,
                              String majorMorphologicalTransitionCount, Boolean hasTerminalAdultStage,
                              Integer observedMoultCount, String estimatedMoultCount, String segmentAdditionMode,
                              String bodySegmentCount, String bodySegmentCountInAdults, BigDecimal bodyLengthAverage,
                              BigDecimal bodyLengthIncreaseAverage, BigDecimal bodyWidthAverage,
                              BigDecimal bodyWidthIncreaseAverage, BigDecimal bodyMassIncreaseAverage, String devStagePeriod,
                              String intermoultPeriod, String premoultPeriod, String postmoultPeriod,
                              String variationWithinCohorts, Set<String> sutureLocations, Set<String> cephalicSutureLocations,
                              Set<String> postCephalicSutureLocations,
                              Set<String> resultingNamedMoultingConfigurations, Set<EgressDirection> egressDirections,
                              Set<ExuviaePosition> positionsExuviaeFoundIn, MoultingPhase moultingPhase,
                              MoultingVariability moultingVariability, Calcification calcificationEvent,
                              Set<HeavyMetalReinforcement> heavyMetalReinforcements, Set<String> otherBehaviours,
                              ExuviaeConsumption exuviaeConsumption, Reabsorption reabsorption,
                              Set<String> fossilExuviaeQualities, String generalComments) {
        this.lifeHistoryStyle = lifeHistoryStyle;
        this.lifeModes = Collections.unmodifiableSet(lifeModes == null ? new HashSet<>(): new HashSet<>(lifeModes));
        this.juvenileMoultCount = juvenileMoultCount;
        this.majorMorphologicalTransitionCount = majorMorphologicalTransitionCount;
        this.hasTerminalAdultStage = hasTerminalAdultStage;
        this.observedMoultCount = observedMoultCount;
        this.estimatedMoultCount = estimatedMoultCount;
        this.segmentAdditionMode = segmentAdditionMode;
        this.bodySegmentCount = bodySegmentCount;
        this.bodySegmentCountInAdults = bodySegmentCountInAdults;
        this.bodyLengthAverage = bodyLengthAverage;
        this.bodyLengthIncreaseAverage = bodyLengthIncreaseAverage;
        this.bodyWidthAverage = bodyWidthAverage;
        this.bodyWidthIncreaseAverage = bodyWidthIncreaseAverage;
        this.bodyMassIncreaseAverage = bodyMassIncreaseAverage;
        this.devStagePeriod = devStagePeriod;
        this.intermoultPeriod = intermoultPeriod;
        this.premoultPeriod = premoultPeriod;
        this.postmoultPeriod = postmoultPeriod;
        this.variationWithinCohorts = variationWithinCohorts;
        this.sutureLocations = Collections.unmodifiableSet(sutureLocations == null ? new HashSet<>(): new HashSet<>(sutureLocations));;
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
        this.exuviaeConsumption = exuviaeConsumption;
        this.reabsorption = reabsorption;
        this.fossilExuviaeQualities = Collections.unmodifiableSet(fossilExuviaeQualities == null ? new HashSet<>(): new HashSet<>(fossilExuviaeQualities));
        this.generalComments = generalComments;
    }
    
    public LifeHistoryStyle getLifeHistoryStyle() {
        return lifeHistoryStyle;
    }
    
    public Set<LifeMode> getLifeModes() {
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
    
    public Integer getObservedMoultCount() {
        return observedMoultCount;
    }
    
    public String getEstimatedMoultCount() {
        return estimatedMoultCount;
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
    
    public BigDecimal getBodyWidthAverage() {
        return bodyWidthAverage;
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
    
    public Set<EgressDirection> getEgressDirections() {
        return egressDirections;
    }
    
    public Set<ExuviaePosition> getPositionsExuviaeFoundIn() {
        return positionsExuviaeFoundIn;
    }
    
    public MoultingPhase getMoultingPhase() {
        return moultingPhase;
    }
    
    public MoultingVariability getMoultingVariability() {
        return moultingVariability;
    }
    
    public Calcification getCalcificationEvent() {
        return calcificationEvent;
    }
    
    public Set<HeavyMetalReinforcement> getHeavyMetalReinforcements() {
        return heavyMetalReinforcements;
    }
    
    public Set<String> getOtherBehaviours() {
        return otherBehaviours;
    }
    
    public ExuviaeConsumption getExuviaeConsumption() {
        return exuviaeConsumption;
    }
    
    public Reabsorption getReabsorption() {
        return reabsorption;
    }
    
    public Set<String> getFossilExuviaeQualities() {
        return fossilExuviaeQualities;
    }
    
    public String getGeneralComments() {
        return generalComments;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MoultingCharacters that = (MoultingCharacters) o;
        return lifeHistoryStyle == that.lifeHistoryStyle
                && lifeModes == that.lifeModes
                && Objects.equals(juvenileMoultCount, that.juvenileMoultCount)
                && Objects.equals(majorMorphologicalTransitionCount, that.majorMorphologicalTransitionCount)
                && Objects.equals(hasTerminalAdultStage, that.hasTerminalAdultStage)
                && Objects.equals(observedMoultCount, that.observedMoultCount)
                && Objects.equals(estimatedMoultCount, that.estimatedMoultCount)
                && Objects.equals(segmentAdditionMode, that.segmentAdditionMode)
                && Objects.equals(bodySegmentCount, that.bodySegmentCount)
                && Objects.equals(bodySegmentCountInAdults, that.bodySegmentCountInAdults)
                && Objects.equals(bodyLengthAverage, that.bodyLengthAverage)
                && Objects.equals(bodyLengthIncreaseAverage, that.bodyLengthIncreaseAverage)
                && Objects.equals(bodyWidthAverage, that.bodyWidthAverage)
                && Objects.equals(bodyWidthIncreaseAverage, that.bodyWidthIncreaseAverage)
                && Objects.equals(bodyMassIncreaseAverage, that.bodyMassIncreaseAverage)
                && Objects.equals(devStagePeriod, that.devStagePeriod)
                && Objects.equals(intermoultPeriod, that.intermoultPeriod)
                && Objects.equals(premoultPeriod, that.premoultPeriod)
                && Objects.equals(postmoultPeriod, that.postmoultPeriod)
                && Objects.equals(variationWithinCohorts, that.variationWithinCohorts)
                && Objects.equals(sutureLocations, that.sutureLocations)
                && Objects.equals(cephalicSutureLocations, that.cephalicSutureLocations)
                && Objects.equals(postCephalicSutureLocations, that.postCephalicSutureLocations)
                && Objects.equals(resultingNamedMoultingConfigurations, that.resultingNamedMoultingConfigurations)
                && egressDirections == that.egressDirections
                && positionsExuviaeFoundIn == that.positionsExuviaeFoundIn
                && moultingPhase == that.moultingPhase
                && moultingVariability == that.moultingVariability
                && calcificationEvent == that.calcificationEvent
                && heavyMetalReinforcements == that.heavyMetalReinforcements
                && Objects.equals(otherBehaviours, that.otherBehaviours)
                && exuviaeConsumption == that.exuviaeConsumption
                && reabsorption == that.reabsorption
                && Objects.equals(fossilExuviaeQualities, that.fossilExuviaeQualities)
                && Objects.equals(generalComments, that.generalComments);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(lifeHistoryStyle, lifeModes, juvenileMoultCount, majorMorphologicalTransitionCount,
                hasTerminalAdultStage, observedMoultCount, estimatedMoultCount, segmentAdditionMode, bodySegmentCount,
                bodySegmentCountInAdults, bodyLengthAverage, bodyLengthIncreaseAverage, bodyWidthAverage,
                bodyWidthIncreaseAverage, bodyMassIncreaseAverage, devStagePeriod,
                intermoultPeriod, premoultPeriod, postmoultPeriod, variationWithinCohorts, sutureLocations,
                cephalicSutureLocations, postCephalicSutureLocations, resultingNamedMoultingConfigurations,
                egressDirections, positionsExuviaeFoundIn, moultingPhase, moultingVariability, calcificationEvent,
                heavyMetalReinforcements, otherBehaviours, exuviaeConsumption, reabsorption, fossilExuviaeQualities,
                generalComments);
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", MoultingCharacters.class.getSimpleName() + "[", "]")
                .add("lifeHistoryStyle=" + lifeHistoryStyle)
                .add("lifeModes=" + lifeModes)
                .add("juvenileMoultCount=" + juvenileMoultCount)
                .add("majorMorphologicalTransitionCount=" + majorMorphologicalTransitionCount)
                .add("hasTerminalAdultStage=" + hasTerminalAdultStage)
                .add("observedMoultCount=" + observedMoultCount)
                .add("estimatedMoultCount=" + estimatedMoultCount)
                .add("segmentAdditionMode='" + segmentAdditionMode + "'")
                .add("bodySegmentCount='" + bodySegmentCount + "'")
                .add("bodySegmentCountInAdults='" + bodySegmentCountInAdults + "'")
                .add("bodyLengthAverage=" + bodyLengthAverage)
                .add("bodyLengthIncreaseAverage='" + bodyLengthIncreaseAverage + "'")
                .add("bodyWidthAverage=" + bodyWidthAverage)
                .add("bodyWidthIncreaseAverage='" + bodyWidthIncreaseAverage + "'")
                .add("bodyMassIncreaseAverage='" + bodyMassIncreaseAverage + "'")
                .add("devStagePeriod='" + devStagePeriod + "'")
                .add("intermoultPeriod='" + intermoultPeriod + "'")
                .add("premoultPeriod='" + premoultPeriod + "'")
                .add("postmoultPeriod='" + postmoultPeriod + "'")
                .add("variationWithinCohorts='" + variationWithinCohorts + "'")
                .add("sutureLocations='" + sutureLocations + "'")
                .add("cephalicSutureLocations='" + cephalicSutureLocations + "'")
                .add("postCephalicSutureLocations='" + postCephalicSutureLocations + "'")
                .add("resultingNamedMoultingConfigurations='" + resultingNamedMoultingConfigurations + "'")
                .add("egressDirections=" + egressDirections)
                .add("positionsExuviaeFoundIn=" + positionsExuviaeFoundIn)
                .add("moultingPhase=" + moultingPhase)
                .add("moultingVariability=" + moultingVariability)
                .add("calcificationEvent=" + calcificationEvent)
                .add("heavyMetalReinforcements=" + heavyMetalReinforcements)
                .add("otherBehaviours='" + otherBehaviours + "'")
                .add("exuviaeConsumption=" + exuviaeConsumption)
                .add("reabsorption=" + reabsorption)
                .add("fossilExuviaeQualities='" + fossilExuviaeQualities + "'")
                .add("generalComments='" + generalComments + "'")
                .toString();
    }
}
