package org.moultdb.api.exception;

import java.io.Serial;

/**
 * @author Valentine Rech de Laval
 * @since 2023-04-11
 */
public class UserNotFoundException extends MoultDBException {
    
    @Serial
    private static final long serialVersionUID = -941186233088699030L;
    
    public UserNotFoundException() {
        super("User not found");
    }
    
    public UserNotFoundException(String message) {
        super(message);
    }
    
}
