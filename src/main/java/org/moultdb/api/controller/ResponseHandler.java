package org.moultdb.api.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Valentine Rech de Laval
 * @since 2023-05-31
 */
public class ResponseHandler {
    
    private final static Logger logger = LogManager.getLogger(ResponseHandler.class.getName());
    
    public static ResponseEntity<Map<String, Object>> generateErrorResponse(Exception e) {
        return generateErrorResponse(e.getMessage(), HttpStatus.CONFLICT, e);
    }
    
    public static ResponseEntity<Map<String, Object>> generateErrorResponse(String message, HttpStatus httpStatus) {
        return generateErrorResponse(message, httpStatus, null);
    }
    
    public static ResponseEntity<Map<String, Object>> generateErrorResponse(String message, HttpStatus httpStatus, Exception e) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "true");
        response.put("message", message);
        logger.error("An error has been generated", e);
        return org.springframework.http.ResponseEntity.status(httpStatus).body(response);
    }
    
    public static ResponseEntity<Map<String, Object>> generateValidResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        return ResponseEntity.ok(response);
    }
    
    public static ResponseEntity<Map<String, Object>> generateValidResponse(String message, Object responseObj) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("data", responseObj);
        return ResponseEntity.ok(response);
    }
    
    public static <T> ResponseEntity<Map<String, T>> generateValidResponse(T responseObj) {
        Map<String, T> response = new HashMap<>();
        response.put("data", responseObj);
        return ResponseEntity.ok(response);
    }
    
}
