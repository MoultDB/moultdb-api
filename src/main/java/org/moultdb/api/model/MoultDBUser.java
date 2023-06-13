package org.moultdb.api.model;

import org.apache.commons.lang3.StringUtils;
import org.moultdb.api.model.moutldbenum.Role;
import org.springframework.security.core.userdetails.User;

import java.io.Serial;
import java.util.Collection;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-20
 */
public class MoultDBUser extends User {
    
    @Serial
    private static final long serialVersionUID = -5845388570221521795L;
    
    private final String name;
    
    private final String orcidId;
    
    private final Boolean verified;
    
    public MoultDBUser(String email, String name, String password, Collection<Role> authorities, Boolean verified)
            throws IllegalArgumentException {
        this(email, name, password, authorities, verified, null);
    }
    
    public MoultDBUser(String email, String name, String password, Collection<Role> authorities,
                       Boolean verified, String orcidId) throws IllegalArgumentException {
        super(email, password, authorities);
        this.name = name;
        this.orcidId = orcidId;
        this.verified = verified;
    }
    
    public String getEmail() {
        return getUsername();
    }
    
    public String getName() {
        return name;
    }
    
    public String getOrcidId() {
        return orcidId;
    }
    
    public Boolean isVerified() {
        return verified;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", MoultDBUser.class.getSimpleName() + "[", "]")
                .add(super.toString())
                .add("email='" + getEmail() + "'")
                .add("name='" + getName() + "'")
                .add("password='" + (StringUtils.isNotEmpty(getPassword()) ? "[PROTECTED]" : null) + "'")
                .add("authorities='" + getAuthorities() + "'")
                .add("orcidId='" + getOrcidId() + "'")
                .add("verified='" + isVerified() + "'")
                .toString();
    }
}
