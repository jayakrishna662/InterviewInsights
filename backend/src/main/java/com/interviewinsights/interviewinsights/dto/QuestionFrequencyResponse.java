package com.interviewinsights.interviewinsights.dto;

import com.interviewinsights.interviewinsights.entity.enums.QuestionCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuestionFrequencyResponse {

    private Long id;
    private String questionText;
    private QuestionCategory category;
    private Long frequency;
}