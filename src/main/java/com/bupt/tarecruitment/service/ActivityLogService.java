package com.bupt.tarecruitment.service;

import com.bupt.tarecruitment.model.ActivityLog;
import com.bupt.tarecruitment.model.User;
import com.bupt.tarecruitment.repository.ActivityLogRepository;
import com.bupt.tarecruitment.util.IdUtil;

import javax.servlet.ServletContext;
import java.time.LocalDateTime;
import java.util.List;

public class ActivityLogService {

    // ---- Action type constants ----
    public static final String APPLY_JOB = "APPLY_JOB";
    public static final String WITHDRAW_APPLICATION = "WITHDRAW_APPLICATION";
    public static final String CREATE_JOB = "CREATE_JOB";
    public static final String COMPLETE_JOB = "COMPLETE_JOB";
    public static final String REOPEN_JOB = "REOPEN_JOB";
    public static final String UPDATE_APPLICATION_STATUS = "UPDATE_APPLICATION_STATUS";
    public static final String CREATE_USER = "CREATE_USER";
    public static final String DELETE_USER = "DELETE_USER";

    private final ActivityLogRepository repository;

    public ActivityLogService(ServletContext servletContext) {
        this.repository = new ActivityLogRepository(servletContext);
    }

    /**
     * 通用记录方法
     */
    public void log(User operator, String actionType, String description,
                    String relatedObjectId, String beforeState, String afterState) {
        ActivityLog entry = new ActivityLog(
                IdUtil.generateId("log"),
                operator.getId(),
                operator.getFullName(),
                operator.getRole(),
                actionType,
                description,
                relatedObjectId,
                beforeState,
                afterState
        );
        try {
            repository.save(entry);
        } catch (Exception e) {
            // 日志写入失败不应影响主业务流程
            System.err.println("[ActivityLogService] Failed to write activity log: " + e.getMessage());
        }
    }

    // ---- 便捷方法（无状态变更） ----

    public void logApplyJob(User operator, String jobTitle, String applicationId) {
        log(operator, APPLY_JOB,
                "Applied for TA position: " + jobTitle,
                applicationId, null, null);
    }

    public void logWithdrawApplication(User operator, String jobTitle, String applicationId) {
        log(operator, WITHDRAW_APPLICATION,
                "Withdrew application for: " + jobTitle,
                applicationId, null, null);
    }

    public void logCreateJob(User operator, String jobTitle, String jobId) {
        log(operator, CREATE_JOB,
                "Posted new TA position: " + jobTitle,
                jobId, null, null);
    }

    public void logCompleteJob(User operator, String jobTitle, String jobId) {
        log(operator, COMPLETE_JOB,
                "Marked position as completed: " + jobTitle,
                jobId, "Open", "Completed");
    }

    public void logReopenJob(User operator, String jobTitle, String jobId) {
        log(operator, REOPEN_JOB,
                "Reopened position: " + jobTitle,
                jobId, "Completed", "Open");
    }

    public void logUpdateApplicationStatus(User operator, String applicantName,
                                            String jobTitle, String applicationId,
                                            String oldStatus, String newStatus) {
        log(operator, UPDATE_APPLICATION_STATUS,
                "Updated application status for " + applicantName + " on '" + jobTitle + "'",
                applicationId, oldStatus, newStatus);
    }

    public void logCreateUser(User operator, String newUserName, String newUserRole, String newUserId) {
        log(operator, CREATE_USER,
                "Created new user: " + newUserName + " (" + newUserRole + ")",
                newUserId, null, null);
    }

    public void logDeleteUser(User operator, String deletedUserName, String deletedUserRole, String deletedUserId) {
        log(operator, DELETE_USER,
                "Deleted user: " + deletedUserName + " (" + deletedUserRole + ")",
                deletedUserId, null, null);
    }

    // ---- 查询方法 ----

    public List<ActivityLog> getRecentByUser(String userId, int limit) {
        return repository.findRecentByUserId(userId, limit);
    }

    public List<ActivityLog> getAllByUser(String userId) {
        return repository.findByUserId(userId);
    }

    public List<ActivityLog> getAllByUserAndType(String userId, String actionType) {
        List<ActivityLog> all = repository.findByUserId(userId);
        if (actionType == null || actionType.isEmpty()) {
            return all;
        }
        List<ActivityLog> result = new java.util.ArrayList<>();
        for (ActivityLog log : all) {
            if (actionType.equals(log.getActionType())) {
                result.add(log);
            }
        }
        return result;
    }

    public List<ActivityLog> getFilteredGlobal(String userFullName, String actionType,
                                                String userRole, LocalDateTime fromTime,
                                                LocalDateTime toTime) {
        return repository.findByFilter(userFullName, actionType, userRole, fromTime, toTime);
    }
}
