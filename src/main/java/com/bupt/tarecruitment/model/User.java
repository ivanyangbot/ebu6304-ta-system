package com.bupt.tarecruitment.model;

/**
 * Represents a user account in the TA Recruitment System.
 *
 * <p>This is the base class for all user types. It stores common authentication
 * and identity information shared across {@link Applicant}, {@link MO}, and
 * {@link Admin} role subclasses.</p>
 *
 * <p>Roles are stored as a plain string constant (e.g. {@code "APPLICANT"},
 * {@code "MO"}, {@code "ADMIN"}) and are used throughout the system for
 * access-control decisions.</p>
 *
 * @author  Group 71
 * @version 1.0
 */
public class User {
    /** Unique identifier for this user (generated via {@link com.bupt.tarecruitment.util.IdUtil}). */
    private String id;

    /** Login username; must be unique across the system. */
    private String username;

    /** Plain-text password (stored directly in the JSON data file). */
    private String password;

    /**
     * Role of the user. Supported values:
     * <ul>
     *   <li>{@code "APPLICANT"} – TA applicant (student)</li>
     *   <li>{@code "MO"}       – Module Organiser</li>
     *   <li>{@code "ADMIN"}    – System administrator</li>
     * </ul>
     */
    private String role;

    /** Human-readable full name of the user. */
    private String fullName;

    /** Contact email address of the user. */
    private String email;

    /**
     * Default no-argument constructor required for JSON deserialization.
     */
    public User() {
    }

    /**
     * Constructs a fully-initialised {@code User} object.
     *
     * @param id       unique identifier
     * @param username login name
     * @param password account password
     * @param role     role string (e.g. {@code "APPLICANT"})
     * @param fullName display name
     * @param email    contact email
     */
    public User(String id, String username, String password, String role, String fullName, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
        this.email = email;
    }

    /**
     * Returns the unique identifier of this user.
     *
     * @return user ID string
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier of this user.
     *
     * @param id user ID string
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the login username.
     *
     * @return username string
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the login username.
     *
     * @param username new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the user's password.
     *
     * @return password string
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password.
     *
     * @param password new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the role assigned to this user.
     *
     * @return role string (e.g. {@code "APPLICANT"}, {@code "MO"}, {@code "ADMIN"})
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role for this user.
     *
     * @param role role string
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Returns the user's full name.
     *
     * @return full name string
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets the user's full name.
     *
     * @param fullName new full name
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Returns the user's email address.
     *
     * @return email string
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email address.
     *
     * @param email new email
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
