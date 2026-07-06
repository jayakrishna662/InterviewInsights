package com.interviewinsights.interviewinsights.dto;

import com.interviewinsights.interviewinsights.entity.enums.QuestionCategory;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ExperienceQuestionResponse {

    private Long id;

    private Long questionId;

    private String questionText;

    private QuestionCategory category;
}
