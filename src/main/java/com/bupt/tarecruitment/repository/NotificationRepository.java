package com.bupt.tarecruitment.repository;

import com.bupt.tarecruitment.model.Notification;
import com.bupt.tarecruitment.util.IdUtil;
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

public class NotificationRepository {
    private final Path filePath;
    private final Gson gson;

    public NotificationRepository(ServletContext servletContext) {
        this.filePath = PathUtil.getDataFilePath(servletContext, "notifications.json");
        this.gson = JsonFileUtil.getGson();
    }

    public void createNotification(Notification notification) {
        synchronized (NotificationRepository.class) {
            List<Notification> notifications = findAll();
            notifications.add(notification);
            JsonFileUtil.writeJson(filePath, notifications);
        }
    }

    public List<Notification> findAll() {
        JsonArray jsonArray = JsonFileUtil.readJsonArray(filePath);
        List<Notification> notifications = new ArrayList<>();

        for (JsonElement element : jsonArray) {
            JsonObject object = element.getAsJsonObject();
            Notification notification = new Notification();
            notification.setId(object.get("id").getAsString());
            notification.setUserId(object.get("userId").getAsString());
            notification.setType(object.get("type").getAsString());
            notification.setMessage(object.get("message").getAsString());
            notification.setRelatedJobId(object.has("relatedJobId") ? object.get("relatedJobId").getAsString() : null);
            notification.setRelatedApplicationId(object.has("relatedApplicationId") ? object.get("relatedApplicationId").getAsString() : null);
            notification.setRead(object.has("read") && object.get("read").getAsBoolean());
            if (object.has("createdAt") && !object.get("createdAt").isJsonNull()) {
                notification.setCreatedAt(gson.fromJson(object.get("createdAt"), LocalDateTime.class));
            } else {
                notification.setCreatedAt(LocalDateTime.now());
            }
            notifications.add(notification);
        }

        notifications.sort(Comparator.comparing(Notification::getCreatedAt).reversed());
        return notifications;
    }

    public List<Notification> findByUserId(String userId) {
        List<Notification> allNotifications = findAll();
        List<Notification> userNotifications = new ArrayList<>();
        for (Notification notification : allNotifications) {
            if (notification.getUserId().equals(userId)) {
                userNotifications.add(notification);
            }
        }
        return userNotifications;
    }

    public List<Notification> findUnreadByUserId(String userId) {
        List<Notification> userNotifications = findByUserId(userId);
        List<Notification> unreadNotifications = new ArrayList<>();
        for (Notification notification : userNotifications) {
            if (!notification.isRead()) {
                unreadNotifications.add(notification);
            }
        }
        return unreadNotifications;
    }

    public int countUnreadByUserId(String userId) {
        return findUnreadByUserId(userId).size();
    }

    public void markAsRead(String notificationId) {
        synchronized (NotificationRepository.class) {
            List<Notification> notifications = findAll();
            for (Notification notification : notifications) {
                if (notification.getId().equals(notificationId)) {
                    notification.setRead(true);
                    JsonFileUtil.writeJson(filePath, notifications);
                    return;
                }
            }
        }
    }

    public void markAllAsRead(String userId) {
        synchronized (NotificationRepository.class) {
            List<Notification> notifications = findAll();
            boolean updated = false;
            for (Notification notification : notifications) {
                if (notification.getUserId().equals(userId) && !notification.isRead()) {
                    notification.setRead(true);
                    updated = true;
                }
            }
            if (updated) {
                JsonFileUtil.writeJson(filePath, notifications);
            }
        }
    }

    public void deleteNotification(String notificationId) {
        synchronized (NotificationRepository.class) {
            List<Notification> notifications = findAll();
            boolean removed = notifications.removeIf(n -> n.getId().equals(notificationId));
            if (removed) {
                JsonFileUtil.writeJson(filePath, notifications);
            }
        }
    }

    public void createApplicationStatusNotification(String applicantId, String jobTitle, String newStatus, String applicationId) {
        String message = String.format("Your application for '%s' has been updated to: %s", jobTitle, newStatus);
        Notification notification = new Notification(
                IdUtil.generateId("notification"),
                applicantId,
                "APPLICATION_STATUS",
                message,
                null,
                applicationId
        );
        createNotification(notification);
    }

    public void createNewApplicationNotification(String moId, String jobTitle, String applicantName, String applicationId, String jobId) {
        String message = String.format("New application received for '%s' from %s", jobTitle, applicantName);
        Notification notification = new Notification(
                IdUtil.generateId("notification"),
                moId,
                "NEW_APPLICATION",
                message,
                jobId,
                applicationId
        );
        createNotification(notification);
    }
}