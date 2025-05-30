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
            "/domains/import-file",
            "/genes/import-file",
            "/genomes/import-file",
            "/geological-ages/create",
            "/orthogroups/import-file",
            "/pathways/import-cv-file",
            "/pathways/import-data-file",
            "/taxon-annotations/import-file",
            "/taxon-annotations/import-inat-metadata",
            "/taxon-annotations/delete",
            "/taxa/import-file",
            "/users/forgot-password",
            "/users/reset-password",
            "/users/email-validation",
            "/users/check-token"
    };
    
    protected final static String[] SECURED_USER_URL = {
            // FIXME enable following filters and remove email/token from controllers
//            "/user/reset-password",
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
