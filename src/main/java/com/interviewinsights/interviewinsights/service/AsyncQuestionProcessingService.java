package com.interviewinsights.interviewinsights.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interviewinsights.interviewinsights.dto.gemini.response.ExtractedQuestionsResponse;
import com.interviewinsights.interviewinsights.entity.ExperienceQuestion;
import com.interviewinsights.interviewinsights.entity.InterviewExperience;
import com.interviewinsights.interviewinsights.entity.Question;
import com.interviewinsights.interviewinsights.entity.enums.QuestionCategory;
import com.interviewinsights.interviewinsights.repository.ExperienceQuestionRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AsyncQuestionProcessingService {

    private final GeminiService geminiService;
    private final ObjectMapper objectMapper;
    private final QuestionService questionService;
    private final ExperienceQuestionRepository experienceQuestionRepository;

    public AsyncQuestionProcessingService(
            GeminiService geminiService,
            ObjectMapper objectMapper,
            QuestionService questionService,
            ExperienceQuestionRepository experienceQuestionRepository) {

        this.geminiService = geminiService;
        this.objectMapper = objectMapper;
        this.questionService = questionService;
        this.experienceQuestionRepository = experienceQuestionRepository;
    }

    @Async
    public void processQuestions(
            InterviewExperience savedExperience,
            String interviewText) {

        try {

            String extractedQuestions =
                    geminiService.extractQuestions(interviewText);

            extractedQuestions = extractedQuestions
                    .replace("```json", "")
                    .replace("```", "")
                    .trim();

            ExtractedQuestionsResponse response =
                    objectMapper.readValue(
                            extractedQuestions,
                            ExtractedQuestionsResponse.class);

            saveQuestions(response.getCoding(),
                    QuestionCategory.CODING,
                    savedExperience);

            saveQuestions(response.getTechnical(),
                    QuestionCategory.TECHNICAL,
                    savedExperience);

            saveQuestions(response.getHr(),
                    QuestionCategory.HR,
                    savedExperience);

            saveQuestions(response.getAptitude(),
                    QuestionCategory.APTITUDE,
                    savedExperience);

            saveQuestions(response.getGd(),
                    QuestionCategory.GD,
                    savedExperience);

        } catch (Exception e) {

            System.err.println(
                    "Gemini extraction failed for experience "
                            + savedExperience.getId());

            e.printStackTrace();
        }
    }

    private void saveQuestions(
            List<String> questions,
            QuestionCategory category,
            InterviewExperience experience) {

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