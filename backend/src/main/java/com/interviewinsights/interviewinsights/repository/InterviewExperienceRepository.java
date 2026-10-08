package com.interviewinsights.interviewinsights.repository;

import com.interviewinsights.interviewinsights.entity.InterviewExperience;
import com.interviewinsights.interviewinsights.entity.enums.AiProcessingStatus;
import com.interviewinsights.interviewinsights.dto.InterviewExperienceResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

public interface InterviewExperienceRepository extends JpaRepository<InterviewExperience, Long> {

    @Query("""
    SELECT new com.interviewinsights.interviewinsights.dto.InterviewExperienceResponse(
        e.id,
        u.id,
        u.name,
        c.id,
        c.companyName,
        e.interviewYear,
        CAST(e.result AS string),
        CAST(e.aiProcessingStatus AS string),
        e.aptitudeExperience,
        e.codingExperience,
        e.technicalExperience,
        e.hrExperience,
        e.gdExperience,
        e.overallSuggestions,
        e.createdAt
    )
    FROM InterviewExperience e
    JOIN e.user u
    JOIN e.company c
""")
List<InterviewExperienceResponse> findAllResponses();

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