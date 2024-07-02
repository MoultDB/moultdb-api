package org.moultdb.api.repository.dto;

import java.io.Serial;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2024-03-22
 */
public class DomainTO extends EntityTO<String> {
    
    @Serial
    private static final long serialVersionUID = -8068259646168632939L;
    
    private final String description;
    
    public DomainTO(String id, String description) throws IllegalArgumentException {
        super(id);
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", DomainTO.class.getSimpleName() + "[", "]")
                .add("id='" + getId() + "'")
                .add("description='" + description + "'")
                .toString();
    }
}
