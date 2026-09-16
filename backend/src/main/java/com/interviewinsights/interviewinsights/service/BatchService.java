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

    //  create batch object by taking batch name from request, set ID, created time and save this batch object to DB
    //  create batch response object and return to controller
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

    // Get all Batches from DB ad return to controller
    public List<BatchResponse> getAllBatches() {
        List<Batch> batches = batchRepository.findAll(); // contains all Batches with each batch object containing (ID, BatchName)


        return batches.stream() // process each batch object one by one
                .map(batch -> {  // For each Batch, create a BatchResponse.

                    // Copies the data from the Batch entity into a BatchResponse.
                    BatchResponse response = new BatchResponse();

                    response.setId(batch.getId());
                    response.setBatchName(batch.getBatchName());
                    response.setCreatedAt(batch.getCreatedAt());

                    return response;

                })
                .toList(); // Collects all the BatchResponse objects into a list and returns it.
    }

    // update an existing batch in the database and return the updated details.
    public BatchResponse updateBatch(Long id, BatchRequest request) {

        // Search the database for the batch with the given ID.
        // If no batch exists with that ID, orElseThrow() throws an exception.
        Batch batch = batchRepository.findById(id)
                .orElseThrow();

        batch.setBatchName(request.getBatchName()); // Updates the batch name with the new value from the request.

        Batch updatedBatch = batchRepository.save(batch); // Saves the updated batch back to the database.

        // Return the updated batch details by creating Response object
        BatchResponse response = new BatchResponse();

        response.setId(updatedBatch.getId());
        response.setBatchName(updatedBatch.getBatchName());
        response.setCreatedAt(updatedBatch.getCreatedAt());

        return response;
    }

}