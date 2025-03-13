package org.moultdb.api.model.moutldbenum;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author Valentine Rech de Laval
 * @since 2023-04-11
 */
public enum Role implements MoutldbEnum, GrantedAuthority {
    
    ROLE_EXTERNAL("ROLE_EXTERNAL"),
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN");
    
    private final String stringRepresentation;
    
    Role(String stringRepresentation) {
        this.stringRepresentation = stringRepresentation;
    }
    
    @Override
    public String getStringRepresentation() {
        return this.stringRepresentation;
    }
    
    @Override
    public String getAuthority() {
        return getStringRepresentation();
    }
    
    public static Role valueOfByStringRepresentation(String representation) {
        return MoutldbEnum.valueOfByStringRepresentation(Role.class, representation);
    }
}
