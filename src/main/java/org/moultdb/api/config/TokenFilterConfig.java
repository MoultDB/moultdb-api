package org.moultdb.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Valentine Rech de Laval
 * @since 2023-04-11
 */
@Configuration
public class TokenFilterConfig {
    
    @Value("${jwt.secret}")
    private String secret;
    
    protected final static String[] SECURED_ADMIN_URL = {
            "/geological-age/create",
            "/taxon-annotation/import-file",
            "/taxon/import-file",
            "/genome/import-file"
    };
    
    protected final static String[] SECURED_USER_URL = {
            // FIXME enable following filters and remove email/token from controllers
//            "/user/reset-password",
//            "/image/import"
    };
    
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public FilterRegistrationBean<TokenFilter> jwtFilter() {
        FilterRegistrationBean<TokenFilter> filter = new FilterRegistrationBean<>();
        filter.setFilter(new TokenFilter(secret));
        // Endpoints which needs to be restricted.
        // All Endpoints would be restricted if unspecified
        filter.addUrlPatterns(SECURED_ADMIN_URL);
        filter.addUrlPatterns(SECURED_USER_URL);
        return filter;
    }
}
