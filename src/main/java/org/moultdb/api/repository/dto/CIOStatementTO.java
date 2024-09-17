package org.moultdb.api.repository.dto;

import java.io.Serial;

/**
 * @author Valentine Rech de Laval
 * @since 2024-09-17
 */
public class CIOStatementTO extends NamedEntityTO<String> {
    
    @Serial
    private static final long serialVersionUID = 328789296979715107L;
    
    public CIOStatementTO(String id, String name, String description) throws IllegalArgumentException {
        super(id, name, description);
    }
}
