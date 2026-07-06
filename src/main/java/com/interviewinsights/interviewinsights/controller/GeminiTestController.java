package com.interviewinsights.interviewinsights.controller;

import com.interviewinsights.interviewinsights.service.GeminiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeminiTestController {

    private final GeminiService geminiService;

    public GeminiTestController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @GetMapping("/api/test/gemini")
    public String testGemini() {

        String interviewExperience = """
            Coding Experience:
            The interviewer asked me to write Fibonacci series and Reverse Linked List.

            Technical Experience:
            Difference between HashMap and ConcurrentHashMap.

            HR Experience:
            Tell me about yourself.
            """;

        return geminiService.extractQuestions(interviewExperience);
    }
}