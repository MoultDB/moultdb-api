package org.moultdb.api.model;

import org.apache.commons.lang3.StringUtils;
import org.moultdb.api.model.moutldbenum.Role;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-20
 */
public class User {
    
    private  String email;
    
    private  String name;
    
    private Set<Role> roles;
    
    private String password;
    
    private String orcidId;
    
    private Boolean verified;
    
    public User() {
    }
    
    public User(String email, String name, Collection<Role> roles, String password, Boolean verified)
            throws IllegalArgumentException {
        this(email, name, roles, password, null, verified);
    }
    
    public User(String email, String name, Collection<Role> roles, String password,
                String orcidId, Boolean verified) throws IllegalArgumentException {
        this.email = email;
        this.name = name;
        this.roles = Collections.unmodifiableSet(roles == null ? new HashSet<>(): new HashSet<>(roles));
        this.password = password;
        this.orcidId = orcidId;
        this.verified = verified;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getName() {
        return name;
    }
    
    public Set<Role> getRoles() {
        return roles;
    }
    
    public String getPassword() {
        return password;
    }
    
    public String getOrcidId() {
        return orcidId;
    }
    
    public Boolean isVerified() {
        return verified;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
                .add("email='" + getEmail() + "'")
                .add("name='" + getName() + "'")
                .add("roles='" + getRoles() + "'")
                .add("password='" + (StringUtils.isNotEmpty(getPassword()) ? "***" : null) + "'")
                .add("orcidId='" + getOrcidId() + "'")
                .add("verified='" + isVerified() + "'")
                .toString();
    }
}
