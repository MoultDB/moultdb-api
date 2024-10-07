package org.moultdb.api.repository.dto;

import java.io.Serial;

/**
 * @author Valentine Rech de Laval
 * @since 2024-08-14
 */
public class FossilExuviaeQualityTO extends NamedEntityTO<Integer> {
    
    @Serial
    private static final long serialVersionUID = 6950145194041877556L;
    
    public FossilExuviaeQualityTO(Integer id, String name) throws IllegalArgumentException {
        super(id, name, null);
    }
}
