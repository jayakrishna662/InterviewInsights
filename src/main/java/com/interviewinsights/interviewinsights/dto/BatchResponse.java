package com.interviewinsights.interviewinsights.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
// After saving the batch in the database, the backend sends data back to the client which includes batch id,batch name and created time
public class BatchResponse {

    private Long id;

    private String batchName;

    private LocalDateTime createdAt;
}