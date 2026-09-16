package com.interviewinsights.interviewinsights.service;

import org.springframework.stereotype.Service;

import com.interviewinsights.interviewinsights.entity.ExperienceQuestion;
import com.interviewinsights.interviewinsights.entity.InterviewExperience;
import com.interviewinsights.interviewinsights.entity.Question;
import com.interviewinsights.interviewinsights.entity.enums.AiProcessingStatus;
import com.interviewinsights.interviewinsights.repository.ExperienceQuestionRepository;
import com.interviewinsights.interviewinsights.repository.InterviewExperienceRepository;
import com.interviewinsights.interviewinsights.repository.QuestionRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class AiRetryService {

    private final InterviewExperienceRepository experienceRepository;
private final ExperienceQuestionRepository experienceQuestionRepository;
private final QuestionRepository questionRepository;

public AiRetryService(
        InterviewExperienceRepository experienceRepository,
        ExperienceQuestionRepository experienceQuestionRepository,
        QuestionRepository questionRepository) {

    this.experienceRepository = experienceRepository;
    this.experienceQuestionRepository = experienceQuestionRepository;
    this.questionRepository = questionRepository;
}

@Transactional
public void resetFailedAiProcessing(Long experienceId) {

    InterviewExperience experience =
            experienceRepository.findById(experienceId)
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Interview experience not found"));

    List<ExperienceQuestion> oldLinks =
            experienceQuestionRepository.findByExperienceId(experienceId);

    for (ExperienceQuestion link : oldLinks) {
        Question question = link.getQuestion();

        experienceQuestionRepository.delete(link);

        long remainingLinks =
                experienceQuestionRepository.countByQuestionId(question.getId());

        question.setFrequency((int) remainingLinks);
        questionRepository.save(question);
    }

    experience.setAiProcessingStatus(AiProcessingStatus.PENDING);
    experienceRepository.save(experience);
}

}