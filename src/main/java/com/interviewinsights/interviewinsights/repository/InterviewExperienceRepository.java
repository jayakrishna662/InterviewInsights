package com.interviewinsights.interviewinsights.repository;

import com.interviewinsights.interviewinsights.entity.InterviewExperience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InterviewExperienceRepository extends JpaRepository<InterviewExperience, Long> {

    List<InterviewExperience> findByCompanyId(Long companyId);
    Optional<InterviewExperience> findById(Long id);
}