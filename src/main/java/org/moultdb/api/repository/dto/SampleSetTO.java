package org.moultdb.api.repository.dto;

import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
public class SampleSetTO extends EntityTO<Integer> {
    
    @Serial
    private static final long serialVersionUID = 9090994360207976303L;
    
    /**
     * Older bound of the geological age range
     */
    private final GeologicalAgeTO fromGeologicalAgeTO;
    /**
     * Youngest bound of the geological age range
     */
    private final GeologicalAgeTO toGeologicalAgeTO;
    private final String specimenCount;
    private final Boolean isFossil;
    private final Boolean isCaptive;
    private final Set<String> storageAccessions;
    private final Set<String> storageLocationNames;
    private final Set<String> collectionLocationNames;
    private final Set<String> fossilPreservationTypes;
    private final Set<String> environments;
    private final Set<String> geologicalFormations;
    private final Set<String> specimenTypes;
    private final String biozone;
    
    public SampleSetTO(Integer id, GeologicalAgeTO fromGeologicalAgeTO, GeologicalAgeTO toGeologicalAgeTO,
                       String specimenCount, Boolean isFossil, Boolean isCaptive, String collectionLocationName) {
        this(id, fromGeologicalAgeTO, toGeologicalAgeTO, specimenCount, isFossil, isCaptive, null, null,
                Collections.unmodifiableSet(StringUtils.isBlank(collectionLocationName) ?
                        new HashSet<>() :
                        new HashSet<>(List.of(collectionLocationName))),
                null, null, null, null, null);
    }
    
    public SampleSetTO(Integer id, GeologicalAgeTO fromGeologicalAgeTO, GeologicalAgeTO toGeologicalAgeTO,
                       String specimenCount, Boolean isFossil, Boolean isCaptive, Set<String> storageAccessions,
                       Set<String> storageLocationNames, Set<String> collectionLocationNames,
                       Set<String> fossilPreservationTypes, Set<String> environments, Set<String> geologicalFormations,
                       Set<String> specimenTypes, String biozone)
            throws IllegalArgumentException {
        super(id);
        this.fromGeologicalAgeTO = fromGeologicalAgeTO;
        this.toGeologicalAgeTO = toGeologicalAgeTO;
        this.specimenCount = specimenCount;
        this.isFossil = isFossil;
        this.isCaptive = isCaptive;
        this.storageAccessions = storageAccessions;
        this.storageLocationNames = storageLocationNames;
        this.collectionLocationNames = collectionLocationNames;
        this.fossilPreservationTypes = fossilPreservationTypes;
        this.environments = environments;
        this.geologicalFormations = geologicalFormations;
        this.specimenTypes = specimenTypes;
        this.biozone = biozone;
    }
    
    public GeologicalAgeTO getFromGeologicalAgeTO() {
        return fromGeologicalAgeTO;
    }
    
    public GeologicalAgeTO getToGeologicalAgeTO() {
        return toGeologicalAgeTO;
    }
    
    public String getSpecimenCount() {
        return specimenCount;
    }
    
    public Boolean isFossil() {
        return isFossil;
    }
    
    public Boolean isCaptive() {
        return isCaptive;
    }
    
    public Set<String> getStorageAccessions() {
        return storageAccessions;
    }
    
    public Set<String> getStorageLocationNames() {
        return storageLocationNames;
    }
    
    public Set<String> getCollectionLocationNames() {
        return collectionLocationNames;
    }
    
    public Set<String> getFossilPreservationTypes() {
        return fossilPreservationTypes;
    }
    
    public Set<String> getEnvironments() {
        return environments;
    }
    
    public Set<String> getGeologicalFormations() {
        return geologicalFormations;
    }
    
    public Set<String> getSpecimenTypes() {
        return specimenTypes;
    }
    
    public String getBiozone() {
        return biozone;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", SampleSetTO.class.getSimpleName() + "[", "]")
                .add("id=" + getId())
                .add("fromGeologicalAgeTO=" + fromGeologicalAgeTO)
                .add("toGeologicalAgeTO=" + toGeologicalAgeTO)
                .add("specimenCount=" + specimenCount)
                .add("isFossil=" + isFossil)
                .add("isCaptive=" + isCaptive)
                .add("storageAccessions=" + storageAccessions)
                .add("storageLocationNames=" + storageLocationNames)
                .add("collectionLocationNames=" + collectionLocationNames)
                .add("fossilPreservationTypes=" + fossilPreservationTypes)
                .add("environments=" + environments)
                .add("geologicalFormations=" + geologicalFormations)
                .add("specimenTypes=" + specimenTypes)
                .add("biozone=" + biozone)
                .toString();
    }
}
