package org.moultdb.api.repository.dto;

import java.io.Serial;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
public class GeneralMoultingCharactersTO extends EntityTO {
    
    @Serial
    private static final long serialVersionUID = -558466073558777076L;
    
    private final Integer datedTaxonId;
    
    private final Boolean isHemimetabolous;
    
    private final Integer moultCount;
    
    private final Integer sizeIncrease;
    
    private final Boolean hasAdultStage;
    
    private final Boolean isAnamorphic;
    
    private final Boolean hasFixedMoultNumber;

    private final SutureLocationTO sutureLocationTO;
    
    private final EgressDirectionTO egressDirectionTO;
    
    private final Boolean isFragmentedExuviae;
    
    private final Boolean isMonoPhasicMoulting;
    
    private final Boolean hasExoskeletalMaterialReabsorption;
    
    private final Boolean hasExuviaeConsumed;
    
    private final Integer repairExtent;
    
    private final Boolean massMoulting;
    
    private final Boolean isMatingLinked;
    
    private final String hormoneRegulation;
    
    private final Integer versionId;
    
    public GeneralMoultingCharactersTO(Integer id, Integer datedTaxonId, Boolean isHemimetabolous, Integer moultCount,
                                       Integer sizeIncrease, Boolean hasAdultStage, Boolean isAnamorphic,
                                       Boolean hasFixedMoultNumber, SutureLocationTO sutureLocationTO,
                                       EgressDirectionTO egressDirectionTO, Boolean isFragmentedExuviae,
                                       Boolean isMonoPhasicMoulting, Boolean hasExoskeletalMaterialReabsorption,
                                       Boolean hasExuviaeConsumed, Integer repairExtent, Boolean massMoulting,
                                       Boolean isMatingLinked, String hormoneRegulation, Integer versionId) {
        super(id);
        this.datedTaxonId = datedTaxonId;
        this.isHemimetabolous = isHemimetabolous;
        this.moultCount = moultCount;
        this.sizeIncrease = sizeIncrease;
        this.hasAdultStage = hasAdultStage;
        this.isAnamorphic = isAnamorphic;
        this.hasFixedMoultNumber = hasFixedMoultNumber;
        this.sutureLocationTO = sutureLocationTO;
        this.egressDirectionTO = egressDirectionTO;
        this.isFragmentedExuviae = isFragmentedExuviae;
        this.isMonoPhasicMoulting = isMonoPhasicMoulting;
        this.hasExoskeletalMaterialReabsorption = hasExoskeletalMaterialReabsorption;
        this.hasExuviaeConsumed = hasExuviaeConsumed;
        this.repairExtent = repairExtent;
        this.massMoulting = massMoulting;
        this.isMatingLinked = isMatingLinked;
        this.hormoneRegulation = hormoneRegulation;
        this.versionId = versionId;
    }
    
    public Integer getDatedTaxonId() {
        return datedTaxonId;
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
    
    public SutureLocationTO getSutureLocationTO() {
        return sutureLocationTO;
    }
    
    public EgressDirectionTO getEgressDirectionTO() {
        return egressDirectionTO;
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
    
    public Integer getVersionId() {
        return versionId;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", GeneralMoultingCharactersTO.class.getSimpleName() + "[", "]")
                .add("id=" + getId())
                .add("datedTaxonId=" + datedTaxonId)
                .add("isHemimetabolous=" + isHemimetabolous)
                .add("moultCount=" + moultCount)
                .add("sizeIncrease=" + sizeIncrease)
                .add("hasAdultStage=" + hasAdultStage)
                .add("isAnamorphic=" + isAnamorphic)
                .add("hasFixedMoultNumber=" + hasFixedMoultNumber)
                .add("sutureLocationTO=" + sutureLocationTO)
                .add("egressDirectionTO=" + egressDirectionTO)
                .add("isFragmentedExuviae=" + isFragmentedExuviae)
                .add("isMonoPhasicMoulting=" + isMonoPhasicMoulting)
                .add("hasExoskeletalMaterialReabsorption=" + hasExoskeletalMaterialReabsorption)
                .add("hasExuviaeConsumed=" + hasExuviaeConsumed)
                .add("repairExtent=" + repairExtent)
                .add("massMoulting=" + massMoulting)
                .add("isMatingLinked=" + isMatingLinked)
                .add("hormoneRegulation='" + hormoneRegulation + "'")
                .add("versionId=" + versionId)
                .toString();
    }
}
