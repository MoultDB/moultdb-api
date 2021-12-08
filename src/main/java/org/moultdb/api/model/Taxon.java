package org.moultdb.api.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-14
 */
public class Taxon extends NamedEntity {
    
    private final String commonName;
    private final DbXref dbXref;
    private final Integer parentTaxonId;
    private final String taxonRank;
    private final boolean extinct;
    private final String path;
    private final Set<Article> articles;
    
    public Taxon(String scientificName, String commonName, DbXref dbXref, String taxonRank,
                 Integer parentTaxonId, boolean extinct, String path, Collection<Article> articles) {
        super(scientificName);
        this.commonName = commonName;
        this.taxonRank = taxonRank;
        this.dbXref = dbXref;
        this.parentTaxonId = parentTaxonId;
        this.extinct = extinct;
        this.path = path;
        this.articles = Collections.unmodifiableSet(articles == null ? new HashSet<>(): new HashSet<>(articles));
    }
    
    public String getScientificName() {
        return getName();
    }
    
    public String getCommonName() {
        return commonName;
    }
    
    public DbXref getDbXref() {
        return dbXref;
    }
    
    public Integer getParentTaxonId() {
        return parentTaxonId;
    }
    
    public String getTaxonRank() {
        return taxonRank;
    }
    
    public boolean isExtinct() {
        return extinct;
    }
    
    public String getPath() {
        return path;
    }
    
    public Set<Article> getArticles() {
        return articles;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", Taxon.class.getSimpleName() + "[", "]")
                .add("scientificName=" + getName())
                .add("commonName='" + commonName + "'")
                .add("dbXref=" + dbXref)
                .add("taxonRank='" + taxonRank + "'")
                .add("parentTaxonId=" + parentTaxonId)
                .add("extinct=" + extinct)
                .add("path='" + path + "'")
                .add("articles='" + articles + "'")
                .toString();
    }
}
