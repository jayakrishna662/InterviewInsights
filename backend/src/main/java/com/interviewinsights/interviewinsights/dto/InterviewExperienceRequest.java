package com.interviewinsights.interviewinsights.dto;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import com.interviewinsights.interviewinsights.entity.enums.Result;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InterviewExperienceRequest {

    private Long userId;

    @NotNull
    private Long companyId;

    @NotNull
    private Integer interviewYear;

    @NotNull
private Result result;

    @Size(max = 5000)
    private String aptitudeExperience;

    @Size(max = 5000)
    private String codingExperience;

    @Size(max = 5000)
    private String technicalExperience;

    @Size(max = 5000)
    private String hrExperience;

    @Size(max = 5000)
    private String gdExperience;

    @Size(max = 3000)
    private String overallSuggestions;
}