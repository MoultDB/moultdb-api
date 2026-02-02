package org.moultdb.api.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moultdb.api.exception.AuthenticationException;
import org.moultdb.api.exception.RegistrationException;
import org.moultdb.api.exception.UserNotFoundException;
import org.moultdb.api.model.MoultDBUser;
import org.moultdb.api.model.moutldbenum.Role;
import org.moultdb.api.repository.dao.UserDAO;
import org.moultdb.api.repository.dto.UserTO;
import org.moultdb.api.service.MailService;
import org.moultdb.api.service.ServiceUtils;
import org.moultdb.api.service.TokenService;
import org.moultdb.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.moultdb.api.model.MoultDBUser.generateRandomPassword;
import static org.moultdb.api.service.impl.TokenServiceImpl.getLongTokenValidity;
import static org.moultdb.api.service.impl.TokenServiceImpl.getShortTokenValidity;

/**
 * @author Valentine Rech de Laval
 * @since 2023-04-11
 */
@Service
public class UserServiceImpl implements UserService {
    
    private final static Logger logger = LogManager.getLogger(UserServiceImpl.class.getName());
    
    @Autowired
    private UserDAO userDAO;
    
    @Autowired
    TokenService tokenService;
    
    @Autowired
    MailService mailService;
    
    @Value("${moultdb.url.webapp.moulting}")
    private String WEBAPP_URL;
    
    private static final String ROLE_SEPARATOR = ",";
    
    @Override
    public void saveUser(MoultDBUser user) {
        if (StringUtils.isBlank(user.getFullName()) || StringUtils.isBlank(user.getEmail())
                || StringUtils.isBlank(user.getPassword())) {
            throw new IllegalArgumentException("Name, e-mail and/or password is empty");
        }
        saveUser(user, Role.ROLE_USER.getStringRepresentation());
    }
    
    @Override
    public void saveAdmin(MoultDBUser user) {
        if (StringUtils.isBlank(user.getFullName()) || StringUtils.isBlank(user.getEmail())
                || StringUtils.isBlank(user.getPassword())) {
            throw new IllegalArgumentException("Name, e-mail and/or password is empty");
        }
        String authorities = user.getAuthorities().stream()
                           .map(GrantedAuthority::getAuthority)
                           .collect(Collectors.joining(ROLE_SEPARATOR));
        saveUser(user, authorities);
    }
    
    @Override
    public void saveExternalUser(MoultDBUser user) {
        saveUser(user, Role.ROLE_EXTERNAL.getStringRepresentation());
    }
    
    private void saveUser(MoultDBUser user, String roles) {
        if (user == null) {
            throw new IllegalArgumentException("User is empty");
        }
        if (StringUtils.isBlank(user.getUsername())) {
            throw new IllegalArgumentException("Username is empty");
        }
    
        try {
            userDAO.insertUser(new UserTO(null, user.getUsername(), user.getFullName(), user.getEmail(), user.getPassword(),
                    roles, user.getOrcidId(), false));
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
        tokenService.validateToken(email, token);
        int[] ints = userDAO.updateUserAsVerified(email);
        // Returns true if the user has been set to 'verified'
        return ints.length > 0 && ints[0] == 1;
    }
    
    @Override
    public MoultDBUser getUser(String email, String password) throws UserNotFoundException {
        if (StringUtils.isBlank(email) || StringUtils.isBlank(password)) {
            throw new IllegalArgumentException("E-mail or password is empty");
        }
    
        UserTO userTO = userDAO.findByEmailAndPassword(email, password);
        if (!userTO.isVerified()) {
            throw new AuthenticationException("User not validated");
        }
        return ServiceUtils.mapFromTO(userTO);
    }
    
    @Override
    public void forgotPassword(String email, String urlSuffix) {
        if (StringUtils.isBlank(email) || StringUtils.isBlank(urlSuffix)) {
            throw new IllegalArgumentException("E-mail or URL suffix is empty");
        }
        UserTO userTO = userDAO.findByEmail(email);
        if (userTO == null) {
            throw new UserNotFoundException(email);
        }
        String[] roles = getRoles(userTO);
        // Get token with short validity
        String token = tokenService.generateShortExpirationToken(userTO.getEmail(), roles);
        int tokenValidityMin = (int) (getShortTokenValidity() / 60000);
    
        // Send e-mail with link to reset password
        String resetPasswordLink = WEBAPP_URL + urlSuffix  + "?email=" + userTO.getEmail() + "&token=" + token;
        String message = "Dear " + userTO.getName() + ",\n\n" +
                "We have just received a password reset request for " + userTO.getEmail() + ".\n\n" +
                "Choose your new password to access your account using the link below (valid " + tokenValidityMin + " minutes):\n"+
                resetPasswordLink + "\n\n" +
                "If the link doesn't work, please copy and paste it into your browser.\n\n" +
                "Thank you,\n\n" +
                "The MoultDB team";
        logger.debug(resetPasswordLink);
        mailService.sendEmail(userTO.getEmail(), "[MoultDB] Reset your password", message);
    }
    
    private static String[] getRoles(UserTO userTO) {
        String[] roles = new String[0];
        if (StringUtils.isNotBlank(userTO.getRoles())) {
            roles = userTO.getRoles().split(ROLE_SEPARATOR); 
        }
        return roles;
    }
    
    @Override
    public void askEmailValidation(String email, String urlSuffix) {
        if (StringUtils.isBlank(email) || StringUtils.isBlank(urlSuffix)) {
            throw new IllegalArgumentException("E-mail or URL suffix is empty");
        }
        UserTO userTO = userDAO.findByEmail(email);
        if (userTO == null) {
            throw new UserNotFoundException(email);
        }
    
        // Get token with long validity
        String token = tokenService.generateLongExpirationToken(userTO.getEmail(), getRoles(userTO));
        int tokenValidityMin = (int) (getLongTokenValidity() / 60000 / 1440);
        
        // Send e-mail with link to reset password
        String validationLink = WEBAPP_URL + urlSuffix + "?email=" + userTO.getEmail() + "&token=" + token;
        String message = "Dear " + userTO.getName() + ",\n\n" +
                "To continue setting up your MoultDB account, please verify that this is your email address.\n\n" +
                "Please validate your e-mail address using the link below (valid " + tokenValidityMin + " days):\n"+
                validationLink + "\n\n" +
                "If the link doesn't work, please copy and paste it into your browser.\n\n" +
                "Thank you,\n\n" +
                "The MoultDB team";
        mailService.sendEmail(userTO.getEmail(), "[MoultDB] Verify email address", message);
    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (StringUtils.isBlank(email)) {
            throw new IllegalArgumentException("E-mail is empty");
        }
    
        UserTO userTO = userDAO.findByEmail(email);
        if (!userTO.isVerified()) {
            throw new AuthenticationException("User not validated");
        }
        
        return MoultDBUser.builder()
                          .username(userTO.getEmail())
                          .password(StringUtils.isNotBlank(userTO.getPassword()) ? userTO.getPassword() : generateRandomPassword())
                          .roles(getRoles(userTO))
                          .build();
    }
}
