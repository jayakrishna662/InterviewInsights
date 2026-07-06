package com.interviewinsights.interviewinsights.repository;

import com.interviewinsights.interviewinsights.entity.ExperienceQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExperienceQuestionRepository
        extends JpaRepository<ExperienceQuestion, Long> {

    List<ExperienceQuestion> findByExperienceId(
            Long experienceId);
}