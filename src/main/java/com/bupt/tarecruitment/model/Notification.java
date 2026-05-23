package com.bupt.tarecruitment.model;

import java.time.LocalDateTime;

/**
 * Represents an in-system notification delivered to a user.
 *
 * <p>Notifications are generated automatically when significant events occur,
 * such as a change in application status. They are stored in
 * {@code notifications.json} and displayed on the user's dashboard until they
 * are deleted.</p>
 *
 * <p>A notification can optionally be linked to a job or an application record
 * via {@link #relatedJobId} and {@link #relatedApplicationId}, enabling
 * deep-links in the UI.</p>
 *
 * @author  Group 71
 * @version 1.0
 * @see     com.bupt.tarecruitment.repository.NotificationRepository
 */
public class Notification {

    /** Unique identifier for this notification. */
    private String id;

    /** ID of the user who should receive this notification. */
    private String userId;

    /**
     * Category of the notification (e.g. {@code "APPLICATION_STATUS_UPDATED"},
     * {@code "JOB_POSTED"}).
     */
    private String type;

    /** Human-readable notification message. */
    private String message;

    /** ID of the related job, or {@code null} if not applicable. */
    private String relatedJobId;

    /** ID of the related application record, or {@code null} if not applicable. */
    private String relatedApplicationId;

    /** Whether the user has read/acknowledged this notification. */
    private boolean read;

    /** Timestamp of when this notification was created. */
    private LocalDateTime createdAt;

    /**
     * Default no-argument constructor required for JSON deserialization.
     */
    public Notification() {
    }

    /**
     * Constructs a new unread notification with the current timestamp.
     *
     * @param id                    unique identifier
     * @param userId                recipient user ID
     * @param type                  notification type string
     * @param message               notification message text
     * @param relatedJobId          related job ID (may be {@code null})
     * @param relatedApplicationId  related application ID (may be {@code null})
     */
    public Notification(String id, String userId, String type, String message,
                       String relatedJobId, String relatedApplicationId) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.message = message;
        this.relatedJobId = relatedJobId;
        this.relatedApplicationId = relatedApplicationId;
        this.read = false;
        this.createdAt = LocalDateTime.now();
    }

    /** @return unique identifier */
    public String getId() { return id; }
    /** @param id unique identifier */
    public void setId(String id) { this.id = id; }

    /** @return recipient user ID */
    public String getUserId() { return userId; }
    /** @param userId recipient user ID */
    public void setUserId(String userId) { this.userId = userId; }

    /** @return notification type string */
    public String getType() { return type; }
    /** @param type notification type */
    public void setType(String type) { this.type = type; }

    /** @return notification message */
    public String getMessage() { return message; }
    /** @param message notification message */
    public void setMessage(String message) { this.message = message; }

    /** @return related job ID, or {@code null} */
    public String getRelatedJobId() { return relatedJobId; }
    /** @param relatedJobId related job ID */
    public void setRelatedJobId(String relatedJobId) { this.relatedJobId = relatedJobId; }

    /** @return related application ID, or {@code null} */
    public String getRelatedApplicationId() { return relatedApplicationId; }
    /** @param relatedApplicationId related application ID */
    public void setRelatedApplicationId(String relatedApplicationId) { this.relatedApplicationId = relatedApplicationId; }

    /** @return {@code true} if the user has read this notification */
    public boolean isRead() { return read; }
    /** @param read read/unread flag */
    public void setRead(boolean read) { this.read = read; }

    /** @return creation timestamp */
    public LocalDateTime getCreatedAt() { return createdAt; }
    /** @param createdAt creation timestamp */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
