package org.moultdb.api.repository.dto;

import java.io.Serial;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
public class ArticleTO extends EntityTO<Integer> {
    
    @Serial
    private static final long serialVersionUID = -5535682409937959172L;
    
    private final String citation;
    private final String title;
    private final String authors;
    private final Set<DbXrefTO> dbXrefTOs;
    
    public ArticleTO(Integer id, String citation, String title, String authors,
                     Collection<DbXrefTO> dbXrefTOs) throws IllegalArgumentException {
        super(id);
        this.citation = citation;
        this.title = title;
        this.authors = authors;
        this.dbXrefTOs = Collections.unmodifiableSet(dbXrefTOs == null ?
                new HashSet<>(): new HashSet<>(dbXrefTOs));
    }
    
    public String getCitation() {
        return citation;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getAuthors() {
        return authors;
    }
    
    public Set<DbXrefTO> getDbXrefTOs() {
        return dbXrefTOs;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", ArticleTO.class.getSimpleName() + "[", "]")
                .add("id='" + getId() + "'")
                .add("citation='" + citation + "'")
                .add("title='" + title + "'")
                .add("authors='" + authors + "'")
                .add("dbXrefTOs='" + dbXrefTOs + "'")
                .toString();
    }
}
