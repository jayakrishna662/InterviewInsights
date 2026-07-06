package com.interviewinsights.interviewinsights.service;

import com.interviewinsights.interviewinsights.dto.QuestionRequest;
import com.interviewinsights.interviewinsights.dto.QuestionResponse;
import com.interviewinsights.interviewinsights.entity.Question;
import com.interviewinsights.interviewinsights.entity.enums.QuestionCategory;
import com.interviewinsights.interviewinsights.repository.QuestionRepository;
import com.interviewinsights.interviewinsights.util.EmbeddingUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final EmbeddingService embeddingService;

    public QuestionService(QuestionRepository questionRepository,EmbeddingService embeddingService) {
        this.questionRepository = questionRepository;
        this.embeddingService = embeddingService;
    }

    public QuestionResponse createQuestion(
            QuestionRequest request) {

        Question question = new Question();

        question.setQuestionText(request.getQuestionText());

        question.setCategory(
                QuestionCategory.valueOf(request.getCategory()));

        question.setCreatedAt(LocalDateTime.now());

        question.setFrequency(1);

        Question savedQuestion =
                questionRepository.save(question);

        QuestionResponse response =
                new QuestionResponse();

        response.setId(savedQuestion.getId());
        response.setQuestionText(savedQuestion.getQuestionText());
        response.setCategory(
                savedQuestion.getCategory());
        response.setCreatedAt(savedQuestion.getCreatedAt());


        return response;
    }

    public List<QuestionResponse> getQuestionsByCategory(
            QuestionCategory category) {

        List<Question> questions =
                questionRepository.findByCategory(category);

        return questions.stream()
                .map(question -> {

                    QuestionResponse response =
                            new QuestionResponse();

                    response.setId(question.getId());
                    response.setQuestionText(
                            question.getQuestionText());
                    response.setCategory(
                            question.getCategory());
                    response.setCreatedAt(
                            question.getCreatedAt());

                    return response;
                })
                .toList();
    }

    public List<QuestionResponse> getAllQuestions() {

        List<Question> questions =
                questionRepository.findAll();

        return questions.stream()
                .map(question -> {

                    QuestionResponse response =
                            new QuestionResponse();

                    response.setId(question.getId());
                    response.setQuestionText(
                            question.getQuestionText());
                    response.setCategory(
                            question.getCategory());
                    response.setCreatedAt(
                            question.getCreatedAt());

                    return response;
                })
                .toList();
    }

    public Question saveQuestion(String questionText, QuestionCategory category) {

        String normalizedQuestion = questionText.trim();

        if (normalizedQuestion.endsWith(".")) {
            normalizedQuestion = normalizedQuestion.substring(0, normalizedQuestion.length() - 1);
        }

        var existingQuestion = questionRepository
                .findByQuestionTextAndCategory(normalizedQuestion, category);

        if (existingQuestion.isPresent()) {

            Question question = existingQuestion.get();

            question.setFrequency(question.getFrequency() + 1);

            return questionRepository.save(question);
        }

        Question newQuestion = new Question();

        String newEmbedding =
                embeddingService.generateEmbedding(normalizedQuestion);

        List<Question> existingQuestions =
                questionRepository.findAllByCategory(category);

        Question bestMatch = null;
        double highestSimilarity = 0;

        for (Question question : existingQuestions) {

            if (question.getEmbedding() == null) {
                continue;
            }

            List<Float> existingVector =
                    EmbeddingUtils.parseEmbedding(question.getEmbedding());

            List<Float> newVector =
                    EmbeddingUtils.parseEmbedding(newEmbedding);

            double similarity =
                    EmbeddingUtils.cosineSimilarity(existingVector, newVector);

            if (similarity > highestSimilarity) {
                highestSimilarity = similarity;
                bestMatch = question;
            }
        }

        final double SIMILARITY_THRESHOLD = 0.85;




        if (bestMatch != null && highestSimilarity >= SIMILARITY_THRESHOLD) {

            bestMatch.setFrequency(bestMatch.getFrequency() + 1);

            return questionRepository.save(bestMatch);
        }

        newQuestion.setQuestionText(normalizedQuestion);
        newQuestion.setCategory(category);
        newQuestion.setCreatedAt(LocalDateTime.now());
        newQuestion.setFrequency(1);
        newQuestion.setEmbedding(newEmbedding);

        return questionRepository.save(newQuestion);
    }
}