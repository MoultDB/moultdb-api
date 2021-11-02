package org.moultdb.api.model;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-20
 */
public class ArticleToTaxon {
    private final Article article;
    private final Taxon taxon;
    private final Version version;
    
    public ArticleToTaxon(Article article, Taxon taxon, Version version) {
        this.article = article;
        this.taxon = taxon;
        this.version = version;
    }
    
    public Article getArticle() {
        return article;
    }
    
    public Taxon getTaxon() {
        return taxon;
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
        ArticleToTaxon that = (ArticleToTaxon) o;
        return Objects.equals(article, that.article)
                && Objects.equals(taxon, that.taxon)
                && Objects.equals(version, that.version);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(article, taxon, version);
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", ArticleToTaxon.class.getSimpleName() + "[", "]")
                .add("article=" + article)
                .add("taxon=" + taxon)
                .add("version=" + version)
                .toString();
    }
}
