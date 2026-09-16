package com.interviewinsights.interviewinsights.controller;

import com.interviewinsights.interviewinsights.dto.AdminStatsResponse;
import com.interviewinsights.interviewinsights.service.AdminService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.interviewinsights.interviewinsights.dto.AdminAiProcessingResponse;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/stats")
    public AdminStatsResponse getStats() {
        return adminService.getDashboardStats();
    }

    @PostMapping("/experiences/{experienceId}/ai/retry")
public void retryAiProcessing(
        @PathVariable Long experienceId) {

    adminService.retryAiProcessing(experienceId);
}

@GetMapping("/experiences/ai-status")
public List<AdminAiProcessingResponse> getAiProcessingStatus() {
    return adminService.getAiProcessingStatus();
}
}
