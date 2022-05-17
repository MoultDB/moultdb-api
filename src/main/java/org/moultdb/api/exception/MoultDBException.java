package org.moultdb.api.exception;

import java.io.Serial;

/**
 * @author Valentine Rech de Laval
 * @since 2022-05-10
 */
public class MoultDBException extends RuntimeException {
    
    @Serial
    private static final long serialVersionUID = 8646537622780183745L;
    
    public MoultDBException(Exception e) {
        super(e);
    }
    
    public MoultDBException(String string) {
        super(string);
    }
    
    public MoultDBException(String message, Exception e) {
        super(message, e);
    }

}
