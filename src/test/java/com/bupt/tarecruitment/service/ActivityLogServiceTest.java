package com.bupt.tarecruitment.service;

import com.bupt.tarecruitment.model.ActivityLog;
import com.bupt.tarecruitment.model.User;
import com.bupt.tarecruitment.repository.ActivityLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.ServletContext;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ActivityLogService Unit Tests")
class ActivityLogServiceTest {

    @Mock
    private ActivityLogRepository mockRepository;

    @Mock
    private ServletContext mockServletContext;

    private ActivityLogService service;

    // 测试用的操作者用户
    private User makeUser(String id, String fullName, String role) {
        User u = new User();
        u.setId(id);
        u.setFullName(fullName);
        u.setRole(role);
        return u;
    }

    // 构造一条日志条目
    private ActivityLog makeLog(String userId, String actionType, String userRole,
                                 String userFullName, LocalDateTime createdAt) {
        ActivityLog log = new ActivityLog();
        log.setId("log-" + System.nanoTime());
        log.setUserId(userId);
        log.setUserFullName(userFullName);
        log.setUserRole(userRole);
        log.setActionType(actionType);
        log.setDescription("desc");
        log.setCreatedAt(createdAt);
        return log;
    }

    @BeforeEach
    void setUp() throws Exception {
        // 构造 service，然后通过反射注入 mock repository
        service = new ActivityLogService(mockServletContext);
        Field repoField = ActivityLogService.class.getDeclaredField("repository");
        repoField.setAccessible(true);
        repoField.set(service, mockRepository);
    }

    // ----------------------------------------------------------------
    // 1. log() — 基本记录能力
    // ----------------------------------------------------------------

    @Test
    @DisplayName("log() should save an ActivityLog with correct fields")
    void log_savesEntryWithCorrectFields() {
        User operator = makeUser("u1", "Alice", "APPLICANT");

        service.log(operator, ActivityLogService.APPLY_JOB,
                "Applied for DS TA", "app-001", null, null);

        ArgumentCaptor<ActivityLog> captor = ArgumentCaptor.forClass(ActivityLog.class);
        verify(mockRepository, times(1)).save(captor.capture());

        ActivityLog saved = captor.getValue();
        assertEquals("u1", saved.getUserId());
        assertEquals("Alice", saved.getUserFullName());
        assertEquals("APPLICANT", saved.getUserRole());
        assertEquals(ActivityLogService.APPLY_JOB, saved.getActionType());
        assertEquals("Applied for DS TA", saved.getDescription());
        assertEquals("app-001", saved.getRelatedObjectId());
        assertNull(saved.getBeforeState());
        assertNull(saved.getAfterState());
        assertNotNull(saved.getCreatedAt());
    }

    @Test
    @DisplayName("log() should persist beforeState and afterState for status-change events")
    void log_savesStateTransitionFields() {
        User operator = makeUser("mo1", "Bob", "MO");

        service.log(operator, ActivityLogService.UPDATE_APPLICATION_STATUS,
                "Updated status", "app-002", "Pending", "Accepted");

        ArgumentCaptor<ActivityLog> captor = ArgumentCaptor.forClass(ActivityLog.class);
        verify(mockRepository).save(captor.capture());

        ActivityLog saved = captor.getValue();
        assertEquals("Pending", saved.getBeforeState());
        assertEquals("Accepted", saved.getAfterState());
    }

    @Test
    @DisplayName("log() should NOT throw even if repository.save() throws")
    void log_doesNotPropagateRepositoryException() {
        User operator = makeUser("u1", "Alice", "APPLICANT");
        doThrow(new RuntimeException("disk full")).when(mockRepository).save(any());

        assertDoesNotThrow(() ->
                service.log(operator, ActivityLogService.APPLY_JOB, "test", null, null, null));
    }

    // ----------------------------------------------------------------
    // 2. Convenience log methods — verify correct actionType
    // ----------------------------------------------------------------

    @Test
    @DisplayName("logApplyJob() should record APPLY_JOB actionType")
    void logApplyJob_correctActionType() {
        User op = makeUser("u1", "Alice", "APPLICANT");
        service.logApplyJob(op, "Data Structures TA", "app-001");

        ArgumentCaptor<ActivityLog> captor = ArgumentCaptor.forClass(ActivityLog.class);
        verify(mockRepository).save(captor.capture());
        assertEquals(ActivityLogService.APPLY_JOB, captor.getValue().getActionType());
        assertTrue(captor.getValue().getDescription().contains("Data Structures TA"));
    }

