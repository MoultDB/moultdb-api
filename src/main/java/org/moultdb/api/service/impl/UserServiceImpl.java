package org.moultdb.api.service.impl;

import org.moultdb.api.exception.RegistrationException;
import org.moultdb.api.exception.UserNotFoundException;
import org.moultdb.api.model.User;
import org.moultdb.api.model.moutldbenum.Role;
import org.moultdb.api.repository.dao.UserDAO;
import org.moultdb.api.repository.dto.UserTO;
import org.moultdb.api.service.MailService;
import org.moultdb.api.service.TokenGeneratorService;
import org.moultdb.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.moultdb.api.service.impl.TokenGeneratorServiceImpl.getShortTokenValidity;

/**
 * @author Valentine Rech de Laval
 * @since 2023-04-11
 */
@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserDAO userDAO;
    
    @Autowired
    TokenGeneratorService tokenGeneratorService;
    
    @Autowired
    MailService mailService;
    
    @Value("${url.api}")
    private String API_URL;
    
    @Override
    public void saveUser(User user) {
        saveUser(user, Role.ROLE_USER.getStringRepresentation());
    }
    
    @Override
    public void saveAdmin(User user) {
        String roles = user.getRoles().stream()
                           .map(Role::getStringRepresentation)
                           .collect(Collectors.joining(","));
        saveUser(user, roles);
    }
    
    private void saveUser(User user, String roles) {
        try {
            userDAO.insertUser(new UserTO(null, user.getName(), user.getEmail(), user.getPassword(), roles, user.getOrcidId()));
        } catch (Exception e) {
            Matcher m = Pattern.compile("Duplicate entry '(.*)' for key 'user\\.u_(.*)'")
                               .matcher(e.getCause().getLocalizedMessage());
            String message = e.getMessage();
            if (m.find()) {
                message = m.group(2) + " already exists";
            }
            throw new RegistrationException(message);
        }
    }
    
    @Override
    public boolean updateUserPassword(String email, String password) {
        int[] ints = userDAO.updateUserPassword(email, password);
    
        // Returns true if the pwd has been updated
        return ints.length > 0 && ints[0] == 1;
    }
    
    @Override
    public User getUserByNameAndPassword(String name, String password) throws UserNotFoundException {
        UserTO userTO = userDAO.findByEmailAndPassword(name, password);
        return org.moultdb.api.service.Service.mapFromTO(userTO);
    }
    
    @Override
    public void askNewPassword(String email) {
        UserTO userTO = userDAO.findByEmail(email);
        if (userTO == null) {
            throw new UserNotFoundException();
        }
    
        // Get token with short validity
        String token = tokenGeneratorService.generateShortExpirationToken(userTO.getEmail());
    
        // Send e-mail with link to reset password
        String resetPasswordLink = API_URL + "/reset-password?token=" + token;
        int tokenValidityMin = (int) (getShortTokenValidity() / 60000);
        String message = "Dear " + userTO.getName() + ",\n\n" +
                "We have just received a password reset request for " + userTO.getEmail() + ".\n\n" +
                "Choose your new password to access your account using the link below (valid " + tokenValidityMin + " minutes):\n"+
                resetPasswordLink + "\n\n" +
                "If the link doesn't work, please copy and paste it into your browser.\n\n" +
                "Thank you,\n\n" +
                "The MoultDB team";
        mailService.sendEmail(userTO.getEmail(), "Reset your password", message);
    }
}
