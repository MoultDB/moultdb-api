package org.moultdb.api.service;

import org.moultdb.api.model.User;

/**
 * @author Valentine Rech de Laval
 * @since 2023-04-11
 */
public interface TokenGeneratorService {
    
    String generateLongExpirationToken(User user);
    
    String generateShortExpirationToken(String email);
}
