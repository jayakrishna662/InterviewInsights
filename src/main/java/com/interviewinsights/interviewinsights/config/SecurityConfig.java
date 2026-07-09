package com.interviewinsights.interviewinsights.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

// This class creates and stores security-related tools and settings.
// This class is mainly used to create reusable objects (Beans) that other classes need.
// Eg: BCryptPasswordEncoder and CorsConfigurationSource are reusable beans


@Configuration  // tells Spring "this class has configuration settings"
public class SecurityConfig {

    // Create a BCryptPasswordEncoder Bean that can be shared across the application.
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // CORS configuration
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        //Create an empty CORS settings object
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // Allow requests from this origin
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE")); // Allow these http methods
        configuration.setAllowedHeaders(Arrays.asList("*")); // Allow any request headers
        configuration.setAllowCredentials(true); // Allow to send cookies or other credentials with requests.

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); //object that will hold your CORS rules.

        source.registerCorsConfiguration("/**", configuration); // Apply these CORS rules to every URL.
        return source; // Return the completed CORS configuration to Spring, when browser sends a request spring checks this stored CORS rules
    }
}