package org.moultdb.api.model;

import java.util.Objects;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-20
 */
public class TaxonAnnotation {
    
    private final Taxon taxon;
    private final String authorSpeciesName;
    private final String determinedBy;
    private final SampleSet sampleSet;
    private final Condition condition;
    private final String authorDevStage;
    private final String authorAnatEntity;
    private final MoultingCharacters moultingCharacters;
    private final Article article;
    private final Observation observation;
    private final ECOTerm ecoTerm;
    private final CIOStatement cioStatement;
    private final Version version;
    
    public TaxonAnnotation(Taxon taxon, String authorSpeciesName, String determinedBy, SampleSet sampleSet,
                           Condition condition, String authorDevStage, String authorAnatEntity,
                           MoultingCharacters moultingCharacters, Article article, Observation observation,
                           ECOTerm ecoTerm, CIOStatement cioStatement, Version version) {
        this.taxon = taxon;
        this.authorSpeciesName = authorSpeciesName;
        this.determinedBy = determinedBy;
        this.sampleSet = sampleSet;
        this.condition = condition;
        this.authorDevStage = authorDevStage;
        this.authorAnatEntity = authorAnatEntity;
        this.moultingCharacters = moultingCharacters;
        this.article = article;
        this.observation = observation;
        this.ecoTerm = ecoTerm;
        this.cioStatement = cioStatement;
        this.version = version;
    }
    
    public Taxon getTaxon() {
        return taxon;
    }
    
    public String getAuthorSpeciesName() {
        return authorSpeciesName;
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
    
    public String getAuthorDevStage() {
        return authorDevStage;
    }
    
    public String getAuthorAnatEntity() {
        return authorAnatEntity;
    }
    
    public MoultingCharacters getMoultingCharacters() {
        return moultingCharacters;
    }
    
    public Article getArticle() {
        return article;
    }
    
    public Observation getObservation() {
        return observation;
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
                && Objects.equals(authorSpeciesName, that.authorSpeciesName)
                && Objects.equals(determinedBy, that.determinedBy)
                && Objects.equals(sampleSet, that.sampleSet)
                && Objects.equals(condition, that.condition)
                && Objects.equals(authorDevStage, that.authorDevStage)
                && Objects.equals(authorAnatEntity, that.authorAnatEntity)
                && Objects.equals(moultingCharacters, that.moultingCharacters)
                && Objects.equals(article, that.article)
                && Objects.equals(observation, that.observation)
                && Objects.equals(ecoTerm, that.ecoTerm)
                && Objects.equals(cioStatement, that.cioStatement)
                && Objects.equals(version, that.version);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(taxon, authorSpeciesName, determinedBy, sampleSet, condition,
                authorDevStage, authorAnatEntity, article, observation, ecoTerm, cioStatement, version);
    }
}
