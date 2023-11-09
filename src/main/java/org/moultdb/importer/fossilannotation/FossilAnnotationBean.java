package org.moultdb.importer.fossilannotation;

import org.apache.commons.lang3.StringUtils;

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
    private String determinedBy;
    private String publishedReferenceText;
    private String publishedReferenceAcc;
    private String contributor;
    private String museumCollection;
    private String museumAccession;
    private String location;
    private String geologicalFormation;
    private String geologicalAge;
    private String fossilPreservationType;
    private String environment;
    private String biozone;
    private Integer specimenCount;
    private String specimenType;
    private String lifeHistoryStyle;
    private String lifeMode;
    private Integer juvenileMoultCount;
    private Integer majorMorphologicalTransitionCount;
    private Boolean adultStageMoulting;
    private Integer observedMoultStagesCount;
    private String observedMoultStage;
    private Integer estimatedMoultStagesCount;
    private String segmentAdditionMode;
    private String bodySegmentCount;
    private String bodySegmentCountInAdults;
    private BigDecimal bodyLengthAverage;
    private String bodyLengthIncreaseAverage;
    private BigDecimal bodyMassIncreaseAverage;
    private String intermoultPeriod;
    private String premoultPeriod;
    private String postmoultPeriod;
    private String variationWithinCohorts;
    private String moultingSutureLocation;
    private String cephalicSutureLocation;
    private String postCephalicSutureLocation;
    private String resultingNamedMoultingConfigurations;
    private String egressDirectionDuringMoulting;
    private String positionExuviaeFoundIn;
    private String moultingPhase;
    private String moultingVariability;
    private String calcificationEvent;
    private String heavyMetalReinforcement;
    private String otherBehavioursAssociatedWithMoulting;
    private String exuviaeConsumption;
    private String reabsorption;
    private String fossilExuviaeQuality;
    private String evidenceCode;
    private String confidence;
    private String generalComments;
    
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
    
    public String getDeterminedBy() {
        return determinedBy;
    }
    
    public void setDeterminedBy(String determinedBy) {
        this.determinedBy = determinedBy;
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
    
    public String getContributor() {
        return contributor;
    }
    
    public void setContributor(String contributor) {
        this.contributor = contributor;
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
        this.fossilPreservationType = convertToLowerCase(fossilPreservationType);
    }
    
    public String getEnvironment() {
        return environment;
    }
    
    public void setEnvironment(String environment) {
        this.environment = environment;
    }
    
    public String getBiozone() {
        return biozone;
    }
    
    public void setBiozone(String biozone) {
        this.biozone = biozone;
    }
    
    public Integer getSpecimenCount() {
        return specimenCount;
    }
    
    public void setSpecimenCount(Integer specimenCount) {
        this.specimenCount = specimenCount;
    }
    
    public String getSpecimenType() {
        return specimenType;
    }
    
    public void setSpecimenType(String specimenType) {
        this.specimenType = convertToLowerCase(specimenType);
    
    }
    
    public String getLifeHistoryStyle() {
        return lifeHistoryStyle;
    }
    
    public void setLifeHistoryStyle(String lifeHistoryStyle) {
        this.lifeHistoryStyle = convertToLowerCase(lifeHistoryStyle);
    }
    
    public String getLifeMode() {
        return lifeMode;
    }
    
    public void setLifeMode(String lifeMode) {
        this.lifeMode = convertToLowerCase(lifeMode);
    }
    
    public Integer getJuvenileMoultCount() {
        return juvenileMoultCount;
    }
    
    public void setJuvenileMoultCount(Integer juvenileMoultCount) {
        this.juvenileMoultCount = juvenileMoultCount;
    }
    
    public Integer getMajorMorphologicalTransitionCount() {
        return majorMorphologicalTransitionCount;
    }
    
    public void setMajorMorphologicalTransitionCount(Integer majorMorphologicalTransitionCount) {
        this.majorMorphologicalTransitionCount = majorMorphologicalTransitionCount;
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
    
    public String getObservedMoultStage() {
        return observedMoultStage;
    }
    
    public void setObservedMoultStage(String observedMoultStage) {
        this.observedMoultStage = observedMoultStage;
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
        this.segmentAdditionMode = convertToLowerCase(segmentAdditionMode);
    }
    
    public String getBodySegmentCount() {
        return bodySegmentCount;
    }
    
    public void setBodySegmentCount(String bodySegmentCount) {
        this.bodySegmentCount = bodySegmentCount;
    }
    
    public String getBodySegmentCountInAdults() {
        return bodySegmentCountInAdults;
    }
    
    public void setBodySegmentCountInAdults(String bodySegmentCountInAdults) {
        this.bodySegmentCountInAdults = bodySegmentCountInAdults;
    }
    
    public BigDecimal getBodyLengthAverage() {
        return bodyLengthAverage;
    }
    
    public void setBodyLengthAverage(BigDecimal bodyLengthAverage) {
        this.bodyLengthAverage = bodyLengthAverage;
    }
    
    public String getBodyLengthIncreaseAverage() {
        return bodyLengthIncreaseAverage;
    }
    
    public void setBodyLengthIncreaseAverage(String bodyLengthIncreaseAverage) {
        this.bodyLengthIncreaseAverage = bodyLengthIncreaseAverage;
    }
    
    public BigDecimal getBodyMassIncreaseAverage() {
        return bodyMassIncreaseAverage;
    }
    
    public void setBodyMassIncreaseAverage(BigDecimal bodyMassIncreaseAverage) {
        this.bodyMassIncreaseAverage = bodyMassIncreaseAverage;
    }
    
    public String getIntermoultPeriod() {
        return intermoultPeriod;
    }
    
    public void setIntermoultPeriod(String intermoultPeriod) {
        this.intermoultPeriod = intermoultPeriod;
    }
    
    public String getPremoultPeriod() {
        return premoultPeriod;
    }
    
    public void setPremoultPeriod(String premoultPeriod) {
        this.premoultPeriod = premoultPeriod;
    }
    
    public String getPostmoultPeriod() {
        return postmoultPeriod;
    }
    
    public void setPostmoultPeriod(String postmoultPeriod) {
        this.postmoultPeriod = postmoultPeriod;
    }
    
    public String getVariationWithinCohorts() {
        return variationWithinCohorts;
    }
    
    public void setVariationWithinCohorts(String variationWithinCohorts) {
        this.variationWithinCohorts = variationWithinCohorts;
    }
    
    public String getMoultingSutureLocation() {
        return moultingSutureLocation;
    }
    
    public void setMoultingSutureLocation(String moultingSutureLocation) {
        this.moultingSutureLocation = convertToLowerCase(moultingSutureLocation);
    }
    
    public String getCephalicSutureLocation() {
        return cephalicSutureLocation;
    }
    
    public void setCephalicSutureLocation(String cephalicSutureLocation) {
        this.cephalicSutureLocation = convertToLowerCase(cephalicSutureLocation);
    }
    
    public String getPostCephalicSutureLocation() {
        return postCephalicSutureLocation;
    }
    
    public void setPostCephalicSutureLocation(String postCephalicSutureLocation) {
        this.postCephalicSutureLocation = convertToLowerCase(postCephalicSutureLocation);
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
        this.egressDirectionDuringMoulting = convertToLowerCase(egressDirectionDuringMoulting);
    }
    
    public String getPositionExuviaeFoundIn() {
        return positionExuviaeFoundIn;
    }
    
    public void setPositionExuviaeFoundIn(String positionExuviaeFoundIn) {
        this.positionExuviaeFoundIn = convertToLowerCase(positionExuviaeFoundIn);
    }
    
    public String getMoultingPhase() {
        return moultingPhase;
    }
    
    public void setMoultingPhase(String moultingPhase) {
        this.moultingPhase = convertToLowerCase(moultingPhase);
    }
    
    public String getMoultingVariability() {
        return moultingVariability;
    }
    
    public void setMoultingVariability(String moultingVariability) {
        this.moultingVariability = convertToLowerCase(moultingVariability);
    }
    
    public String getCalcificationEvent() {
        return calcificationEvent;
    }
    
    public void setCalcificationEvent(String calcificationEvent) {
        this.calcificationEvent = calcificationEvent;
    }
    
    public String getHeavyMetalReinforcement() {
        return heavyMetalReinforcement;
    }
    
    public void setHeavyMetalReinforcement(String heavyMetalReinforcement) {
        this.heavyMetalReinforcement = heavyMetalReinforcement;
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
        this.exuviaeConsumption = convertToLowerCase(exuviaeConsumption);
    }
    
    public String getReabsorption() {
        return reabsorption;
    }
    
    public void setReabsorption(String reabsorption) {
        this.reabsorption = convertToLowerCase(reabsorption);
    }
    
    public String getFossilExuviaeQuality() {
        return fossilExuviaeQuality;
    }
    
    public void setFossilExuviaeQuality(String fossilExuviaeQuality) {
        this.fossilExuviaeQuality = convertToLowerCase(fossilExuviaeQuality);
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
    
    public String getGeneralComments() {
        return generalComments;
    }
    
    public void setGeneralComments(String generalComments) {
        this.generalComments = generalComments;
    }
    
    private String convertToLowerCase(String s) {
        return StringUtils.isBlank(s) ? null : s.toLowerCase();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FossilAnnotationBean that = (FossilAnnotationBean) o;
        return Objects.equals(order, that.order)
                && Objects.equals(taxon, that.taxon)
                && Objects.equals(determinedBy, that.determinedBy)
                && Objects.equals(publishedReferenceText, that.publishedReferenceText)
                && Objects.equals(publishedReferenceAcc, that.publishedReferenceAcc)
                && Objects.equals(contributor, that.contributor)
                && Objects.equals(museumCollection, that.museumCollection)
                && Objects.equals(museumAccession, that.museumAccession)
                && Objects.equals(location, that.location)
                && Objects.equals(geologicalFormation, that.geologicalFormation)
                && Objects.equals(geologicalAge, that.geologicalAge)
                && Objects.equals(fossilPreservationType, that.fossilPreservationType)
                && Objects.equals(environment, that.environment)
                && Objects.equals(biozone, that.biozone)
                && Objects.equals(specimenCount, that.specimenCount)
                && Objects.equals(specimenType, that.specimenType)
                && Objects.equals(lifeHistoryStyle, that.lifeHistoryStyle)
                && Objects.equals(lifeMode, that.lifeMode)
                && Objects.equals(juvenileMoultCount, that.juvenileMoultCount)
                && Objects.equals(majorMorphologicalTransitionCount, that.majorMorphologicalTransitionCount)
                && Objects.equals(adultStageMoulting, that.adultStageMoulting)
                && Objects.equals(observedMoultStagesCount, that.observedMoultStagesCount)
                && Objects.equals(observedMoultStage, that.observedMoultStage)
                && Objects.equals(estimatedMoultStagesCount, that.estimatedMoultStagesCount)
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
                && Objects.equals(moultingSutureLocation, that.moultingSutureLocation)
                && Objects.equals(cephalicSutureLocation, that.cephalicSutureLocation)
                && Objects.equals(postCephalicSutureLocation, that.postCephalicSutureLocation)
                && Objects.equals(resultingNamedMoultingConfigurations, that.resultingNamedMoultingConfigurations)
                && Objects.equals(egressDirectionDuringMoulting, that.egressDirectionDuringMoulting)
                && Objects.equals(positionExuviaeFoundIn, that.positionExuviaeFoundIn)
                && Objects.equals(moultingPhase, that.moultingPhase)
                && Objects.equals(moultingVariability, that.moultingVariability)
                && Objects.equals(calcificationEvent, that.calcificationEvent)
                && Objects.equals(heavyMetalReinforcement, that.heavyMetalReinforcement)
                && Objects.equals(otherBehavioursAssociatedWithMoulting, that.otherBehavioursAssociatedWithMoulting)
                && Objects.equals(exuviaeConsumption, that.exuviaeConsumption)
                && Objects.equals(reabsorption, that.reabsorption)
                && Objects.equals(fossilExuviaeQuality, that.fossilExuviaeQuality)
                && Objects.equals(evidenceCode, that.evidenceCode)
                && Objects.equals(confidence, that.confidence)
                && Objects.equals(generalComments, that.generalComments);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(order, taxon, determinedBy, publishedReferenceText, publishedReferenceAcc, contributor,
                museumCollection, museumAccession, location, geologicalFormation, geologicalAge, fossilPreservationType,
                environment, biozone, specimenCount, specimenType, lifeHistoryStyle, lifeMode, juvenileMoultCount,
                majorMorphologicalTransitionCount, adultStageMoulting, observedMoultStagesCount, observedMoultStage,
                estimatedMoultStagesCount, segmentAdditionMode, bodySegmentCount, bodySegmentCountInAdults,
                bodyLengthAverage, bodyLengthIncreaseAverage, bodyMassIncreaseAverage, intermoultPeriod, premoultPeriod,
                postmoultPeriod, variationWithinCohorts, moultingSutureLocation, cephalicSutureLocation,
                postCephalicSutureLocation, resultingNamedMoultingConfigurations, egressDirectionDuringMoulting,
                positionExuviaeFoundIn, moultingPhase, moultingVariability, calcificationEvent, heavyMetalReinforcement,
                otherBehavioursAssociatedWithMoulting, exuviaeConsumption, reabsorption, fossilExuviaeQuality,
                evidenceCode, confidence, generalComments);
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", FossilAnnotationBean.class.getSimpleName() + "[", "]")
                .add("order='" + order + "'")
                .add("taxon='" + taxon + "'")
                .add("determinedBy='" + determinedBy + "'")
                .add("publishedReferenceText='" + publishedReferenceText + "'")
                .add("publishedReferenceAcc='" + publishedReferenceAcc + "'")
                .add("contributor='" + contributor + "'")
                .add("museumCollection='" + museumCollection + "'")
                .add("museumAccession='" + museumAccession + "'")
                .add("location='" + location + "'")
                .add("geologicalFormation='" + geologicalFormation + "'")
                .add("geologicalAge='" + geologicalAge + "'")
                .add("fossilPreservationType='" + fossilPreservationType + "'")
                .add("environment='" + environment + "'")
                .add("biozone='" + biozone + "'")
                .add("specimenCount=" + specimenCount)
                .add("specimenType='" + specimenType + "'")
                .add("lifeHistoryStyle='" + lifeHistoryStyle + "'")
                .add("lifeMode='" + lifeMode + "'")
                .add("juvenileMoultCount='" + juvenileMoultCount + "'")
                .add("majorMorphologicalTransitionCount='" + majorMorphologicalTransitionCount + "'")
                .add("adultStageMoulting=" + adultStageMoulting)
                .add("observedMoultStagesCount=" + observedMoultStagesCount)
                .add("observedMoultStage='" + observedMoultStage + "'")
                .add("estimatedMoultStagesCount=" + estimatedMoultStagesCount)
                .add("segmentAdditionMode='" + segmentAdditionMode + "'")
                .add("bodySegmentCount='" + bodySegmentCount + "'")
                .add("bodySegmentCountInAdults='" + bodySegmentCountInAdults + "'")
                .add("bodyLengthAverage=" + bodyLengthAverage)
                .add("bodyLengthIncreaseAverage='" + bodyLengthIncreaseAverage + "'")
                .add("bodyMassAverage=" + bodyMassIncreaseAverage)
                .add("intermoultPeriod='" + intermoultPeriod + "'")
                .add("premoultPeriod='" + premoultPeriod + "'")
                .add("postmoultPeriod='" + postmoultPeriod + "'")
                .add("variationWithinCohorts='" + variationWithinCohorts + "'")
                .add("moultingSutureLocation='" + moultingSutureLocation + "'")
                .add("cephalicSutureLocation='" + cephalicSutureLocation + "'")
                .add("postCephalicSutureLocation='" + postCephalicSutureLocation + "'")
                .add("resultingNamedMoultingConfigurations='" + resultingNamedMoultingConfigurations + "'")
                .add("egressDirectionDuringMoulting='" + egressDirectionDuringMoulting + "'")
                .add("positionExuviaeFoundIn='" + positionExuviaeFoundIn + "'")
                .add("moultingPhase='" + moultingPhase + "'")
                .add("moultingVariability='" + moultingVariability + "'")
                .add("calcificationEvent='" + calcificationEvent + "'")
                .add("heavyMetalReinforcement='" + heavyMetalReinforcement + "'")
                .add("otherBehavioursAssociatedWithMoulting='" + otherBehavioursAssociatedWithMoulting + "'")
                .add("exuviaeConsumption='" + exuviaeConsumption + "'")
                .add("reabsorption='" + reabsorption + "'")
                .add("fossilExuviaeQuality='" + fossilExuviaeQuality + "'")
                .add("evidenceCode='" + evidenceCode + "'")
                .add("confidence='" + confidence + "'")
                .add("generalComments='" + generalComments + "'")
                .toString();
    }
}
