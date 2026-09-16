package com.interviewinsights.interviewinsights.service;

import com.interviewinsights.interviewinsights.dto.CurrentUserResponse;
import com.interviewinsights.interviewinsights.entity.Batch;
import com.interviewinsights.interviewinsights.entity.Department;
import com.interviewinsights.interviewinsights.entity.enums.Role;
import com.interviewinsights.interviewinsights.exception.EmailAlreadyExistsException;
import com.interviewinsights.interviewinsights.repository.BatchRepository;
import com.interviewinsights.interviewinsights.repository.DepartmentRepository;
import com.interviewinsights.interviewinsights.repository.UserRepository;
import org.springframework.stereotype.Service;

import com.interviewinsights.interviewinsights.dto.UserRegistrationRequest;
import com.interviewinsights.interviewinsights.entity.User;

import java.time.LocalDateTime;
import java.util.List;

import com.interviewinsights.interviewinsights.dto.UserResponse;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final BatchRepository batchRepository;

    private final DepartmentRepository departmentRepository;

    public UserService(UserRepository userRepository,
                       BatchRepository batchRepository, DepartmentRepository departmentRepository) {
        this.userRepository = userRepository;
        this.batchRepository = batchRepository;
        this.departmentRepository = departmentRepository;
    }

    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public UserResponse registerUser(UserRegistrationRequest request) {

        if (existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already registered");
        }
        Long batchId = request.getBatchId();
        Batch batch = batchRepository.findById(batchId)
                .orElseThrow();

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRollNumber(request.getRollNumber());
        // Get department from database
        Department department = departmentRepository.findById(Long.parseLong(request.getDepartmentId()))
                .orElseThrow(() -> new RuntimeException("Department not found"));
        user.setDepartment(department);
        user.setBatch(batch);
        user.setRole(Role.USER);
        user.setCreatedAt(LocalDateTime.now());

        // temporary (will be replaced later)
        user.setPasswordHash(request.getPassword());

        User savedUser = userRepository.save(user);

        return mapToUserResponse(savedUser);
    }


    public List<UserResponse> getAllUsers() {

        List<User> users = userRepository.findAll();

        // Convert each User entity into a UserResponse DTO
        // before returning the data to the client.
        return users.stream()
                .map(user -> mapToUserResponse(user)) // For each user  call mapToUserResponse(user)
                .toList();
    }

    // Helper method used to convert a User entity into a UserResponse DTO.
    // This ensures only required fields are sent to the client
    // and avoids repeating the same mapping code in multiple methods.
    private UserResponse mapToUserResponse(User user) {

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());
        response.setRollNumber(user.getRollNumber());
        response.setDepartment(user.getDepartment().getDepartmentName());
        response.setBatchName(user.getBatch().getBatchName());
        response.setCreatedAt(user.getCreatedAt());

        return response;
    }

    public CurrentUserResponse getCurrentUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new CurrentUserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRollNumber(),
                user.getRole()
        );
    }
}