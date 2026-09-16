package com.interviewinsights.interviewinsights.service;

import com.interviewinsights.interviewinsights.dto.ExperienceQuestionRequest;
import com.interviewinsights.interviewinsights.dto.ExperienceQuestionResponse;
import com.interviewinsights.interviewinsights.entity.ExperienceQuestion;
import com.interviewinsights.interviewinsights.entity.InterviewExperience;
import com.interviewinsights.interviewinsights.entity.Question;
import com.interviewinsights.interviewinsights.repository.ExperienceQuestionRepository;
import com.interviewinsights.interviewinsights.repository.InterviewExperienceRepository;
import com.interviewinsights.interviewinsights.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ExperienceQuestionService {
    private final ExperienceQuestionRepository experienceQuestionRepository;
    private final InterviewExperienceRepository interviewExperienceRepository;
    private final QuestionRepository questionRepository;

    public ExperienceQuestionService(
            ExperienceQuestionRepository experienceQuestionRepository,
            InterviewExperienceRepository interviewExperienceRepository,
            QuestionRepository questionRepository) {

        this.experienceQuestionRepository =
                experienceQuestionRepository;

        this.interviewExperienceRepository =
                interviewExperienceRepository;

        this.questionRepository =
                questionRepository;
    }

    public void linkExperienceAndQuestion(
            ExperienceQuestionRequest request) {

        InterviewExperience experience =
                interviewExperienceRepository
                        .findById(request.getExperienceId())
                        .orElseThrow();

        Question question =
                questionRepository
                        .findById(request.getQuestionId())
                        .orElseThrow();

        ExperienceQuestion experienceQuestion =
                new ExperienceQuestion();

        experienceQuestion.setExperience(experience);

        experienceQuestion.setQuestion(question);

        experienceQuestionRepository.save(
                experienceQuestion);
    }

    public List<ExperienceQuestionResponse> getQuestionsByExperience(
            Long experienceId) {

        List<ExperienceQuestion> experienceQuestions =
                experienceQuestionRepository
                        .findByExperienceId(experienceId);

        return experienceQuestions.stream()
                .map(experienceQuestion -> {

                    ExperienceQuestionResponse response =
                            new ExperienceQuestionResponse();

                    response.setId(
                            experienceQuestion.getId());

                    response.setQuestionId(
                            experienceQuestion.getQuestion().getId());

                    response.setQuestionText(
                            experienceQuestion.getQuestion().getQuestionText());

                    response.setCategory(
                            experienceQuestion.getQuestion().getCategory());

                    return response;
                })
                .toList();
    }

}
