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
    
    private static final String ALL_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+{}[]";
    
    private final String fullName;
    private final String email;
    private final Boolean verified;

    private final String orcidId;

    public MoultDBUser(String username, String fullName, String email, String password, String orcidId)
            throws IllegalArgumentException {
        this(username, fullName, email, password, orcidId, Collections.singleton(Role.ROLE_USER), false);
    }
    
    public MoultDBUser(String username, String fullName, String email, String password, String orcidId,
                       Collection<Role> authorities, Boolean verified) throws IllegalArgumentException {
        super(username, password, authorities);
        this.fullName = fullName;
        this.email = email;
        this.orcidId = orcidId;
        this.verified = verified;
    }
    
    //FIXME check if the method is used correctly
    public static String generateRandomPassword() {
        // Generate random length between 8 and 12
        int length = (int) (Math.random() * 5) + 8;
        
        // Generate random password
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            password.append(ALL_CHARS.charAt((int) (Math.random() * ALL_CHARS.length())));
        }
        return password.toString();
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
        return email;
    }
    
    public String getFullName() {
        return fullName;
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
                .add("username='" + getUsername() + "'")
                .add("name='" + getFullName() + "'")
                .add("email='" + getEmail() + "'")
                .add("password='" + (StringUtils.isNotEmpty(getPassword()) ? "[PROTECTED]" : null) + "'")
                .add("orcidId='" + getOrcidId() + "'")
                .add("authorities='" + getAuthorities() + "'")
                .add("verified='" + isVerified() + "'")
                .toString();
    }
}
