package org.moultdb.api.repository.dto;

import java.io.Serial;

/**
 * @author Valentine Rech de Laval
 * @since 2024-08-14
 */
public class ResultingNamedMoultingConfigurationTO extends NamedEntityTO<Integer> {
    
    @Serial
    private static final long serialVersionUID = 2774802631560533711L;
    
    public ResultingNamedMoultingConfigurationTO(Integer id, String name) throws IllegalArgumentException {
        super(id, name, null);
    }
}
