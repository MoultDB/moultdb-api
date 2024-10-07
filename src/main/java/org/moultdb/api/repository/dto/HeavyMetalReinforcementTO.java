package org.moultdb.api.repository.dto;

import java.io.Serial;

/**
 * @author Valentine Rech de Laval
 * @since 2024-08-14
 */
public class HeavyMetalReinforcementTO extends NamedEntityTO<Integer> {
    
    @Serial
    private static final long serialVersionUID = 7280398818401381489L;
    
    public HeavyMetalReinforcementTO(Integer id, String name) throws IllegalArgumentException {
        super(id, name, null);
    }
}
