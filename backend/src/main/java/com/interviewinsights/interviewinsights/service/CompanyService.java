package com.interviewinsights.interviewinsights.service;

import com.interviewinsights.interviewinsights.dto.CompanyRequest;
import com.interviewinsights.interviewinsights.dto.CompanyResponse;
import com.interviewinsights.interviewinsights.entity.Company;
import com.interviewinsights.interviewinsights.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    // Creates a new company, saves it to the database, and returns the saved company details by creating response object.
    public CompanyResponse createCompany(CompanyRequest request) {

        Company company = new Company();

        company.setCompanyName(request.getCompanyName());
        company.setCreatedAt(LocalDateTime.now());

        Company savedCompany = companyRepository.save(company);

        return mapToResponse(savedCompany);
    }

    // Retrieves all companies from the database and returns them.
    public List<CompanyResponse> getAllCompanies() {
        return companyRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Retrieves only active companies for student forms and dropdowns.
    public List<CompanyResponse> getActiveCompanies() {
        return companyRepository.findByActiveTrue().stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Updates an existing company and returns the updated details.
    public CompanyResponse updateCompany(Long id, CompanyRequest request) {

        Company company = companyRepository.findById(id)
                .orElseThrow();

        company.setCompanyName(request.getCompanyName());

        Company updatedCompany = companyRepository.save(company);

        return mapToResponse(updatedCompany);
    }

    // Toggles active status of a company (soft-delete / reactivate)
    public CompanyResponse toggleCompanyStatus(Long id) {

        Company company = companyRepository.findById(id)
                .orElseThrow();

        company.setActive(!company.isActive());

        Company savedCompany = companyRepository.save(company);

        return mapToResponse(savedCompany);
    }

    private CompanyResponse mapToResponse(Company company) {
        CompanyResponse response = new CompanyResponse();
        response.setId(company.getId());
        response.setCompanyName(company.getCompanyName());
        response.setCreatedAt(company.getCreatedAt());
        response.setActive(company.isActive());
        return response;
    }

}