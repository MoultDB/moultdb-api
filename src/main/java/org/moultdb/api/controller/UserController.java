package org.moultdb.api.controller;

import org.apache.commons.lang3.StringUtils;
import org.moultdb.api.exception.TokenExpiredException;
import org.moultdb.api.exception.UserNotFoundException;
import org.moultdb.api.model.User;
import org.moultdb.api.service.TokenGeneratorService;
import org.moultdb.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Valentine Rech de Laval
 * @since 2023-04-11
 */
@RestController
@RequestMapping("user")
public class UserController {
    
    private final UserService userService;
    
    private final TokenGeneratorService tokenGeneratorService;
    
    @Autowired
    public UserController(UserService userService, TokenGeneratorService tokenGeneratorService) {
        this.userService = userService;
        this.tokenGeneratorService = tokenGeneratorService;
    }
    @PostMapping("/registration")
    public ResponseEntity<?> postUser(@RequestBody User user) {
        try {
            userService.saveUser(user);
            userService.askEmailValidation(user.getEmail(), "/user/validation");
    
            return getSimpleResponseEntity("New user " + user.getName() + " created." +
                    "We have just sent an e-mail validation request. " +
                    "Please check your e-mail inbox for a message from us that contains instructions " +
                    "on how to validate your e-mail address. " +
                    "If you do not see an e-mail from us in your inbox, please check your spam folder " +
                    "or contact our support team for assistance. " +
                    "Thank you, " +
                    "The MoultDB team");
        } catch (Exception e) {
            return getErrorResponseEntity(e);
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> json) {
        String email = getParam(json, "email");
        String password = getParam(json, "password");
        try {
            User user = userService.getUser(email, password);
            Map<String, Object> userResp = new HashMap<>();
            userResp.put("email", user.getEmail());
            userResp.put("name", user.getName());
            userResp.put("roles", user.getRoles());
            userResp.put("orcidId", user.getOrcidId());
            userResp.put("token", tokenGeneratorService.generateMiddleExpirationToken(user.getEmail()));
    
            Map<String, Object> resp = new HashMap<>();
            resp.put("data", userResp);
            resp.put("message", "User logged in");
    
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            return getErrorResponseEntity(e);
        }
    }
    
    @GetMapping("/ask-password")
    public ResponseEntity<?> askNewPassword(@RequestParam String email) {
        try {
            userService.askNewPassword(email, "/user/reset-password");
            return getSimpleResponseEntity( "We have just received a password reset request. " +
                    "Please check your e-mail inbox for a message from us that contains instructions " +
                    "on how to create your new password. " +
                    "If you do not see an e-mail from us in your inbox, please check your spam folder " +
                    "or contact our support team for assistance. " +
                    "Thank you, " +
                    "The MoultDB team");
        } catch (Exception e) {
            return getErrorResponseEntity(e);
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> json) {
        String email = getParam(json, "email");
        String password = getParam(json, "password");
        try {
            boolean isUpdated = userService.updateUserPassword(email, password);
    
            Map<String, String> response = new HashMap<>();
            if (isUpdated) {
                return getSimpleResponseEntity("Your password has been updated.");
            }
            return getErrorResponseEntity("An error occurs during the update of the password. " +
                        "Please contact our support team for assistance.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return getErrorResponseEntity(e);
        }
    }
    
    @GetMapping("/email-validation")
    public ResponseEntity<?> validateUser(@RequestParam String email, @RequestParam String token) {
        try {
            boolean isUpdated = userService.setUserAsVerified(email, token);
            
            if (isUpdated) {
                return getSimpleResponseEntity("Your e-mail has been validated.");
            }
            return getErrorResponseEntity("An error occurs during the validation of the e-mail. " +
                        "Please contact our support team for assistance.", HttpStatus.INTERNAL_SERVER_ERROR);
            
        } catch (Exception e) {
            return getErrorResponseEntity(e);
        }
    }
    
    @GetMapping("/check-token")
    public ResponseEntity<?> checkToken(@RequestParam String email, @RequestParam String token) {
        try {
            boolean isValid = tokenGeneratorService.validateToken(email, token);
            if (isValid) {
                return getSimpleResponseEntity("Your token is valid.");
            }
            throw new TokenExpiredException("");
        } catch (Exception e) {
            return getErrorResponseEntity(e);
        }
    }
    
    private static ResponseEntity<Map<String, String>> getErrorResponseEntity(Exception e) {
        return getErrorResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
    }
    
    private static ResponseEntity<Map<String, String>> getErrorResponseEntity(String msg, HttpStatus httpStatus) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "true");
        response.put("message", msg);
        return new ResponseEntity<>(response, httpStatus);
    }
    
    private static ResponseEntity<Map<String, String>> getSimpleResponseEntity(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    private static String getParam(Map<String, String> json, String paramKey) {
        return json == null ? null : json.get(paramKey);
    }
}
