package com.interviewinsights.interviewinsights.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.interviewinsights.interviewinsights.dto.embedding.EmbeddingResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

@Service
public class EmbeddingService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.embedding.url}")
    private String embeddingUrl;

    private final RestClient restClient = RestClient.create();


    public String generateEmbedding(String text) {

    try {
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> requestBody = Map.of(
                "model", "models/gemini-embedding-001",
                "content", Map.of(
                        "parts", List.of(
                                Map.of("text", text)
                        )
                )
        );

        String requestJson = objectMapper.writeValueAsString(requestBody);

        String response = restClient.post()
                .uri(embeddingUrl)
                .header("x-goog-api-key", apiKey)
                .header("Content-Type", "application/json")
                .body(requestJson)
                .retrieve()
                .body(String.class);

        EmbeddingResponse embeddingResponse =
                objectMapper.readValue(response, EmbeddingResponse.class);

        return embeddingResponse.getEmbedding().getValues().toString();

    } catch (JsonProcessingException e) {
        throw new RuntimeException(
                "Failed to process embedding request/response", e);
    }
}

}