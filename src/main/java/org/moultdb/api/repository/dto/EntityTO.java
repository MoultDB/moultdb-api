package org.moultdb.api.repository.dto;

import java.io.Serial;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
public abstract class EntityTO extends TransfertObject {
    
    @Serial
    private static final long serialVersionUID = 209058652122319455L;
    
    private final Integer id;
    
    public EntityTO(Integer id) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("the id provided cannot be blank.");
        }
        this.id = id;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    @Override
    public String toString() {
        return "EntityTO[id='" + id + "']";
    }
    
}
