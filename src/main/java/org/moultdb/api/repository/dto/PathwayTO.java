package org.moultdb.api.repository.dto;

import java.io.Serial;
import java.util.*;

/**
 * @author Valentine Rech de Laval
 * @since 2024-03-22
 */
public class PathwayTO extends NamedEntityTO<String> {
    
    @Serial
    private static final long serialVersionUID = -8068259646168632939L;
    
    private final ArticleTO articleTO;
    
    private final Set<Integer> figureIds;
    
    public PathwayTO(String id, String name, String description, ArticleTO articleTO, Collection<Integer> figureIds)
            throws IllegalArgumentException {
        super(id, name, description);
        this.articleTO = articleTO;
        this.figureIds = Collections.unmodifiableSet(figureIds == null ?
                new HashSet<>(): new HashSet<>(figureIds));
    }
    
    public ArticleTO getArticleTO() {
        return articleTO;
    }
    
    public Set<Integer> getFigureIds() {
        return figureIds;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", PathwayTO.class.getSimpleName() + "[", "]")
                .add("id='" + getId() + "'")
                .add("name='" + getName() + "'")
                .add("description='" + getDescription() + "'")
                .add("articleTO=" + articleTO)
                .add("figureIds=" + figureIds)
                .toString();
    }
}
