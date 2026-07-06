package com.interviewinsights.interviewinsights.repository;

import com.interviewinsights.interviewinsights.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import com.interviewinsights.interviewinsights.entity.enums.QuestionCategory;
import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByCategory(QuestionCategory category);
    Optional<Question> findByQuestionTextAndCategory(
            String questionText,
            QuestionCategory category
    );
    List<Question> findAllByCategory(QuestionCategory category);
}