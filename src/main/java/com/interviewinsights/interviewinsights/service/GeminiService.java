package com.interviewinsights.interviewinsights.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestClient;

import com.interviewinsights.interviewinsights.dto.gemini.Content;
import com.interviewinsights.interviewinsights.dto.gemini.GeminiRequest;
import com.interviewinsights.interviewinsights.dto.gemini.Part;

import com.interviewinsights.interviewinsights.dto.gemini.response.GeminiResponse;

import java.util.List;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final RestClient restClient = RestClient.create();

    public String extractQuestions(String interviewExperience) {


        String prompt = """
                   You are an AI assistant for an interview preparation platform.
                   
                   Your task is to extract only the actual interview questions or coding problems from the interview experience.
                   
                   Rules:
                   
                   1. Extract only questions that another student can prepare for.
                   
                   2. Categorize every question into one of these:
                      - coding
                      - technical
                      - hr
                      - aptitude
                      - gd
                   
                   3. Ignore:
                      - greetings
                      - interviewer comments
                      - student opinions
                      - interview process details
                      - difficulty level
                      - conversational follow-up questions like:
                          - Can you optimize it?
                          - Can you make it faster?
                          - Any other approach?
                          - Why?
                          - Explain more.
                        unless they introduce a completely new interview question.
                   
                   4. If the student describes a coding problem instead of writing the exact question,
                      rewrite it as a clear interview question.
                   
                   Example:
                   
                   Student writes:
                   "First was to find the sum of two numbers in an array."
                   
                   Return:
                   "Find the sum of two numbers in an array."
                   
                   Student writes:
                   "They asked me to reverse a linked list."
                   
                   Return:
                   "Reverse a linked list."
                   
                   5. Remove duplicate questions.
                   
                   6. Keep each question short and clear.
                   
                   7. If a category has no questions, return an empty array.
                   
                   8. Return ONLY valid JSON.
                   9. If the same question appears multiple times in the experience,
                    return it only once.
                   
                   The JSON format must be exactly:
                   
                   {
                     "coding": [],
                     "technical": [],
                     "hr": [],
                     "aptitude": [],
                     "gd": []
                   }
                   
                   Interview Experience:"""
                   + interviewExperience;

        GeminiRequest request = new GeminiRequest(
                List.of(
                        new Content(
                                List.of(
                                        new Part(prompt)
                                )
                        )
                )
        );

        GeminiResponse response = restClient.post()
                .uri(apiUrl + "?key=" + apiKey)
                .header("Content-Type", "application/json")
                .body(request)
                .retrieve()
                .body(GeminiResponse.class);

        return response.getCandidates()
                .get(0)
                .getContent()
                .getParts()
                .get(0)
                .getText();
    }

}