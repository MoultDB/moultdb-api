package org.moultdb.api.controller;

import io.micrometer.common.util.StringUtils;
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
            return new ResponseEntity<>("User " + user.getName() + " created",  HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        try {
            if (user.getEmail() == null || user.getPassword() == null) {
                throw new UserNotFoundException("E-mail or password is empty");
            }
            User userData = userService.getUserByNameAndPassword(user.getEmail(), user.getPassword());
            if (userData == null) {
                throw new UserNotFoundException("E-mail or password is invalid");
            }
            Map<String, String> jwtTokenGen = new HashMap<>();
            jwtTokenGen.put("token", tokenGeneratorService.generateLongExpirationToken(user));
            jwtTokenGen.put("message", "New user token");
    
            return new ResponseEntity<>(jwtTokenGen, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
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
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
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
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }
}
