package com.interviewinsights.interviewinsights.service;

import com.interviewinsights.interviewinsights.entity.ExperienceQuestion;
import com.interviewinsights.interviewinsights.entity.InterviewExperience;
import com.interviewinsights.interviewinsights.entity.Question;
import com.interviewinsights.interviewinsights.entity.enums.QuestionCategory;
import com.interviewinsights.interviewinsights.repository.ExperienceQuestionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AiQuestionPersistenceService {

    private final QuestionService questionService;
    private final ExperienceQuestionRepository experienceQuestionRepository;

    public AiQuestionPersistenceService(
            QuestionService questionService,
            ExperienceQuestionRepository experienceQuestionRepository) {

        this.questionService = questionService;
        this.experienceQuestionRepository = experienceQuestionRepository;
    }

    @Transactional
    public void saveAllQuestions(
            List<String> coding,
            List<String> technical,
            List<String> hr,
            List<String> aptitude,
            List<String> gd,
            InterviewExperience experience) {

        Set<Long> processedQuestionIds = new HashSet<>();

        saveQuestions(coding, QuestionCategory.CODING, experience, processedQuestionIds);
        saveQuestions(technical, QuestionCategory.TECHNICAL, experience, processedQuestionIds);
        saveQuestions(hr, QuestionCategory.HR, experience, processedQuestionIds);
        saveQuestions(aptitude, QuestionCategory.APTITUDE, experience, processedQuestionIds);
        saveQuestions(gd, QuestionCategory.GD, experience, processedQuestionIds);
    }

    private void saveQuestions(
            List<String> questions,
            QuestionCategory category,
            InterviewExperience experience,
            Set<Long> processedQuestionIds) {

        if (questions == null) {
            return;
        }

        for (String questionText : questions) {

    Question savedQuestion =
            questionService.saveQuestion(questionText, category);

    boolean alreadyLinked =
            experienceQuestionRepository
                    .existsByExperienceIdAndQuestionId(
                            experience.getId(),
                            savedQuestion.getId()
                    );

    if (alreadyLinked) {
        continue;
    }

    ExperienceQuestion experienceQuestion = new ExperienceQuestion();

    experienceQuestion.setExperience(experience);
    experienceQuestion.setQuestion(savedQuestion);
    experienceQuestion.setCreatedAt(LocalDateTime.now());

    experienceQuestionRepository.save(experienceQuestion);
}
    }
}