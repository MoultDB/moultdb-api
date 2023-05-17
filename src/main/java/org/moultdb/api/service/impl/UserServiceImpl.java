package org.moultdb.api.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.moultdb.api.exception.AuthenticationException;
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

import static org.moultdb.api.service.impl.TokenGeneratorServiceImpl.getLongTokenValidity;
import static org.moultdb.api.service.impl.TokenGeneratorServiceImpl.getMiddleTokenValidity;
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
        if (user == null) {
            throw new IllegalArgumentException("User is empty");
        }
    
        try {
            userDAO.insertUser(new UserTO(null, user.getName(), user.getEmail(), user.getPassword(), roles,
                    user.getOrcidId(), false));
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
        if (StringUtils.isBlank(email) || StringUtils.isBlank(password)) {
            throw new IllegalArgumentException("E-mail or password is empty");
        }
        int[] ints = userDAO.updateUserPassword(email, password);
    
        // Returns true if the pwd has been updated
        return ints.length > 0 && ints[0] == 1;
    }
    
    @Override
    public boolean setUserAsVerified(String email, String token) {
        if (StringUtils.isBlank(email) || StringUtils.isBlank(token)) {
            throw new IllegalArgumentException("E-mail or token is empty");
        }
        int[] ints = userDAO.updateUserAsVerified(email);
        tokenGeneratorService.validateToken(email, token);
        // Returns true if the user has been set to 'verified'
        return ints.length > 0 && ints[0] == 1;
    }
    
    @Override
    public User getUser(String email, String password) throws UserNotFoundException {
        if (StringUtils.isBlank(email) || StringUtils.isBlank(password)) {
            throw new IllegalArgumentException("E-mail or password is empty");
        }
    
        UserTO userTO = userDAO.findByEmailAndPassword(email, password);
        if (!userTO.isVerified()) {
            throw new AuthenticationException("User not validated");
        }
        return org.moultdb.api.service.Service.mapFromTO(userTO);
    }
    
    @Override
    public void askNewPassword(String email, String urlSuffix) {
        if (StringUtils.isBlank(email) || StringUtils.isBlank(urlSuffix)) {
            throw new IllegalArgumentException("E-amil or URL suffix is empty");
        }
        UserTO userTO = userDAO.findByEmail(email);
        if (userTO == null) {
            throw new UserNotFoundException();
        }
    
        // Get token with short validity
        String token = tokenGeneratorService.generateShortExpirationToken(userTO.getEmail());
        int tokenValidityMin = (int) (getShortTokenValidity() / 60000);
    
        // Send e-mail with link to reset password
        String resetPasswordLink = API_URL + urlSuffix +"?token=" + token;
        String message = "Dear " + userTO.getName() + ",\n\n" +
                "We have just received a password reset request for " + userTO.getEmail() + ".\n\n" +
                "Choose your new password to access your account using the link below (valid " + tokenValidityMin + " minutes):\n"+
                resetPasswordLink + "\n\n" +
                "If the link doesn't work, please copy and paste it into your browser.\n\n" +
                "Thank you,\n\n" +
                "The MoultDB team";
        mailService.sendEmail(userTO.getEmail(), "[MoultDB] Reset your password", message);
    }
    
    @Override
    public void askEmailValidation(String email, String urlSuffix) {
        if (StringUtils.isBlank(email) || StringUtils.isBlank(urlSuffix)) {
            throw new IllegalArgumentException("E-amil or URL suffix is empty");
        }
        UserTO userTO = userDAO.findByEmail(email);
        if (userTO == null) {
            throw new UserNotFoundException();
        }
    
        // Get token with long validity
        String token = tokenGeneratorService.generateLongExpirationToken(userTO.getEmail());
        int tokenValidityMin = (int) (getLongTokenValidity() / 60000 / 1440);
        
        // Send e-mail with link to reset password
        String validationLink = API_URL + urlSuffix +"?email=" + userTO.getEmail() + "&token=" + token;
        String message = "Dear " + userTO.getName() + ",\n\n" +
                "To continue setting up your MoultDB account, please verify that this is your email address.\n\n" +
                "Please validate your e-mail address using the link below (valid " + tokenValidityMin + " days):\n"+
                validationLink + "\n\n" +
                "If the link doesn't work, please copy and paste it into your browser.\n\n" +
                "Thank you,\n\n" +
                "The MoultDB team";
        mailService.sendEmail(userTO.getEmail(), "[MoultDB] Verify email address", message);
    }
}
