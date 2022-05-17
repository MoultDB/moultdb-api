package org.moultdb.api.repository.dto;

import java.io.Serial;
import java.util.Objects;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
public abstract class EntityTO<T extends Comparable<T>> extends TransfertObject {
    
    @Serial
    private static final long serialVersionUID = 209058652122319455L;
    
    private final T id;
    
    public EntityTO(T id) throws IllegalArgumentException {
        this.id = id;
    }
    
    public T getId() {
        return this.id;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        EntityTO<?> entityTO = (EntityTO<?>) o;
        return Objects.equals(id, entityTO.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "EntityTO[id='" + id + "']";
    }
}
