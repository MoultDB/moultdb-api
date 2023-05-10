package org.moultdb.api.repository.dto;

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
    
    private final String email;
    private final String password;
    private final String orcidId;
    private final String roles;
    
    public UserTO(Integer id, String name, String email, String password, String roles, String orcidId)
            throws IllegalArgumentException {
        super(id, name, null);
        this.email = email;
        this.password = password;
        this.orcidId = orcidId;
        this.roles = roles;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public String getRoles() {
        return roles;
    }
    
    public String getOrcidId() {
        return orcidId;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", UserTO.class.getSimpleName() + "[", "]")
                .add("id='" + getId() + "'")
                .add("name='" + getName() + "'")
                .add("email='" + email + "'")
                .add("roles='" + roles + "'")
                .add("password='" + (StringUtils.isNotBlank(password) ? "***" : "") + "'")
                .add("orcidId='" + orcidId + "'")
                .toString();
    }
}
