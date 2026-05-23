package com.bupt.tarecruitment.repository;

import com.bupt.tarecruitment.model.ActivityLog;
import com.bupt.tarecruitment.util.JsonFileUtil;
import com.bupt.tarecruitment.util.PathUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.ServletContext;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * ActivityLogRepository 集成测试：使用真实临时文件验证读写行为。
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ActivityLogRepository Unit Tests")
class ActivityLogRepositoryTest {

    @Mock
    private ServletContext mockServletContext;

    private ActivityLogRepository repository;
    private Path tempFile;

    @BeforeEach
    void setUp() throws Exception {
        tempFile = Files.createTempFile("activity_logs_test_", ".json");
        Files.writeString(tempFile, "[]");

        // 通过反射直接注入 filePath，绕过 ServletContext 和 PathUtil
        repository = new ActivityLogRepository(mockServletContext);
        Field filePathField = ActivityLogRepository.class.getDeclaredField("filePath");
        filePathField.setAccessible(true);
        filePathField.set(repository, tempFile);
    }

    @AfterEach
    void tearDown() throws Exception {
        Files.deleteIfExists(tempFile);
    }

    private ActivityLog makeLog(String id, String userId, String actionType,
                                 String userRole, String userFullName,
                                 LocalDateTime createdAt) {
        ActivityLog log = new ActivityLog();
        log.setId(id);
        log.setUserId(userId);
        log.setUserFullName(userFullName);
        log.setUserRole(userRole);
        log.setActionType(actionType);
        log.setDescription("desc-" + id);
        log.setCreatedAt(createdAt);
        return log;
    }

    // ----------------------------------------------------------------
    // save / findAll
    // ----------------------------------------------------------------

    @Test
    @DisplayName("save() and findAll() should persist and retrieve a log entry")
    void saveAndFindAll_roundTrip() {
        ActivityLog log = makeLog("log-1", "u1", "APPLY_JOB", "APPLICANT", "Alice",
                LocalDateTime.of(2026, 5, 1, 10, 0));

        repository.save(log);
        List<ActivityLog> all = repository.findAll();

        assertEquals(1, all.size());
        ActivityLog retrieved = all.get(0);
        assertEquals("log-1", retrieved.getId());
        assertEquals("u1", retrieved.getUserId());
        assertEquals("Alice", retrieved.getUserFullName());
        assertEquals("APPLICANT", retrieved.getUserRole());
        assertEquals("APPLY_JOB", retrieved.getActionType());
    }

    @Test
    @DisplayName("findAll() should return results in descending createdAt order")
    void findAll_returnsDescendingOrder() {
        ActivityLog older = makeLog("log-1", "u1", "APPLY_JOB", "APPLICANT", "Alice",
                LocalDateTime.of(2026, 5, 1, 9, 0));
        ActivityLog newer = makeLog("log-2", "u1", "WITHDRAW_APPLICATION", "APPLICANT", "Alice",
                LocalDateTime.of(2026, 5, 1, 10, 0));

        repository.save(older);
        repository.save(newer);

        List<ActivityLog> all = repository.findAll();
        assertEquals("log-2", all.get(0).getId(), "Newer entry should come first");
        assertEquals("log-1", all.get(1).getId());
    }

    // ----------------------------------------------------------------
    // findByUserId
    // ----------------------------------------------------------------

    @Test
    @DisplayName("findByUserId() should return only logs belonging to the given user")
    void findByUserId_filtersCorrectly() {
        repository.save(makeLog("log-1", "u1", "APPLY_JOB", "APPLICANT", "Alice",
                LocalDateTime.of(2026, 5, 1, 10, 0)));
        repository.save(makeLog("log-2", "u2", "CREATE_JOB", "MO", "Bob",
                LocalDateTime.of(2026, 5, 1, 11, 0)));

        List<ActivityLog> result = repository.findByUserId("u1");

        assertEquals(1, result.size());
        assertEquals("u1", result.get(0).getUserId());
    }

    @Test
    @DisplayName("findByUserId() should return empty list when user has no logs")
    void findByUserId_noLogs_returnsEmpty() {
        List<ActivityLog> result = repository.findByUserId("unknown-user");
        assertTrue(result.isEmpty());
    }

    // ----------------------------------------------------------------
    // findRecentByUserId
    // ----------------------------------------------------------------

    @Test
    @DisplayName("findRecentByUserId() should return at most `limit` entries")
    void findRecentByUserId_respectsLimit() {
        for (int i = 1; i <= 7; i++) {
            repository.save(makeLog("log-" + i, "u1", "APPLY_JOB", "APPLICANT", "Alice",
                    LocalDateTime.of(2026, 5, 1, i, 0)));
        }

        List<ActivityLog> result = repository.findRecentByUserId("u1", 5);
        assertEquals(5, result.size());
    }

    @Test
    @DisplayName("findRecentByUserId() returns all when count <= limit")
    void findRecentByUserId_fewerThanLimit_returnsAll() {
        repository.save(makeLog("log-1", "u1", "APPLY_JOB", "APPLICANT", "Alice",
                LocalDateTime.of(2026, 5, 1, 10, 0)));
        repository.save(makeLog("log-2", "u1", "WITHDRAW_APPLICATION", "APPLICANT", "Alice",
                LocalDateTime.of(2026, 5, 1, 11, 0)));

        List<ActivityLog> result = repository.findRecentByUserId("u1", 5);
        assertEquals(2, result.size());
    }

    // ----------------------------------------------------------------
    // findByFilter
    // ----------------------------------------------------------------

