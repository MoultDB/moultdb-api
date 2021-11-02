package org.moultdb.api.repository.dto;

import java.io.Serial;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
public abstract class NamedEntityTO extends EntityTO {
    
    @Serial
    private static final long serialVersionUID = -2589259593879371993L;
    
    private final String name;
    
    public NamedEntityTO(Integer id, String name) throws IllegalArgumentException {
        super(id);
        if (name == null) {
            throw new IllegalArgumentException("the name provided cannot be blank.");
        }
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", NamedEntityTO.class.getSimpleName() + "[", "]")
                .add("id='" + getId() + "'")
                .add("name='" + name + "'")
                .toString();
    }
}
