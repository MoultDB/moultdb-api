package org.moultdb.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Valentine Rech de Laval
 * @since 2023-05-31
 */
public class ResponseHandler {
    
    public static ResponseEntity<Map<String, Object>> generateErrorResponse(Exception e) {
        return generateErrorResponse(e.getMessage(), HttpStatus.CONFLICT);
    }
    
    public static ResponseEntity<Map<String, Object>> generateErrorResponse(String message, HttpStatus httpStatus) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "true");
        response.put("message", message);
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
    
    public static <T> ResponseEntity<T> generateValidResponse(T responseObj) {
        return ResponseEntity.ok(responseObj);
    }
    
}
