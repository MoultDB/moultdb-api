package org.moultdb.api.repository.dto;

import java.io.Serial;

/**
 * @author Valentine Rech de Laval
 * @since 2022-05-02
 */
public class AnatEntityTO extends NamedEntityTO<String> {
    
    @Serial
    private static final long serialVersionUID = -2871190938086136387L;
    
    public AnatEntityTO(String id, String name, String description) throws IllegalArgumentException {
        super(id, name, description);
    }
}
