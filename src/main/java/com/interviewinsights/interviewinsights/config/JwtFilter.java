package com.interviewinsights.interviewinsights.config;

import com.interviewinsights.interviewinsights.util.JwtUtil; // token validation helper class
import jakarta.servlet.FilterChain; // passes request to next filter/controller
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter; // Spring class to create a filter

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder; // Stores authenticated user info
import org.springframework.security.core.authority.AuthorityUtils;

import java.io.IOException;


// Main job of this class: Validate token before controller runs
@Component
public class JwtFilter extends OncePerRequestFilter { // this class becomes a filter , Filter runs once per request (on every HTTP request)

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Extract token from request header
        String authHeader = request.getHeader("Authorization");

        // Check if Header Exists and Starts with "Bearer"
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);  // Remove "Bearer " prefix and keep only actual token

            // Validate token - checks if token is in correct format, signature valid or not, expired or not etc
            if (jwtUtil.validateToken(token)) {

                // Extract  user ID (encoded in the token)
                Long userId = jwtUtil.extractUserId(token);

                // Create an authentication Object that represents this user is logged in
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userId, //  the user's ID
                                null, // no password (we already validated the token)
                                AuthorityUtils.NO_AUTHORITIES  //no special permissions (we can add roles later)

                        );

                // Store the authentication object in SecurityContextHolder
                // This is a Spring Security feature that stores the logged-in user info
                // Now any controller can access the user:
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Token is valid ✓
                //Pass the request to the next filter or controller
                filterChain.doFilter(request, response);
            } else {
                // Token is invalid or expired, return 401 with error message
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid or expired token");
            }

        }
        // No Authorization Header — Continue Anyway without authentication
        // bcz Some endpoints are public (like home page, login page)
        else {

            filterChain.doFilter(request, response);
        }
    }
}