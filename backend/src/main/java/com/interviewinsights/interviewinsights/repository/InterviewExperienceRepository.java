package com.interviewinsights.interviewinsights.repository;

import com.interviewinsights.interviewinsights.entity.InterviewExperience;
import com.interviewinsights.interviewinsights.entity.enums.AiProcessingStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface InterviewExperienceRepository extends JpaRepository<InterviewExperience, Long> {

    List<InterviewExperience> findByCompanyId(Long companyId);

    long countByUserId(Long userId);

    @Transactional
    @Modifying
    @Query("""
        UPDATE InterviewExperience e
        SET e.aiProcessingStatus = :processing
        WHERE e.id = :id
          AND e.aiProcessingStatus = :pending
    """)
    int markAsProcessing(
            @Param("id") Long id,
            @Param("processing") AiProcessingStatus processing,
            @Param("pending") AiProcessingStatus pending
    );
}