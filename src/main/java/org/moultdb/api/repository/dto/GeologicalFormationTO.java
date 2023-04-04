package org.moultdb.api.repository.dto;

import java.io.Serial;

/**
 * @author Valentine Rech de Laval
 * @since 2022-06-28
 */
public class GeologicalFormationTO extends NamedEntityTO<Integer> {
    
    @Serial
    private static final long serialVersionUID = -6230282249481421568L;
    
    public GeologicalFormationTO(Integer id, String name) throws IllegalArgumentException {
        super(id, name, null);
    }
}
