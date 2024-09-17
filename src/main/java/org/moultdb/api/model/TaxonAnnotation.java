package org.moultdb.api.model;

import java.util.Objects;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-20
 */
public class TaxonAnnotation {
    
    private final Taxon taxon;
    private final String annotatedSpeciesName;
    private final String determinedBy;
    private final SampleSet sampleSet;
    private final Condition condition;
    private final String annotatedDevStage;
    private final String annotatedAnatEntity;
    private final MoultingCharacters moultingCharacters;
    private final Article article;
    private final ImageInfo imageInfo;
    private final ECOTerm ecoTerm;
    private final CIOStatement cioStatement;
    private final Version version;
    
    public TaxonAnnotation(Taxon taxon, String annotatedSpeciesName, String determinedBy, SampleSet sampleSet,
                           Condition condition, String annotatedDevStage, String annotatedAnatEntity,
                           MoultingCharacters moultingCharacters, Article article, ImageInfo imageInfo,
                           ECOTerm ecoTerm, CIOStatement cioStatement, Version version) {
        this.taxon = taxon;
        this.annotatedSpeciesName = annotatedSpeciesName;
        this.determinedBy = determinedBy;
        this.sampleSet = sampleSet;
        this.condition = condition;
        this.annotatedDevStage = annotatedDevStage;
        this.annotatedAnatEntity = annotatedAnatEntity;
        this.moultingCharacters = moultingCharacters;
        this.article = article;
        this.imageInfo = imageInfo;
        this.ecoTerm = ecoTerm;
        this.cioStatement = cioStatement;
        this.version = version;
    }
    
    public Taxon getTaxon() {
        return taxon;
    }
    
    public String getAnnotatedSpeciesName() {
        return annotatedSpeciesName;
    }
    
    public String getDeterminedBy() {
        return determinedBy;
    }
    
    public SampleSet getSampleSet() {
        return sampleSet;
    }
    
    public Condition getCondition() {
        return condition;
    }
    
    public String getAnnotatedDevStage() {
        return annotatedDevStage;
    }
    
    public String getAnnotatedAnatEntity() {
        return annotatedAnatEntity;
    }
    
    public MoultingCharacters getMoultingCharacters() {
        return moultingCharacters;
    }
    
    public Article getArticle() {
        return article;
    }
    
    public ImageInfo getImageInfo() {
        return imageInfo;
    }
    
    public ECOTerm getEcoTerm() {
        return ecoTerm;
    }
    
    public CIOStatement getCioStatement() {
        return cioStatement;
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
        TaxonAnnotation that = (TaxonAnnotation) o;
        return Objects.equals(taxon, that.taxon)
                && Objects.equals(annotatedSpeciesName, that.annotatedSpeciesName)
                && Objects.equals(determinedBy, that.determinedBy)
                && Objects.equals(sampleSet, that.sampleSet)
                && Objects.equals(condition, that.condition)
                && Objects.equals(annotatedDevStage, that.annotatedDevStage)
                && Objects.equals(annotatedAnatEntity, that.annotatedAnatEntity)
                && Objects.equals(moultingCharacters, that.moultingCharacters)
                && Objects.equals(article, that.article)
                && Objects.equals(imageInfo, that.imageInfo)
                && Objects.equals(ecoTerm, that.ecoTerm)
                && Objects.equals(cioStatement, that.cioStatement)
                && Objects.equals(version, that.version);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(taxon, annotatedSpeciesName, determinedBy, sampleSet, condition,
                annotatedDevStage, annotatedAnatEntity, article, imageInfo, ecoTerm, cioStatement, version);
    }
}
