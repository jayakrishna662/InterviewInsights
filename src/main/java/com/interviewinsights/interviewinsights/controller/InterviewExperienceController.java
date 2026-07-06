package com.interviewinsights.interviewinsights.controller;

import com.interviewinsights.interviewinsights.dto.InterviewExperienceRequest;
import com.interviewinsights.interviewinsights.dto.InterviewExperienceResponse;
import com.interviewinsights.interviewinsights.service.InterviewExperienceService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/interview-experiences")
public class InterviewExperienceController {


    private final InterviewExperienceService interviewExperienceService;

    public InterviewExperienceController(
            InterviewExperienceService interviewExperienceService) {

        this.interviewExperienceService = interviewExperienceService;
    }

    @PostMapping
    public InterviewExperienceResponse createInterviewExperience(
            @RequestBody InterviewExperienceRequest request,
            Authentication authentication) {

        Long userId = (Long) authentication.getPrincipal();

        return interviewExperienceService.createInterviewExperience(request, userId);
    }

    @GetMapping
    public List<InterviewExperienceResponse>
    getAllInterviewExperiences() {

        return interviewExperienceService
                .getAllInterviewExperiences();
    }

    @GetMapping("/company/{companyId}")
    public List<InterviewExperienceResponse>
    getInterviewExperiencesByCompany(
            @PathVariable Long companyId) {

        return interviewExperienceService
                .getInterviewExperiencesByCompany(companyId);
    }

    @GetMapping("/{id}")
    public InterviewExperienceResponse getInterviewExperienceById(
            @PathVariable Long id) {

        return interviewExperienceService
                .getInterviewExperienceById(id);
    }
}
