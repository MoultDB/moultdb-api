package org.moultdb.api.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-11-22
 */
public class Sample {
    
    private final GeologicalAge geologicalAge;
    private final String collectionLocation;
    private final String storageAccession;
    private final String storageLocation;
    private final String collector;
    private final Set<Article> articles;
    private final Version version;
    
    public Sample(GeologicalAge geologicalAge, String collectionLocation, String storageAccession,
                  String storageLocation, String collector, Collection<Article> articles, Version version) {
        this.geologicalAge = geologicalAge;
        this.collectionLocation = collectionLocation;
        this.storageAccession = storageAccession;
        this.storageLocation = storageLocation;
        this.collector = collector;
        this.articles = Collections.unmodifiableSet(articles == null ? new HashSet<>(): new HashSet<>(articles));
        this.version = version;
    }
    
    public GeologicalAge getGeologicalAge() {
        return geologicalAge;
    }
    
    public String getCollectionLocation() {
        return collectionLocation;
    }
    
    public String getStorageAccession() {
        return storageAccession;
    }
    
    public String getStorageLocation() {
        return storageLocation;
    }
    
    public String getCollector() {
        return collector;
    }
    
    public Set<Article> getArticles() {
        return articles;
    }
    
    public Version getVersion() {
        return version;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", Sample.class.getSimpleName() + "[", "]")
                .add("geologicalAge=" + geologicalAge)
                .add("collectionLocation='" + collectionLocation + "'")
                .add("storageAccession='" + storageAccession + "'")
                .add("storageLocation='" + storageLocation + "'")
                .add("collector='" + collector + "'")
                .add("articles=" + articles)
                .add("version=" + articles)
                .toString();
    }
}
