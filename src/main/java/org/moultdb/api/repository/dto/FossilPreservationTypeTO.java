package org.moultdb.api.repository.dto;

import java.io.Serial;

/**
 * @author Valentine Rech de Laval
 * @since 2022-06-28
 */
public class FossilPreservationTypeTO extends NamedEntityTO<Integer> {
    
    @Serial
    private static final long serialVersionUID = 1449448638488175553L;
    
    public FossilPreservationTypeTO(Integer id, String name) throws IllegalArgumentException {
        super(id, name, null);
    }
}
