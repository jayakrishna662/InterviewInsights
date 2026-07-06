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

    public CompanyResponse createCompany(CompanyRequest request) {

        Company company = new Company();

        company.setCompanyName(request.getCompanyName());
        company.setCreatedAt(LocalDateTime.now());

        Company savedCompany = companyRepository.save(company);

        CompanyResponse response = new CompanyResponse();

        response.setId(savedCompany.getId());
        response.setCompanyName(savedCompany.getCompanyName());
        response.setCreatedAt(savedCompany.getCreatedAt());

        return response;
    }
    public List<CompanyResponse> getAllCompanies() {

        List<Company> companies = companyRepository.findAll();

        return companies.stream()
                .map(company -> {

                    CompanyResponse response = new CompanyResponse();

                    response.setId(company.getId());
                    response.setCompanyName(company.getCompanyName());
                    response.setCreatedAt(company.getCreatedAt());

                    return response;
                })
                .toList();
    }

    public CompanyResponse updateCompany(Long id, CompanyRequest request) {

        Company company = companyRepository.findById(id)
                .orElseThrow();

        company.setCompanyName(request.getCompanyName());

        Company updatedCompany = companyRepository.save(company);

        CompanyResponse response = new CompanyResponse();

        response.setId(updatedCompany.getId());
        response.setCompanyName(updatedCompany.getCompanyName());
        response.setCreatedAt(updatedCompany.getCreatedAt());

        return response;
    }

}