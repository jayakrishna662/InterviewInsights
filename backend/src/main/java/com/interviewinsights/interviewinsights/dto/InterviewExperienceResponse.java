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

    private String aiProcessingStatus;

    private String aptitudeExperience;

    private String codingExperience;

    private String technicalExperience;

    private String hrExperience;

    private String gdExperience;

    private String overallSuggestions;

    private LocalDateTime createdAt;

    public InterviewExperienceResponse(
        Long id,
        Long userId,
        String userName,
        Long companyId,
        String companyName,
        Integer interviewYear,
        String result,
        String aiProcessingStatus,
        String aptitudeExperience,
        String codingExperience,
        String technicalExperience,
        String hrExperience,
        String gdExperience,
        String overallSuggestions,
        LocalDateTime createdAt
) {
    this.id = id;
    this.userId = userId;
    this.userName = userName;
    this.companyId = companyId;
    this.companyName = companyName;
    this.interviewYear = interviewYear;
    this.result = result;
    this.aiProcessingStatus = aiProcessingStatus;
    this.aptitudeExperience = aptitudeExperience;
    this.codingExperience = codingExperience;
    this.technicalExperience = technicalExperience;
    this.hrExperience = hrExperience;
    this.gdExperience = gdExperience;
    this.overallSuggestions = overallSuggestions;
    this.createdAt = createdAt;
}

}
