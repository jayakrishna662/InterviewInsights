package com.interviewinsights.interviewinsights.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "experience_questions",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"experience_id", "question_id"})
        }
)
public class ExperienceQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "experience_id", nullable = false)
    private InterviewExperience experience;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}