package com.bupt.tarecruitment.model;

/**
 * Represents a Module Organiser (MO) user in the TA Recruitment System.
 *
 * <p>A Module Organiser is a staff member who can create and manage TA job
 * postings for their modules, review applications, and update applicant
 * statuses. The role is always set to {@code "MO"}.</p>
 *
 * <p>This class extends {@link User} and currently adds no additional fields;
 * the specialised behaviour for MO users is handled at the servlet and service
 * layers via role-based access control.</p>
 *
 * @author  Group 71
 * @version 1.0
 * @see     com.bupt.tarecruitment.servlet.MOJobsServlet
 * @see     com.bupt.tarecruitment.servlet.MOApplicationsServlet
 */
public class MO extends User {

    /**
     * Default no-argument constructor required for JSON deserialization.
     */
    public MO() {
    }

    /**
     * Constructs a fully-initialised {@code MO} object.
     *
     * @param id       unique identifier
     * @param username login name
     * @param password account password
     * @param role     role string (should be {@code "MO"})
     * @param fullName display name
     * @param email    contact email
     */
    public MO(String id, String username, String password, String role, String fullName, String email) {
        super(id, username, password, role, fullName, email);
    }
}
