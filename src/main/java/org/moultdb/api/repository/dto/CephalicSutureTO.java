package org.moultdb.api.repository.dto;

import java.io.Serial;

/**
 * @author Valentine Rech de Laval
 * @since 2024-08-14
 */
public class CephalicSutureTO extends NamedEntityTO<Integer> {
    
    @Serial
    private static final long serialVersionUID = -4266131924393155950L;
    
    public CephalicSutureTO(Integer id, String name) throws IllegalArgumentException {
        super(id, name, null);
    }
}
