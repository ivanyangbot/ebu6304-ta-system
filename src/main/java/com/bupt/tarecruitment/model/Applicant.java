package com.bupt.tarecruitment.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a TA applicant (student) in the recruitment system.
 *
 * <p>An {@code Applicant} extends {@link User} with two additional profile
 * fields: a list of skills used for job matching and a free-text
 * self-introduction. The role is always set to {@code "APPLICANT"}.</p>
 *
 * <p>Skills are stored as a {@code List<String>} and are compared
 * case-insensitively by {@link com.bupt.tarecruitment.service.MatchService}
 * to produce a match score.</p>
 *
 * @author  Group 71
 * @version 1.0
 * @see     com.bupt.tarecruitment.service.MatchService
 * @see     com.bupt.tarecruitment.service.AuthService
 */
public class Applicant extends User {

    /** Skills declared by the applicant (e.g. "Java", "Python"). */
    private List<String> skills;

    /** Optional free-text self-introduction shown on the profile page. */
    private String selfIntroduction;

    /**
     * Default no-argument constructor required for JSON deserialization.
     * Initialises {@code skills} to an empty list and {@code selfIntroduction} to an empty string.
     */
    public Applicant() {
        this.skills = new ArrayList<>();
        this.selfIntroduction = "";
    }

    /**
     * Convenience constructor without a self-introduction (defaults to empty string).
     *
     * @param id       unique identifier
     * @param username login name
     * @param password account password
     * @param role     role string (should be {@code "APPLICANT"})
     * @param fullName display name
     * @param email    contact email
     * @param skills   list of skill strings; {@code null} is treated as empty list
     */
    public Applicant(String id, String username, String password, String role, String fullName, String email,
                     List<String> skills) {
        this(id, username, password, role, fullName, email, skills, "");
    }

    /**
     * Full constructor.
     *
     * @param id                unique identifier
     * @param username          login name
     * @param password          account password
     * @param role              role string (should be {@code "APPLICANT"})
     * @param fullName          display name
     * @param email             contact email
     * @param skills            list of skill strings; {@code null} is treated as empty list
     * @param selfIntroduction  free-text introduction; {@code null} is treated as empty string
     */
    public Applicant(String id, String username, String password, String role, String fullName, String email,
                     List<String> skills, String selfIntroduction) {
        super(id, username, password, role, fullName, email);
        this.skills = skills == null ? new ArrayList<>() : skills;
        this.selfIntroduction = selfIntroduction == null ? "" : selfIntroduction;
    }

    /**
     * Returns the list of skills declared by this applicant.
     * Lazily initialises the list if it was somehow set to {@code null}.
     *
     * @return non-null list of skill strings
     */
    public List<String> getSkills() {
        if (skills == null) {
            skills = new ArrayList<>();
        }
        return skills;
    }

    /**
     * Replaces the applicant's skill list.
     *
     * @param skills new skill list; {@code null} is treated as empty list
     */
    public void setSkills(List<String> skills) {
        this.skills = skills == null ? new ArrayList<>() : skills;
    }

    /**
     * Returns the applicant's self-introduction text.
     * Lazily initialises the field if it was somehow set to {@code null}.
     *
     * @return non-null self-introduction string (may be empty)
     */
    public String getSelfIntroduction() {
        if (selfIntroduction == null) {
            selfIntroduction = "";
        }
        return selfIntroduction;
    }

    /**
     * Sets the applicant's self-introduction text.
     *
     * @param selfIntroduction new self-introduction; {@code null} is treated as empty string
     */
    public void setSelfIntroduction(String selfIntroduction) {
        this.selfIntroduction = selfIntroduction == null ? "" : selfIntroduction;
    }
}
