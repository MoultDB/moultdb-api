package org.moultdb.api.exception;

import java.io.Serial;

/**
 * @author Valentine Rech de Laval
 * @since 2023-11-09
 */
public class ImportException extends MoultDBException {
    
    @Serial
    private static final long serialVersionUID = 6887711131432689936L;
    
    public ImportException(String message) {
        super(message);
    }
    
    public ImportException(String message, Exception e) {
        super(message, e);
    }
}
