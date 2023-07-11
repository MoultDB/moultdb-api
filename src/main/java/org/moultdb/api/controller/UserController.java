package org.moultdb.api.controller;

import org.moultdb.api.model.MoultDBUser;
import org.moultdb.api.service.TokenService;
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

import static org.moultdb.api.controller.ResponseHandler.generateErrorResponse;
import static org.moultdb.api.controller.ResponseHandler.generateValidResponse;

/**
 * @author Valentine Rech de Laval
 * @since 2023-04-11
 */
@RestController
@RequestMapping("user")
public class UserController {
    
    private final UserService userService;
    
    private final TokenService tokenService;
    
    @Autowired
    public UserController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }
    
    @PostMapping("/registration")
    public ResponseEntity<?> postUser(@RequestBody Map<String, String> json) {
        MoultDBUser user = new MoultDBUser(json.get("email"), json.get("name"), json.get("password"), json.get("orcidId"));
        try {
            userService.saveUser(user);
            userService.askEmailValidation(user.getEmail(), "/user/email-validation");
        } catch (Exception e) {
            return generateErrorResponse(e);
        }
        return generateValidResponse("New user " + user.getEmail() + " created.\n" +
                "We have sent an e-mail validation request.\n" +
                "Please check your e-mail inbox for a message from us that contains instructions " +
                "on how to validate your e-mail address.\n" +
                "If you do not see an e-mail from us in your inbox, please check your spam folder " +
                "or contact our support team for assistance.");
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> json) {
        String email = getParam(json, "email");
        String password = getParam(json, "password");
        MoultDBUser user;
        try {
            user = userService.getUser(email, password);
        } catch (Exception e) {
            return generateErrorResponse(e);
        }
        Map<String, Object> userResp = new HashMap<>();
        userResp.put("email", user.getEmail());
        userResp.put("name", user.getName());
        userResp.put("authorities", user.getAuthorities());
        userResp.put("orcidId", user.getOrcidId());
        userResp.put("token", tokenService.generateMiddleExpirationToken(user.getEmail()));
    
        return generateValidResponse("User logged in", userResp);
    }
    
    @GetMapping("/ask-password")
    public ResponseEntity<?> askNewPassword(@RequestParam String email) {
        try {
            userService.askNewPassword(email, "/user/reset-password");
        } catch (Exception e) {
            return generateErrorResponse(e);
        }
        return generateValidResponse( "We have received your password reset request.\n" +
                "Please check your e-mail inbox for a message from us that contains instructions " +
                "on how to create your new password.\n" +
                "If you do not see an e-mail from us in your inbox, please check your spam folder " +
                "or contact our support team for assistance.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> json) {
        String email = getParam(json, "email");
        String password = getParam(json, "password");
        boolean isUpdated = false;
        try {
            isUpdated = userService.updateUserPassword(email, password);
        } catch (Exception e) {
            return generateErrorResponse(e);
        }
        if (isUpdated) {
            return generateValidResponse("Your password has been updated.");
        }
        return generateErrorResponse("An error occurs during the update of the password. " +
                "Please contact our support team for assistance.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @GetMapping("/email-validation")
    public ResponseEntity<?> validateUser(@RequestParam String email, @RequestParam String token) {
        boolean isUpdated = false;
        try {
            isUpdated = userService.setUserAsVerified(email, token);
        } catch (Exception e) {
            return generateErrorResponse(e);
        }
        if (isUpdated) {
            return generateValidResponse("Your e-mail has been validated.");
        }
        return generateErrorResponse("An error occurs during the validation of the e-mail. " +
                "Please contact our support team for assistance.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @GetMapping("/check-token")
    public ResponseEntity<?> checkToken(@RequestParam String email, @RequestParam String token) {
        boolean isValid = false;
        try {
            isValid = tokenService.validateToken(email, token);
        } catch (Exception e) {
            return generateErrorResponse(e);
        }
        Map<String, Object> resp = new HashMap<>();
        resp.put("valid", isValid);
        if (isValid) {
            return generateValidResponse("Your token is valid.", resp);
        }
        return generateValidResponse("Your token is not valid.", resp);
    }
    
    private static String getParam(Map<String, String> json, String paramKey) {
        return json == null ? null : json.get(paramKey);
    }
}
