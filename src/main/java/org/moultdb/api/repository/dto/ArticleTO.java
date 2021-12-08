package org.moultdb.api.repository.dto;

import java.io.Serial;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
public class ArticleTO extends NamedEntityTO {
    
    @Serial
    private static final long serialVersionUID = -5535682409937959172L;
    
    private final String authors;
    
    public ArticleTO(Integer id, String title, String authors) throws IllegalArgumentException {
        super(id, title);
        this.authors = authors;
    }
    
    public String getTitle() {
        return getName();
    }
    
    public String getAuthors() {
        return authors;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", ArticleTO.class.getSimpleName() + "[", "]")
                .add("id='" + getId() + "'")
                .add("title='" + getName() + "'")
                .add("authors='" + authors + "'")
                .toString();
    }
}
