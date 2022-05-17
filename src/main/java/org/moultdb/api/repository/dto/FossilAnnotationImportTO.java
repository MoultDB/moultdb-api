package org.moultdb.api.repository.dto;

import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2021-12-20
 */
public class FossilAnnotationImportTO {
    
    Set<ArticleTO> articleTOs;
//    Set<ArticleToDbXrefTO> articleToDbXrefTOs;
//    Set<ArticleToSampleTO> articleToSampleTOs;
//    Set<ArticleToTaxonTO> articleToTaxonTOS;
//    Set<CollectionLocationTO> collectionLocationTOS;
    Set<ConditionTO> conditionTOS;
    Set<DataSourceTO> dataSourceTOS;
    Set<DbXrefTO> dbXrefTOS;
    Set<EntityTO> entityTOS;
    Set<GeologicalAgeTO> geologicalAgeTOS;
    Set<TaxonAnnotationTO> taxonAnnotationTOS;
    Set<MoultingCharactersTO> moultingCharactersTOS;
    Set<NamedEntityTO> namedEntityTOS;
    Set<SampleSetTO> sampleSetTOS;
    Set<StorageLocationTO> storageLocationTOS;
    
    public Set<ArticleTO> getArticleTOs() {
        return articleTOs;
    }
    
    public void setArticleTOs(Set<ArticleTO> articleTOs) {
        this.articleTOs = articleTOs;
    }
    
//    public Set<ArticleToDbXrefTO> getArticleToDbXrefTOs() {
//        return articleToDbXrefTOs;
//    }
//
//    public void setArticleToDbXrefTOs(Set<ArticleToDbXrefTO> articleToDbXrefTOs) {
//        this.articleToDbXrefTOs = articleToDbXrefTOs;
//    }
//
//    public Set<ArticleToSampleTO> getArticleToSampleTOs() {
//        return articleToSampleTOs;
//    }
//
//    public void setArticleToSampleTOs(Set<ArticleToSampleTO> articleToSampleTOs) {
//        this.articleToSampleTOs = articleToSampleTOs;
//    }
//
//    public Set<ArticleToTaxonTO> getArticleToTaxonTOS() {
//        return articleToTaxonTOS;
//    }
//
//    public void setArticleToTaxonTOS(Set<ArticleToTaxonTO> articleToTaxonTOS) {
//        this.articleToTaxonTOS = articleToTaxonTOS;
//    }
//
//    public Set<CollectionLocationTO> getCollectionLocationTOS() {
//        return collectionLocationTOS;
//    }
//
//    public void setCollectionLocationTOS(Set<CollectionLocationTO> collectionLocationTOS) {
//        this.collectionLocationTOS = collectionLocationTOS;
//    }
//
    public Set<ConditionTO> getConditionTOS() {
        return conditionTOS;
    }
    
    public void setConditionTOS(Set<ConditionTO> conditionTOS) {
        this.conditionTOS = conditionTOS;
    }
    
    public Set<DataSourceTO> getDataSourceTOS() {
        return dataSourceTOS;
    }
    
    public void setDataSourceTOS(Set<DataSourceTO> dataSourceTOS) {
        this.dataSourceTOS = dataSourceTOS;
    }
    
    public Set<DbXrefTO> getDbXrefTOS() {
        return dbXrefTOS;
    }
    
    public void setDbXrefTOS(Set<DbXrefTO> dbXrefTOS) {
        this.dbXrefTOS = dbXrefTOS;
    }
    
    public Set<EntityTO> getEntityTOS() {
        return entityTOS;
    }
    
    public void setEntityTOS(Set<EntityTO> entityTOS) {
        this.entityTOS = entityTOS;
    }
    
    public Set<GeologicalAgeTO> getGeologicalAgeTOS() {
        return geologicalAgeTOS;
    }
    
    public void setGeologicalAgeTOS(Set<GeologicalAgeTO> geologicalAgeTOS) {
        this.geologicalAgeTOS = geologicalAgeTOS;
    }
    
    public Set<TaxonAnnotationTO> getIndividualTOS() {
        return taxonAnnotationTOS;
    }
    
    public void setIndividualTOS(Set<TaxonAnnotationTO> taxonAnnotationTOS) {
        this.taxonAnnotationTOS = taxonAnnotationTOS;
    }
    
    public Set<MoultingCharactersTO> getMoultingCharactersTOS() {
        return moultingCharactersTOS;
    }
    
    public void setMoultingCharactersTOS(Set<MoultingCharactersTO> moultingCharactersTOS) {
        this.moultingCharactersTOS = moultingCharactersTOS;
    }
    
    public Set<NamedEntityTO> getNamedEntityTOS() {
        return namedEntityTOS;
    }
    
    public void setNamedEntityTOS(Set<NamedEntityTO> namedEntityTOS) {
        this.namedEntityTOS = namedEntityTOS;
    }
    
    public Set<SampleSetTO> getSampleTOS() {
        return sampleSetTOS;
    }
    
    public void setSampleTOS(Set<SampleSetTO> sampleSetTOS) {
        this.sampleSetTOS = sampleSetTOS;
    }
    
    public Set<StorageLocationTO> getStorageLocationTOS() {
        return storageLocationTOS;
    }
    
    public void setStorageLocationTOS(Set<StorageLocationTO> storageLocationTOS) {
        this.storageLocationTOS = storageLocationTOS;
    }
}
