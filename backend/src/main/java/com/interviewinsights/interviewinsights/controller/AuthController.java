package com.interviewinsights.interviewinsights.controller;

import com.interviewinsights.interviewinsights.dto.AuthLoginRequest;
import com.interviewinsights.interviewinsights.dto.AuthRegisterRequest;
import com.interviewinsights.interviewinsights.dto.AuthResponse;
import com.interviewinsights.interviewinsights.dto.CurrentUserResponse;
import com.interviewinsights.interviewinsights.service.AuthService;
import com.interviewinsights.interviewinsights.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService,UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    // when registration form is submitted , this code runs
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody AuthRegisterRequest request) { // request object contains form inputs

        // Registration request is send to service and  response is received from service
        // If any validation fails, service throws an exception and GlobalExceptionHandler handles it.
        AuthResponse response = authService.register(request);


        // If service returns {success:true}, registration was successful.
        // Return HTTP 201 Created along with the response object to the browser. Eg: {success:true, message:"Registration successful", userId:5}
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // when login form is submitted, this code runs
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody AuthLoginRequest request) {

        // Login request is sent to service and response is received from service
        // If user is not found or password is invalid,
        // service throws an exception and GlobalExceptionHandler handles it.
        AuthResponse response = authService.login(request);

        // If service returns successfully, login was successful.
        // Return HTTP 200 OK along with the response object.
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<CurrentUserResponse> getCurrentUser(Authentication authentication) {

        Long userId = (Long) authentication.getPrincipal();

        return ResponseEntity.ok(userService.getCurrentUser(userId));
    }
}