package com.bupt.tarecruitment.repository;

import com.bupt.tarecruitment.model.ActivityLog;
import com.bupt.tarecruitment.util.JsonFileUtil;
import com.bupt.tarecruitment.util.PathUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.servlet.ServletContext;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Data-access repository for {@link com.bupt.tarecruitment.model.ActivityLog} records.
 *
 * <p>Activity logs are appended to {@code activity_logs.json} and are sorted in
 * reverse chronological order on read. This repository provides both general
 * and user-scoped queries, as well as a rich multi-field filter used by the
 * administrator activity page.</p>
 *
 * <p>Write operations are synchronised on the class monitor to prevent concurrent
 * file corruption in a multi-threaded servlet environment.</p>
 *
 * @author  Group 71
 * @version 1.0
 * @see     com.bupt.tarecruitment.service.ActivityLogService
 */
public class ActivityLogRepository {
    private final Path filePath;
    private final Gson gson;

    public ActivityLogRepository(ServletContext servletContext) {
        this.filePath = PathUtil.getDataFilePath(servletContext, "activity_logs.json");
        this.gson = JsonFileUtil.getGson();
    }

    /**
     * Appends a new activity log entry to the JSON store.
     *
     * @param log the log entry to persist; must not be {@code null}
     */
    public void save(ActivityLog log) {
        synchronized (ActivityLogRepository.class) {
            List<ActivityLog> logs = findAll();
            logs.add(log);
            JsonFileUtil.writeJson(filePath, logs);
        }
    }

    /**
     * Returns all activity log entries sorted in reverse chronological order
     * (newest first).
     *
     * @return list of all {@link com.bupt.tarecruitment.model.ActivityLog} records
     */
    public List<ActivityLog> findAll() {
        JsonArray jsonArray = JsonFileUtil.readJsonArray(filePath);
        List<ActivityLog> logs = new ArrayList<>();

        for (JsonElement element : jsonArray) {
            JsonObject obj = element.getAsJsonObject();
            ActivityLog log = new ActivityLog();
            log.setId(getStr(obj, "id"));
            log.setUserId(getStr(obj, "userId"));
            log.setUserFullName(getStr(obj, "userFullName"));
            log.setUserRole(getStr(obj, "userRole"));
            log.setActionType(getStr(obj, "actionType"));
            log.setDescription(getStr(obj, "description"));
            log.setRelatedObjectId(getStr(obj, "relatedObjectId"));
            log.setBeforeState(getStr(obj, "beforeState"));
            log.setAfterState(getStr(obj, "afterState"));
            if (obj.has("createdAt") && !obj.get("createdAt").isJsonNull()) {
                log.setCreatedAt(gson.fromJson(obj.get("createdAt"), LocalDateTime.class));
            } else {
                log.setCreatedAt(LocalDateTime.now());
            }
            logs.add(log);
        }

        logs.sort(Comparator.comparing(ActivityLog::getCreatedAt).reversed());
        return logs;
    }

    /**
     * Returns all activity logs for a specific user, newest first.
     *
     * @param userId the user ID to filter by
     * @return list of matching log entries; never {@code null}
     */
    public List<ActivityLog> findByUserId(String userId) {
        List<ActivityLog> result = new ArrayList<>();
        for (ActivityLog log : findAll()) {
            if (userId.equals(log.getUserId())) {
                result.add(log);
            }
        }
        return result;
    }

    /**
     * Returns the most recent {@code limit} activity log entries for a specific user.
     * Results are already in reverse chronological order.
     *
     * @param userId the user ID to filter by
     * @param limit  maximum number of entries to return
     * @return list of at most {@code limit} log entries; never {@code null}
     */
    public List<ActivityLog> findRecentByUserId(String userId, int limit) {
        List<ActivityLog> all = findByUserId(userId);
        return all.size() <= limit ? all : new ArrayList<>(all.subList(0, limit));
    }

    /**
     * Filters activity logs globally using optional criteria (admin use).
     * Each parameter is optional: passing {@code null} or an empty string means
     * "no filter on this field".
     *
     * @param userFullName case-insensitive substring match on the operator's full name
     * @param actionType   exact match on the action type constant
     * @param userRole     case-insensitive exact match on the operator's role
     * @param fromTime     only return logs created at or after this time; {@code null} means no lower bound
     * @param toTime       only return logs created at or before this time; {@code null} means no upper bound
     * @return filtered list of log entries in reverse chronological order
     */
    public List<ActivityLog> findByFilter(String userFullName, String actionType,
                                           String userRole, LocalDateTime fromTime, LocalDateTime toTime) {
        List<ActivityLog> result = new ArrayList<>();
        for (ActivityLog log : findAll()) {
            if (userFullName != null && !userFullName.isEmpty()
                    && (log.getUserFullName() == null || !log.getUserFullName().toLowerCase().contains(userFullName.toLowerCase()))) {
                continue;
            }
            if (actionType != null && !actionType.isEmpty()
                    && !actionType.equals(log.getActionType())) {
                continue;
            }
            if (userRole != null && !userRole.isEmpty()
                    && (log.getUserRole() == null || !userRole.equalsIgnoreCase(log.getUserRole()))) {
                continue;
            }
            if (fromTime != null && log.getCreatedAt().isBefore(fromTime)) {
                continue;
            }
            if (toTime != null && log.getCreatedAt().isAfter(toTime)) {
                continue;
            }
            result.add(log);
        }
        return result;
    }

    private String getStr(JsonObject obj, String key) {
        if (obj.has(key) && !obj.get(key).isJsonNull()) {
            return obj.get(key).getAsString();
        }
        return null;
    }
}
