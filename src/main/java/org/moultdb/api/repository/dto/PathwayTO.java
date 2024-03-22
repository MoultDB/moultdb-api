package org.moultdb.api.repository.dto;

import java.io.Serial;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2024-03-22
 */
public class PathwayTO extends EntityTO<String> {
    
    @Serial
    private static final long serialVersionUID = -8068259646168632939L;
    
    private final String name;
    
    public PathwayTO(String id, String name) throws IllegalArgumentException {
        super(id);
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", PathwayTO.class.getSimpleName() + "[", "]")
                .add("id='" + getId() + "'")
                .add("name='" + name + "'")
                .toString();
    }
}
