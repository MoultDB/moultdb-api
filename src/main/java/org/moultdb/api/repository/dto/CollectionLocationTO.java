package org.moultdb.api.repository.dto;

import java.io.Serial;

/**
 * @author Valentine Rech de Laval
 * @since 2022-06-28
 */
public class CollectionLocationTO extends NamedEntityTO<Integer> {
    
    @Serial
    private static final long serialVersionUID = -6043320245268177473L;
    
    public CollectionLocationTO(Integer id, String name) throws IllegalArgumentException {
        super(id, name, null);
    }
}
