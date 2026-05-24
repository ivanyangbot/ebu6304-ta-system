package com.bupt.tarecruitment;

import com.bupt.tarecruitment.model.ActivityLog;
import com.bupt.tarecruitment.model.ApplicationRecord;
import com.bupt.tarecruitment.model.Job;
import com.bupt.tarecruitment.model.Notification;
import com.bupt.tarecruitment.model.User;
import com.bupt.tarecruitment.util.JsonFileUtil;

import java.nio.file.Path;
import java.util.List;

/**
 * Writes temporary JSON data files for repository and servlet tests.
 *
 * <p>Each method mirrors a production data file name so tests can point
 * {@code PathUtil.DATA_DIR_ATTRIBUTE} at a JUnit {@code @TempDir} and exercise
 * real repository code without touching deployed application data.</p>
 */
public final class JsonTestData {
    private JsonTestData() {
    }

    /**
     * Creates all core JSON data files as empty arrays in the supplied directory.
     *
     * @param dataDir temporary data directory used by the test
     */
    public static void writeEmptyCoreFiles(Path dataDir) {
        writeUsers(dataDir, List.of());
        writeJobs(dataDir, List.of());
        writeApplications(dataDir, List.of());
        writeNotifications(dataDir, List.of());
        writeActivityLogs(dataDir, List.of());
    }

    /**
     * Writes {@code users.json}.
     *
     * @param dataDir temporary data directory
     * @param users users to persist
     */
    public static void writeUsers(Path dataDir, List<? extends User> users) {
        JsonFileUtil.writeJson(dataDir.resolve("users.json"), users);
    }

    /**
     * Writes {@code jobs.json}.
     *
     * @param dataDir temporary data directory
     * @param jobs jobs to persist
     */
    public static void writeJobs(Path dataDir, List<Job> jobs) {
        JsonFileUtil.writeJson(dataDir.resolve("jobs.json"), jobs);
    }

    /**
     * Writes {@code applications.json}.
     *
     * @param dataDir temporary data directory
     * @param applications application records to persist
     */
    public static void writeApplications(Path dataDir, List<ApplicationRecord> applications) {
        JsonFileUtil.writeJson(dataDir.resolve("applications.json"), applications);
    }

    /**
     * Writes {@code notifications.json}.
     *
     * @param dataDir temporary data directory
     * @param notifications notifications to persist
     */
    public static void writeNotifications(Path dataDir, List<Notification> notifications) {
        JsonFileUtil.writeJson(dataDir.resolve("notifications.json"), notifications);
    }

    /**
     * Writes {@code activity_logs.json}.
     *
     * @param dataDir temporary data directory
     * @param logs activity logs to persist
     */
    public static void writeActivityLogs(Path dataDir, List<ActivityLog> logs) {
        JsonFileUtil.writeJson(dataDir.resolve("activity_logs.json"), logs);
    }
}
