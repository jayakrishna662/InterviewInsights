package com.interviewinsights.interviewinsights.repository;

import com.interviewinsights.interviewinsights.entity.ExperienceQuestion;
import com.interviewinsights.interviewinsights.entity.enums.QuestionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExperienceQuestionRepository
        extends JpaRepository<ExperienceQuestion, Long> {

    List<ExperienceQuestion> findByExperienceId(
            Long experienceId);

    @Query("""
        SELECT
            q.id,
            q.questionText,
            q.category,
            COUNT(DISTINCT eq.experience.id)
        FROM ExperienceQuestion eq
        JOIN eq.question q
        JOIN eq.experience e
        WHERE e.company.id = :companyId
          AND (:category IS NULL OR q.category = :category)
          AND (
              :years IS NULL
              OR e.interviewYear >= :cutoffYear
          )
        GROUP BY q.id, q.questionText, q.category
        ORDER BY COUNT(DISTINCT eq.experience.id) DESC
        """)
    List<Object[]> findQuestionFrequencyByCompany(
            @Param("companyId") Long companyId,
            @Param("category") QuestionCategory category,
            @Param("cutoffYear") Integer cutoffYear,
            @Param("years") Integer years
    );

    void deleteByExperienceId(Long experienceId);

    long countByQuestionId(Long questionId);
}