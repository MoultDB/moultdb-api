package org.moultdb.importer.taxonannotation;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-12-13
 */
public class TaxonAnnotationBean {
    
    private String order;
    private String taxon;
    private String determinedBy;
    private String publishedReferenceText;
    private String publishedReferenceAcc;
    private String contributor;
    private String museumCollection;
    private String museumAccession;
    private String locationName;
    private String locationGps;
    private String geologicalFormation;
    private String geologicalAge;
    private Set<String> fossilPreservationType;
    private String environment;
    private String biozone;
    private String sampleSpecimenCount;
    private String annotSpecimenCount;
    private String sex;
    private String previousReproductiveState;
    private String previousDevStage;
    private String reproductiveState;
    private String devStage;
    private Set<String> specimenType;
    private String lifeHistoryStyle;
    private Set<String> lifeModes;
    private String juvenileMoultCount;
    private String majorMorphologicalTransitionCount;
    private Boolean adultStageMoulting;
    private Integer observedMoultStagesCount;
    private String estimatedMoultStagesCount;
    private String segmentAdditionMode;
    private String bodySegmentCount;
    private String bodySegmentCountInAdults;
    private BigDecimal bodyLengthAverage;
    private BigDecimal bodyWidthAverage;
    private BigDecimal bodyLengthIncreaseAverage;
    private BigDecimal bodyWidthIncreaseAverage;
    private BigDecimal bodyMassIncreaseAverage;
    private String stagePeriod;
    private String intermoultPeriod;
    private String premoultPeriod;
    private String postmoultPeriod;
    private String variationWithinCohorts;
    private Set<String> moultingSutureLocation;
    private Set<String> cephalicSutureLocation;
    private Set<String> postCephalicSutureLocation;
    private Set<String> resultingNamedMoultingConfigurations;
    private Set<String> egressDirectionDuringMoulting;
    private Set<String> positionExuviaeFoundIn;
    private String moultingPhase;
    private String moultingVariability;
    private String calcificationEvent;
    private Set<String> heavyMetalReinforcement;
    private Set<String> otherBehavioursAssociatedWithMoulting;
    private String exuviaeConsumption;
    private String reabsorption;
    private Set<String> fossilExuviaeQuality;
    private String evidenceCode;
    private String confidence;
    private String generalComments;
    
