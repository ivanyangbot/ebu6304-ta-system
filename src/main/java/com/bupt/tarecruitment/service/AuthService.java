package com.bupt.tarecruitment.service;

import com.bupt.tarecruitment.model.Applicant;
import com.bupt.tarecruitment.model.User;
import com.bupt.tarecruitment.repository.UserRepository;
import com.bupt.tarecruitment.util.IdUtil;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class AuthService {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", Pattern.CASE_INSENSITIVE);
    private final UserRepository userRepository;

    public AuthService(ServletContext servletContext) {
        this.userRepository = new UserRepository(servletContext);
    }

    public User login(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return null;
        }
        return userRepository.findByUsernameAndPassword(username.trim(), password.trim());
    }

    public Applicant registerApplicant(String fullName, String email, String username, String password,
                                       String confirmPassword) {
        String normalizedFullName = normalize(fullName);
        String normalizedEmail = normalize(email);
        String normalizedUsername = normalize(username);
        String normalizedPassword = password == null ? "" : password.trim();
        String normalizedConfirmPassword = confirmPassword == null ? "" : confirmPassword.trim();

        validateRegistrationInput(normalizedFullName, normalizedEmail, normalizedUsername,
                normalizedPassword, normalizedConfirmPassword);

        if (userRepository.findByUsername(normalizedUsername) != null) {
            throw new IllegalArgumentException("Username is already taken.");
        }

        if (userRepository.findByEmail(normalizedEmail) != null) {
            throw new IllegalArgumentException("Email is already registered.");
        }

        Applicant applicant = new Applicant(
                IdUtil.generateId("applicant"),
                normalizedUsername,
                normalizedPassword,
                "APPLICANT",
                normalizedFullName,
                normalizedEmail,
                new ArrayList<>(),
                ""
        );
        userRepository.createApplicant(applicant);
        return applicant;
    }

    private void validateRegistrationInput(String fullName, String email, String username, String password,
                                           String confirmPassword) {
        if (fullName.isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be empty.");
        }
        if (email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Please enter a valid email address.");
        }
        if (username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }
        if (!username.matches("[A-Za-z0-9._-]{4,20}")) {
            throw new IllegalArgumentException("Username must be 4-20 characters and use letters, numbers, dot, underscore, or hyphen only.");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters.");
        }
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match.");
        }
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
