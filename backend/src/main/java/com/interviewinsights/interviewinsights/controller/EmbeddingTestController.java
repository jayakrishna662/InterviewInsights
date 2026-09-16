package com.interviewinsights.interviewinsights.controller;

import com.interviewinsights.interviewinsights.service.EmbeddingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmbeddingTestController {

    private final EmbeddingService embeddingService;

    public EmbeddingTestController(EmbeddingService embeddingService) {
        this.embeddingService = embeddingService;
    }

    @GetMapping("/api/test/embedding")
    public String testEmbedding() {

        return embeddingService.generateEmbedding(
                "Reverse a linked list");
    }
}