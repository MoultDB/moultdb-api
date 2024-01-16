package org.moultdb.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

/**
 * @author Valentine Rech de Laval
 * @since 2023-04-12
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No such Order")  // 404
public class AuthenticationException extends MoultDBException {
    @Serial
    private static final long serialVersionUID = -6525059105953181989L;
    
    public AuthenticationException(String message) {
        super(message);
    }
    
    public AuthenticationException(String message, Exception e) {
        super(message, e);
    }
}
