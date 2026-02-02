package org.moultdb.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Valentine Rech de Laval
 * @since 2024-11-28
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Value("${moultdb.url.webapp.moultdb}")
    private String moultdbUrl;
    
    @Value("${moultdb.url.webapp.moulting}")
    private String moultingUrl;
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", moultdbUrl, moultingUrl)
                .allowedMethods("GET", "POST")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}