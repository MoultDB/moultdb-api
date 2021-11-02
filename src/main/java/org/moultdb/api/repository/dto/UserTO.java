package org.moultdb.api.repository.dto;

import java.io.Serial;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
public class UserTO extends NamedEntityTO {
    
    @Serial
    private static final long serialVersionUID = -4015573667422118355L;
    
    public UserTO(Integer id, String name) throws IllegalArgumentException {
        super(id, name);
    }
}
