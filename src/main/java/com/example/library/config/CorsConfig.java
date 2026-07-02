package com.example.library.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**") // Applies to all API endpoints

                // Allowed frontend applications
                .allowedOrigins(
                        "http://localhost:3000", // React
                        "http://localhost:4200"  // Angular
                )

                // Allowed HTTP methods
                .allowedMethods(
                        "GET",
                        "POST",
                        "PUT",
                        "PATCH",
                        "DELETE",
                        "OPTIONS"
                )

                // Allowed request headers
                .allowedHeaders("*")

                // Allow cookies and Authorization headers
                .allowCredentials(true);

        /*
         * ----------------------------------------------------------------------
         * DEVELOPMENT ONLY
         * ----------------------------------------------------------------------
         * Quick configuration for local development.
         *
         * DO NOT USE IN PRODUCTION.
         *
         * If you enable this configuration:
         *   - Comment or remove the configuration above.
         *   - Credentials must remain disabled when using "*".
         *
         * Example:
         *
         * registry.addMapping("/**")
         *         .allowedOriginPatterns("*")
         *         .allowedMethods("*")
         *         .allowedHeaders("*")
         *         .allowCredentials(false);
         *
         * ----------------------------------------------------------------------
         */
    }
}