package com.bupt.tarecruitment.repository;

import com.bupt.tarecruitment.JsonTestData;
import com.bupt.tarecruitment.TestFixtures;
import com.bupt.tarecruitment.model.ActivityLog;
import com.bupt.tarecruitment.util.PathUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.servlet.ServletContext;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link ActivityLogRepository} using temporary audit-log data.
 *
 * <p>The tests verify chronological ordering, append persistence, user-scoped
 * queries, recent-log limits, and administrator filter criteria.</p>
 */
class ActivityLogRepositoryTest {
    @TempDir
    Path dataDir;

    private ActivityLogRepository repository;

    /**
     * Seeds {@code activity_logs.json} and binds the repository to the temp directory.
     */
    @BeforeEach
    void setUp() {
        JsonTestData.writeActivityLogs(dataDir, List.of(
                TestFixtures.activityLog("log-1", "user-1", "Alice Wang", "APPLICANT", "APPLY_JOB",
                        LocalDateTime.of(2026, 5, 20, 9, 0)),
                TestFixtures.activityLog("log-2", "user-1", "Alice Wang", "APPLICANT", "WITHDRAW_APPLICATION",
                        LocalDateTime.of(2026, 5, 22, 9, 0)),
                TestFixtures.activityLog("log-3", "user-2", "Bob Li", "MO", "UPDATE_APPLICATION_STATUS",
                        LocalDateTime.of(2026, 5, 21, 9, 0))
        ));
        ServletContext context = mock(ServletContext.class);
        when(context.getAttribute(PathUtil.DATA_DIR_ATTRIBUTE)).thenReturn(dataDir.toString());
        repository = new ActivityLogRepository(context);
    }

    /**
     * Verifies that all logs are sorted in reverse chronological order.
     */
    @Test
    void findAll_sortsNewestFirst() {
        List<ActivityLog> logs = repository.findAll();

        assertEquals(List.of("log-2", "log-3", "log-1"), logs.stream().map(ActivityLog::getId).toList());
    }

    /**
     * Verifies that saving appends a log and preserves newest-first ordering.
     */
    @Test
    void save_appendsLog() {
        repository.save(TestFixtures.activityLog("log-4", "user-3", "Chen Admin", "ADMIN", "CREATE_USER",
                LocalDateTime.of(2026, 5, 23, 9, 0)));

        assertEquals(4, repository.findAll().size());
        assertEquals("log-4", repository.findAll().get(0).getId());
    }

    /**
     * Verifies user-specific audit-log filtering.
     */
    @Test
    void findByUserId_filtersByOperator() {
        assertEquals(List.of("log-2", "log-1"), repository.findByUserId("user-1").stream().map(ActivityLog::getId).toList());
    }

    /**
     * Verifies recent-log queries respect the requested limit.
     */
    @Test
    void findRecentByUserId_appliesLimit() {
        assertEquals(List.of("log-2"), repository.findRecentByUserId("user-1", 1).stream().map(ActivityLog::getId).toList());
    }

    /**
     * Verifies administrator filtering by name, action type, role, and timestamp range.
     */
    @Test
    void findByFilter_filtersByNameActionRoleAndTimeRange() {
        List<ActivityLog> byName = repository.findByFilter("alice", null, null, null, null);
        List<ActivityLog> byAction = repository.findByFilter(null, "UPDATE_APPLICATION_STATUS", null, null, null);
        List<ActivityLog> byRole = repository.findByFilter(null, null, "mo", null, null);
        List<ActivityLog> byTime = repository.findByFilter(null, null, null,
                LocalDateTime.of(2026, 5, 21, 0, 0), LocalDateTime.of(2026, 5, 22, 23, 59));

        assertEquals(List.of("log-2", "log-1"), byName.stream().map(ActivityLog::getId).toList());
        assertEquals(List.of("log-3"), byAction.stream().map(ActivityLog::getId).toList());
        assertEquals(List.of("log-3"), byRole.stream().map(ActivityLog::getId).toList());
        assertEquals(List.of("log-2", "log-3"), byTime.stream().map(ActivityLog::getId).toList());
    }
}
