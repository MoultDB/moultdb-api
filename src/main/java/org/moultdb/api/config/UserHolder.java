package org.moultdb.api.config;

/**
 * @author Valentine Rech de Laval
 * @since 2023-07-11
 */
public class UserHolder {
    private static final ThreadLocal<String> tokenThreadLocal = new ThreadLocal<>();
    
    public static void setUsername(String token) {
        tokenThreadLocal.set(token);
    }
    
    public static String getUsername() {
        return tokenThreadLocal.get();
    }
    
    public static void clearUsername() {
        tokenThreadLocal.remove();
    }
}
