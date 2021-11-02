package org.moultdb.api.model;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-20
 */
public class Article {
    
    private final String title;
    private final String authors;
    private final String doi;
    private final Integer pmid;
    
    public Article(String title, String authors, String doi, Integer pmid) {
        this.title = title;
        this.authors = authors;
        this.doi = doi;
        this.pmid = pmid;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getAuthors() {
        return authors;
    }
    
    public String getDoi() {
        return doi;
    }
    
    public Integer getPmid() {
        return pmid;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Article article = (Article) o;
        return Objects.equals(title, article.title)
                && Objects.equals(authors, article.authors)
                && Objects.equals(doi, article.doi)
                && Objects.equals(pmid, article.pmid);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(title, authors, doi, pmid);
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", Article.class.getSimpleName() + "[", "]")
                .add("title='" + title + "'")
                .add("authors='" + authors + "'")
                .add("doi='" + doi + "'")
                .add("pmid=" + pmid)
                .toString();
    }
}
