package org.moultdb.api.model;

import java.util.Objects;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-20
 */
public abstract class NamedEntity {
    
    private final String name;
    
    public NamedEntity(String name) throws IllegalArgumentException {
        if (name == null) {
            throw new IllegalArgumentException("the name provided cannot be blank.");
        }
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        NamedEntity that = (NamedEntity) o;
        return Objects.equals(name, that.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
    
    @Override
    public String toString() {
        return "NamedEntity[name='" + name + "']";
    }
    
}
