package org.moultdb.api.repository.dto;

import java.io.Serial;

/**
 * @author Valentine Rech de Laval
 * @since 2024-08-14
 */
public class EgressDirectionTO extends NamedEntityTO<Integer> {
    
    @Serial
    private static final long serialVersionUID = -1308570402339195481L;
    
    public EgressDirectionTO(Integer id, String name) throws IllegalArgumentException {
        super(id, name, null);
    }
}
