package org.moultdb.api.repository.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
public abstract class TransfertObject implements Serializable {
    
    @Serial
    private static final long serialVersionUID = -4784865020663037892L;
    
    public static  <T extends TransfertObject> T getOneTO(List<T> toList) {
        if (toList == null || toList.isEmpty()) {
            return null;
        } else if (toList.size() > 1) {
            throw new IllegalStateException("Unique TO could not be found");
        }
        return toList.get(0);
    }
    
}
