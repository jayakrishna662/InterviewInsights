package com.interviewinsights.interviewinsights.controller;


import com.interviewinsights.interviewinsights.dto.ExperienceQuestionRequest;
import com.interviewinsights.interviewinsights.dto.ExperienceQuestionResponse;
import com.interviewinsights.interviewinsights.service.ExperienceQuestionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/experience-questions")
public class ExperienceQuestionController {

    private final ExperienceQuestionService experienceQuestionService;

    public ExperienceQuestionController(
            ExperienceQuestionService experienceQuestionService) {

        this.experienceQuestionService =
                experienceQuestionService;
    }

    @PostMapping("/link")
    public void linkExperienceAndQuestion(
            @RequestBody ExperienceQuestionRequest request) {

        experienceQuestionService
                .linkExperienceAndQuestion(request);
    }

    @GetMapping("/experience/{experienceId}")
    public List<ExperienceQuestionResponse> getQuestionsByExperience(
            @PathVariable Long experienceId) {

        return experienceQuestionService
                .getQuestionsByExperience(experienceId);
    }
}