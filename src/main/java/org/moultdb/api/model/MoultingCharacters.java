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
    
    private final Integer majorMorphologicalTransitionCount;
    
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
    private final Integer estimatedMoultCount;

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
     * Define the average body mass increase with the previous moult stage (can be a range)
     */
    private final BigDecimal bodyMassIncreaseAverage;
    
    private final String intermoultPeriod;
    
    private final String premoultPeriod;
    
    private final String postmoultPeriod;
    
    private final String variationWithinCohorts;
    
    /**
     * Define location of moulting suture opened in adult specimens of this species in order to moult
     */
    private final String sutureLocation;
    
    /**
     * Define the locations of cephalic sutures if cephalic moulting sutures are used
     */
    private final String cephalicSutureLocation;
    
    /**
     * Define the locations of post-cephalic sutures if post-cephalic moulting is used
     */
    private final String postCephalicSutureLocation;
    
    /**
     * Define the moult configurations resulting from a moulting event in the species formally described and named
     */
    private final String resultingNamedMoultingConfiguration;
    
    /**
     * Define the direction in which individuals of this species exist their exuviae
     */
    private final EgressDirection egressDirection;
    
    /**
     * Define the position in which moults of this species are found in - prone (dorsum-up) or supine (ventrum-up), or both
     */
    private final ExuviaePosition positionExuviaeFoundIn;
    
    /**
     * Define whether individuals of this species moult all parts of the exoskeleton in one go, or in multiple phases
     */
    private final MoultingPhase moultingPhase;
    
    /**
     * Define whether individuals of this species vary in the ways they moult their exoskeletons? Such as whether different individuals use different moulting sutures, or produce a variety of resulting moulting configurations
     */
    private final MoultingVariability moultingVariability;
    
    private final Calcification calcificationEvent;
    
    private final HeavyMetalReinforcement heavyMetalReinforcement;
    
    /**
     * Define other notable behaviours, excepting methods of moulting itself, that the species shows in conjunction with individual moulting events
     */
    private final String otherBehaviour;
    
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
    private final String fossilExuviaeQuality;
    
    private final String generalComments;
    
    public MoultingCharacters(LifeHistoryStyle lifeHistoryStyle, Set<LifeMode> lifeModes, String juvenileMoultCount,
                              Integer majorMorphologicalTransitionCount, Boolean hasTerminalAdultStage,
                              Integer observedMoultCount, Integer estimatedMoultCount, String segmentAdditionMode,
                              String bodySegmentCount, String bodySegmentCountInAdults, BigDecimal bodyLengthAverage,
                              BigDecimal bodyLengthIncreaseAverage, BigDecimal bodyMassIncreaseAverage, String intermoultPeriod,
                              String premoultPeriod, String postmoultPeriod, String variationWithinCohorts,
                              String sutureLocation, String cephalicSutureLocation, String postCephalicSutureLocation,
                              String resultingNamedMoultingConfiguration, EgressDirection egressDirection,
                              ExuviaePosition positionExuviaeFoundIn, MoultingPhase moultingPhase,
                              MoultingVariability moultingVariability, Calcification calcificationEvent,
                              HeavyMetalReinforcement heavyMetalReinforcement, String otherBehaviour,
                              ExuviaeConsumption exuviaeConsumption, Reabsorption reabsorption,
                              String fossilExuviaeQuality, String generalComments) {
        this.lifeHistoryStyle = lifeHistoryStyle;
        this.lifeModes =  Collections.unmodifiableSet(lifeModes == null ? new HashSet<>(): new HashSet<>(lifeModes));
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
        this.exuviaeConsumption = exuviaeConsumption;
        this.reabsorption = reabsorption;
        this.fossilExuviaeQuality = fossilExuviaeQuality;
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
    
    public Integer getMajorMorphologicalTransitionCount() {
        return majorMorphologicalTransitionCount;
    }
    
    public Boolean getHasTerminalAdultStage() {
        return hasTerminalAdultStage;
    }
    
    public Integer getObservedMoultCount() {
        return observedMoultCount;
    }
    
    public Integer getEstimatedMoultCount() {
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
    
    public EgressDirection getEgressDirection() {
        return egressDirection;
    }
    
    public ExuviaePosition getPositionExuviaeFoundIn() {
        return positionExuviaeFoundIn;
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
    
    public HeavyMetalReinforcement getHeavyMetalReinforcement() {
        return heavyMetalReinforcement;
    }
    
    public String getOtherBehaviour() {
        return otherBehaviour;
    }
    
    public ExuviaeConsumption getExuviaeConsumption() {
        return exuviaeConsumption;
    }
    
    public Reabsorption getReabsorption() {
        return reabsorption;
    }
    
    public String getFossilExuviaeQuality() {
        return fossilExuviaeQuality;
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
                && Objects.equals(bodyMassIncreaseAverage, that.bodyMassIncreaseAverage)
                && Objects.equals(intermoultPeriod, that.intermoultPeriod)
                && Objects.equals(premoultPeriod, that.premoultPeriod)
                && Objects.equals(postmoultPeriod, that.postmoultPeriod)
                && Objects.equals(variationWithinCohorts, that.variationWithinCohorts)
                && Objects.equals(sutureLocation, that.sutureLocation)
                && Objects.equals(cephalicSutureLocation, that.cephalicSutureLocation)
                && Objects.equals(postCephalicSutureLocation, that.postCephalicSutureLocation)
                && Objects.equals(resultingNamedMoultingConfiguration, that.resultingNamedMoultingConfiguration)
                && egressDirection == that.egressDirection
                && positionExuviaeFoundIn == that.positionExuviaeFoundIn
                && moultingPhase == that.moultingPhase
                && moultingVariability == that.moultingVariability
                && calcificationEvent == that.calcificationEvent
                && heavyMetalReinforcement == that.heavyMetalReinforcement
                && Objects.equals(otherBehaviour, that.otherBehaviour)
                && exuviaeConsumption == that.exuviaeConsumption
                && reabsorption == that.reabsorption
                && Objects.equals(fossilExuviaeQuality, that.fossilExuviaeQuality)
                && Objects.equals(generalComments, that.generalComments);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(lifeHistoryStyle, lifeModes, juvenileMoultCount, majorMorphologicalTransitionCount,
                hasTerminalAdultStage, observedMoultCount, estimatedMoultCount, segmentAdditionMode, bodySegmentCount,
                bodySegmentCountInAdults, bodyLengthAverage, bodyLengthIncreaseAverage, bodyMassIncreaseAverage,
                intermoultPeriod, premoultPeriod, postmoultPeriod, variationWithinCohorts, sutureLocation,
                cephalicSutureLocation, postCephalicSutureLocation, resultingNamedMoultingConfiguration,
                egressDirection, positionExuviaeFoundIn, moultingPhase, moultingVariability, calcificationEvent,
                heavyMetalReinforcement, otherBehaviour, exuviaeConsumption, reabsorption, fossilExuviaeQuality,
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
                .add("bodyMassIncreaseAverage='" + bodyMassIncreaseAverage + "'")
                .add("intermoultPeriod='" + intermoultPeriod + "'")
                .add("premoultPeriod='" + premoultPeriod + "'")
                .add("postmoultPeriod='" + postmoultPeriod + "'")
                .add("variationWithinCohorts='" + variationWithinCohorts + "'")
                .add("sutureLocation='" + sutureLocation + "'")
                .add("cephalicSutureLocation='" + cephalicSutureLocation + "'")
                .add("postCephalicSutureLocation='" + postCephalicSutureLocation + "'")
                .add("resultingNamedMoultingConfiguration='" + resultingNamedMoultingConfiguration + "'")
                .add("egressDirection=" + egressDirection)
                .add("positionExuviaeFoundIn=" + positionExuviaeFoundIn)
                .add("moultingPhase=" + moultingPhase)
                .add("moultingVariability=" + moultingVariability)
                .add("calcificationEvent=" + calcificationEvent)
                .add("heavyMetalReinforcement=" + heavyMetalReinforcement)
                .add("otherBehaviour='" + otherBehaviour + "'")
                .add("exuviaeConsumption=" + exuviaeConsumption)
                .add("reabsorption=" + reabsorption)
                .add("fossilExuviaeQuality='" + fossilExuviaeQuality + "'")
                .add("generalComments='" + generalComments + "'")
                .toString();
    }
}
