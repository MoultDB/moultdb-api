package org.moultdb.api.repository.dao;

import org.moultdb.api.repository.dto.UserTO;

import java.util.List;
import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
public interface UserDAO extends DAO<UserTO> {
    
    List<UserTO> findAll();
    
    UserTO findById(int id);
    
    List<UserTO> findByIds(Set<Integer> ids);
    
    UserTO findByEmailAndPassword(String email, String password);
    
    UserTO findByEmail(String email);

    UserTO findByOrcidId(String orcidId);
    
    int[] insertUser(UserTO userTO);
    
    int[] updateUserPassword(String email, String password);
    
    int[] updateUserAsVerified(String email);

}
