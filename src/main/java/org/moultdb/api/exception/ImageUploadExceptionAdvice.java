package org.moultdb.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.Map;

import static org.moultdb.api.controller.ResponseHandler.generateErrorResponse;

/**
 * @author Valentine Rech de Laval
 * @since 2023-05-31
 */
@ControllerAdvice
public class ImageUploadExceptionAdvice {
    
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, Object>> handleMaxSizeException( MaxUploadSizeExceededException e) {
        return generateErrorResponse(e.getMessage(), HttpStatus.PAYLOAD_TOO_LARGE);
    }
    
}
