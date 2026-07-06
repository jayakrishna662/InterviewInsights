package com.interviewinsights.interviewinsights.service;

import com.interviewinsights.interviewinsights.dto.BatchRequest;
import com.interviewinsights.interviewinsights.dto.BatchResponse;
import com.interviewinsights.interviewinsights.entity.Batch;
import com.interviewinsights.interviewinsights.repository.BatchRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BatchService {

    private final BatchRepository batchRepository;

    public BatchService(BatchRepository batchRepository) {
        this.batchRepository = batchRepository;
    }

    public BatchResponse createBatch(BatchRequest request) {

        Batch batch = new Batch();
        batch.setBatchName(request.getBatchName());
        batch.setCreatedAt(LocalDateTime.now());
        Batch savedBatch = batchRepository.save(batch);

        BatchResponse response = new BatchResponse();

        response.setId(savedBatch.getId());
        response.setBatchName(savedBatch.getBatchName());
        response.setCreatedAt(savedBatch.getCreatedAt());

        return response;

    }
    public List<BatchResponse> getAllBatches() {
        List<Batch> batches = batchRepository.findAll();

        return batches.stream()
                .map(batch -> {

                    BatchResponse response = new BatchResponse();

                    response.setId(batch.getId());
                    response.setBatchName(batch.getBatchName());
                    response.setCreatedAt(batch.getCreatedAt());

                    return response;

                })
                .toList();
    }

    public BatchResponse updateBatch(Long id, BatchRequest request) {
        Batch batch = batchRepository.findById(id)
                .orElseThrow();

        batch.setBatchName(request.getBatchName());

        Batch updatedBatch = batchRepository.save(batch);

        BatchResponse response = new BatchResponse();

        response.setId(updatedBatch.getId());
        response.setBatchName(updatedBatch.getBatchName());
        response.setCreatedAt(updatedBatch.getCreatedAt());

        return response;
    }

}