package com.interviewinsights.interviewinsights.repository;

import com.interviewinsights.interviewinsights.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}