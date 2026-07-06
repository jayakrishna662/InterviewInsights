package com.interviewinsights.interviewinsights.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// This file is configuration file for Spring Security.


@Configuration // This class contains configuration settings.
@EnableWebSecurity // Turn on Spring Security.
public class WebSecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;  // WebSecurityConfig needs CORS rules , so we use that CORS class object

    private final JwtFilter jwtFilter;

    public WebSecurityConfig(CorsConfigurationSource corsConfigurationSource,JwtFilter jwtFilter) {
        this.corsConfigurationSource = corsConfigurationSource;
        this.jwtFilter = jwtFilter;
    }


    // Before request reaches to controller, it goes through many security checks
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource)) // Use the CORS rules from our CorsConfig class,so that browser talks to server from different origins.
                .csrf(csrf -> csrf.disable())  // Turn off CSRF protection.
                .authorizeHttpRequests(authz -> authz // Decide who is allowed to access which URLs.
                        .requestMatchers("/api/auth/**").permitAll()  // Anyone can access Anything starting with /api/auth/
                        .requestMatchers(HttpMethod.GET, "/api/companies/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/departments/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/batches/**").permitAll()
                        .requestMatchers("/api/test/**").permitAll()
                        .requestMatchers("/", "/index", "/auth","/companies","/questions","/experiences","/submit-experience").permitAll()  // Anyone can access (public pages) /, index, auth pages
                        .requestMatchers("/index.html","/auth.html","/companies.html", "/questions.html","/experiences.html","/submit-experience.html").permitAll()
                        .requestMatchers("/css/**").permitAll() //  Anyone can access (static files) inside CSS folder
                        .requestMatchers("/js/**").permitAll() // Anyone can access  inside js folder
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();  // Converts all those security rules into a SecurityFilterChain object and send to spring
        // When a request comes in, Spring looks for the SecurityFilterChain bean and uses those rules to:
            //Check if the request is allowed
            //Apply CORS rules
            //Check authentication (if needed)
            //Block or allow the request
    }
}