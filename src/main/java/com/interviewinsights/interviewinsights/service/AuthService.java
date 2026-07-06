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

        // Check if roll number already exists
        if (userRepository.findByRollNumber(request.getRollNumber()).isPresent()) {
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
        user.setEmail(request.getEmail());
        user.setRollNumber(request.getRollNumber());
        user.setDepartment(department);
        user.setBatch(batch); // Set the retrieved Batch object to the User.
        user.setRole(Role.USER); // Every newly registered user gets USER role automatically

        // Hash password
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        user.setCreatedAt(LocalDateTime.now());

        // Saving user object into database , .save() returns saved user object
        // after saving, now savedUser has id along with above entities you set
        User savedUser = userRepository.save(user);

        // Return response
        AuthResponse response = new AuthResponse();
        response.setUserId(savedUser.getId());
        response.setName(savedUser.getName());
        response.setEmail(savedUser.getEmail());
        response.setSuccess(true);
        response.setMessage("Registration successful");

        return response;
    }

    // Login user
    public AuthResponse login(AuthLoginRequest request) {

        // Find user by roll number
        User user = userRepository.findByRollNumber(request.getRollNumber())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Check password matches with the password in DB
        // BCrypt takes user entered password and hashes it internally and compares it with stored hash in DB
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new InvalidPasswordException("Invalid password");
        }

        // Return response
        AuthResponse response = new AuthResponse();
        response.setUserId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setSuccess(true);
        response.setMessage("Login successful");
        response.setToken(jwtUtil.generateToken(user.getId(), user.getName()));

        return response;
    }
}