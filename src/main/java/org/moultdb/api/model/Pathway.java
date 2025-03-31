package org.moultdb.api.model;

import java.util.*;

/**
 * @author Valentine Rech de Laval
 * @since 2024-03-25
 */
public class Pathway extends NamedEntity<String> {
    
    private final Article article;
    
    private final Set<Integer> figureIds;
    
    public Pathway(String id, String name, String description, Article article, Collection<Integer> figureIds)
            throws IllegalArgumentException {
        super(id, name, description);
        this.article = article;
        this.figureIds = Collections.unmodifiableSet(figureIds == null ? new HashSet<>(): new HashSet<>(figureIds));
    }
    
    public Article getArticle() {
        return article;
    }
    
    public Set<Integer> getFigureIds() {
        return figureIds;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", Pathway.class.getSimpleName() + "[", "]")
                .add("id='" + getId() + "'")
                .add("name='" + getName() + "'")
                .add("description='" + getDescription() + "'")
                .add("article=" + article)
                .add("figureIds=" + figureIds)
                .toString();
    }
}
