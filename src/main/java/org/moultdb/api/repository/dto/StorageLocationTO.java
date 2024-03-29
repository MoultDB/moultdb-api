package org.moultdb.api.repository.dto;

import java.io.Serial;

/**
 * @author Valentine Rech de Laval
 * @since 2021-11-22
 */
public class StorageLocationTO extends NamedEntityTO<Integer> {
    
    @Serial
    private static final long serialVersionUID = -6043320245268177473L;
    
    public StorageLocationTO(Integer id, String name) throws IllegalArgumentException {
        super(id, name, null);
    }
}
