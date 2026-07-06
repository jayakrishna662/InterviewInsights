package com.interviewinsights.interviewinsights.service;


import com.interviewinsights.interviewinsights.dto.InterviewExperienceRequest;
import com.interviewinsights.interviewinsights.dto.InterviewExperienceResponse;
import com.interviewinsights.interviewinsights.entity.*;
import com.interviewinsights.interviewinsights.entity.enums.Result;
import com.interviewinsights.interviewinsights.repository.CompanyRepository;
import com.interviewinsights.interviewinsights.repository.InterviewExperienceRepository;
import com.interviewinsights.interviewinsights.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class InterviewExperienceService {

    private final InterviewExperienceRepository interviewExperienceRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final AsyncQuestionProcessingService asyncQuestionProcessingService;

    public InterviewExperienceService(
            InterviewExperienceRepository interviewExperienceRepository,
            UserRepository userRepository,
            CompanyRepository companyRepository, AsyncQuestionProcessingService asyncQuestionProcessingService) {

        this.interviewExperienceRepository = interviewExperienceRepository;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.asyncQuestionProcessingService = asyncQuestionProcessingService;
    }

    public InterviewExperienceResponse createInterviewExperience(
            InterviewExperienceRequest request,Long userId) {

        boolean hasExperience =
                hasText(request.getAptitudeExperience()) ||
                        hasText(request.getCodingExperience()) ||
                        hasText(request.getTechnicalExperience()) ||
                        hasText(request.getHrExperience()) ||
                        hasText(request.getGdExperience()) ||
                        hasText(request.getOverallSuggestions());

        if (!hasExperience) {
            throw new IllegalArgumentException(
                    "Please enter at least one interview experience.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow();

        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow();

        InterviewExperience experience = new InterviewExperience();

        experience.setUser(user);
        experience.setCompany(company);

        experience.setInterviewYear(request.getInterviewYear());
        experience.setResult(Result.valueOf(request.getResult()));

        experience.setAptitudeExperience(request.getAptitudeExperience());
        experience.setCodingExperience(request.getCodingExperience());
        experience.setTechnicalExperience(request.getTechnicalExperience());
        experience.setHrExperience(request.getHrExperience());
        experience.setGdExperience(request.getGdExperience());
        experience.setOverallSuggestions(request.getOverallSuggestions());

        experience.setCreatedAt(LocalDateTime.now());

        InterviewExperience savedExperience =
                interviewExperienceRepository.save(experience);
        String interviewText = """
Coding Experience:
%s

Technical Experience:
%s

HR Experience:
%s

Aptitude Experience:
%s

GD Experience:
%s

Overall Suggestions:
%s
""".formatted(
                savedExperience.getCodingExperience(),
                savedExperience.getTechnicalExperience(),
                savedExperience.getHrExperience(),
                savedExperience.getAptitudeExperience(),
                savedExperience.getGdExperience(),
                savedExperience.getOverallSuggestions()
        );

        asyncQuestionProcessingService.processQuestions(
                savedExperience,
                interviewText);

        InterviewExperienceResponse response = new InterviewExperienceResponse();

        response.setId(savedExperience.getId());

        response.setUserId(savedExperience.getUser().getId());
        response.setUserName(savedExperience.getUser().getName());

        response.setCompanyId(savedExperience.getCompany().getId());
        response.setCompanyName(savedExperience.getCompany().getCompanyName());

        response.setInterviewYear(savedExperience.getInterviewYear());
        response.setResult(savedExperience.getResult().name());

        response.setAptitudeExperience(savedExperience.getAptitudeExperience());
        response.setCodingExperience(savedExperience.getCodingExperience());
        response.setTechnicalExperience(savedExperience.getTechnicalExperience());
        response.setHrExperience(savedExperience.getHrExperience());
        response.setGdExperience(savedExperience.getGdExperience());
        response.setOverallSuggestions(savedExperience.getOverallSuggestions());

        response.setCreatedAt(savedExperience.getCreatedAt());

        return response;
    }


    public List<InterviewExperienceResponse> getAllInterviewExperiences() {

        List<InterviewExperience> experiences =
                interviewExperienceRepository.findAll();

        return experiences.stream()
                .map(experience -> {

                    InterviewExperienceResponse response =
                            new InterviewExperienceResponse();

                    response.setId(experience.getId());

                    response.setUserId(experience.getUser().getId());
                    response.setUserName(experience.getUser().getName());

                    response.setCompanyId(experience.getCompany().getId());
                    response.setCompanyName(
                            experience.getCompany().getCompanyName());

                    response.setInterviewYear(
                            experience.getInterviewYear());

                    response.setResult(
                            experience.getResult().name());

                    response.setAptitudeExperience(
                            experience.getAptitudeExperience());

                    response.setCodingExperience(
                            experience.getCodingExperience());

                    response.setTechnicalExperience(
                            experience.getTechnicalExperience());

                    response.setHrExperience(
                            experience.getHrExperience());

                    response.setGdExperience(
                            experience.getGdExperience());

                    response.setOverallSuggestions(
                            experience.getOverallSuggestions());

                    response.setCreatedAt(
                            experience.getCreatedAt());

                    return response;
                })
                .toList();
    }

    public List<InterviewExperienceResponse>
    getInterviewExperiencesByCompany(Long companyId) {
        List<InterviewExperience> experiences =
                interviewExperienceRepository.findByCompanyId(companyId);

        return experiences.stream()
                .map(experience -> {

                    InterviewExperienceResponse response =
                            new InterviewExperienceResponse();

                    response.setId(experience.getId());

                    response.setUserId(experience.getUser().getId());
                    response.setUserName(experience.getUser().getName());

                    response.setCompanyId(experience.getCompany().getId());
                    response.setCompanyName(
                            experience.getCompany().getCompanyName());

                    response.setInterviewYear(
                            experience.getInterviewYear());

                    response.setResult(
                            experience.getResult().name());

                    response.setAptitudeExperience(
                            experience.getAptitudeExperience());

                    response.setCodingExperience(
                            experience.getCodingExperience());

                    response.setTechnicalExperience(
                            experience.getTechnicalExperience());

                    response.setHrExperience(
                            experience.getHrExperience());

                    response.setGdExperience(
                            experience.getGdExperience());

                    response.setOverallSuggestions(
                            experience.getOverallSuggestions());

                    response.setCreatedAt(
                            experience.getCreatedAt());

                    return response;

                })
                .toList();
    }

    public InterviewExperienceResponse getInterviewExperienceById(
            Long id) {

        InterviewExperience experience =
                interviewExperienceRepository
                        .findById(id)
                        .orElseThrow();

        InterviewExperienceResponse response =
                new InterviewExperienceResponse();

        response.setId(
                experience.getId());

        response.setUserId(
                experience.getUser().getId());

        response.setUserName(
                experience.getUser().getName());

        response.setCompanyId(
                experience.getCompany().getId());

        response.setCompanyName(
                experience.getCompany().getCompanyName());

        response.setInterviewYear(
                experience.getInterviewYear());

        response.setResult(
                experience.getResult().name());

        response.setAptitudeExperience(
                experience.getAptitudeExperience());

        response.setCodingExperience(
                experience.getCodingExperience());

        response.setTechnicalExperience(
                experience.getTechnicalExperience());

        response.setHrExperience(
                experience.getHrExperience());

        response.setGdExperience(
                experience.getGdExperience());

        response.setOverallSuggestions(
                experience.getOverallSuggestions());

        response.setCreatedAt(
                experience.getCreatedAt());

        return response;
    }

    private boolean hasText(String text) {
        return text != null && !text.trim().isEmpty();
    }
}