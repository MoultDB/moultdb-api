package org.moultdb.api.model;

import org.moultdb.api.model.moutldbenum.EgressDirection;
import org.moultdb.api.model.moutldbenum.ExuviaeConsumption;
import org.moultdb.api.model.moutldbenum.ExuviaePosition;
import org.moultdb.api.model.moutldbenum.LifeHistoryStyle;
import org.moultdb.api.model.moutldbenum.MoultingPhase;
import org.moultdb.api.model.moutldbenum.MoultingVariability;
import org.moultdb.api.model.moutldbenum.Reabsorption;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

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
     * Define the number of specimen at each moult stage
     */
    private final Integer specimenCount;
    
    /**
     * Define the mode in which the species adds segments through its life history
     */
    private final String segmentAdditionMode;
    
    /**
     * Define the number of body segments added by individuals of the species at each moult stage
     */
    private final Integer bodySegmentsCountPerMoultStage;
    
    /**
     * Define the number of body segments in adult individuals of the species
     */
    private final Integer bodySegmentsCountInAdults;
    
    /**
     * Define the average body length of individuals at this defined and observed moult stage
     */
    private final Integer bodyLengthAverage;
    
    /**
     * Define the average body size increase with the previous moult stage
     */
    private final Integer bodyLengthIncreaseAverage;
    
    /**
     * Define the average quantity by which total body length increases with each moult stage in this species
     */
    private final BigDecimal bodyLengthIncreaseAveragePerMoult;
    
    /**
     * Define the unit of body length measurements
     */
    private final String measurementUnit;
    
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
    
    /**
     * Define whether juvenile moulting behaviours in same as in adults (=true) or different to in adults (=false)
     */
    private final Boolean juvenileMoultingBehaviours;
    
    /**
     * Define the location of moulting suture opened in juvenile specimens of this species in order to moult
     */
    private final String juvenileSutureLocation;
    
    /**
     * Define, if cephalic moulting sutures are used, the locations of these cephalic sutures in juvenile specimens
     */
    private final String juvenileCephalicSutureLocation;
    
    /**
     * Define, if post-cephalic moulting is used, the locations of these post-cephalic sutures in juvenile specimens
     */
    private final String juvenilePostCephalicSutureLocation;
    
    /**
     * Define the moult configurations resulting from a moulting event in juvenile specimens in the species formally described and named
     */
    private final String juvenileResultingNamedMoultingConfiguration;
    
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
    
    public MoultingCharacters(LifeHistoryStyle lifeHistoryStyle, Boolean hasTerminalAdultStage, Integer observedMoultCount,
                              Integer estimatedMoultCount, Integer specimenCount, String segmentAdditionMode,
                              Integer bodySegmentsCountPerMoultStage, Integer bodySegmentsCountInAdults,
                              Integer bodyLengthAverage, Integer bodyLengthIncreaseAverage,
                              BigDecimal bodyLengthIncreaseAveragePerMoult, String measurementUnit, String sutureLocation,
                              String cephalicSutureLocation, String postCephalicSutureLocation,
                              String resultingNamedMoultingConfiguration, EgressDirection egressDirection,
                              ExuviaePosition positionExuviaeFoundIn, MoultingPhase moultingPhase,
                              MoultingVariability moultingVariability, Boolean juvenileMoultingBehaviours,
                              String juvenileSutureLocation, String juvenileCephalicSutureLocation,
                              String juvenilePostCephalicSutureLocation, String juvenileResultingNamedMoultingConfiguration,
                              String otherBehaviour, ExuviaeConsumption exuviaeConsumption, Reabsorption reabsorption,
                              String fossilExuviaeQuality) {
        this.lifeHistoryStyle = lifeHistoryStyle;
        this.hasTerminalAdultStage = hasTerminalAdultStage;
        this.observedMoultCount = observedMoultCount;
        this.estimatedMoultCount = estimatedMoultCount;
        this.specimenCount = specimenCount;
        this.segmentAdditionMode = segmentAdditionMode;
        this.bodySegmentsCountPerMoultStage = bodySegmentsCountPerMoultStage;
        this.bodySegmentsCountInAdults = bodySegmentsCountInAdults;
        this.bodyLengthAverage = bodyLengthAverage;
        this.bodyLengthIncreaseAverage = bodyLengthIncreaseAverage;
        this.bodyLengthIncreaseAveragePerMoult = bodyLengthIncreaseAveragePerMoult;
        this.measurementUnit = measurementUnit;
        this.sutureLocation = sutureLocation;
        this.cephalicSutureLocation = cephalicSutureLocation;
        this.postCephalicSutureLocation = postCephalicSutureLocation;
        this.resultingNamedMoultingConfiguration = resultingNamedMoultingConfiguration;
        this.egressDirection = egressDirection;
        this.positionExuviaeFoundIn = positionExuviaeFoundIn;
        this.moultingPhase = moultingPhase;
        this.moultingVariability = moultingVariability;
        this.juvenileMoultingBehaviours = juvenileMoultingBehaviours;
        this.juvenileSutureLocation = juvenileSutureLocation;
        this.juvenileCephalicSutureLocation = juvenileCephalicSutureLocation;
        this.juvenilePostCephalicSutureLocation = juvenilePostCephalicSutureLocation;
        this.juvenileResultingNamedMoultingConfiguration = juvenileResultingNamedMoultingConfiguration;
        this.otherBehaviour = otherBehaviour;
        this.exuviaeConsumption = exuviaeConsumption;
        this.reabsorption = reabsorption;
        this.fossilExuviaeQuality = fossilExuviaeQuality;
    }
    
    public LifeHistoryStyle getLifeHistoryStyle() {
        return lifeHistoryStyle;
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
    
    public Integer getSpecimenCount() {
        return specimenCount;
    }
    
    public String getSegmentAdditionMode() {
        return segmentAdditionMode;
    }
    
    public Integer getBodySegmentsCountPerMoultStage() {
        return bodySegmentsCountPerMoultStage;
    }
    
    public Integer getBodySegmentsCountInAdults() {
        return bodySegmentsCountInAdults;
    }
    
    public Integer getBodyLengthAverage() {
        return bodyLengthAverage;
    }
    
    public Integer getBodyLengthIncreaseAverage() {
        return bodyLengthIncreaseAverage;
    }
    
    public BigDecimal getBodyLengthIncreaseAveragePerMoult() {
        return bodyLengthIncreaseAveragePerMoult;
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
    
    public Boolean getJuvenileMoultingBehaviours() {
        return juvenileMoultingBehaviours;
    }
    
    public String getJuvenileSutureLocation() {
        return juvenileSutureLocation;
    }
    
    public String getJuvenileCephalicSutureLocation() {
        return juvenileCephalicSutureLocation;
    }
    
    public String getJuvenilePostCephalicSutureLocation() {
        return juvenilePostCephalicSutureLocation;
    }
    
    public String getJuvenileResultingNamedMoultingConfiguration() {
        return juvenileResultingNamedMoultingConfiguration;
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
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MoultingCharacters that = (MoultingCharacters) o;
        return lifeHistoryStyle == that.lifeHistoryStyle
                && Objects.equals(hasTerminalAdultStage, that.hasTerminalAdultStage)
                && Objects.equals(observedMoultCount, that.observedMoultCount)
                && Objects.equals(estimatedMoultCount, that.estimatedMoultCount)
                && Objects.equals(specimenCount, that.specimenCount)
                && Objects.equals(segmentAdditionMode, that.segmentAdditionMode)
                && Objects.equals(bodySegmentsCountPerMoultStage, that.bodySegmentsCountPerMoultStage)
                && Objects.equals(bodySegmentsCountInAdults, that.bodySegmentsCountInAdults)
                && Objects.equals(bodyLengthAverage, that.bodyLengthAverage)
                && Objects.equals(bodyLengthIncreaseAverage, that.bodyLengthIncreaseAverage)
                && Objects.equals(bodyLengthIncreaseAveragePerMoult, that.bodyLengthIncreaseAveragePerMoult)
                && Objects.equals(measurementUnit, that.measurementUnit)
                && Objects.equals(sutureLocation, that.sutureLocation)
                && Objects.equals(cephalicSutureLocation, that.cephalicSutureLocation)
                && Objects.equals(postCephalicSutureLocation, that.postCephalicSutureLocation)
                && Objects.equals(resultingNamedMoultingConfiguration, that.resultingNamedMoultingConfiguration)
                && egressDirection == that.egressDirection
                && positionExuviaeFoundIn == that.positionExuviaeFoundIn
                && moultingPhase == that.moultingPhase
                && moultingVariability == that.moultingVariability
                && Objects.equals(juvenileMoultingBehaviours, that.juvenileMoultingBehaviours)
                && Objects.equals(juvenileSutureLocation, that.juvenileSutureLocation)
                && Objects.equals(juvenileCephalicSutureLocation, that.juvenileCephalicSutureLocation)
                && Objects.equals(juvenilePostCephalicSutureLocation, that.juvenilePostCephalicSutureLocation)
                && Objects.equals(juvenileResultingNamedMoultingConfiguration, that.juvenileResultingNamedMoultingConfiguration)
                && Objects.equals(otherBehaviour, that.otherBehaviour)
                && exuviaeConsumption == that.exuviaeConsumption
                && reabsorption == that.reabsorption
                && Objects.equals(fossilExuviaeQuality, that.fossilExuviaeQuality);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(lifeHistoryStyle, hasTerminalAdultStage, observedMoultCount, estimatedMoultCount, specimenCount,
                segmentAdditionMode, bodySegmentsCountPerMoultStage, bodySegmentsCountInAdults, bodyLengthAverage,
                bodyLengthIncreaseAverage, bodyLengthIncreaseAveragePerMoult, measurementUnit, sutureLocation,
                cephalicSutureLocation, postCephalicSutureLocation, resultingNamedMoultingConfiguration, egressDirection,
                positionExuviaeFoundIn, moultingPhase, moultingVariability, juvenileMoultingBehaviours, juvenileSutureLocation,
                juvenileCephalicSutureLocation, juvenilePostCephalicSutureLocation, juvenileResultingNamedMoultingConfiguration,
                otherBehaviour, exuviaeConsumption, reabsorption, fossilExuviaeQuality);
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", MoultingCharacters.class.getSimpleName() + "[", "]")
                .add("lifeHistoryStyle=" + lifeHistoryStyle)
                .add("hasTerminalAdultStage=" + hasTerminalAdultStage)
                .add("observedMoultCount=" + observedMoultCount)
                .add("estimatedMoultCount=" + estimatedMoultCount)
                .add("specimenCount=" + specimenCount)
                .add("segmentAdditionMode='" + segmentAdditionMode + "'")
                .add("bodySegmentsCountPerMoultStage=" + bodySegmentsCountPerMoultStage)
                .add("bodySegmentsCountInAdults=" + bodySegmentsCountInAdults)
                .add("bodyLengthAverage=" + bodyLengthAverage)
                .add("bodyLengthIncreaseAverage=" + bodyLengthIncreaseAverage)
                .add("bodyLengthIncreaseAveragePerMoult=" + bodyLengthIncreaseAveragePerMoult)
                .add("measurementUnit=" + measurementUnit)
                .add("sutureLocation='" + sutureLocation + "'")
                .add("cephalicSutureLocation='" + cephalicSutureLocation + "'")
                .add("postCephalicSutureLocation='" + postCephalicSutureLocation + "'")
                .add("resultingNamedMoultingConfiguration='" + resultingNamedMoultingConfiguration + "'")
                .add("egressDirection=" + egressDirection)
                .add("positionExuviaeFoundIn=" + positionExuviaeFoundIn)
                .add("moultingPhase=" + moultingPhase)
                .add("moultingVariability=" + moultingVariability)
                .add("juvenileMoultingBehaviours=" + juvenileMoultingBehaviours)
                .add("juvenileSutureLocation='" + juvenileSutureLocation + "'")
                .add("juvenileCephalicSutureLocation='" + juvenileCephalicSutureLocation + "'")
                .add("juvenilePostCephalicSutureLocation='" + juvenilePostCephalicSutureLocation + "'")
                .add("juvenileResultingNamedMoultingConfiguration='" + juvenileResultingNamedMoultingConfiguration + "'")
                .add("otherBehaviour='" + otherBehaviour + "'")
                .add("exuviaeConsumption=" + exuviaeConsumption)
                .add("reabsorption=" + reabsorption)
                .add("fossilExuviaeQuality='" + fossilExuviaeQuality + "'")
                .toString();
    }
}
