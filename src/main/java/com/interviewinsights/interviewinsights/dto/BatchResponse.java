package com.interviewinsights.interviewinsights.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class BatchResponse {

    private Long id;

    private String batchName;

    private LocalDateTime createdAt;
}