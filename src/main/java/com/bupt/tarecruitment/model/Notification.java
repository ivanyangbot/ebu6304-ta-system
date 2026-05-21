package com.bupt.tarecruitment.model;

import java.time.LocalDateTime;

public class Notification {
    private String id;
    private String userId;
    private String type;
    private String message;
    private String relatedJobId;
    private String relatedApplicationId;
    private boolean read;
    private LocalDateTime createdAt;

    public Notification() {
    }

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRelatedJobId() {
        return relatedJobId;
    }

    public void setRelatedJobId(String relatedJobId) {
        this.relatedJobId = relatedJobId;
    }

    public String getRelatedApplicationId() {
        return relatedApplicationId;
    }

    public void setRelatedApplicationId(String relatedApplicationId) {
        this.relatedApplicationId = relatedApplicationId;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}