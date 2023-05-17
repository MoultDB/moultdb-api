package org.moultdb.api.service;

import org.moultdb.api.model.User;

/**
 * @author Valentine Rech de Laval
 * @since 2023-04-11
 */
public interface TokenGeneratorService {
    
    boolean validateToken(String email, String token);
    
    String generateLongExpirationToken(String email);
    
    String generateMiddleExpirationToken(String email);
    
    String generateShortExpirationToken(String email);
}
