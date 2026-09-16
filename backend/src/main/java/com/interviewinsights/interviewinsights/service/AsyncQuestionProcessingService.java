package com.interviewinsights.interviewinsights.service;
import com.interviewinsights.interviewinsights.repository.InterviewExperienceRepository;
import com.interviewinsights.interviewinsights.entity.enums.AiProcessingStatus;

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
    private final InterviewExperienceRepository interviewExperienceRepository;

    public AsyncQuestionProcessingService(
            GeminiService geminiService,
            ObjectMapper objectMapper,
            QuestionService questionService,
            ExperienceQuestionRepository experienceQuestionRepository,
            InterviewExperienceRepository interviewExperienceRepository) {

        this.geminiService = geminiService;
        this.objectMapper = objectMapper;
        this.questionService = questionService;
        this.experienceQuestionRepository = experienceQuestionRepository;
        this.interviewExperienceRepository = interviewExperienceRepository;
    }

    @Async
    public void processQuestions(
            InterviewExperience savedExperience,
            String interviewText) {

        try {
                savedExperience.setAiProcessingStatus(AiProcessingStatus.PROCESSING);
                interviewExperienceRepository.save(savedExperience);

            String extractedQuestions =
                    geminiService.extractQuestions(interviewText);

            if (extractedQuestions != null) {
                int startIndex = extractedQuestions.indexOf('{');
                int endIndex = extractedQuestions.lastIndexOf('}');
                if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
                    extractedQuestions = extractedQuestions.substring(startIndex, endIndex + 1);
                } else {
                    extractedQuestions = extractedQuestions
                            .replace("```json", "")
                            .replace("```", "")
                            .trim();
                }
            }

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
                savedExperience.setAiProcessingStatus(AiProcessingStatus.COMPLETED);
interviewExperienceRepository.save(savedExperience);

        } catch (Exception e) {
    savedExperience.setAiProcessingStatus(AiProcessingStatus.FAILED);
    interviewExperienceRepository.save(savedExperience);

    System.err.println(
            "Gemini extraction failed for experience "
                    + savedExperience.getId());
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