package com.interviewinsights.interviewinsights.dto;

import com.interviewinsights.interviewinsights.entity.enums.AiProcessingStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminAiProcessingResponse {

    private Long experienceId;
    private String studentName;
    private String companyName;
    private AiProcessingStatus aiProcessingStatus;
}