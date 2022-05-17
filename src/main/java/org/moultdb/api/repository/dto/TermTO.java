package org.moultdb.api.repository.dto;

import java.io.Serial;

/**
 * @author Valentine Rech de Laval
 * @since 2022-05-03
 */
public class TermTO extends NamedEntityTO<String> {
    
    @Serial
    private static final long serialVersionUID = -2059860445347943560L;
    
    public TermTO(String id, String name, String description) throws IllegalArgumentException {
        super(id, name, description);
    }
}
