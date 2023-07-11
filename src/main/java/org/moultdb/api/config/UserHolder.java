package org.moultdb.api.config;

/**
 * @author Valentine Rech de Laval
 * @since 2023-07-11
 */
public class UserHolder {
    private static final ThreadLocal<String> tokenThreadLocal = new ThreadLocal<>();
    
    public static void setEmail(String token) {
        tokenThreadLocal.set(token);
    }
    
    public static String getEmail() {
        return tokenThreadLocal.get();
    }
    
    public static void clearEmail() {
        tokenThreadLocal.remove();
    }
}
