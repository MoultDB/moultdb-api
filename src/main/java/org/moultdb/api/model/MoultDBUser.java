package org.moultdb.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import org.moultdb.api.model.moutldbenum.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.Serial;
import java.util.Collection;
import java.util.Collections;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-20
 */
public class MoultDBUser extends User {
    
    @Serial
    private static final long serialVersionUID = -5845388570221521795L;
    
    private final String name;
    private final Boolean verified;

    private final String orcidId;

    public MoultDBUser(String email, String name, String password, String orcidId)
            throws IllegalArgumentException {
        this(email, name, password, orcidId, Collections.singleton(Role.ROLE_USER), false);
    }
    
    public MoultDBUser(String email, String name, String password, String orcidId,
                       Collection<Role> authorities, Boolean verified) throws IllegalArgumentException {
        super(email, password, authorities);
        this.name = name;
        this.orcidId = orcidId;
        this.verified = verified;
    }
    
    @JsonIgnore
    @Override
    public String getUsername() {
        return super.getUsername();
    }
    
    @JsonIgnore
    @Override
    public String getPassword() {
        return super.getPassword();
    }
    
    @JsonIgnore
    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return super.getAuthorities();
    }
    
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return super.isAccountNonExpired();
    }
    
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return super.isAccountNonLocked();
    }
    
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return super.isCredentialsNonExpired();
    }
    
    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }
    
    @JsonIgnore
    public String getEmail() {
        return getUsername();
    }
    
    public String getName() {
        return name;
    }
    
    public String getOrcidId() {
        return orcidId;
    }
    
    @JsonIgnore
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
                .add("orcidId='" + getOrcidId() + "'")
                .add("authorities='" + getAuthorities() + "'")
                .add("verified='" + isVerified() + "'")
                .toString();
    }
}
