package com.cafe.backend.service;

import com.cafe.backend.dto.*;
import com.cafe.backend.entity.User;
import com.cafe.backend.exception.InvalidCredentialsException;
import com.cafe.backend.exception.ResourceNotFoundException;
import com.cafe.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&^#()_+\\-={}\\[\\]:;\"'<>,./]).{8,}$");

    public UserResponse registerUser(RegisterRequest request) {
        String username = request.getUsername() != null ? request.getUsername().trim() : null;
        String password = request.getPassword();
        String role  = "CASHIER";
        String securityQuestion = request.getSecurityQuestion() != null ? request.getSecurityQuestion().trim() : null;
        String securityAnswer = request.getSecurityAnswer() != null ? request.getSecurityAnswer().trim().toLowerCase() : null;

        if (username == null || username.isBlank()) {
            throw new RuntimeException("Username is required");
        }

        validatePassword(password);

        if (securityQuestion == null || securityQuestion.isBlank()) {
            throw new RuntimeException("Security question is required");
        }

        if (securityAnswer == null || securityAnswer.isBlank()) {
            throw new RuntimeException("Security answer is required");
        }

        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setSecurityQuestion(securityQuestion);
        user.setSecurityAnswer(securityAnswer);
        user.setEnabled(true);

        User savedUser = userRepository.save(user);
        return mapToUserResponse(savedUser);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        if (!Boolean.TRUE.equals(user.getEnabled())) {
            throw new InvalidCredentialsException("User account is disabled");
        }

        return LoginResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .message("Login successful")
                .build();
    }

    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .enabled(user.getEnabled())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public void resetPassword(ForgotPasswordRequest request) {
        String username = request.getUsername() != null ? request.getUsername().trim() : null;
        String securityQuestion = request.getSecurityQuestion() != null ? request.getSecurityQuestion().trim() : null;
        String securityAnswer = request.getSecurityAnswer() != null ? request.getSecurityAnswer().trim().toLowerCase() : null;
        String newPassword = request.getNewPassword();

        if (username == null || username.isBlank()) {
            throw new RuntimeException("Username is required");
        }

        if (securityQuestion == null || securityQuestion.isBlank()) {
            throw new RuntimeException("Security question is required");
        }

        if (securityAnswer == null || securityAnswer.isBlank()) {
            throw new RuntimeException("Security answer is required");
        }

        validatePassword(newPassword);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!securityQuestion.equals(user.getSecurityQuestion())) {
            throw new RuntimeException("Security question does not match");
        }

        if (!securityAnswer.equals(user.getSecurityAnswer())) {
            throw new RuntimeException("Security answer does not match");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .enabled(user.getEnabled())
                .createdAt(user.getCreatedAt())
                .build();
    }
    private void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new RuntimeException("Password is required");
        }

        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new RuntimeException(
                    "Password must be at least 8 characters and include uppercase, lowercase, number, and special character"
            );
        }
    }
    public void verifyResetData(VerifyResetRequest request) {
        String username = request.getUsername() != null ? request.getUsername().trim() : null;
        String securityQuestion = request.getSecurityQuestion() != null ? request.getSecurityQuestion().trim() : null;
        String securityAnswer = request.getSecurityAnswer() != null ? request.getSecurityAnswer().trim().toLowerCase() : null;

        if (username == null || username.isBlank()) {
            throw new RuntimeException("Username is required");
        }

        if (securityQuestion == null || securityQuestion.isBlank()) {
            throw new RuntimeException("Security question is required");
        }

        if (securityAnswer == null || securityAnswer.isBlank()) {
            throw new RuntimeException("Security answer is required");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!securityQuestion.equals(user.getSecurityQuestion())) {
            throw new RuntimeException("Security question does not match");
        }

        if (!securityAnswer.equals(user.getSecurityAnswer())) {
            throw new RuntimeException("Security answer does not match");
        }
    }
    public java.util.List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .map(this::mapToUserResponse)
                .toList();
    }

    public UserResponse createUserByAdmin(AdminCreateUserRequest request) {
        String username = request.getUsername() != null ? request.getUsername().trim() : null;
        String password = request.getPassword();
        String role = request.getRole() != null ? request.getRole().trim().toUpperCase() : null;
        String securityQuestion = request.getSecurityQuestion() != null ? request.getSecurityQuestion().trim() : null;
        String securityAnswer = request.getSecurityAnswer() != null ? request.getSecurityAnswer().trim().toLowerCase() : null;

        if (username == null || username.isBlank()) {
            throw new RuntimeException("Username is required");
        }

        validatePassword(password);

        if (role == null || role.isBlank()) {
            throw new RuntimeException("Role is required");
        }

        if (!role.equals("ADMIN") && !role.equals("CASHIER")) {
            throw new RuntimeException("Role must be ADMIN or CASHIER");
        }

        if (securityQuestion == null || securityQuestion.isBlank()) {
            throw new RuntimeException("Security question is required");
        }

        if (securityAnswer == null || securityAnswer.isBlank()) {
            throw new RuntimeException("Security answer is required");
        }

        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setSecurityQuestion(securityQuestion);
        user.setSecurityAnswer(securityAnswer);
        user.setEnabled(true);

        return mapToUserResponse(userRepository.save(user));
    }

    public UserResponse updateUserRole(Long userId, UpdateUserRoleRequest request) {
        String role = request.getRole() != null ? request.getRole().trim().toUpperCase() : null;

        if (role == null || role.isBlank()) {
            throw new RuntimeException("Role is required");
        }

        if (!role.equals("ADMIN") && !role.equals("CASHIER")) {
            throw new RuntimeException("Role must be ADMIN or CASHIER");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole(role);
        return mapToUserResponse(userRepository.save(user));
    }

    public UserResponse updateUserStatus(Long userId, UpdateUserStatusRequest request) {
        if (request.getEnabled() == null) {
            throw new RuntimeException("Enabled value is required");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEnabled(request.getEnabled());
        return mapToUserResponse(userRepository.save(user));
    }
}