    public TaxonAnnotationBean() {
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
    
    public String getLocationName() {
        return locationName;
    }
    
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
    
    public String getLocationGps() {
        return locationGps;
    }
    
    public void setLocationGps(String locationGps) {
        this.locationGps = locationGps;
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
    
    public Set<String> getFossilPreservationType() {
        return fossilPreservationType;
    }
    
    public void setFossilPreservationType(Set<String> fossilPreservationType) {
        this.fossilPreservationType = fossilPreservationType;
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
    
    public String getSampleSpecimenCount() {
        return sampleSpecimenCount;
    }
    
    public void setSampleSpecimenCount(String sampleSpecimenCount) {
        this.sampleSpecimenCount = sampleSpecimenCount;
    }
    
    public String getAnnotSpecimenCount() {
        return annotSpecimenCount;
    }
    
    public void setAnnotSpecimenCount(String annotSpecimenCount) {
        this.annotSpecimenCount = annotSpecimenCount;
    }
    
    public String getSex() {
        return sex;
    }
    
    public void setSex(String sex) {
        this.sex = sex;
    }
    
    public String getPreviousReproductiveState() {
        return previousReproductiveState;
    }
    
    public void setPreviousReproductiveState(String previousReproductiveState) {
        this.previousReproductiveState = previousReproductiveState;
    }
    
    public String getPreviousDevStage() {
        return previousDevStage;
    }
    
    public void setPreviousDevStage(String previousDevStage) {
        this.previousDevStage = previousDevStage;
    }
    
    public String getReproductiveState() {
        return reproductiveState;
    }
    
    public void setReproductiveState(String reproductiveState) {
        this.reproductiveState = reproductiveState;
    }
    
    public String getDevStage() {
        return devStage;
    }
    
    public void setDevStage(String devStage) {
        this.devStage = devStage;
    }
    
    public Set<String> getSpecimenType() {
        return specimenType;
    }
    
    public void setSpecimenType(Set<String> specimenType) {
        this.specimenType = specimenType;
    
    }
    
    public String getLifeHistoryStyle() {
        return lifeHistoryStyle;
    }
    
    public void setLifeHistoryStyle(String lifeHistoryStyle) {
        this.lifeHistoryStyle = convertToLowerCase(lifeHistoryStyle);
    }
    
    public Set<String> getLifeModes() {
        return lifeModes;
    }
    
    public void setLifeModes(Set<String> lifeModes) {
        this.lifeModes = lifeModes;
    }
    
    public String getJuvenileMoultCount() {
        return juvenileMoultCount;
    }
    
    public void setJuvenileMoultCount(String juvenileMoultCount) {
        this.juvenileMoultCount = juvenileMoultCount;
    }
    
    public String getMajorMorphologicalTransitionCount() {
        return majorMorphologicalTransitionCount;
    }
    
    public void setMajorMorphologicalTransitionCount(String majorMorphologicalTransitionCount) {
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
    
    public String getEstimatedMoultStagesCount() {
        return estimatedMoultStagesCount;
    }
    
    public void setEstimatedMoultStagesCount(String estimatedMoultStagesCount) {
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
    
    public BigDecimal getBodyWidthAverage() {
        return bodyWidthAverage;
    }
    
    public void setBodyWidthAverage(BigDecimal bodyWidthAverage) {
        this.bodyWidthAverage = bodyWidthAverage;
    }
    
    public BigDecimal getBodyLengthIncreaseAverage() {
        return bodyLengthIncreaseAverage;
    }
    
    public void setBodyLengthIncreaseAverage(BigDecimal bodyLengthIncreaseAverage) {
        this.bodyLengthIncreaseAverage = bodyLengthIncreaseAverage;
    }
    
    public BigDecimal getBodyWidthIncreaseAverage() {
        return bodyWidthIncreaseAverage;
    }
    
    public void setBodyWidthIncreaseAverage(BigDecimal bodyWidthIncreaseAverage) {
        this.bodyWidthIncreaseAverage = bodyWidthIncreaseAverage;
    }
    
    public BigDecimal getBodyMassIncreaseAverage() {
        return bodyMassIncreaseAverage;
    }
    
    public void setBodyMassIncreaseAverage(BigDecimal bodyMassIncreaseAverage) {
        this.bodyMassIncreaseAverage = bodyMassIncreaseAverage;
    }
    
    public String getStagePeriod() {
        return stagePeriod;
    }
    
    public void setStagePeriod(String stagePeriod) {
        this.stagePeriod = stagePeriod;
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
    
    public Set<String> getMoultingSutureLocation() {
        return moultingSutureLocation;
    }
    
    public void setMoultingSutureLocation(Set<String> moultingSutureLocation) {
        this.moultingSutureLocation = moultingSutureLocation;
    }
    
    public Set<String> getCephalicSutureLocation() {
        return cephalicSutureLocation;
    }
    
    public void setCephalicSutureLocation(Set<String> cephalicSutureLocation) {
        this.cephalicSutureLocation = cephalicSutureLocation;
    }
    
    public Set<String> getPostCephalicSutureLocation() {
        return postCephalicSutureLocation;
    }
    
    public void setPostCephalicSutureLocation(Set<String> postCephalicSutureLocation) {
        this.postCephalicSutureLocation = postCephalicSutureLocation;
    }
    
    public Set<String> getResultingNamedMoultingConfigurations() {
        return resultingNamedMoultingConfigurations;
    }
    
    public void setResultingNamedMoultingConfigurations(Set<String> resultingNamedMoultingConfigurations) {
        this.resultingNamedMoultingConfigurations = resultingNamedMoultingConfigurations;
    }
    
    public Set<String> getEgressDirectionDuringMoulting() {
        return egressDirectionDuringMoulting;
    }
    
    public void setEgressDirectionDuringMoulting(Set<String> egressDirectionDuringMoulting) {
        this.egressDirectionDuringMoulting = egressDirectionDuringMoulting;
    }
    
    public Set<String> getPositionExuviaeFoundIn() {
        return positionExuviaeFoundIn;
    }
    
    public void setPositionExuviaeFoundIn(Set<String> positionExuviaeFoundIn) {
        this.positionExuviaeFoundIn = positionExuviaeFoundIn;
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
    
    public Set<String> getHeavyMetalReinforcement() {
        return heavyMetalReinforcement;
    }
    
    public void setHeavyMetalReinforcement(Set<String> heavyMetalReinforcement) {
        this.heavyMetalReinforcement = heavyMetalReinforcement;
    }
    
    public Set<String> getOtherBehavioursAssociatedWithMoulting() {
        return otherBehavioursAssociatedWithMoulting;
    }
    
    public void setOtherBehavioursAssociatedWithMoulting(Set<String> otherBehavioursAssociatedWithMoulting) {
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
    
    public Set<String> getFossilExuviaeQuality() {
        return fossilExuviaeQuality;
    }
    
    public void setFossilExuviaeQuality(Set<String> fossilExuviaeQuality) {
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
        TaxonAnnotationBean that = (TaxonAnnotationBean) o;
        return Objects.equals(order, that.order)
                && Objects.equals(taxon, that.taxon)
                && Objects.equals(determinedBy, that.determinedBy)
                && Objects.equals(publishedReferenceText, that.publishedReferenceText)
                && Objects.equals(publishedReferenceAcc, that.publishedReferenceAcc)
                && Objects.equals(contributor, that.contributor)
                && Objects.equals(museumCollection, that.museumCollection)
                && Objects.equals(museumAccession, that.museumAccession)
                && Objects.equals(locationName, that.locationName)
                && Objects.equals(locationGps, that.locationGps)
                && Objects.equals(geologicalFormation, that.geologicalFormation)
                && Objects.equals(geologicalAge, that.geologicalAge)
                && Objects.equals(fossilPreservationType, that.fossilPreservationType)
                && Objects.equals(environment, that.environment)
                && Objects.equals(biozone, that.biozone)
                && Objects.equals(sampleSpecimenCount, that.sampleSpecimenCount)
                && Objects.equals(annotSpecimenCount, that.annotSpecimenCount)
                && Objects.equals(sex, that.sex)
                && Objects.equals(previousReproductiveState, that.previousReproductiveState)
                && Objects.equals(previousDevStage, that.previousDevStage)
                && Objects.equals(reproductiveState, that.reproductiveState)
                && Objects.equals(devStage, that.devStage)
                && Objects.equals(specimenType, that.specimenType)
                && Objects.equals(lifeHistoryStyle, that.lifeHistoryStyle)
                && Objects.equals(lifeModes, that.lifeModes)
                && Objects.equals(juvenileMoultCount, that.juvenileMoultCount)
                && Objects.equals(majorMorphologicalTransitionCount, that.majorMorphologicalTransitionCount)
                && Objects.equals(adultStageMoulting, that.adultStageMoulting)
                && Objects.equals(observedMoultStagesCount, that.observedMoultStagesCount)
                && Objects.equals(estimatedMoultStagesCount, that.estimatedMoultStagesCount)
                && Objects.equals(segmentAdditionMode, that.segmentAdditionMode)
                && Objects.equals(bodySegmentCount, that.bodySegmentCount)
                && Objects.equals(bodySegmentCountInAdults, that.bodySegmentCountInAdults)
                && Objects.equals(bodyLengthAverage, that.bodyLengthAverage)
                && Objects.equals(bodyWidthAverage, that.bodyWidthAverage)
                && Objects.equals(bodyLengthIncreaseAverage, that.bodyLengthIncreaseAverage)
                && Objects.equals(bodyWidthIncreaseAverage, that.bodyWidthIncreaseAverage)
                && Objects.equals(bodyMassIncreaseAverage, that.bodyMassIncreaseAverage)
                && Objects.equals(stagePeriod, that.stagePeriod)
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
                museumCollection, museumAccession, locationName, locationGps, geologicalFormation, geologicalAge,
                fossilPreservationType, environment, biozone, sampleSpecimenCount, annotSpecimenCount, sex,
                previousReproductiveState, previousDevStage, reproductiveState, devStage,
                specimenType, lifeHistoryStyle, lifeModes, juvenileMoultCount,
                majorMorphologicalTransitionCount, adultStageMoulting, observedMoultStagesCount, 
                estimatedMoultStagesCount, segmentAdditionMode, bodySegmentCount, bodySegmentCountInAdults,
                bodyLengthAverage, bodyWidthAverage, bodyLengthIncreaseAverage, bodyWidthIncreaseAverage,
                bodyMassIncreaseAverage, stagePeriod, intermoultPeriod, premoultPeriod, postmoultPeriod,
                variationWithinCohorts, moultingSutureLocation, cephalicSutureLocation,
                postCephalicSutureLocation, resultingNamedMoultingConfigurations, egressDirectionDuringMoulting,
                positionExuviaeFoundIn, moultingPhase, moultingVariability, calcificationEvent, heavyMetalReinforcement,
                otherBehavioursAssociatedWithMoulting, exuviaeConsumption, reabsorption, fossilExuviaeQuality,
                evidenceCode, confidence, generalComments);
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", TaxonAnnotationBean.class.getSimpleName() + "[", "]")
                .add("order='" + order + "'")
                .add("taxon='" + taxon + "'")
                .add("determinedBy='" + determinedBy + "'")
                .add("publishedReferenceText='" + publishedReferenceText + "'")
                .add("publishedReferenceAcc='" + publishedReferenceAcc + "'")
                .add("contributor='" + contributor + "'")
                .add("museumCollection='" + museumCollection + "'")
                .add("museumAccession='" + museumAccession + "'")
                .add("locationName='" + locationName + "'")
                .add("locationGps='" + locationGps + "'")
                .add("geologicalFormation='" + geologicalFormation + "'")
                .add("geologicalAge='" + geologicalAge + "'")
                .add("fossilPreservationType='" + fossilPreservationType + "'")
                .add("environment='" + environment + "'")
                .add("biozone='" + biozone + "'")
                .add("sampleSpecimenCount=" + sampleSpecimenCount)
                .add("annotSpecimenCount=" + annotSpecimenCount)
                .add("sex=" + sex)
                .add("previousReproductiveState=" + previousReproductiveState)
                .add("previousDevStage='" + previousDevStage + "'")
                .add("reproductiveState=" + reproductiveState)
                .add("devStage='" + devStage + "'")
                .add("specimenType='" + specimenType + "'")
                .add("lifeHistoryStyle='" + lifeHistoryStyle + "'")
                .add("lifeModes='" + lifeModes + "'")
                .add("juvenileMoultCount='" + juvenileMoultCount + "'")
                .add("majorMorphologicalTransitionCount='" + majorMorphologicalTransitionCount + "'")
                .add("adultStageMoulting=" + adultStageMoulting)
                .add("observedMoultStagesCount=" + observedMoultStagesCount)
                .add("estimatedMoultStagesCount=" + estimatedMoultStagesCount)
                .add("segmentAdditionMode='" + segmentAdditionMode + "'")
                .add("bodySegmentCount='" + bodySegmentCount + "'")
                .add("bodySegmentCountInAdults='" + bodySegmentCountInAdults + "'")
                .add("bodyLengthAverage=" + bodyLengthAverage)
                .add("bodyWidthAverage=" + bodyWidthAverage)
                .add("bodyLengthIncreaseAverage='" + bodyLengthIncreaseAverage + "'")
                .add("bodyWidthIncreaseAverage='" + bodyWidthIncreaseAverage + "'")
                .add("bodyMassAverage=" + bodyMassIncreaseAverage)
                .add("stagePeriod='" + stagePeriod + "'")
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
