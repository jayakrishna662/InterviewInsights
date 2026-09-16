package com.interviewinsights.interviewinsights.dto;

import com.interviewinsights.interviewinsights.entity.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthResponse {

    private Long userId;
    private String name;
    private String email;
    private String message;
    private boolean success;
    private String token;
    private Role role;
}