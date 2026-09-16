package com.interviewinsights.interviewinsights.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.interviewinsights.interviewinsights.dto.embedding.EmbeddingResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EmbeddingService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.embedding.url}")
    private String embeddingUrl;

    private final RestClient restClient = RestClient.create();


    public String generateEmbedding(String text){

        String requestBody = """
            {
              "model": "models/gemini-embedding-001",
              "content": {
                "parts": [
                  {
                    "text": "%s"
                  }
                ]
              }
            }
            """.formatted(text);

        String response = restClient.post()
                .uri(embeddingUrl + "?key=" + apiKey)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .retrieve()
                .body(String.class);
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            EmbeddingResponse embeddingResponse =
                    objectMapper.readValue(response, EmbeddingResponse.class);

            return embeddingResponse.getEmbedding().getValues().toString();
        }catch (JsonProcessingException e){
            throw new RuntimeException("Failed to parse embedding response",e);
        }
    }

}