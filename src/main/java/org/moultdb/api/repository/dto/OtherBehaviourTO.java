package org.moultdb.api.repository.dto;

import java.io.Serial;

/**
 * @author Valentine Rech de Laval
 * @since 2024-08-14
 */
public class OtherBehaviourTO extends NamedEntityTO<Integer> {
    
    @Serial
    private static final long serialVersionUID = -2649609698069951033L;
    
    public OtherBehaviourTO(Integer id, String name) throws IllegalArgumentException {
        super(id, name, null);
    }
}
