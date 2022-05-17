package org.moultdb.api.repository.dto;

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
    private final String orcidId;
    
    public UserTO(Integer id, String name, String email, String orcidId) throws IllegalArgumentException {
        super(id, name, null);
        this.email = email;
        this.orcidId = orcidId;
    }
    
    public String getEmail() {
        return email;
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
                .add("orcidId='" + orcidId + "'")
                .toString();
    }
}
