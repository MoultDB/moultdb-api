package org.moultdb.api.repository.dto;

import java.io.Serial;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2024-03-22
 */
public class PathwayTO extends NamedEntityTO<String> {
    
    @Serial
    private static final long serialVersionUID = -8068259646168632939L;
    
    private final ArticleTO articleTO;
    
    public PathwayTO(String id, String name, String description, ArticleTO articleTO) throws IllegalArgumentException {
        super(id, name, description);
        this.articleTO = articleTO;
    }
    
    public ArticleTO getArticleTO() {
        return articleTO;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", PathwayTO.class.getSimpleName() + "[", "]")
                .add("id='" + getId() + "'")
                .add("name='" + getName() + "'")
                .add("description='" + getDescription() + "'")
                .add("articleTO=" + articleTO)
                .toString();
    }
}
