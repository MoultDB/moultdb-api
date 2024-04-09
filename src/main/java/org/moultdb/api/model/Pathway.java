package org.moultdb.api.model;

import java.util.*;

/**
 * @author Valentine Rech de Laval
 * @since 2024-03-25
 */
public class Pathway extends NamedEntity<String> {
    
    public Pathway(String id, String name) throws IllegalArgumentException {
        super(id, name);
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", Pathway.class.getSimpleName() + "[", "]")
                .add("id='" + getId() + "'")
                .add("name='" + getName() + "'")
                .toString();
    }
}
