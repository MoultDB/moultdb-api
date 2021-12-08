package org.moultdb.api.model;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * Parent class of all classes corresponding to real entities in the MoultDB database.
 *
 * @author Valentine Rech de Laval
 * @since 2021-11-23
 *
 * @param <T> The type of ID of this {@code Entity}
 */
public abstract class Entity<T extends Comparable<T>> {

    private final T id;
    
    /**
     * Constructor providing the ID of this {@code Entity}.
     * {@code id} cannot be {@code null}, otherwise an {@code IllegalArgumentException} is thrown.
     *
     * @param id	A {@code T} representing the ID of this {@code Entity}.
     * @throws IllegalArgumentException 	if {@code id} is blank.
     */
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
    
    @Override
    public String toString() {
        return new StringJoiner(", ", Entity.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .toString();
    }
}
