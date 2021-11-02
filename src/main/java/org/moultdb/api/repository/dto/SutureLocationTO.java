package org.moultdb.api.repository.dto;

import java.io.Serial;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
public class SutureLocationTO extends NamedEntityTO {
    
    @Serial
    private static final long serialVersionUID = 4216715198720281346L;
    
    public SutureLocationTO(Integer id, String name) throws IllegalArgumentException {
        super(id, name);
    }
}
