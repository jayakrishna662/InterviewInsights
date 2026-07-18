package com.interviewinsights.interviewinsights.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
// Represents the  company data that is sent back to the client after creating, retrieving, or updating a company.
public class CompanyResponse {

    private Long id;

    private String companyName;

    private LocalDateTime createdAt;
}