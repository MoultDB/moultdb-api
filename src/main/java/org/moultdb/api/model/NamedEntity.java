package org.moultdb.api.model;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-20
 */
public abstract class NamedEntity<T extends Comparable<T>> extends Entity<T> {
    
    private final String name;
    private final String description;
    
    public NamedEntity(T id, String name) throws IllegalArgumentException {
        this(id, name, null);
    }
    
    public NamedEntity(T id, String name, String description) throws IllegalArgumentException {
        super(id);
        if (name == null) {
            throw new IllegalArgumentException("the name provided cannot be blank.");
        }
        this.name = name;
        this.description = description;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }

}
