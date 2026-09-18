package com.interviewinsights.interviewinsights.service;

import com.interviewinsights.interviewinsights.entity.ExperienceQuestion;
import com.interviewinsights.interviewinsights.entity.InterviewExperience;
import com.interviewinsights.interviewinsights.entity.Question;
import com.interviewinsights.interviewinsights.entity.enums.QuestionCategory;
import com.interviewinsights.interviewinsights.repository.ExperienceQuestionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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

        saveQuestions(coding, QuestionCategory.CODING, experience);
        saveQuestions(technical, QuestionCategory.TECHNICAL, experience);
        saveQuestions(hr, QuestionCategory.HR, experience);
        saveQuestions(aptitude, QuestionCategory.APTITUDE, experience);
        saveQuestions(gd, QuestionCategory.GD, experience);
    }

    private void saveQuestions(
            List<String> questions,
            QuestionCategory category,
            InterviewExperience experience) {

        if (questions == null) {
            return;
        }

        for (String questionText : questions) {

            Question savedQuestion =
                    questionService.saveQuestion(questionText, category);

            ExperienceQuestion experienceQuestion =
                    new ExperienceQuestion();

            experienceQuestion.setExperience(experience);
            experienceQuestion.setQuestion(savedQuestion);
            experienceQuestion.setCreatedAt(LocalDateTime.now());

            experienceQuestionRepository.save(experienceQuestion);
        }
    }
}