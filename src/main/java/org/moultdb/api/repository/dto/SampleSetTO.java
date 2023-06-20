package org.moultdb.api.repository.dto;

import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.util.Arrays;
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
    
    private final GeologicalAgeTO fromGeologicalAgeTO;
    private final GeologicalAgeTO toGeologicalAgeTO;
    private final Integer specimenCount;
    private final Set<String> storageAccessions;
    private final Set<String> storageLocationNames;
    private final Set<String> collectionLocationNames;
    private final Set<String> fossilPreservationTypes;
    private final Set<String> environments;
    private final Set<String> geologicalFormations;
    private final Set<String> specimenTypes;
    
    public SampleSetTO(Integer id, GeologicalAgeTO fromGeologicalAgeTO, GeologicalAgeTO toGeologicalAgeTO,
                       Integer specimenCount, String collectionLocationName) {
        this(id, fromGeologicalAgeTO, toGeologicalAgeTO, specimenCount, null, null,
                Collections.unmodifiableSet(StringUtils.isBlank(collectionLocationName) ?
                        new HashSet<>() :
                        new HashSet<>(List.of(collectionLocationName))),
                null, null, null, null);
    }
    
    public SampleSetTO(Integer id, GeologicalAgeTO fromGeologicalAgeTO, GeologicalAgeTO toGeologicalAgeTO,
                       Integer specimenCount, Set<String> storageAccessions, Set<String> storageLocationNames,
                       Set<String> collectionLocationNames, Set<String> fossilPreservationTypes,
                       Set<String> environments, Set<String> geologicalFormations, Set<String> specimenTypes)
            throws IllegalArgumentException {
        super(id);
        this.fromGeologicalAgeTO = fromGeologicalAgeTO;
        this.toGeologicalAgeTO = toGeologicalAgeTO;
        this.specimenCount = specimenCount;
        this.storageAccessions = storageAccessions;
        this.storageLocationNames = storageLocationNames;
        this.collectionLocationNames = collectionLocationNames;
        this.fossilPreservationTypes = fossilPreservationTypes;
        this.environments = environments;
        this.geologicalFormations = geologicalFormations;
        this.specimenTypes = specimenTypes;
    }
    
    public GeologicalAgeTO getFromGeologicalAgeTO() {
        return fromGeologicalAgeTO;
    }
    
    public GeologicalAgeTO getToGeologicalAgeTO() {
        return toGeologicalAgeTO;
    }
    
    public Integer getSpecimenCount() {
        return specimenCount;
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
    
    @Override
    public String toString() {
        return new StringJoiner(", ", SampleSetTO.class.getSimpleName() + "[", "]")
                .add("id=" + getId())
                .add("fromGeologicalAgeTO=" + fromGeologicalAgeTO)
                .add("toGeologicalAgeTO=" + toGeologicalAgeTO)
                .add("specimenCount=" + specimenCount)
                .add("storageAccessions=" + storageAccessions)
                .add("storageLocationNames=" + storageLocationNames)
                .add("collectionLocationNames=" + collectionLocationNames)
                .add("fossilPreservationTypes=" + fossilPreservationTypes)
                .add("environments=" + environments)
                .add("geologicalFormations=" + geologicalFormations)
                .add("specimenTypes=" + specimenTypes)
                .toString();
    }
}