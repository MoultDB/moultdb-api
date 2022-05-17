package org.moultdb.api.model;

import java.util.Objects;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-20
 */
public class TaxonAnnotation {
    
    private final Taxon taxon;
    private final String annotatedSpeciesName;
    private final SampleSet sampleSet;
    private final Condition condition;
    private final MoultingCharacters moultingCharacters;
    private final Article article;
    private final Term eco;
    private final Term cio;
    private final Version version;
    
    public TaxonAnnotation(Taxon taxon, String annotatedSpeciesName, SampleSet sampleSet,
                           Condition condition, MoultingCharacters moultingCharacters, Article article,
                           Term eco, Term cio, Version version) {
        this.taxon = taxon;
        this.annotatedSpeciesName = annotatedSpeciesName;
        this.sampleSet = sampleSet;
        this.condition = condition;
        this.moultingCharacters = moultingCharacters;
        this.article = article;
        this.eco = eco;
        this.cio = cio;
        this.version = version;
    }
    
    public Taxon getTaxon() {
        return taxon;
    }
    
    public String getAnnotatedSpeciesName() {
        return annotatedSpeciesName;
    }
    
    public SampleSet getSampleSet() {
        return sampleSet;
    }
    
    public Condition getCondition() {
        return condition;
    }
    
    public MoultingCharacters getMoultingCharacters() {
        return moultingCharacters;
    }
    
    public Article getArticle() {
        return article;
    }
    
    public Term getEco() {
        return eco;
    }
    
    public Term getCio() {
        return cio;
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
                && Objects.equals(sampleSet, that.sampleSet)
                && Objects.equals(condition, that.condition)
                && Objects.equals(moultingCharacters, that.moultingCharacters)
                && Objects.equals(article, that.article)
                && Objects.equals(eco, that.eco)
                && Objects.equals(cio, that.cio)
                && Objects.equals(version, that.version);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(taxon, annotatedSpeciesName, sampleSet, condition, moultingCharacters, article, eco, cio, version);
    }
}
