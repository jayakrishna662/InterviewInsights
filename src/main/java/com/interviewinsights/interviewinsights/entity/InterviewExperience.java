package com.interviewinsights.interviewinsights.entity;
import com.interviewinsights.interviewinsights.entity.enums.Result;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "interview_experiences")
public class InterviewExperience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "interview_year", nullable = false)
    private Integer interviewYear;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Result result;

    @Column(name = "aptitude_experience", columnDefinition = "TEXT")
    private String aptitudeExperience;

    @Column(name = "coding_experience", columnDefinition = "TEXT")
    private String codingExperience;

    @Column(name = "technical_experience", columnDefinition = "TEXT")
    private String technicalExperience;

    @Column(name = "hr_experience", columnDefinition = "TEXT")
    private String hrExperience;

    @Column(name = "gd_experience", columnDefinition = "TEXT")
    private String gdExperience;

    @Column(name = "overall_suggestions", columnDefinition = "TEXT")
    private String overallSuggestions;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Multiple InterviewExperiences can belong to one User.
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Use the user_id column as the foreign key.
    private User user;

    //  Multiple interviewExperiences belongs to One company
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
}