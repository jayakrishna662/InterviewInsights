package com.interviewinsights.interviewinsights.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InterviewExperienceRequest {

    private Long userId;

    private Long companyId;

    private Integer interviewYear;

    private String result;

    private String aptitudeExperience;

    private String codingExperience;

    private String technicalExperience;

    private String hrExperience;

    private String gdExperience;

    private String overallSuggestions;
}
