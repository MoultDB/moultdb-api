package org.moultdb.api.service;

import org.moultdb.api.exception.UserNotFoundException;
import org.moultdb.api.model.User;

/**
 * @author Valentine Rech de Laval
 * @since 2023-04-11
 */
public interface UserService extends Service {
    
    public void saveUser(User user);
    
    public void saveAdmin(User user);
    
    public boolean updateUserPassword(String email, String password);
    
    public boolean setUserAsVerified(String email, String token);
    
    public User getUser(String email, String password) throws UserNotFoundException;
    
    public void askNewPassword(String email, String urlSuffix);
    
    public void askEmailValidation(String email, String urlSuffix);
    
}
