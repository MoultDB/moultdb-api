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
    
    public User getUserByNameAndPassword(String name, String password) throws UserNotFoundException;
    
    public void askNewPassword(String email);
    
}
