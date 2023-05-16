package org.moultdb.api.controller;

import io.micrometer.common.util.StringUtils;
import org.moultdb.api.exception.AuthenticationException;
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
    
            Map<String, String> response = new HashMap<>();
            response.put("message", "New user " + user.getName() + " created");
    
            return new ResponseEntity<>(response,  HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(getErrorResponse(e), HttpStatus.CONFLICT);
        }
    }
    
    private static Map<String, String> getErrorResponse(Exception e) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "true");
        response.put("message", e.getMessage());
        return response;
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> json) {
        String email = json == null ? null : json.get("email");
        String password = json == null ? null : json.get("password");
        if (email == null || password == null) {
            throw new IllegalArgumentException("E-mail or password is empty");
        }
        try {
            User user = userService.getUserByNameAndPassword(email, password);
    
            Map<String, Object> userResp = new HashMap<>();
            userResp.put("email", user.getEmail());
            userResp.put("name", user.getName());
            userResp.put("roles", user.getRoles());
            userResp.put("orcidId", user.getOrcidId());
            userResp.put("token", tokenGeneratorService.generateLongExpirationToken(user));
    
            Map<String, Object> resp = new HashMap<>();
            resp.put("data", userResp);
            resp.put("message", "User logged in");
    
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(getErrorResponse(e), HttpStatus.CONFLICT);
        }
    }
    
    @GetMapping("/ask-password")
    public ResponseEntity<?> askNewPassword(@RequestParam String email) {
        try {
            if (StringUtils.isBlank(email)) {
                throw new UserNotFoundException("E-mail is empty");
            }
            userService.askNewPassword(email);

            Map<String, String> response = new HashMap<>();
            response.put("message", "We have just received a password reset request. " +
                    "Please check your e-mail inbox for a message from us that contains instructions " +
                    "on how to create your new password. " +
                    "If you do not see an e-mail from us in your inbox, please check your spam folder " +
                    "or contact our support team for assistance. " +
                    "Thank you, " +
                    "The MoultDB team");
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(getErrorResponse(e), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody User user) {
        try {
            if (user.getEmail() == null || user.getPassword() == null) {
                throw new UserNotFoundException("E-mail or password is empty");
            }
            boolean isUpdated = userService.updateUserPassword(user.getEmail(), user.getPassword());
    
            Map<String, String> response = new HashMap<>();
            if (isUpdated) {
                response.put("message", "Your password has been updated.");
                return new ResponseEntity<>(response, HttpStatus.OK);
    
            } else {
                response.put("message", "An error occurs during the update of the password. " +
                        "Please contact our support team for assistance.");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(getErrorResponse(e), HttpStatus.CONFLICT);
        }
    }
}
