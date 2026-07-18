package com.interviewinsights.interviewinsights.repository;

import com.interviewinsights.interviewinsights.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    // Finds a company in the database using its company name.
    Optional<Company> findByCompanyName(String companyName);
}