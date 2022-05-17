package org.moultdb.api.repository.dto;

import java.io.Serial;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
public abstract class NamedEntityTO<T extends Comparable<T>> extends EntityTO<T> {
    
    @Serial
    private static final long serialVersionUID = -2589259593879371993L;
    
    private final String name;
    private final String description;
    
    public NamedEntityTO(T id, String name, String description) throws IllegalArgumentException {
        super(id);
        this.name = name;
        this.description = description;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", NamedEntityTO.class.getSimpleName() + "[", "]")
                .add("id='" + getId() + "'")
                .add("name='" + name + "'")
                .add("description='" + description + "'")
                .toString();
    }
}
