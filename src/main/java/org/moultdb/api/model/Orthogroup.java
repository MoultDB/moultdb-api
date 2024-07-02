package org.moultdb.api.model;

import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2024-05-22
 */
public class Orthogroup extends Entity<Integer> {
    
    private final String name;
    
    public Orthogroup(Integer accession, String name) throws IllegalArgumentException {
        super(accession);
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", Orthogroup.class.getSimpleName() + "[", "]")
                .add("id='" + getId() + "'")
                .add("name='" + name + "'")
                .toString();
    }
}
