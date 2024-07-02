package org.moultdb.api.model;

import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2024-03-25
 */
public class Domain extends Entity<String> {
    
    private final String description;
    
    public Domain(String id, String description) throws IllegalArgumentException {
        super(id);
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", Domain.class.getSimpleName() + "[", "]")
                .add("id='" + getId() + "'")
                .add("description='" + description + "'")
                .toString();
    }
}
