package org.moultdb.importer.fossilannotation;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-12-13
 */
public class FossilAnnotationBean {
    
    private String order;
    private String taxon;
    private String publishedReferenceText;
    private String publishedReferenceAcc;
    private String museumCollection;
    private String museumAccession;
    private String location;
    private String geologicalFormation;
    private String geologicalAge;
    private String fossilPreservationType;
    private String environment;
    private Integer specimenCount;
    private Integer specimenCountPerMoultStage;
    private String specimenType;
    private String lifeHistoryStyle;
    private Boolean adultStageMoulting;
    private Integer observedMoultStagesCount;
    private String observedMoultStages;
    private Integer estimatedMoultStagesCount;
    private String segmentAdditionMode;
    private String bodySegmentsCountPerMoultStage;
    private Integer bodySegmentsCountInAdult;
    private String bodyLengthIncreaseAveragePerMoult;
    private BigDecimal bodyLengthAverageAtEachMoultStage;
    private String unitOfMeasurement;
    private String moultingSutureLocation;
    private String cephalicSutureLocation;
    private String postCephalicSutureLocation;
    private String resultingNamedMoultingConfigurations;
    private String egressDirectionDuringMoulting;
    private String positionExuviaeFoundIn;
    private String moultingPhase;
    private String moultingVariability;
    private String juvenileMoultingBehaviours;
    private String juvenileMoultingSutureLocation;
    private String juvenileCephalicSutureLocation;
    private String juvenilePostCephalicSutureLocation;
    private String juvenileResultingNamedMoultingConfigurations;
    private String otherBehavioursAssociatedWithMoulting;
    private String exuviaeConsumption;
    private String reabsorption;
    private String fossilExuviaeQuality;
    private String evidenceCode;
    private String confidence;
    
    public FossilAnnotationBean() {
    }
    
    public String getOrder() {
        return order;
    }
    
    public void setOrder(String order) {
        this.order = order;
    }
    
    public String getTaxon() {
        return taxon;
    }
    
    public void setTaxon(String taxon) {
        this.taxon = taxon;
    }
    
    public String getScientificName() {
        return order + " " + taxon;
    }
    
    public String getPublishedReferenceText() {
        return publishedReferenceText;
    }
    
    public void setPublishedReferenceText(String publishedReferenceText) {
        this.publishedReferenceText = publishedReferenceText;
    }
    
    public String getPublishedReferenceAcc() {
        return publishedReferenceAcc;
    }
    
    public void setPublishedReferenceAcc(String publishedReferenceAcc) {
        this.publishedReferenceAcc = publishedReferenceAcc;
    }
    
    public String getMuseumCollection() {
        return museumCollection;
    }
    
    public void setMuseumCollection(String museumCollection) {
        this.museumCollection = museumCollection;
    }
    
    public String getMuseumAccession() {
        return museumAccession;
    }
    
