package com.interviewinsights.interviewinsights.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class InterviewExperienceResponse {
    private Long id;

    private Long userId;

    private String userName;

    private Long companyId;

    private String companyName;

    private Integer interviewYear;

    private String result;

    private String aptitudeExperience;

    private String codingExperience;

    private String technicalExperience;

    private String hrExperience;

    private String gdExperience;

    private String overallSuggestions;

    private LocalDateTime createdAt;

}
