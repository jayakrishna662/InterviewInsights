package com.interviewinsights.interviewinsights.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminStatsResponse {

    private long totalCompanies;
    private long totalExperiences;
    private long totalStudents;
}
