package org.moultdb.api.repository.dto;

import java.io.Serial;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
public class EgressDirectionTO extends NamedEntityTO {
    
    @Serial
    private static final long serialVersionUID = 8193485479606730914L;
    
    public EgressDirectionTO(Integer id, String name) throws IllegalArgumentException {
        super(id, name);
    }
}
