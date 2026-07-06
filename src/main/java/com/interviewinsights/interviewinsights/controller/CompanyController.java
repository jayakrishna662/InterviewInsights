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

    @PostMapping
    public CompanyResponse createCompany(@RequestBody CompanyRequest request) {
        return companyService.createCompany(request);
    }

    @GetMapping
    public List<CompanyResponse> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @PutMapping("/{id}")
    public CompanyResponse updateCompany(@PathVariable Long id,
                                         @RequestBody CompanyRequest request) {

        return companyService.updateCompany(id, request);
    }
}
