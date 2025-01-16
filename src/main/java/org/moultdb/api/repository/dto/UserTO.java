package org.moultdb.api.repository.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
public class UserTO extends NamedEntityTO<Integer> {
    
    @Serial
    private static final long serialVersionUID = -4015573667422118355L;
    
    private final String username;
    private final String email;
    private final String password;
    private final String orcidId;
    private final String roles;
    private final Boolean verified;
    
    public UserTO(Integer id, String username, String fullName, String roles, String orcidId)
            throws IllegalArgumentException {
        this(id, username, fullName, null, null, roles, orcidId, false);
    }
    
    public UserTO(Integer id, String username, String fullName, String email,
                  String password, String roles, String orcidId, Boolean verified)
            throws IllegalArgumentException {
        super(id, fullName, null);
        this.username = username;
        this.email = email;
        this.password = password;
        this.orcidId = orcidId;
        this.roles = roles;
        this.verified = verified;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getFullName() {
        return getName();
    }
    
    public String getEmail() {
        return email;
    }
    
    @JsonIgnore
    public String getPassword() {
        return password;
    }
    
    public String getRoles() {
        return roles;
    }
    
    public String getOrcidId() {
        return orcidId;
    }
    
    public Boolean isVerified() {
        return verified;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", UserTO.class.getSimpleName() + "[", "]")
                .add("id='" + getId() + "'")
                .add("username='" + getUsername() + "'")
                .add("fullName='" + getFullName() + "'")
                .add("email='" + email + "'")
                .add("roles='" + roles + "'")
                .add("password='" + (StringUtils.isNotBlank(password) ? "[PROTECTED]" : "") + "'")
                .add("orcidId='" + orcidId + "'")
                .add("verified='" + verified + "'")
                .toString();
    }
}
