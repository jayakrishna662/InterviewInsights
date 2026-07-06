package com.interviewinsights.interviewinsights.entity;

import com.interviewinsights.interviewinsights.entity.enums.Role;
import jakarta.persistence.*;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;

        @Column(unique = true, nullable = false)
        private String email;

        @Column(name = "password_hash", nullable = false)
        private String passwordHash;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private Role role;

        @Column(name = "roll_number")
        private String rollNumber;

        @ManyToOne  // many users have same department
        @JoinColumn(name = "department_id") // Foreign key in users table
        private Department department;

        @ManyToOne
        @JoinColumn(name = "batch_id")
        private Batch batch;

        @Column(name = "created_at")
        private LocalDateTime createdAt;

}

