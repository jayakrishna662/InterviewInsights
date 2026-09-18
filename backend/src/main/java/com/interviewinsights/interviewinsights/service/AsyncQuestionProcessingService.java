package com.interviewinsights.interviewinsights.service;
import com.interviewinsights.interviewinsights.repository.InterviewExperienceRepository;
import com.interviewinsights.interviewinsights.entity.enums.AiProcessingStatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interviewinsights.interviewinsights.dto.gemini.response.ExtractedQuestionsResponse;

import com.interviewinsights.interviewinsights.entity.InterviewExperience;


import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AsyncQuestionProcessingService {

    private final GeminiService geminiService;
    private final ObjectMapper objectMapper;
    
    private final AiQuestionPersistenceService aiQuestionPersistenceService;
    private final InterviewExperienceRepository interviewExperienceRepository;

    public AsyncQuestionProcessingService(
            GeminiService geminiService,
            ObjectMapper objectMapper,
            AiQuestionPersistenceService aiQuestionPersistenceService,
            InterviewExperienceRepository interviewExperienceRepository) {

        this.geminiService = geminiService;
        this.objectMapper = objectMapper;
        this.aiQuestionPersistenceService = aiQuestionPersistenceService;
        this.interviewExperienceRepository = interviewExperienceRepository;
    }

    @Async
    public void processQuestions(
            InterviewExperience savedExperience,
            String interviewText) {

        try {
                 int updated = interviewExperienceRepository.markAsProcessing(
                        savedExperience.getId(),
                        AiProcessingStatus.PROCESSING,
                        AiProcessingStatus.PENDING
                );

                if (updated == 0) {
                return;
                }

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
        validateExtractedQuestions(response);

            aiQuestionPersistenceService.saveAllQuestions(
        response.getCoding(),
        response.getTechnical(),
        response.getHr(),
        response.getAptitude(),
        response.getGd(),
        savedExperience
);

savedExperience.setAiProcessingStatus(AiProcessingStatus.COMPLETED);
interviewExperienceRepository.save(savedExperience);

        } catch (Exception e) {
    savedExperience.setAiProcessingStatus(AiProcessingStatus.FAILED);
    interviewExperienceRepository.save(savedExperience);

    e.printStackTrace();
}
    }



    private void validateExtractedQuestions(ExtractedQuestionsResponse response) {

    if (response == null) {
        throw new IllegalArgumentException("Invalid Gemini response");
    }

    validateList(response.getCoding());
    validateList(response.getTechnical());
    validateList(response.getHr());
    validateList(response.getAptitude());
    validateList(response.getGd());
}

private void validateList(List<String> questions) {

    if (questions == null) {
        return;
    }

    if (questions.size() > 30) {
        throw new IllegalArgumentException(
                "Gemini returned too many questions");
    }

    for (String question : questions) {

        if (question == null || question.isBlank()) {
            throw new IllegalArgumentException(
                    "Gemini returned an invalid question");
        }

        if (question.length() > 500) {
            throw new IllegalArgumentException(
                    "Gemini returned an excessively long question");
        }
    }
}
}