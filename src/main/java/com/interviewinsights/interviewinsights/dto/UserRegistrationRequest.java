package com.interviewinsights.interviewinsights.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRegistrationRequest {

    private String name;
    private String email;
    private String password;
    private String rollNumber;
    private String departmentId;
    private Long batchId;
}