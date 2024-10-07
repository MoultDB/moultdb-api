package org.moultdb.api.repository.dto;

import java.io.Serial;

/**
 * @author Valentine Rech de Laval
 * @since 2024-09-17
 */
public class ECOTermTO extends NamedEntityTO<String> {
    
    @Serial
    private static final long serialVersionUID = 6919144138192098969L;
    
    public ECOTermTO(String id, String name, String description) throws IllegalArgumentException {
        super(id, name, description);
    }
}
