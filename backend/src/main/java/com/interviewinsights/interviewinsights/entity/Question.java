package com.interviewinsights.interviewinsights.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.interviewinsights.interviewinsights.entity.enums.QuestionCategory;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "questions",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"question_text", "category"})
        }
)
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question_text", nullable = false)
    private String questionText;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionCategory category;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Integer frequency;

    @Column(columnDefinition = "TEXT")
    private String embedding;
}
