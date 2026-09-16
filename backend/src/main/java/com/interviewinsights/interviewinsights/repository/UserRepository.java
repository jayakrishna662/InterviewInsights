package com.interviewinsights.interviewinsights.repository;

import com.interviewinsights.interviewinsights.entity.User;
import com.interviewinsights.interviewinsights.entity.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByRollNumber(String rollNumber);

    long countByRole(Role role);
}