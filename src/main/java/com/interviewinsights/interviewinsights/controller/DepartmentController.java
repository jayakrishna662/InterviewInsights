package com.interviewinsights.interviewinsights.controller;

import com.interviewinsights.interviewinsights.entity.Department;
import com.interviewinsights.interviewinsights.repository.DepartmentRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentRepository departmentRepository;

    public DepartmentController(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @GetMapping
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @PostMapping
    public Department createDepartment(@RequestBody Department department) {
        department.setCreatedAt(LocalDateTime.now());
        return departmentRepository.save(department);
    }
}