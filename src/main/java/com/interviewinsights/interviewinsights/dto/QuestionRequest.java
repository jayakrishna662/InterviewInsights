package com.interviewinsights.interviewinsights.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QuestionRequest {
    private String questionText;

    private String category;
}
