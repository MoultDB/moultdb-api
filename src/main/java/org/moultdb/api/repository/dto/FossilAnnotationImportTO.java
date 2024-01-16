package org.moultdb.api.repository.dto;

import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2021-12-20
 */
public class FossilAnnotationImportTO {
    
    Set<TaxonAnnotationTO> taxonAnnotationTOs;
    Set<MoultingCharactersTO> moultingCharactersTOs;
    Set<SampleSetTO> sampleSetTOs;
    Set<ConditionTO> conditionTOs;
    Set<ArticleTO> articleTOs;
    Set<DbXrefTO> dbXrefTOs;
    Set<StorageLocationTO> storageLocationTOs;
    Set<CollectionLocationTO> collectionLocationTOs;
    
    public Set<TaxonAnnotationTO> getTaxonAnnotationTOs() {
        return taxonAnnotationTOs;
    }
    
    public void setTaxonAnnotationTOs(Set<TaxonAnnotationTO> taxonAnnotationTOs) {
        this.taxonAnnotationTOs = taxonAnnotationTOs;
    }
    
    public Set<MoultingCharactersTO> getMoultingCharactersTOs() {
        return moultingCharactersTOs;
    }
    
    public void setMoultingCharactersTOs(Set<MoultingCharactersTO> moultingCharactersTOs) {
        this.moultingCharactersTOs = moultingCharactersTOs;
    }
    
    public Set<SampleSetTO> getSampleSetTOs() {
        return sampleSetTOs;
    }
    
    public void setSampleSetTOs(Set<SampleSetTO> sampleSetTOs) {
        this.sampleSetTOs = sampleSetTOs;
    }
    
    public Set<ConditionTO> getConditionTOs() {
        return conditionTOs;
    }
    
    public void setConditionTOs(Set<ConditionTO> conditionTOs) {
        this.conditionTOs = conditionTOs;
    }
    
    public Set<ArticleTO> getArticleTOs() {
        return articleTOs;
    }
    
    public void setArticleTOs(Set<ArticleTO> articleTOs) {
        this.articleTOs = articleTOs;
    }
    
    public Set<DbXrefTO> getDbXrefTOs() {
        return dbXrefTOs;
    }
    
    public void setDbXrefTOs(Set<DbXrefTO> dbXrefTOs) {
        this.dbXrefTOs = dbXrefTOs;
    }
    
    public Set<StorageLocationTO> getStorageLocationTOs() {
        return storageLocationTOs;
    }
    
    public void setStorageLocationTOs(Set<StorageLocationTO> storageLocationTOs) {
        this.storageLocationTOs = storageLocationTOs;
    }
    
    public Set<CollectionLocationTO> getCollectionLocationTOs() {
        return collectionLocationTOs;
    }
    
    public void setCollectionLocationTOs(Set<CollectionLocationTO> collectionLocationTOs) {
        this.collectionLocationTOs = collectionLocationTOs;
    }
}
