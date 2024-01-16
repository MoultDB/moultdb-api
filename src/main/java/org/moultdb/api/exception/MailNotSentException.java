package org.moultdb.api.exception;

import java.io.Serial;

/**
 * @author Valentine Rech de Laval
 * @since 2023-04-25
 */
public class MailNotSentException extends MoultDBException {
    
    @Serial
    private static final long serialVersionUID = -3848875773170328973L;
    
    public MailNotSentException(String message, Exception e) {
        super(message, e);
    }
}
