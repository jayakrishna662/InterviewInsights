package com.interviewinsights.interviewinsights.service;

import com.interviewinsights.interviewinsights.dto.AdminStatsResponse;
import com.interviewinsights.interviewinsights.entity.enums.Role;
import com.interviewinsights.interviewinsights.repository.CompanyRepository;
import com.interviewinsights.interviewinsights.repository.InterviewExperienceRepository;
import com.interviewinsights.interviewinsights.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.interviewinsights.interviewinsights.entity.InterviewExperience;
import com.interviewinsights.interviewinsights.entity.enums.AiProcessingStatus;


import com.interviewinsights.interviewinsights.dto.AdminAiProcessingResponse;
import java.util.List;
import com.interviewinsights.interviewinsights.service.AiRetryService;

@Service
public class AdminService {

    private final CompanyRepository companyRepository;
    private final InterviewExperienceRepository experienceRepository;
    private final UserRepository userRepository;
    private final AsyncQuestionProcessingService asyncQuestionProcessingService;
    private final AiRetryService aiRetryService;
        

    public AdminService(CompanyRepository companyRepository,
                        InterviewExperienceRepository experienceRepository,
                        UserRepository userRepository, AsyncQuestionProcessingService asyncQuestionProcessingService,
 AiRetryService aiRetryService) {
        this.companyRepository = companyRepository;
        this.experienceRepository = experienceRepository;
        this.userRepository = userRepository;
        this.asyncQuestionProcessingService = asyncQuestionProcessingService;
this.aiRetryService = aiRetryService;
    }

    public AdminStatsResponse getDashboardStats() {
        long totalCompanies = companyRepository.count();
        long totalExperiences = experienceRepository.count();
        long totalStudents = userRepository.countByRole(Role.USER);

        return new AdminStatsResponse(totalCompanies, totalExperiences, totalStudents);
    }

    public void retryAiProcessing(Long experienceId) {

    InterviewExperience experience =
        experienceRepository.findById(experienceId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Interview experience not found"));

if (experience.getAiProcessingStatus() != AiProcessingStatus.FAILED) {
    throw new IllegalStateException(
            "Only failed AI processing can be retried");
}

aiRetryService.resetFailedAiProcessing(experienceId);

asyncQuestionProcessingService.processQuestions(
        experience,
        buildInterviewText(experience)
);
}


private String buildInterviewText(InterviewExperience experience) {
    return """
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
        experience.getCodingExperience(),
        experience.getTechnicalExperience(),
        experience.getHrExperience(),
        experience.getAptitudeExperience(),
        experience.getGdExperience(),
        experience.getOverallSuggestions()  
);
}

public List<AdminAiProcessingResponse> getAiProcessingStatus() {

    return experienceRepository.findAll()
            .stream()
            .map(experience -> {
                AdminAiProcessingResponse response =
                        new AdminAiProcessingResponse();

                response.setExperienceId(experience.getId());
                response.setStudentName(experience.getUser().getName());
                response.setCompanyName(experience.getCompany().getCompanyName());
                response.setAiProcessingStatus(
                        experience.getAiProcessingStatus());

                return response;
            })
            .toList();
}
}
