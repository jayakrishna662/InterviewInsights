package com.interviewinsights.interviewinsights.controller;

import com.interviewinsights.interviewinsights.dto.CompanyRequest;
import com.interviewinsights.interviewinsights.dto.CompanyResponse;
import com.interviewinsights.interviewinsights.service.CompanyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    // Receives company data from client, passes to service and returns created company details
    @PostMapping
    public CompanyResponse createCompany(@RequestBody CompanyRequest request) {
        return companyService.createCompany(request);
    }

    // Retrieves all companies
    @GetMapping
    public List<CompanyResponse> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    // Retrieves only active companies (for student forms/dropdowns)
    @GetMapping("/active")
    public List<CompanyResponse> getActiveCompanies() {
        return companyService.getActiveCompanies();
    }

    // Update an existing company
    @PutMapping("/{id}")
    public CompanyResponse updateCompany(@PathVariable Long id,
                                         @RequestBody CompanyRequest request) {

        return companyService.updateCompany(id, request);
    }

    // Toggle active status (soft delete / reactivate)
    @PatchMapping("/{id}/toggle-status")
    public CompanyResponse toggleCompanyStatus(@PathVariable Long id) {
        return companyService.toggleCompanyStatus(id);
    }
}
