package com.bupt.tarecruitment.model;

import java.time.LocalDateTime;

public class ActivityLog {
    private String id;
    private String userId;
    private String userFullName;
    private String userRole;
    private String actionType;
    private String description;
    private String relatedObjectId;
    private String beforeState;
    private String afterState;
    private LocalDateTime createdAt;

    public ActivityLog() {
    }

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

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRelatedObjectId() {
        return relatedObjectId;
    }

    public void setRelatedObjectId(String relatedObjectId) {
        this.relatedObjectId = relatedObjectId;
    }

    public String getBeforeState() {
        return beforeState;
    }

    public void setBeforeState(String beforeState) {
        this.beforeState = beforeState;
    }

    public String getAfterState() {
        return afterState;
    }

    public void setAfterState(String afterState) {
        this.afterState = afterState;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
