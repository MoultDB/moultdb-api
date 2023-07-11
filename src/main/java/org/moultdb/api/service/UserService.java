package org.moultdb.api.service;

import org.moultdb.api.exception.UserNotFoundException;
import org.moultdb.api.model.MoultDBUser;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author Valentine Rech de Laval
 * @since 2023-04-11
 */
public interface UserService extends UserDetailsService {
    
    public void saveUser(MoultDBUser user);
    
    public void saveAdmin(MoultDBUser user);
    
    public boolean updateUserPassword(String email, String password);
    
    public boolean setUserAsVerified(String email, String token);
    
    public MoultDBUser getUser(String email, String password) throws UserNotFoundException;
    
    public void forgotPassword(String email, String urlSuffix);
    
    public void askEmailValidation(String email, String urlSuffix);
    
}
