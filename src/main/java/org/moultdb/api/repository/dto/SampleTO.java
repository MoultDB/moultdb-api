package org.moultdb.api.repository.dto;

import java.io.Serial;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
public class SampleTO extends EntityTO {
    
    @Serial
    private static final long serialVersionUID = 9090994360207976303L;
    
    private final GeologicalAgeTO geologicalAgeTO;
    private final String collectionLocationName;
    private final String storageAccession;
    private final String storageLocationName;
    private final String collector;
    private final Integer versionId;
    
    public SampleTO(Integer id, GeologicalAgeTO geologicalAgeTO, String collectionLocationName,
                    String storageAccession, String storageLocationName, String collector, Integer versionId) {
        super(id);
        this.geologicalAgeTO = geologicalAgeTO;
        this.collectionLocationName = collectionLocationName;
        this.storageAccession = storageAccession;
        this.storageLocationName = storageLocationName;
        this.collector = collector;
        this.versionId = versionId;
    }
    
    public GeologicalAgeTO getGeologicalAgeTO() {
        return geologicalAgeTO;
    }
    
    public String getCollectionLocationName() {
        return collectionLocationName;
    }
    
    public String getStorageAccession() {
        return storageAccession;
    }
    
    public String getStorageLocationName() {
        return storageLocationName;
    }
    
    public String getCollector() {
        return collector;
    }
    
    public Integer getVersionId() {
        return versionId;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", SampleTO.class.getSimpleName() + "[", "]")
                .add("id=" + getId())
                .add("geologicalAgeTO=" + geologicalAgeTO)
                .add("collectionLocationName=" + collectionLocationName)
                .add("storageAccession=" + storageAccession)
                .add("storageLocationName=" + storageLocationName)
                .add("collector='" + collector + "'")
                .add("versionId=" + versionId)
                .toString();
    }
}
