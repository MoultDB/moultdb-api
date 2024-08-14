package org.moultdb.api.repository.dto;

import java.io.Serial;

/**
 * @author Valentine Rech de Laval
 * @since 2024-08-14
 */
public class ExuviaePositionTO extends NamedEntityTO<Integer> {
    
    @Serial
    private static final long serialVersionUID = 1966167742492101661L;
    
    public ExuviaePositionTO(Integer id, String name) throws IllegalArgumentException {
        super(id, name, null);
    }
}
