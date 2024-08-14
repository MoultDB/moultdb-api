package org.moultdb.api.repository.dto;

import java.io.Serial;

/**
 * @author Valentine Rech de Laval
 * @since 2022-06-28
 */
public class LifeModeTO extends NamedEntityTO<Integer> {
    
    @Serial
    private static final long serialVersionUID = -3161203895730721809L;
    
    public LifeModeTO(Integer id, String name) throws IllegalArgumentException {
        super(id, name, null);
    }
}
