package com.bupt.tarecruitment.service;

import com.bupt.tarecruitment.model.Applicant;
import com.bupt.tarecruitment.model.User;
import com.bupt.tarecruitment.repository.UserRepository;
import com.bupt.tarecruitment.util.IdUtil;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Handles user authentication and self-registration for the TA Recruitment System.
 *
 * <p>This service provides two primary operations:</p>
 * <ol>
 *   <li>{@link #login(String, String)} – validates username/password credentials and
 *       returns the matching {@link User} object, or {@code null} on failure.</li>
 *   <li>{@link #registerApplicant(String, String, String, String, String)} – validates
 *       registration fields, checks for uniqueness, and persists a new
 *       {@link Applicant} account with the {@code "APPLICANT"} role.</li>
 * </ol>
 *
 * <p><strong>Note:</strong> passwords are stored as plain text in the JSON data files
 * (no hashing) in order to satisfy the project constraint of using only simple file
 * formats without external security libraries.</p>
 *
 * @author  Group 71
 * @version 1.0
 * @see     com.bupt.tarecruitment.servlet.LoginServlet
 * @see     com.bupt.tarecruitment.servlet.RegisterServlet
 */
public class AuthService {

    /** Pattern used to validate email addresses during registration. */
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", Pattern.CASE_INSENSITIVE);

    private final UserRepository userRepository;

    /**
     * Creates an {@code AuthService} backed by the JSON user store.
     *
     * @param servletContext the servlet context used to resolve the data file path
     */
    public AuthService(ServletContext servletContext) {
        this.userRepository = new UserRepository(servletContext);
    }

    /**
     * Authenticates a user by username and password.
     *
     * <p>Both parameters are trimmed before the lookup. Returns {@code null} if
     * either parameter is blank or if no matching user exists in the data file.</p>
     *
     * @param username the login username
     * @param password the account password
     * @return the authenticated {@link User}, or {@code null} if authentication fails
     */
    public User login(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return null;
        }
        return userRepository.findByUsernameAndPassword(username.trim(), password.trim());
    }

    /**
     * Registers a new applicant account.
     *
     * <p>All input fields are trimmed before validation. Validation rules are:</p>
     * <ul>
     *   <li>Full name must not be blank.</li>
     *   <li>Email must be a syntactically valid email address.</li>
     *   <li>Username must be 4–20 characters using letters, digits, {@code .}, {@code _}, or {@code -}.</li>
     *   <li>Password must be at least 6 characters.</li>
     *   <li>Password and confirm-password must match.</li>
     *   <li>Username and email must each be unique in the system.</li>
     * </ul>
     *
     * @param fullName        the applicant's full name
     * @param email           the applicant's email address
     * @param username        the desired login username
     * @param password        the desired password
     * @param confirmPassword password confirmation, must match {@code password}
     * @return the newly created {@link Applicant}
     * @throws IllegalArgumentException if any validation rule is violated
     */
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

    /**
     * Validates the registration input fields according to the defined rules.
     *
     * @param fullName        trimmed full name
     * @param email           trimmed email address
     * @param username        trimmed username
     * @param password        trimmed password
     * @param confirmPassword trimmed confirmation password
     * @throws IllegalArgumentException if any validation rule is violated
     */
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

    /**
     * Trims and null-safes a string value.
     *
     * @param value the raw input value
     * @return the trimmed string, or an empty string if {@code value} is {@code null}
     */
    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
