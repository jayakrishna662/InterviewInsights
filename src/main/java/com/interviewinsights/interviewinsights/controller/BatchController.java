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

    @PostMapping
    public BatchResponse createBatch(@RequestBody BatchRequest request) {
        return batchService.createBatch(request);
    }

    @GetMapping
    public List<BatchResponse> getAllBatches() {
        return batchService.getAllBatches();
    }

    @PutMapping("/{id}")
    public BatchResponse updateBatch(@PathVariable Long id,
                                     @RequestBody BatchRequest request) {

        return batchService.updateBatch(id, request);
    }


}
