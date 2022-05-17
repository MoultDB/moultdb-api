package org.moultdb.api.model;

import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-20
 */
public class User extends NamedEntity<String> {
    
    private final String orcidId;
    
    public User(String email, String name) throws IllegalArgumentException {
        this(email, name, null);
    }
    
    public User(String email, String name, String orcidId) throws IllegalArgumentException {
        super(email, name);
        this.orcidId = orcidId;
    }
    
    public String getEmail() {
        return getId();
    }
    
    public String getOrcidId() {
        return orcidId;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
                .add("email='" + getEmail() + "'")
                .add("name='" + getName() + "'")
                .add("orcidId='" + orcidId + "'")
                .toString();
    }
}
