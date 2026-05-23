package com.bupt.tarecruitment.model;

import java.time.LocalDateTime;

/**
 * Represents a single auditable activity event in the TA Recruitment System.
 *
 * <p>Activity logs are written automatically by
 * {@link com.bupt.tarecruitment.service.ActivityLogService} whenever a
 * significant user action occurs (e.g. applying for a job, updating an
 * application status). They are stored in {@code activity_logs.json} and can
 * be queried by individual users or filtered globally by administrators.</p>
 *
 * <p>Each log entry captures the operator, the type of action, a human-readable
 * description, the related domain object, and optional before/after state
 * snapshots to support audit trails.</p>
 *
 * @author  Group 71
 * @version 1.0
 * @see     com.bupt.tarecruitment.service.ActivityLogService
 * @see     com.bupt.tarecruitment.repository.ActivityLogRepository
 */
public class ActivityLog {

    /** Unique identifier for this log entry. */
    private String id;

    /** ID of the user who performed the action. */
    private String userId;

    /** Full name of the user at the time of the action. */
    private String userFullName;

    /** Role of the user at the time of the action (e.g. {@code "APPLICANT"}, {@code "MO"}). */
    private String userRole;

    /**
     * Type of action performed. Common values are defined as constants in
     * {@link com.bupt.tarecruitment.service.ActivityLogService}
     * (e.g. {@code "APPLY_JOB"}, {@code "UPDATE_APPLICATION_STATUS"}).
     */
    private String actionType;

    /** Human-readable description of the action. */
    private String description;

    /**
     * ID of the domain object that was acted upon (e.g. a job ID or
     * application ID). May be {@code null} when not applicable.
     */
    private String relatedObjectId;

    /**
     * Snapshot of the state before the action was taken.
     * May be {@code null} for actions without a meaningful prior state.
     */
    private String beforeState;

    /**
     * Snapshot of the state after the action was taken.
     * May be {@code null} for actions without a meaningful result state.
     */
    private String afterState;

    /** Timestamp of when this log entry was created. */
    private LocalDateTime createdAt;

    /**
     * Default no-argument constructor required for JSON deserialization.
     */
    public ActivityLog() {
    }

    /**
     * Full constructor. Sets {@link #createdAt} to the current date-time.
     *
     * @param id              unique identifier
     * @param userId          operator's user ID
     * @param userFullName    operator's full name
     * @param userRole        operator's role string
     * @param actionType      action type constant
     * @param description     human-readable description
     * @param relatedObjectId ID of the affected domain object (may be {@code null})
     * @param beforeState     state before the action (may be {@code null})
     * @param afterState      state after the action (may be {@code null})
     */
    public ActivityLog(String id, String userId, String userFullName, String userRole,
                       String actionType, String description, String relatedObjectId,
                       String beforeState, String afterState) {
        this.id = id;
        this.userId = userId;
        this.userFullName = userFullName;
        this.userRole = userRole;
        this.actionType = actionType;
        this.description = description;
        this.relatedObjectId = relatedObjectId;
        this.beforeState = beforeState;
        this.afterState = afterState;
        this.createdAt = LocalDateTime.now();
    }

    /** @return unique identifier of this log entry */
    public String getId() { return id; }
    /** @param id unique identifier */
    public void setId(String id) { this.id = id; }

    /** @return ID of the user who performed the action */
    public String getUserId() { return userId; }
    /** @param userId operator's user ID */
    public void setUserId(String userId) { this.userId = userId; }

    /** @return full name of the operator */
    public String getUserFullName() { return userFullName; }
    /** @param userFullName operator's full name */
    public void setUserFullName(String userFullName) { this.userFullName = userFullName; }

    /** @return role of the operator */
    public String getUserRole() { return userRole; }
    /** @param userRole operator's role string */
    public void setUserRole(String userRole) { this.userRole = userRole; }

    /** @return action type constant string */
    public String getActionType() { return actionType; }
    /** @param actionType action type constant */
    public void setActionType(String actionType) { this.actionType = actionType; }

    /** @return human-readable description of the action */
    public String getDescription() { return description; }
    /** @param description new description */
    public void setDescription(String description) { this.description = description; }

    /** @return ID of the affected domain object, or {@code null} */
    public String getRelatedObjectId() { return relatedObjectId; }
    /** @param relatedObjectId ID of the affected object */
    public void setRelatedObjectId(String relatedObjectId) { this.relatedObjectId = relatedObjectId; }

    /** @return state before the action, or {@code null} */
    public String getBeforeState() { return beforeState; }
    /** @param beforeState before-state snapshot */
    public void setBeforeState(String beforeState) { this.beforeState = beforeState; }

    /** @return state after the action, or {@code null} */
    public String getAfterState() { return afterState; }
    /** @param afterState after-state snapshot */
    public void setAfterState(String afterState) { this.afterState = afterState; }

    /** @return timestamp of log creation */
    public LocalDateTime getCreatedAt() { return createdAt; }
    /** @param createdAt creation timestamp */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
