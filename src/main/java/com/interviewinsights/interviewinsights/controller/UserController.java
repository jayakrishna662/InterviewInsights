package com.interviewinsights.interviewinsights.controller;

import com.interviewinsights.interviewinsights.dto.UserRegistrationRequest;
import com.interviewinsights.interviewinsights.entity.User;
import com.interviewinsights.interviewinsights.service.UserService;
import org.springframework.web.bind.annotation.*;

import com.interviewinsights.interviewinsights.dto.UserResponse;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserResponse registerUser(@RequestBody UserRegistrationRequest request) {
        return userService.registerUser(request);
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

}