    @Test
    @DisplayName("logWithdrawApplication() should record WITHDRAW_APPLICATION actionType")
    void logWithdrawApplication_correctActionType() {
        User op = makeUser("u1", "Alice", "APPLICANT");
        service.logWithdrawApplication(op, "OS TA", "app-002");

        ArgumentCaptor<ActivityLog> captor = ArgumentCaptor.forClass(ActivityLog.class);
        verify(mockRepository).save(captor.capture());
        assertEquals(ActivityLogService.WITHDRAW_APPLICATION, captor.getValue().getActionType());
    }

    @Test
    @DisplayName("logCreateJob() should record CREATE_JOB actionType with null states")
    void logCreateJob_correctActionTypeAndNullStates() {
        User op = makeUser("mo1", "Bob", "MO");
        service.logCreateJob(op, "Algorithms TA", "job-001");

        ArgumentCaptor<ActivityLog> captor = ArgumentCaptor.forClass(ActivityLog.class);
        verify(mockRepository).save(captor.capture());
        ActivityLog saved = captor.getValue();
        assertEquals(ActivityLogService.CREATE_JOB, saved.getActionType());
        assertNull(saved.getBeforeState());
        assertNull(saved.getAfterState());
    }

    @Test
    @DisplayName("logCompleteJob() should record COMPLETE_JOB with Open->Completed states")
    void logCompleteJob_stateTransition() {
        User op = makeUser("mo1", "Bob", "MO");
        service.logCompleteJob(op, "Algorithms TA", "job-001");

        ArgumentCaptor<ActivityLog> captor = ArgumentCaptor.forClass(ActivityLog.class);
        verify(mockRepository).save(captor.capture());
        ActivityLog saved = captor.getValue();
        assertEquals(ActivityLogService.COMPLETE_JOB, saved.getActionType());
        assertEquals("Open", saved.getBeforeState());
        assertEquals("Completed", saved.getAfterState());
    }

    @Test
    @DisplayName("logReopenJob() should record REOPEN_JOB with Completed->Open states")
    void logReopenJob_stateTransition() {
        User op = makeUser("mo1", "Bob", "MO");
        service.logReopenJob(op, "Algorithms TA", "job-001");

        ArgumentCaptor<ActivityLog> captor = ArgumentCaptor.forClass(ActivityLog.class);
        verify(mockRepository).save(captor.capture());
        ActivityLog saved = captor.getValue();
        assertEquals(ActivityLogService.REOPEN_JOB, saved.getActionType());
        assertEquals("Completed", saved.getBeforeState());
        assertEquals("Open", saved.getAfterState());
    }

    @Test
    @DisplayName("logUpdateApplicationStatus() should include applicant name and job title in description")
    void logUpdateApplicationStatus_descriptionContent() {
        User op = makeUser("mo1", "Bob", "MO");
        service.logUpdateApplicationStatus(op, "Alice", "DS TA", "app-001", "Pending", "Rejected");

        ArgumentCaptor<ActivityLog> captor = ArgumentCaptor.forClass(ActivityLog.class);
        verify(mockRepository).save(captor.capture());
        ActivityLog saved = captor.getValue();
        assertEquals(ActivityLogService.UPDATE_APPLICATION_STATUS, saved.getActionType());
        assertTrue(saved.getDescription().contains("Alice"));
        assertTrue(saved.getDescription().contains("DS TA"));
        assertEquals("Pending", saved.getBeforeState());
        assertEquals("Rejected", saved.getAfterState());
    }

    @Test
    @DisplayName("logCreateUser() should record CREATE_USER with target user info in description")
    void logCreateUser_descriptionContent() {
        User op = makeUser("admin1", "AdminUser", "ADMIN");
        service.logCreateUser(op, "Charlie", "MO", "mo-new-001");

        ArgumentCaptor<ActivityLog> captor = ArgumentCaptor.forClass(ActivityLog.class);
        verify(mockRepository).save(captor.capture());
        ActivityLog saved = captor.getValue();
        assertEquals(ActivityLogService.CREATE_USER, saved.getActionType());
        assertTrue(saved.getDescription().contains("Charlie"));
        assertTrue(saved.getDescription().contains("MO"));
        assertEquals("mo-new-001", saved.getRelatedObjectId());
    }

