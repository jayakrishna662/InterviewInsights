package com.interviewinsights.interviewinsights.controller;

import com.interviewinsights.interviewinsights.dto.QuestionRequest;
import com.interviewinsights.interviewinsights.dto.QuestionResponse;
import com.interviewinsights.interviewinsights.entity.enums.QuestionCategory;
import com.interviewinsights.interviewinsights.service.QuestionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping
    public QuestionResponse createQuestion(
            @RequestBody QuestionRequest request) {

        return questionService.createQuestion(request);
    }
    @GetMapping("/category/{category}")
    public List<QuestionResponse> getQuestionsByCategory(
            @PathVariable QuestionCategory category) {

        return questionService
                .getQuestionsByCategory(category);
    }

    @GetMapping
    public List<QuestionResponse> getAllQuestions() {

        return questionService.getAllQuestions();
    }
}