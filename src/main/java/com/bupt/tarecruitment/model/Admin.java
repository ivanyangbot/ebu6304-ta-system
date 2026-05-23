package com.bupt.tarecruitment.model;

/**
 * Represents an administrator user in the TA Recruitment System.
 *
 * <p>An {@code Admin} can perform privileged operations such as creating and
 * deleting user accounts, viewing the workload of all applicants, and
 * inspecting the system-wide activity log. The role is always set to
 * {@code "ADMIN"}.</p>
 *
 * <p>This class extends {@link User} and currently adds no additional fields;
 * the specialised behaviour for admin users is handled at the servlet and
 * service layers via role-based access control.</p>
 *
 * @author  Group 71
 * @version 1.0
 * @see     com.bupt.tarecruitment.servlet.AdminWorkloadServlet
 * @see     com.bupt.tarecruitment.servlet.AdminUserManagementServlet
 * @see     com.bupt.tarecruitment.servlet.AdminActivityServlet
 */
public class Admin extends User {

    /**
     * Default no-argument constructor required for JSON deserialization.
     */
    public Admin() {
    }

    /**
     * Constructs a fully-initialised {@code Admin} object.
     *
     * @param id       unique identifier
     * @param username login name
     * @param password account password
     * @param role     role string (should be {@code "ADMIN"})
     * @param fullName display name
     * @param email    contact email
     */
    public Admin(String id, String username, String password, String role, String fullName, String email) {
        super(id, username, password, role, fullName, email);
    }
}
