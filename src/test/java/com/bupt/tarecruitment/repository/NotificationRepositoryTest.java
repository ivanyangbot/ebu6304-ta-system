package com.bupt.tarecruitment.repository;

import com.bupt.tarecruitment.JsonTestData;
import com.bupt.tarecruitment.TestFixtures;
import com.bupt.tarecruitment.model.Notification;
import com.bupt.tarecruitment.util.PathUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.servlet.ServletContext;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link NotificationRepository} using temporary notification data.
 *
 * <p>The tests cover notification sorting, user-scoped queries, read-state updates,
 * deletion, and generated application-notification helper methods.</p>
 */
class NotificationRepositoryTest {
    @TempDir
    Path dataDir;

    private NotificationRepository repository;

    /**
     * Seeds {@code notifications.json} with deterministic timestamps and users.
     */
    @BeforeEach
    void setUp() {
        Notification olderUnread = TestFixtures.notification("n-1", "user-1", false, LocalDateTime.of(2026, 5, 20, 9, 0));
        Notification newestRead = TestFixtures.notification("n-2", "user-1", true, LocalDateTime.of(2026, 5, 22, 9, 0));
        Notification middleUnread = TestFixtures.notification("n-3", "user-2", false, LocalDateTime.of(2026, 5, 21, 9, 0));
        JsonTestData.writeNotifications(dataDir, List.of(olderUnread, newestRead, middleUnread));
        ServletContext context = mock(ServletContext.class);
        when(context.getAttribute(PathUtil.DATA_DIR_ATTRIBUTE)).thenReturn(dataDir.toString());
        repository = new NotificationRepository(context);
    }

    /**
     * Verifies that notifications are returned newest first.
     */
    @Test
    void findAll_sortsNewestFirst() {
        List<Notification> notifications = repository.findAll();

        assertEquals(List.of("n-2", "n-3", "n-1"), notifications.stream().map(Notification::getId).toList());
    }

    /**
     * Verifies user filtering and unread counting behaviour.
     */
    @Test
    void userQueries_returnOnlyMatchingUnreadNotifications() {
        assertEquals(2, repository.findByUserId("user-1").size());
        assertEquals(1, repository.findUnreadByUserId("user-1").size());
        assertEquals(1, repository.countUnreadByUserId("user-1"));
    }

    /**
     * Verifies that marking one notification as read does not affect other users.
     */
    @Test
    void markAsRead_updatesOnlySelectedNotification() {
        repository.markAsRead("n-1");

        assertTrue(repository.findById("n-1").isRead());
        assertFalse(repository.findById("n-3").isRead());
    }

    /**
     * Verifies bulk read updates are scoped to the selected recipient.
     */
    @Test
    void markAllAsRead_updatesOnlySelectedUser() {
        repository.markAllAsRead("user-1");

        assertEquals(0, repository.countUnreadByUserId("user-1"));
        assertEquals(1, repository.countUnreadByUserId("user-2"));
    }

    /**
     * Verifies that deleting a notification removes only that record.
     */
    @Test
    void deleteNotification_removesSelectedNotification() {
        repository.deleteNotification("n-2");

        assertNull(repository.findById("n-2"));
        assertEquals(2, repository.findAll().size());
    }

    /**
     * Verifies generated applicant status notifications contain the expected metadata.
     */
    @Test
    void createApplicationStatusNotification_persistsExpectedFields() {
        repository.createApplicationStatusNotification("applicant-1", "Lab TA", "Accepted", "application-1");

        Notification created = repository.findByUserId("applicant-1").get(0);
        assertEquals("APPLICATION_STATUS", created.getType());
        assertEquals("Your application for 'Lab TA' has been updated to: Accepted", created.getMessage());
        assertNull(created.getRelatedJobId());
        assertEquals("application-1", created.getRelatedApplicationId());
    }

    /**
     * Verifies generated MO notifications link to both the job and application.
     */
    @Test
    void createNewApplicationNotification_persistsExpectedFields() {
        repository.createNewApplicationNotification("mo-1", "Lab TA", "Alice", "application-2", "job-1");

        Notification created = repository.findByUserId("mo-1").get(0);
        assertEquals("NEW_APPLICATION", created.getType());
        assertEquals("New application received for 'Lab TA' from Alice", created.getMessage());
        assertEquals("job-1", created.getRelatedJobId());
        assertEquals("application-2", created.getRelatedApplicationId());
    }
}
