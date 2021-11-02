package org.moultdb.api.model;

import java.util.Objects;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-20
 */
public class GeneralMoultingCharacters {
    
    /**
     *  Define whether this insect is hemimetabolous (=true) or holometabolous (= false) (it's gradual or punctuated by metamorphosis)
     */
    private final Boolean isHemimetabolous;
    
    /**
     *  Define count of moults required to reach adult stage (indefinite/variable = -1)
     */
    private final Integer moultCount;
    
    /**
     *  Define body size increase between each moulting phase
     */
    private final Integer sizeIncrease;
    
    /**
     *  Define whether there is a terminal adult moult (=true) or it's a continued moulting (=false) during adult stage
     */
    private final Boolean hasAdultStage;
    
    /**
     *  Define whether the development is anamorphic (=true) vs. epimorphic (=false) (segments added/not added during moults)
     */
    private final Boolean isAnamorphic;
    
    /**
     *  Define whether the number of moults is fixed  (=true) or variable (=false) within one species
     */
    private final Boolean hasFixedMoultNumber;

    private final SutureLocation sutureLocation;
    
    private final EgressDirection egressDirection;
    
    /**
     *  Define whether exuviae is fragmented (=true) or retained (=false) as a single empty unit
     */
    private final Boolean isFragmentedExuviae;
    
    /**
     *  Define whether entire exoskeleton moulted during one phase (=true), or biphasic moulting (=false)
     */
    private final Boolean isMonoPhasicMoulting;
    
    /**
     *  Define whether there is a presence (=true) or absence (=false) of exoskeletal material reabsorption before moulting
     */
    private final Boolean hasExoskeletalMaterialReabsorption;
    
    /**
     *  Define whether there is a presence (=true) or absence (=false) of consumption of exuviae after moulting
     */
    private final Boolean hasExuviaeConsumed;
    
    /**
     *  Extent of repair of exoskeleton damages occurring with each moult
     */
    private final Integer repairExtent;
    
    /**
     *  Define whether moulting of individuals is synchronous (=true) or asynchronous (=false)
     */
    private final Boolean massMoulting;
    
    /**
     *  Define whether moulting events are linked to mating events (=true) or not (=false)
     */
    private final Boolean isMatingLinked;
    
    /**
     *  E.g. ecdysone and juvenile hormone signalling and regulation
     */
    private final String hormoneRegulation;
    
    private final Version version;
    
    public GeneralMoultingCharacters(Boolean isHemimetabolous, Integer moultCount, Integer sizeIncrease,
                                     Boolean hasAdultStage, Boolean isAnamorphic, Boolean hasFixedMoultNumber,
                                     SutureLocation sutureLocation, EgressDirection egressDirection,
                                     Boolean isFragmentedExuviae, Boolean isMonoPhasicMoulting,
                                     Boolean hasExoskeletalMaterialReabsorption, Boolean hasExuviaeConsumed,
                                     Integer repairExtent, Boolean massMoulting, Boolean isMatingLinked,
                                     String hormoneRegulation, Version version) {
        this.isHemimetabolous = isHemimetabolous;
        this.moultCount = moultCount;
        this.sizeIncrease = sizeIncrease;
        this.hasAdultStage = hasAdultStage;
        this.isAnamorphic = isAnamorphic;
        this.hasFixedMoultNumber = hasFixedMoultNumber;
        this.sutureLocation = sutureLocation;
        this.egressDirection = egressDirection;
        this.isFragmentedExuviae = isFragmentedExuviae;
        this.isMonoPhasicMoulting = isMonoPhasicMoulting;
        this.hasExoskeletalMaterialReabsorption = hasExoskeletalMaterialReabsorption;
        this.hasExuviaeConsumed = hasExuviaeConsumed;
        this.repairExtent = repairExtent;
        this.massMoulting = massMoulting;
        this.isMatingLinked = isMatingLinked;
        this.hormoneRegulation = hormoneRegulation;
        this.version = version;
    }
    
    public Boolean getHemimetabolous() {
        return isHemimetabolous;
    }
    
    public Integer getMoultCount() {
        return moultCount;
    }
    
    public Integer getSizeIncrease() {
        return sizeIncrease;
    }
    
    public Boolean getHasAdultStage() {
        return hasAdultStage;
    }
    
    public Boolean getAnamorphic() {
        return isAnamorphic;
    }
    
    public Boolean getHasFixedMoultNumber() {
        return hasFixedMoultNumber;
    }
    
    public SutureLocation getSutureLocation() {
        return sutureLocation;
    }
    
    public EgressDirection getEgressDirection() {
        return egressDirection;
    }
    
    public Boolean getFragmentedExuviae() {
        return isFragmentedExuviae;
    }
    
    public Boolean getMonoPhasicMoulting() {
        return isMonoPhasicMoulting;
    }
    
    public Boolean getHasExoskeletalMaterialReabsorption() {
        return hasExoskeletalMaterialReabsorption;
    }
    
    public Boolean getHasExuviaeConsumed() {
        return hasExuviaeConsumed;
    }
    
    public Integer getRepairExtent() {
        return repairExtent;
    }
    
    public Boolean getMassMoulting() {
        return massMoulting;
    }
    
    public Boolean getMatingLinked() {
        return isMatingLinked;
    }
    
    public String getHormoneRegulation() {
        return hormoneRegulation;
    }
    
    public Version getVersion() {
        return version;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        GeneralMoultingCharacters that = (GeneralMoultingCharacters) o;
        return Objects.equals(isHemimetabolous, that.isHemimetabolous)
                && Objects.equals(moultCount, that.moultCount)
                && Objects.equals(sizeIncrease, that.sizeIncrease)
                && Objects.equals(hasAdultStage, that.hasAdultStage)
                && Objects.equals(isAnamorphic, that.isAnamorphic)
                && Objects.equals(hasFixedMoultNumber, that.hasFixedMoultNumber)
                && Objects.equals(sutureLocation, that.sutureLocation)
                && Objects.equals(egressDirection, that.egressDirection)
                && Objects.equals(isFragmentedExuviae, that.isFragmentedExuviae)
                && Objects.equals(isMonoPhasicMoulting, that.isMonoPhasicMoulting)
                && Objects.equals(hasExoskeletalMaterialReabsorption, that.hasExoskeletalMaterialReabsorption)
                && Objects.equals(hasExuviaeConsumed, that.hasExuviaeConsumed)
                && Objects.equals(repairExtent, that.repairExtent)
                && Objects.equals(massMoulting, that.massMoulting)
                && Objects.equals(isMatingLinked, that.isMatingLinked)
                && Objects.equals(hormoneRegulation, that.hormoneRegulation)
                && Objects.equals(version, that.version);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(isHemimetabolous, moultCount, sizeIncrease, hasAdultStage, isAnamorphic,
                hasFixedMoultNumber, sutureLocation, egressDirection, isFragmentedExuviae, isMonoPhasicMoulting,
                hasExoskeletalMaterialReabsorption, hasExuviaeConsumed, repairExtent, massMoulting, isMatingLinked,
                hormoneRegulation, version);
    }
}
