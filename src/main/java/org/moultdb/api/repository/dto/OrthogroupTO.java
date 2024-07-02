package org.moultdb.api.repository.dto;

import java.io.Serial;

/**
 * @author Valentine Rech de Laval
 * @since 2024-05-22
 */
public class OrthogroupTO extends NamedEntityTO<Integer> {
    
    @Serial
    private static final long serialVersionUID = -3332044627031300608L;
    
    public OrthogroupTO(Integer id, String name) throws IllegalArgumentException {
        super(id, name, null);
    }
    
    public OrthogroupTO(Integer id, String name, String description) throws IllegalArgumentException {
        super(id, name, description);
    }
}
