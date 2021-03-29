package com.springboot.weather.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Deal with cross origin.
 *
 * @author Beck.Xu
 * @since 29/03/2021
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    static final String[] ORIGINS = {"GET", "POST", "PUT", "DELETE"};

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods(ORIGINS)
                .maxAge(3600);
    }
}