    @Test
    @DisplayName("findByFilter() with all nulls should return all logs")
    void findByFilter_allNulls_returnsAll() {
        repository.save(makeLog("log-1", "u1", "APPLY_JOB", "APPLICANT", "Alice",
                LocalDateTime.of(2026, 5, 1, 10, 0)));
        repository.save(makeLog("log-2", "mo1", "CREATE_JOB", "MO", "Bob",
                LocalDateTime.of(2026, 5, 2, 10, 0)));

        List<ActivityLog> result = repository.findByFilter(null, null, null, null, null);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("findByFilter() by actionType should filter correctly")
    void findByFilter_byActionType() {
        repository.save(makeLog("log-1", "u1", "APPLY_JOB", "APPLICANT", "Alice",
                LocalDateTime.of(2026, 5, 1, 10, 0)));
        repository.save(makeLog("log-2", "mo1", "CREATE_JOB", "MO", "Bob",
                LocalDateTime.of(2026, 5, 2, 10, 0)));

        List<ActivityLog> result = repository.findByFilter(null, "CREATE_JOB", null, null, null);

        assertEquals(1, result.size());
        assertEquals("CREATE_JOB", result.get(0).getActionType());
    }

    @Test
    @DisplayName("findByFilter() by userRole should filter correctly")
    void findByFilter_byUserRole() {
        repository.save(makeLog("log-1", "u1", "APPLY_JOB", "APPLICANT", "Alice",
                LocalDateTime.of(2026, 5, 1, 10, 0)));
        repository.save(makeLog("log-2", "mo1", "CREATE_JOB", "MO", "Bob",
                LocalDateTime.of(2026, 5, 2, 10, 0)));

        List<ActivityLog> result = repository.findByFilter(null, null, "MO", null, null);

        assertEquals(1, result.size());
        assertEquals("MO", result.get(0).getUserRole());
    }

    @Test
    @DisplayName("findByFilter() by userFullName should be case-insensitive partial match")
    void findByFilter_byUserFullName_caseInsensitive() {
        repository.save(makeLog("log-1", "u1", "APPLY_JOB", "APPLICANT", "Alice Wang",
                LocalDateTime.of(2026, 5, 1, 10, 0)));
        repository.save(makeLog("log-2", "mo1", "CREATE_JOB", "MO", "Bob Smith",
                LocalDateTime.of(2026, 5, 2, 10, 0)));

        List<ActivityLog> result = repository.findByFilter("alice", null, null, null, null);

        assertEquals(1, result.size());
        assertEquals("Alice Wang", result.get(0).getUserFullName());
    }

    @Test
    @DisplayName("findByFilter() by fromTime should exclude earlier records")
    void findByFilter_byFromTime_excludesEarlier() {
        repository.save(makeLog("log-old", "u1", "APPLY_JOB", "APPLICANT", "Alice",
                LocalDateTime.of(2026, 4, 1, 10, 0)));
        repository.save(makeLog("log-new", "u1", "APPLY_JOB", "APPLICANT", "Alice",
                LocalDateTime.of(2026, 6, 1, 10, 0)));

        LocalDateTime from = LocalDateTime.of(2026, 5, 1, 0, 0);
        List<ActivityLog> result = repository.findByFilter(null, null, null, from, null);

        assertEquals(1, result.size());
        assertEquals("log-new", result.get(0).getId());
    }

    @Test
    @DisplayName("findByFilter() by toTime should exclude later records")
    void findByFilter_byToTime_excludesLater() {
        repository.save(makeLog("log-old", "u1", "APPLY_JOB", "APPLICANT", "Alice",
                LocalDateTime.of(2026, 4, 1, 10, 0)));
        repository.save(makeLog("log-new", "u1", "APPLY_JOB", "APPLICANT", "Alice",
                LocalDateTime.of(2026, 6, 1, 10, 0)));

        LocalDateTime to = LocalDateTime.of(2026, 5, 1, 0, 0);
        List<ActivityLog> result = repository.findByFilter(null, null, null, null, to);

        assertEquals(1, result.size());
        assertEquals("log-old", result.get(0).getId());
    }

    @Test
    @DisplayName("findByFilter() combined filters should all apply")
    void findByFilter_combinedFilters() {
        repository.save(makeLog("log-1", "u1", "APPLY_JOB", "APPLICANT", "Alice",
                LocalDateTime.of(2026, 5, 10, 10, 0)));
        repository.save(makeLog("log-2", "u1", "WITHDRAW_APPLICATION", "APPLICANT", "Alice",
                LocalDateTime.of(2026, 5, 15, 10, 0)));
        repository.save(makeLog("log-3", "mo1", "CREATE_JOB", "MO", "Bob",
                LocalDateTime.of(2026, 5, 12, 10, 0)));

        LocalDateTime from = LocalDateTime.of(2026, 5, 9, 0, 0);
        LocalDateTime to   = LocalDateTime.of(2026, 5, 16, 0, 0);

        List<ActivityLog> result = repository.findByFilter("Alice", "APPLY_JOB", "APPLICANT", from, to);

        assertEquals(1, result.size());
        assertEquals("log-1", result.get(0).getId());
    }

    @Test
    @DisplayName("save() should persist beforeState and afterState correctly")
    void save_persistsStateFields() {
        ActivityLog log = makeLog("log-s1", "mo1", "UPDATE_APPLICATION_STATUS", "MO", "Bob",
                LocalDateTime.of(2026, 5, 1, 10, 0));
        log.setBeforeState("Pending");
        log.setAfterState("Accepted");

        repository.save(log);

        ActivityLog retrieved = repository.findAll().get(0);
        assertEquals("Pending", retrieved.getBeforeState());
        assertEquals("Accepted", retrieved.getAfterState());
    }

    @Test
    @DisplayName("findAll() on empty file should return empty list")
    void findAll_emptyFile_returnsEmptyList() {
        List<ActivityLog> result = repository.findAll();
        assertTrue(result.isEmpty());
    }
}
