package com.interviewinsights.interviewinsights.service;

import com.interviewinsights.interviewinsights.dto.AuthLoginRequest;
import com.interviewinsights.interviewinsights.dto.AuthRegisterRequest;
import com.interviewinsights.interviewinsights.dto.AuthResponse;
import com.interviewinsights.interviewinsights.entity.Batch;
import com.interviewinsights.interviewinsights.entity.Department;
import com.interviewinsights.interviewinsights.entity.User;
import com.interviewinsights.interviewinsights.entity.enums.Role;
import com.interviewinsights.interviewinsights.exception.*;
import com.interviewinsights.interviewinsights.repository.BatchRepository;
import com.interviewinsights.interviewinsights.repository.DepartmentRepository;
import com.interviewinsights.interviewinsights.repository.UserRepository;
import com.interviewinsights.interviewinsights.util.JwtUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BatchRepository batchRepository;
    private final DepartmentRepository departmentRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    public AuthService(UserRepository userRepository,
                       BatchRepository batchRepository,BCryptPasswordEncoder passwordEncoder,DepartmentRepository departmentRepository,JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.batchRepository = batchRepository;
        this.departmentRepository = departmentRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // Register new user
    public AuthResponse register(AuthRegisterRequest request) { // request object contains user registration data


        // Check if departmentId is empty
        if (request.getDepartmentId() == null || request.getDepartmentId().isEmpty()) {
            AuthResponse response = new AuthResponse();
            response.setSuccess(false);
            response.setMessage("Please select a department");
            return response;
        }


        // Check if password and confirm password match's
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new PasswordMismatchException("Passwords do not match");
        }

        // Check if email already exists in DB
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already registered");
        }

        String rollNumber = request.getRollNumber().trim();

        // Check if roll number already exists
        if (userRepository.findByRollNumber(rollNumber).isPresent()) {
            throw new RollNumberAlreadyExistsException("Roll number already registered");
        }

        // Find the Batch object in the database using the batchId received from the browser (eg: Batch objetc=  batchid=1->2022-20226)
        Batch batch = batchRepository.findById(request.getBatchId())
                .orElseThrow(() -> new BatchNotFoundException("Batch not found"));

        // Get department
        Department department = departmentRepository.findById(Long.parseLong(request.getDepartmentId()))
                .orElseThrow(() -> new RuntimeException("Department not found"));

        // Create new user (notice we don't create id here, id=null initially, because DB creates id automatically)
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail() != null ? request.getEmail().trim() : null);
        user.setRollNumber(rollNumber);
        user.setDepartment(department);
        user.setBatch(batch); // Set the retrieved Batch object to the User.
        user.setRole(Role.USER); // Every newly registered user gets USER role automatically

        // Hash password
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        user.setCreatedAt(LocalDateTime.now());

        // Saving user object into database , .save() returns saved user object
        // after saving, now savedUser has id along with above entities you set
        User savedUser;
        try {
            savedUser = userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException ex) {
            if (isRollNumberConstraintViolation(ex)) {
                throw new RollNumberAlreadyExistsException("Roll number already registered");
            }
            throw ex;
        }

        // Return response
        AuthResponse response = new AuthResponse();
        response.setUserId(savedUser.getId());
        response.setName(savedUser.getName());
        response.setEmail(savedUser.getEmail());
        response.setRole(savedUser.getRole());
        response.setSuccess(true);
        response.setMessage("Registration successful");

        return response;
    }

    private boolean isRollNumberConstraintViolation(
            DataIntegrityViolationException ex) {

        Throwable cause = ex;
        while (cause != null) {
            if (cause.getMessage() != null
                    && cause.getMessage().contains("uk_users_roll_number")) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }

    // Login user
    public AuthResponse login(AuthLoginRequest request) {

    String rollNumber = request.getRollNumber() != null
            ? request.getRollNumber().trim()
            : "";

    User user = userRepository.findByRollNumber(rollNumber)
            .orElseThrow(() ->
                    new InvalidPasswordException("Invalid roll number or password"));

    if (!passwordEncoder.matches(
            request.getPassword(),
            user.getPasswordHash())) {

        throw new InvalidPasswordException("Invalid roll number or password");
    }

    AuthResponse response = new AuthResponse();

    response.setUserId(user.getId());
    response.setName(user.getName());
    response.setEmail(user.getEmail());
    response.setRole(user.getRole());

    response.setSuccess(true);
    response.setMessage("Login successful");

    response.setToken(
            jwtUtil.generateToken(
                    user.getId(),
                    user.getName(),
                    user.getRole() != null
                            ? user.getRole().name()
                            : null
            )
    );

    return response;
}
}
