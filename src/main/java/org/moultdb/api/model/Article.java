package org.moultdb.api.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-20
 */
public class Article {
    
    private final String title;
    private final String authors;
    private final Set<DbXref> dbXrefs;
    
    public Article(String title, String authors, Collection<DbXref> dbXrefs) {
        this.title = title;
        this.authors = authors;
        this.dbXrefs = Collections.unmodifiableSet(dbXrefs == null ? new HashSet<>(): new HashSet<>(dbXrefs));
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getAuthors() {
        return authors;
    }
    
    public Set<DbXref> getDbXrefs() {
        return dbXrefs;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Article article = (Article) o;
        return Objects.equals(title, article.title)
                && Objects.equals(authors, article.authors);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(title, authors);
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", Article.class.getSimpleName() + "[", "]")
                .add("title='" + title + "'")
                .add("authors='" + authors + "'")
                .add("dbXrefs=" + dbXrefs)
                .toString();
    }
}
