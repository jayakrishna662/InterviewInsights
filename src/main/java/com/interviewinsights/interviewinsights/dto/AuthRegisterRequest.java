package com.interviewinsights.interviewinsights.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthRegisterRequest {

    private String name;
    private String email;
    private String rollNumber;
    private String departmentId;
    private Long batchId;
    private String password;
    private String confirmPassword;
}