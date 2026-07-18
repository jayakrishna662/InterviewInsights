package com.interviewinsights.interviewinsights.controller;


import com.interviewinsights.interviewinsights.dto.BatchRequest;
import com.interviewinsights.interviewinsights.dto.BatchResponse;
import com.interviewinsights.interviewinsights.service.BatchService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/batches")
public class BatchController {

    private final BatchService batchService;

    public BatchController(BatchService batchService) {
        this.batchService = batchService;
    }

    // Receives batch data from the client, passes it to the service for saving,
    // and returns the created batch information.
    @PostMapping
    public BatchResponse createBatch(@RequestBody BatchRequest request) {
        return batchService.createBatch(request);
    }

    // Retrieves all batches from DB and converts them into BatchResponse objects and returns as a list
    @GetMapping
    public List<BatchResponse> getAllBatches() {
        return batchService.getAllBatches();
    }

    // Find the batch with this batch_id and update batch name
    @PutMapping("/{id}")
    public BatchResponse updateBatch(@PathVariable Long id,
                                     @RequestBody BatchRequest request) {

        return batchService.updateBatch(id, request);
    }


}
