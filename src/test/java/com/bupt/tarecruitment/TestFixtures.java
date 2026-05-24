package com.bupt.tarecruitment;

import com.bupt.tarecruitment.model.ActivityLog;
import com.bupt.tarecruitment.model.Admin;
import com.bupt.tarecruitment.model.Applicant;
import com.bupt.tarecruitment.model.ApplicationRecord;
import com.bupt.tarecruitment.model.Job;
import com.bupt.tarecruitment.model.MO;
import com.bupt.tarecruitment.model.Notification;
import com.bupt.tarecruitment.model.User;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Factory methods for deterministic model objects used by automated tests.
 *
 * <p>The fixtures keep repository and servlet tests readable while ensuring that
 * every test uses consistent user roles, credentials, job metadata, and timestamps.</p>
 */
public final class TestFixtures {
    private TestFixtures() {
    }

    /**
     * Creates an applicant with a valid profile and reusable password.
     *
     * @param id unique applicant ID
     * @param username login username
     * @return applicant fixture
     */
    public static Applicant applicant(String id, String username) {
        return new Applicant(id, username, "password123", "APPLICANT", username + " Applicant",
                username + "@example.com", List.of("Java", "Communication"), "Experienced teaching assistant candidate.");
    }

    /**
     * Creates a module organiser with a valid account profile.
     *
     * @param id unique MO ID
     * @param username login username
     * @return MO fixture
     */
    public static MO mo(String id, String username) {
        return new MO(id, username, "password123", "MO", username + " MO", username + "@example.com");
    }

    /**
     * Creates an administrator with a valid account profile.
     *
     * @param id unique administrator ID
     * @param username login username
     * @return admin fixture
     */
    public static Admin admin(String id, String username) {
        return new Admin(id, username, "password123", "ADMIN", username + " Admin", username + "@example.com");
    }

    /**
     * Creates a generic user with the supplied role.
     *
     * @param id unique user ID
     * @param username login username
     * @param role role label used by access-control tests
     * @return generic user fixture
     */
    public static User user(String id, String username, String role) {
        return new User(id, username, "password123", role, username + " User", username + "@example.com");
    }

    /**
     * Creates a TA job posting fixture.
     *
     * @param id unique job ID
     * @param moId ID of the posting module organiser
     * @param title job title
     * @param status job status such as {@code Open} or {@code Completed}
     * @return job fixture
     */
    public static Job job(String id, String moId, String title, String status) {
        return new Job(id, title, "Software Engineering", "Support labs and tutorials.",
                List.of("Java", "Testing"), 4, moId, status);
    }

    /**
     * Creates an application record fixture.
     *
     * @param id unique application ID
     * @param jobId target job ID
     * @param applicantId applicant user ID
     * @param status application status
     * @return application fixture
     */
    public static ApplicationRecord application(String id, String jobId, String applicantId, String status) {
        return new ApplicationRecord(id, jobId, applicantId, status, "2026-05-24 10:00");
    }

    /**
     * Creates a notification fixture with explicit read state and timestamp.
     *
     * @param id unique notification ID
     * @param userId recipient user ID
     * @param read whether the notification has been read
     * @param createdAt creation timestamp used for sorting assertions
     * @return notification fixture
     */
    public static Notification notification(String id, String userId, boolean read, LocalDateTime createdAt) {
        Notification notification = new Notification(id, userId, "TEST", "Test notification", null, null);
        notification.setRead(read);
        notification.setCreatedAt(createdAt);
        return notification;
    }

    /**
     * Creates an activity-log fixture with explicit operator metadata and timestamp.
     *
     * @param id unique log ID
     * @param userId operator user ID
     * @param fullName operator display name
     * @param role operator role
     * @param actionType activity action type
     * @param createdAt creation timestamp used for sorting and filtering assertions
     * @return activity-log fixture
     */
    public static ActivityLog activityLog(String id, String userId, String fullName, String role,
                                          String actionType, LocalDateTime createdAt) {
        ActivityLog log = new ActivityLog(id, userId, fullName, role, actionType,
                actionType + " description", "object-1", "before", "after");
        log.setCreatedAt(createdAt);
        return log;
    }
}
