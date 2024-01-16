package org.moultdb.api.service;

/**
 * @author Valentine Rech de Laval
 * @since 2023-04-11
 */
public interface TokenService {
    
    String getUsernameFromToken(String token);
    
    boolean validateToken(String email, String token);
    
    String generateLongExpirationToken(String email, String[] roles);
    
    String generateMiddleExpirationToken(String email, String[] roles);
    
    String generateShortExpirationToken(String email, String[] roles);
}
