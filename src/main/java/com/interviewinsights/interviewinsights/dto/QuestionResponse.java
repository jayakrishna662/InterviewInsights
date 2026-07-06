package com.interviewinsights.interviewinsights.dto;

import com.interviewinsights.interviewinsights.entity.enums.QuestionCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
public class QuestionResponse {

    private Long id;

    private String questionText;

    private QuestionCategory category;

    private LocalDateTime createdAt;
}