    public void setMuseumAccession(String museumAccession) {
        this.museumAccession = museumAccession;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getGeologicalFormation() {
        return geologicalFormation;
    }
    
    public void setGeologicalFormation(String geologicalFormation) {
        this.geologicalFormation = geologicalFormation;
    }
    
    public String getGeologicalAge() {
        return geologicalAge;
    }
    
    public void setGeologicalAge(String geologicalAge) {
        this.geologicalAge = geologicalAge;
    }
    
    public String getFossilPreservationType() {
        return fossilPreservationType;
    }
    
    public void setFossilPreservationType(String fossilPreservationType) {
        this.fossilPreservationType = fossilPreservationType;
    }
    
    public String getEnvironment() {
        return environment;
    }
    
    public void setEnvironment(String environment) {
        this.environment = environment;
    }
    
    public Integer getSpecimenCount() {
        return specimenCount;
    }
    
    public void setSpecimenCount(Integer specimenCount) {
        this.specimenCount = specimenCount;
    }
    
    public Integer getSpecimenCountPerMoultStage() {
        return specimenCountPerMoultStage;
    }
    
    public void setSpecimenCountPerMoultStage(Integer specimenCountPerMoultStage) {
        this.specimenCountPerMoultStage = specimenCountPerMoultStage;
    }
    
    public String getSpecimenType() {
        return specimenType;
    }
    
    public void setSpecimenType(String specimenType) {
        this.specimenType = specimenType;
    }
    
    public String getLifeHistoryStyle() {
        return lifeHistoryStyle;
    }
    
    public void setLifeHistoryStyle(String lifeHistoryStyle) {
        this.lifeHistoryStyle = lifeHistoryStyle;
    }
    
    public Boolean getAdultStageMoulting() {
        return adultStageMoulting;
    }
    
    public void setAdultStageMoulting(Boolean adultStageMoulting) {
        this.adultStageMoulting = adultStageMoulting;
    }
    
    public Integer getObservedMoultStagesCount() {
        return observedMoultStagesCount;
    }
    
    public void setObservedMoultStagesCount(Integer observedMoultStagesCount) {
        this.observedMoultStagesCount = observedMoultStagesCount;
    }
    
    public String getObservedMoultStages() {
        return observedMoultStages;
    }
    
    public void setObservedMoultStages(String observedMoultStages) {
        this.observedMoultStages = observedMoultStages;
    }
    
    public Integer getEstimatedMoultStagesCount() {
        return estimatedMoultStagesCount;
    }
    
    public void setEstimatedMoultStagesCount(Integer estimatedMoultStagesCount) {
        this.estimatedMoultStagesCount = estimatedMoultStagesCount;
    }
    
    public String getSegmentAdditionMode() {
        return segmentAdditionMode;
    }
    
    public void setSegmentAdditionMode(String segmentAdditionMode) {
        this.segmentAdditionMode = segmentAdditionMode;
    }
    
    public String getBodySegmentsCountPerMoultStage() {
        return bodySegmentsCountPerMoultStage;
    }
    
    public void setBodySegmentsCountPerMoultStage(String bodySegmentsCountPerMoultStage) {
        this.bodySegmentsCountPerMoultStage = bodySegmentsCountPerMoultStage;
    }
    
    public Integer getBodySegmentsCountInAdult() {
        return bodySegmentsCountInAdult;
    }
    
    public void setBodySegmentsCountInAdult(Integer bodySegmentsCountInAdult) {
        this.bodySegmentsCountInAdult = bodySegmentsCountInAdult;
    }
    
    public String getBodyLengthIncreaseAveragePerMoult() {
        return bodyLengthIncreaseAveragePerMoult;
    }
    
    public void setBodyLengthIncreaseAveragePerMoult(String bodyLengthIncreaseAveragePerMoult) {
        this.bodyLengthIncreaseAveragePerMoult = bodyLengthIncreaseAveragePerMoult;
    }
    
    public BigDecimal getBodyLengthAverageAtEachMoultStage() {
        return bodyLengthAverageAtEachMoultStage;
    }
    
    public void setBodyLengthAverageAtEachMoultStage(BigDecimal bodyLengthAverageAtEachMoultStage) {
        this.bodyLengthAverageAtEachMoultStage = bodyLengthAverageAtEachMoultStage;
    }
    
    public String getUnitOfMeasurement() {
        return unitOfMeasurement;
    }
    
    public void setUnitOfMeasurement(String unitOfMeasurement) {
        this.unitOfMeasurement = unitOfMeasurement;
    }
    
    public String getMoultingSutureLocation() {
        return moultingSutureLocation;
    }
    
    public void setMoultingSutureLocation(String moultingSutureLocation) {
        this.moultingSutureLocation = moultingSutureLocation;
    }
    
    public String getCephalicSutureLocation() {
        return cephalicSutureLocation;
    }
    
    public void setCephalicSutureLocation(String cephalicSutureLocation) {
        this.cephalicSutureLocation = cephalicSutureLocation;
    }
    
    public String getPostCephalicSutureLocation() {
        return postCephalicSutureLocation;
    }
    
    public void setPostCephalicSutureLocation(String postCephalicSutureLocation) {
        this.postCephalicSutureLocation = postCephalicSutureLocation;
    }
    
    public String getResultingNamedMoultingConfigurations() {
        return resultingNamedMoultingConfigurations;
    }
    
    public void setResultingNamedMoultingConfigurations(String resultingNamedMoultingConfigurations) {
        this.resultingNamedMoultingConfigurations = resultingNamedMoultingConfigurations;
    }
    
    public String getEgressDirectionDuringMoulting() {
        return egressDirectionDuringMoulting;
    }
    
    public void setEgressDirectionDuringMoulting(String egressDirectionDuringMoulting) {
        this.egressDirectionDuringMoulting = egressDirectionDuringMoulting;
    }
    
    public String getPositionExuviaeFoundIn() {
        return positionExuviaeFoundIn;
    }
    
    public void setPositionExuviaeFoundIn(String positionExuviaeFoundIn) {
        this.positionExuviaeFoundIn = positionExuviaeFoundIn;
    }
    
    public String getMoultingPhase() {
        return moultingPhase;
    }
    
    public void setMoultingPhase(String moultingPhase) {
        this.moultingPhase = moultingPhase;
    }
    
    public String getMoultingVariability() {
        return moultingVariability;
    }
    
    public void setMoultingVariability(String moultingVariability) {
        this.moultingVariability = moultingVariability;
    }
    
    public String getJuvenileMoultingBehaviours() {
        return juvenileMoultingBehaviours;
    }
    
    public void setJuvenileMoultingBehaviours(String juvenileMoultingBehaviours) {
        this.juvenileMoultingBehaviours = juvenileMoultingBehaviours;
    }
    
    public String getJuvenileMoultingSutureLocation() {
        return juvenileMoultingSutureLocation;
    }
    
    public void setJuvenileMoultingSutureLocation(String juvenileMoultingSutureLocation) {
        this.juvenileMoultingSutureLocation = juvenileMoultingSutureLocation;
    }
    
    public String getJuvenileCephalicSutureLocation() {
        return juvenileCephalicSutureLocation;
    }
    
    public void setJuvenileCephalicSutureLocation(String juvenileCephalicSutureLocation) {
        this.juvenileCephalicSutureLocation = juvenileCephalicSutureLocation;
    }
    
    public String getJuvenilePostCephalicSutureLocation() {
        return juvenilePostCephalicSutureLocation;
    }
    
    public void setJuvenilePostCephalicSutureLocation(String juvenilePostCephalicSutureLocation) {
        this.juvenilePostCephalicSutureLocation = juvenilePostCephalicSutureLocation;
    }
    
    public String getJuvenileResultingNamedMoultingConfigurations() {
        return juvenileResultingNamedMoultingConfigurations;
    }
    
    public void setJuvenileResultingNamedMoultingConfigurations(String juvenileResultingNamedMoultingConfigurations) {
        this.juvenileResultingNamedMoultingConfigurations = juvenileResultingNamedMoultingConfigurations;
    }
    
    public String getOtherBehavioursAssociatedWithMoulting() {
        return otherBehavioursAssociatedWithMoulting;
    }
    
    public void setOtherBehavioursAssociatedWithMoulting(String otherBehavioursAssociatedWithMoulting) {
        this.otherBehavioursAssociatedWithMoulting = otherBehavioursAssociatedWithMoulting;
    }
    
    public String getExuviaeConsumption() {
        return exuviaeConsumption;
    }
    
    public void setExuviaeConsumption(String exuviaeConsumption) {
        this.exuviaeConsumption = exuviaeConsumption;
    }
    
    public String getReabsorption() {
        return reabsorption;
    }
    
    public void setReabsorption(String reabsorption) {
        this.reabsorption = reabsorption;
    }
    
    public String getFossilExuviaeQuality() {
        return fossilExuviaeQuality;
    }
    
    public void setFossilExuviaeQuality(String fossilExuviaeQuality) {
        this.fossilExuviaeQuality = fossilExuviaeQuality;
    }
    
    public String getEvidenceCode() {
        return evidenceCode;
    }
    
    public void setEvidenceCode(String evidenceCode) {
        this.evidenceCode = evidenceCode;
    }
    
    public String getConfidence() {
        return confidence;
    }
    
    public void setConfidence(String confidence) {
        this.confidence = confidence;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        FossilAnnotationBean that = (FossilAnnotationBean) o;
        return Objects.equals(order, that.order)
                && Objects.equals(taxon, that.taxon)
                && Objects.equals(publishedReferenceText, that.publishedReferenceText)
                && Objects.equals(publishedReferenceAcc, that.publishedReferenceAcc)
                && Objects.equals(museumCollection, that.museumCollection)
                && Objects.equals(museumAccession, that.museumAccession)
                && Objects.equals(location, that.location)
                && Objects.equals(geologicalFormation, that.geologicalFormation)
                && Objects.equals(geologicalAge, that.geologicalAge)
                && Objects.equals(fossilPreservationType, that.fossilPreservationType)
                && Objects.equals(environment, that.environment)
                && Objects.equals(specimenCount, that.specimenCount)
                && Objects.equals(specimenCountPerMoultStage, that.specimenCountPerMoultStage)
                && Objects.equals(specimenType, that.specimenType)
                && Objects.equals(lifeHistoryStyle, that.lifeHistoryStyle)
                && Objects.equals(adultStageMoulting, that.adultStageMoulting)
                && Objects.equals(observedMoultStagesCount, that.observedMoultStagesCount)
                && Objects.equals(observedMoultStages, that.observedMoultStages)
                && Objects.equals(estimatedMoultStagesCount, that.estimatedMoultStagesCount)
                && Objects.equals(segmentAdditionMode, that.segmentAdditionMode)
                && Objects.equals(bodySegmentsCountPerMoultStage, that.bodySegmentsCountPerMoultStage)
                && Objects.equals(bodySegmentsCountInAdult, that.bodySegmentsCountInAdult)
                && Objects.equals(bodyLengthIncreaseAveragePerMoult, that.bodyLengthIncreaseAveragePerMoult)
                && Objects.equals(bodyLengthAverageAtEachMoultStage, that.bodyLengthAverageAtEachMoultStage)
                && Objects.equals(unitOfMeasurement, that.unitOfMeasurement)
                && Objects.equals(moultingSutureLocation, that.moultingSutureLocation)
                && Objects.equals(cephalicSutureLocation, that.cephalicSutureLocation)
                && Objects.equals(postCephalicSutureLocation, that.postCephalicSutureLocation)
                && Objects.equals(resultingNamedMoultingConfigurations, that.resultingNamedMoultingConfigurations)
                && Objects.equals(egressDirectionDuringMoulting, that.egressDirectionDuringMoulting)
                && Objects.equals(positionExuviaeFoundIn, that.positionExuviaeFoundIn)
                && Objects.equals(moultingPhase, that.moultingPhase)
                && Objects.equals(moultingVariability, that.moultingVariability)
                && Objects.equals(juvenileMoultingBehaviours, that.juvenileMoultingBehaviours)
                && Objects.equals(juvenileMoultingSutureLocation, that.juvenileMoultingSutureLocation)
                && Objects.equals(juvenileCephalicSutureLocation, that.juvenileCephalicSutureLocation)
                && Objects.equals(juvenilePostCephalicSutureLocation, that.juvenilePostCephalicSutureLocation)
                && Objects.equals(juvenileResultingNamedMoultingConfigurations, that.juvenileResultingNamedMoultingConfigurations)
                && Objects.equals(otherBehavioursAssociatedWithMoulting, that.otherBehavioursAssociatedWithMoulting)
                && Objects.equals(exuviaeConsumption, that.exuviaeConsumption)
                && Objects.equals(reabsorption, that.reabsorption)
                && Objects.equals(fossilExuviaeQuality, that.fossilExuviaeQuality)
                && Objects.equals(evidenceCode, that.evidenceCode)
                && Objects.equals(confidence, that.confidence);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(order, taxon, publishedReferenceText, publishedReferenceAcc, museumCollection, museumAccession, location,
                geologicalFormation, geologicalAge, fossilPreservationType, environment, specimenCount, specimenCountPerMoultStage,
                specimenType, lifeHistoryStyle, adultStageMoulting, observedMoultStagesCount, observedMoultStages,
                estimatedMoultStagesCount, segmentAdditionMode, bodySegmentsCountPerMoultStage,
                bodySegmentsCountInAdult, bodyLengthIncreaseAveragePerMoult, bodyLengthAverageAtEachMoultStage,
                unitOfMeasurement, moultingSutureLocation, cephalicSutureLocation, postCephalicSutureLocation,
                resultingNamedMoultingConfigurations, egressDirectionDuringMoulting, positionExuviaeFoundIn,
                moultingPhase, moultingVariability, juvenileMoultingBehaviours, juvenileMoultingSutureLocation,
                juvenileCephalicSutureLocation, juvenilePostCephalicSutureLocation,
                juvenileResultingNamedMoultingConfigurations, otherBehavioursAssociatedWithMoulting,
                exuviaeConsumption, reabsorption, fossilExuviaeQuality, evidenceCode, confidence);
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", FossilAnnotationBean.class.getSimpleName() + "[", "]")
                .add("order='" + order + "'")
                .add("taxon='" + taxon + "'")
                .add("publishedReferenceText='" + publishedReferenceText + "'")
                .add("publishedReferenceAcc='" + publishedReferenceAcc + "'")
                .add("museumCollection='" + museumCollection + "'")
                .add("museumAccession='" + museumAccession + "'")
                .add("location='" + location + "'")
                .add("geologicalFormation='" + geologicalFormation + "'")
                .add("geologicalAge='" + geologicalAge + "'")
                .add("fossilPreservationType='" + fossilPreservationType + "'")
                .add("environment='" + environment + "'")
                .add("specimenCount=" + specimenCount)
                .add("specimenCountPerMoultStage=" + specimenCountPerMoultStage)
                .add("specimenType='" + specimenType + "'")
                .add("lifeHistoryStyle='" + lifeHistoryStyle + "'")
                .add("adultStageMoulting='" + adultStageMoulting + "'")
                .add("observedMoultStagesCount=" + observedMoultStagesCount)
                .add("observedMoultStages='" + observedMoultStages + "'")
                .add("estimatedMoultStagesCount=" + estimatedMoultStagesCount)
                .add("segmentAdditionMode='" + segmentAdditionMode + "'")
                .add("bodySegmentsCountPerMoultStage='" + bodySegmentsCountPerMoultStage + "'")
                .add("bodySegmentsCountInAdult='" + bodySegmentsCountInAdult + "'")
                .add("bodyLengthIncreaseAveragePerMoult='" + bodyLengthIncreaseAveragePerMoult + "'")
                .add("bodyLengthAverageAtEachMoultStage='" + bodyLengthAverageAtEachMoultStage + "'")
                .add("unitOfMeasurement='" + unitOfMeasurement + "'")
                .add("moultingSutureLocation='" + moultingSutureLocation + "'")
                .add("cephalicSutureLocation='" + cephalicSutureLocation + "'")
                .add("postCephalicSutureLocation='" + postCephalicSutureLocation + "'")
                .add("resultingNamedMoultingConfigurations='" + resultingNamedMoultingConfigurations + "'")
                .add("egressDirectionDuringMoulting='" + egressDirectionDuringMoulting + "'")
                .add("positionExuviaeFoundIn='" + positionExuviaeFoundIn + "'")
                .add("moultingPhase='" + moultingPhase + "'")
                .add("moultingVariability='" + moultingVariability + "'")
                .add("juvenileMoultingBehaviours='" + juvenileMoultingBehaviours + "'")
                .add("juvenileMoultingSutureLocation='" + juvenileMoultingSutureLocation + "'")
                .add("juvenileCephalicSutureLocation='" + juvenileCephalicSutureLocation + "'")
                .add("juvenilePostCephalicSutureLocation='" + juvenilePostCephalicSutureLocation + "'")
                .add("juvenileResultingNamedMoultingConfigurations='" + juvenileResultingNamedMoultingConfigurations + "'")
                .add("otherBehavioursAssociatedWithMoulting='" + otherBehavioursAssociatedWithMoulting + "'")
                .add("exuviaeConsumption='" + exuviaeConsumption + "'")
                .add("reabsorption='" + reabsorption + "'")
                .add("fossilExuviaeQuality='" + fossilExuviaeQuality + "'")
                .add("evidenceCode='" + evidenceCode + "'")
                .add("confidence='" + confidence + "'")
                .toString();
    }
}
