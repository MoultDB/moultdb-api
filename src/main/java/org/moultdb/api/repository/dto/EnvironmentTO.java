package org.moultdb.api.repository.dto;

import java.io.Serial;

/**
 * @author Valentine Rech de Laval
 * @since 2022-06-28
 */
public class EnvironmentTO extends NamedEntityTO<Integer> {
    
    @Serial
    private static final long serialVersionUID = -183813613544435789L;
    
    public EnvironmentTO(Integer id, String name) throws IllegalArgumentException {
        super(id, name, null);
    }
}
