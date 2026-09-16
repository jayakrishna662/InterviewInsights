package com.interviewinsights.interviewinsights.config;

import com.interviewinsights.interviewinsights.util.JwtUtil; // token validation helper class
import jakarta.servlet.FilterChain; // passes request to next filter/controller
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter; // Spring class to create a filter

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder; // Stores authenticated user info

import java.io.IOException;
import java.util.Collections;
import java.util.List;


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
                                FilterChain filterChain)
        throws ServletException, IOException {

    String authHeader = request.getHeader("Authorization");

    if (authHeader != null && authHeader.startsWith("Bearer ")) {

        String token = authHeader.substring(7);


        if (jwtUtil.validateToken(token)) {

            Long userId = jwtUtil.extractUserId(token);
            String role = jwtUtil.extractRole(token);

            List<GrantedAuthority> authorities = role != null
                    ? List.of(new SimpleGrantedAuthority("ROLE_" + role))
                    : Collections.emptyList();

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userId,
                            null,
                            authorities
                    );

            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);

        } else {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or expired token");
            return;
        }
    }

    // Continue request
    filterChain.doFilter(request, response);

    }
}