    @Test
    @DisplayName("logDeleteUser() should record DELETE_USER with deleted user info")
    void logDeleteUser_descriptionContent() {
        User op = makeUser("admin1", "AdminUser", "ADMIN");
        service.logDeleteUser(op, "Dave", "APPLICANT", "app-user-001");

        ArgumentCaptor<ActivityLog> captor = ArgumentCaptor.forClass(ActivityLog.class);
        verify(mockRepository).save(captor.capture());
        ActivityLog saved = captor.getValue();
        assertEquals(ActivityLogService.DELETE_USER, saved.getActionType());
        assertTrue(saved.getDescription().contains("Dave"));
        assertTrue(saved.getDescription().contains("APPLICANT"));
    }

    // ----------------------------------------------------------------
    // 3. Query methods
    // ----------------------------------------------------------------

    @Test
    @DisplayName("getRecentByUser() should delegate to repository with correct userId and limit")
    void getRecentByUser_delegatesCorrectly() {
        when(mockRepository.findRecentByUserId("u1", 5)).thenReturn(Collections.emptyList());
        service.getRecentByUser("u1", 5);
        verify(mockRepository).findRecentByUserId("u1", 5);
    }

    @Test
    @DisplayName("getAllByUserAndType() with null actionType should return all logs for user")
    void getAllByUserAndType_nullType_returnsAll() {
        ActivityLog log1 = makeLog("u1", ActivityLogService.APPLY_JOB, "APPLICANT", "Alice",
                LocalDateTime.now().minusHours(2));
        ActivityLog log2 = makeLog("u1", ActivityLogService.WITHDRAW_APPLICATION, "APPLICANT", "Alice",
                LocalDateTime.now().minusHours(1));
        when(mockRepository.findByUserId("u1")).thenReturn(Arrays.asList(log1, log2));

        List<ActivityLog> result = service.getAllByUserAndType("u1", null);

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("getAllByUserAndType() with specific actionType should filter correctly")
    void getAllByUserAndType_withType_filtersCorrectly() {
        ActivityLog log1 = makeLog("u1", ActivityLogService.APPLY_JOB, "APPLICANT", "Alice",
                LocalDateTime.now().minusHours(2));
        ActivityLog log2 = makeLog("u1", ActivityLogService.WITHDRAW_APPLICATION, "APPLICANT", "Alice",
                LocalDateTime.now().minusHours(1));
        when(mockRepository.findByUserId("u1")).thenReturn(Arrays.asList(log1, log2));

        List<ActivityLog> result = service.getAllByUserAndType("u1", ActivityLogService.APPLY_JOB);

        assertEquals(1, result.size());
        assertEquals(ActivityLogService.APPLY_JOB, result.get(0).getActionType());
    }

    @Test
    @DisplayName("getAllByUserAndType() with empty actionType should return all logs for user")
    void getAllByUserAndType_emptyType_returnsAll() {
        ActivityLog log1 = makeLog("u1", ActivityLogService.APPLY_JOB, "APPLICANT", "Alice",
                LocalDateTime.now());
        when(mockRepository.findByUserId("u1")).thenReturn(Collections.singletonList(log1));

        List<ActivityLog> result = service.getAllByUserAndType("u1", "");

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("getAllByUserAndType() with non-matching actionType should return empty list")
    void getAllByUserAndType_noMatch_returnsEmpty() {
        ActivityLog log1 = makeLog("u1", ActivityLogService.APPLY_JOB, "APPLICANT", "Alice",
                LocalDateTime.now());
        when(mockRepository.findByUserId("u1")).thenReturn(Collections.singletonList(log1));

        List<ActivityLog> result = service.getAllByUserAndType("u1", ActivityLogService.CREATE_JOB);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("getFilteredGlobal() should delegate all parameters to repository")
    void getFilteredGlobal_delegatesAllParams() {
        LocalDateTime from = LocalDateTime.of(2026, 1, 1, 0, 0);
        LocalDateTime to   = LocalDateTime.of(2026, 12, 31, 23, 59);
        when(mockRepository.findByFilter("Alice", "APPLY_JOB", "APPLICANT", from, to))
                .thenReturn(Collections.emptyList());

        service.getFilteredGlobal("Alice", "APPLY_JOB", "APPLICANT", from, to);

        verify(mockRepository).findByFilter("Alice", "APPLY_JOB", "APPLICANT", from, to);
    }
}
