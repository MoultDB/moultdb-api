package org.moultdb.api.exception;

import java.io.Serial;

/**
 * @author Valentine Rech de Laval
 * @since 2023-06-07
 */
public class ImageUploadException extends MoultDBException {
    
    @Serial
    private static final long serialVersionUID = -1364624577581576140L;
    
    public ImageUploadException(String message) {
        super(message);
    }
    
    public ImageUploadException(String message, Exception e) {
        super(message, e);
    }
}
