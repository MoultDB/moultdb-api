package org.moultdb.api.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-11-22
 */
public class SampleSet {
    
    private final TimePeriod timePeriod;
    private final Set<String> collectionLocations;
    private final Set<String> storageAccessions;
    private final Set<String> storageLocations;
    private final Set<String> geologicalFormations;
    private final Set<String> fossilPreservationTypes;
    private final Set<String> environments;
    private final String biozone;
    private final Set<String> specimenTypes;
    private final String specimenCount;
    private final Boolean isFossil;
    private final Boolean isCaptive;
    
    
    public SampleSet(TimePeriod timePeriod, Collection<String> collectionLocations,
                     Collection<String> storageAccessions, Collection<String> storageLocations,
                     Collection<String> geologicalFormations, Collection<String> fossilPreservationTypes,
                     Collection<String> environments, String biozone, Collection<String> specimenTypes,
                     String specimenCount, Boolean isFossil, Boolean isCaptive) {
        this.timePeriod = timePeriod;
        this.collectionLocations = Collections.unmodifiableSet(collectionLocations == null ?
                new HashSet<>(): new HashSet<>(collectionLocations));
        this.storageAccessions = Collections.unmodifiableSet(storageAccessions == null ?
                new HashSet<>(): new HashSet<>(storageAccessions));
        this.storageLocations = Collections.unmodifiableSet(storageLocations == null ?
                new HashSet<>(): new HashSet<>(storageLocations));
        this.geologicalFormations = Collections.unmodifiableSet(geologicalFormations == null ?
                new HashSet<>(): new HashSet<>(geologicalFormations));
        this.fossilPreservationTypes = Collections.unmodifiableSet(fossilPreservationTypes == null ?
                new HashSet<>(): new HashSet<>(fossilPreservationTypes));
        this.environments = Collections.unmodifiableSet(environments == null ?
                new HashSet<>(): new HashSet<>(environments));
        this.biozone = biozone;
        this.specimenTypes = Collections.unmodifiableSet(specimenTypes == null ?
                new HashSet<>(): new HashSet<>(specimenTypes));
        this.specimenCount = specimenCount;
        this.isFossil = isFossil;
        this.isCaptive = isCaptive;
    }
    
    public TimePeriod getTimePeriod() {
        return timePeriod;
    }
    
    public Set<String> getCollectionLocations() {
        return collectionLocations;
    }
    
    public Set<String> getStorageAccessions() {
        return storageAccessions;
    }
    
    public Set<String> getStorageLocations() {
        return storageLocations;
    }
    
    public Set<String> getGeologicalFormations() {
        return geologicalFormations;
    }
    
    public Set<String> getFossilPreservationTypes() {
        return fossilPreservationTypes;
    }
    
    public Set<String> getEnvironments() {
        return environments;
    }
    
    public String getBiozone() {
        return biozone;
    }
    
    public Set<String> getSpecimenTypes() {
        return specimenTypes;
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
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        SampleSet sampleSet = (SampleSet) o;
        return Objects.equals(timePeriod, sampleSet.timePeriod)
                && Objects.equals(collectionLocations, sampleSet.collectionLocations)
                && Objects.equals(storageAccessions, sampleSet.storageAccessions)
                && Objects.equals(storageLocations, sampleSet.storageLocations);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(timePeriod, collectionLocations, storageAccessions, storageLocations);
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", SampleSet.class.getSimpleName() + "[", "]")
                .add("timePeriod=" + timePeriod)
                .add("collectionLocations=" + collectionLocations)
                .add("storageAccessions=" + storageAccessions)
                .add("storageLocations=" + storageLocations)
                .add("geologicalFormations=" + geologicalFormations)
                .add("fossilPreservationTypes=" + fossilPreservationTypes)
                .add("specimenTypes=" + specimenTypes)
                .add("environments=" + environments)
                .add("biozone=" + biozone)
                .add("specimenCount='" + specimenCount + "'")
                .add("isFossil=" + isFossil)
                .add("isCaptive=" + isCaptive)
                .toString();
    }
}
