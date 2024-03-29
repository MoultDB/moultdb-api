package org.moultdb.api.repository.dto;

import java.io.Serial;

/**
 * @author Valentine Rech de Laval
 * @since 2022-06-28
 */
public class SpecimenTypeTO extends NamedEntityTO<Integer> {
    
    @Serial
    private static final long serialVersionUID = 2031899208563315076L;
    
    public SpecimenTypeTO(Integer id, String name) throws IllegalArgumentException {
        super(id, name, null);
    }
}
