package com.interviewinsights.interviewinsights.dto;

import com.interviewinsights.interviewinsights.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CurrentUserResponse {

    private Long id;
    private String name;
    private String email;
    private String rollNumber;
    private Role role;
}