package org.moultdb.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

/**
 * @author Valentine Rech de Laval
 * @since 2023-04-12
 */
@ResponseStatus(value= HttpStatus.CONFLICT, reason="User or e-mail already exists")
public class RegistrationException extends MoultDBException {
    
    @Serial
    private static final long serialVersionUID = -1100050282089985805L;
    
    public RegistrationException(String message) {
        super(message);
    }
    
    public RegistrationException(String message, Exception e) {
        super(message, e);
    }
}
