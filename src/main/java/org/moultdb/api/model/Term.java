package org.moultdb.api.model;

import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2022-05-02
 */
public class Term extends NamedEntity<String> {
    
    public Term(String id, String name, String description) throws IllegalArgumentException {
        super(id, name, description);
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", Term.class.getSimpleName() + "[", "]")
                .add("id='" + getId() + "'")
                .add("name='" + getName() + "'")
                .add("description='" + getDescription() + "'")
                .toString();
    }
}
