package org.moultdb.api.model;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-20
 */
public class User extends NamedEntity {
    
    public User(String name) throws IllegalArgumentException {
        super(name);
    }
}
