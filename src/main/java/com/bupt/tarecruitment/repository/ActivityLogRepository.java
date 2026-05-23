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

public class ActivityLogRepository {
    private final Path filePath;
    private final Gson gson;

    public ActivityLogRepository(ServletContext servletContext) {
        this.filePath = PathUtil.getDataFilePath(servletContext, "activity_logs.json");
        this.gson = JsonFileUtil.getGson();
    }

    public void save(ActivityLog log) {
        synchronized (ActivityLogRepository.class) {
            List<ActivityLog> logs = findAll();
            logs.add(log);
            JsonFileUtil.writeJson(filePath, logs);
        }
    }

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
     * 查询某用户最新的 N 条日志（已按时间倒序）
     */
    public List<ActivityLog> findRecentByUserId(String userId, int limit) {
        List<ActivityLog> all = findByUserId(userId);
        return all.size() <= limit ? all : new ArrayList<>(all.subList(0, limit));
    }

    /**
     * 全局筛选（Admin 用）：所有条件可选，传 null 或空字符串表示不筛选
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
