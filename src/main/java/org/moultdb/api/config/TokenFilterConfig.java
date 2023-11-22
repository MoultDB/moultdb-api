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
        filter.addUrlPatterns("/geological-age/create");
        filter.addUrlPatterns("/taxon-annotation/import-file");
        filter.addUrlPatterns("/taxon/create-taxon");
        filter.addUrlPatterns("/taxon/import-file");
        // FIXME enable following filters and remove email/token from controllers
//        filter.addUrlPatterns("/user/reset-password");
//        filter.addUrlPatterns("/image/import");
        filter.addUrlPatterns("/genome/import-file");
        return filter;
    }
}
