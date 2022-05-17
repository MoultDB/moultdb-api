package org.moultdb.api.model;

import java.util.Objects;

/**
 * @author Valentine Rech de Laval
 * @since 2021-11-23
 *
 * @param <T> The type of accession of this {@code AccEntity}
 */
public abstract class Entity<T extends Comparable<T>> {

    private final T id;
    
    public Entity(T id) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("the ID provided cannot be blank.");
        }
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
        Entity<?> entity = (Entity<?>) o;
        return Objects.equals(id, entity.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
