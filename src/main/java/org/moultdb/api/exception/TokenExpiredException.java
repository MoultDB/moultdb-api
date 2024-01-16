package org.moultdb.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

/**
 * @author Valentine Rech de Laval
 * @since 2023-05-17
 */
@ResponseStatus(value= HttpStatus.UNAUTHORIZED, reason="Token expired/invalid")  // 401
public class TokenExpiredException extends MoultDBException {
    
    @Serial
    private static final long serialVersionUID = 2051784091124865833L;
    
    public TokenExpiredException() {
        super("Token is expired");
    }
    
    public TokenExpiredException(String message) {
        super(message);
    }
    
    public TokenExpiredException(String message, Exception e) {
        super(message, e);
    }
}

