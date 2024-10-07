package org.moultdb.api.repository.dto;

import java.io.Serial;

/**
 * @author Valentine Rech de Laval
 * @since 2024-08-14
 */
public class SutureLocationTO extends NamedEntityTO<Integer> {
    
    @Serial
    private static final long serialVersionUID = 5253432426445440671L;
    
    public SutureLocationTO(Integer id, String name) throws IllegalArgumentException {
        super(id, name, null);
    }
}
