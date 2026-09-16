package com.interviewinsights.interviewinsights.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
// The client is not decide ID or creation time, so with this class we only receive batch name from client
public class BatchRequest {

    private String batchName;

}