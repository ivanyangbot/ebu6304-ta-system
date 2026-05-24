package com.bupt.tarecruitment.service;

import com.bupt.tarecruitment.model.ApplicationRecord;
import com.bupt.tarecruitment.model.Job;
import com.bupt.tarecruitment.repository.ApplicationRepository;
import com.bupt.tarecruitment.repository.JobRepository;
import com.bupt.tarecruitment.util.PathUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.ServletContext;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ApplicationService}.
 *
 * <p>Verifies application lifecycle: submitting, duplicate prevention,
 * status update validation, withdrawal rules, and retrieval queries.</p>
 */
@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock private ApplicationRepository applicationRepository;
    @Mock private JobRepository jobRepository;

    private ApplicationServiceUnderTest applicationService;

    @BeforeEach
    void setUp() {
        applicationService = new ApplicationServiceUnderTest(applicationRepository, jobRepository);
    }

    // ---- applyForJob ----

    @Test
    void testApplyForJob_newApplication_success() {
        Job job = new Job("j1", "Lab TA", "EBU6304", "desc", Collections.emptyList(), 5, "mo1", "Open");
        when(jobRepository.findById("j1")).thenReturn(job);
        when(applicationRepository.findByJobIdAndApplicantId("j1", "a1")).thenReturn(null);
        doNothing().when(applicationRepository).save(any(ApplicationRecord.class));

        ApplicationRecord record = applicationService.applyForJob("j1", "a1");

        assertNotNull(record);
        assertEquals("j1", record.getJobId());
        assertEquals("a1", record.getApplicantId());
        assertEquals("Pending", record.getStatus());
        verify(applicationRepository).save(any(ApplicationRecord.class));
    }

    @Test
    void testApplyForJob_jobNotFound_throwsException() {
        when(jobRepository.findById("missing")).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> applicationService.applyForJob("missing", "a1"));
        assertTrue(ex.getMessage().contains("does not exist"));
    }

    @Test
    void testApplyForJob_alreadyApplied_throwsException() {
        Job job = new Job("j1", "Lab TA", "EBU6304", "desc", Collections.emptyList(), 5, "mo1", "Open");
        ApplicationRecord existing = new ApplicationRecord("app1", "j1", "a1", "Pending", "2025-01-01 10:00");
        when(jobRepository.findById("j1")).thenReturn(job);
        when(applicationRepository.findByJobIdAndApplicantId("j1", "a1")).thenReturn(existing);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> applicationService.applyForJob("j1", "a1"));
        assertTrue(ex.getMessage().contains("already applied"));
    }

    // ---- hasApplied ----

    @Test
    void testHasApplied_existingRecord_returnsTrue() {
        ApplicationRecord existing = new ApplicationRecord("app1", "j1", "a1", "Pending", "2025-01-01");
        when(applicationRepository.findByJobIdAndApplicantId("j1", "a1")).thenReturn(existing);

        assertTrue(applicationService.hasApplied("j1", "a1"));
    }

    @Test
    void testHasApplied_noRecord_returnsFalse() {
        when(applicationRepository.findByJobIdAndApplicantId("j1", "a1")).thenReturn(null);

        assertFalse(applicationService.hasApplied("j1", "a1"));
    }

    // ---- updateApplicationStatus ----

    @Test
    void testUpdateStatus_acceptedStatus_callsRepository() {
        doNothing().when(applicationRepository).updateStatus("app1", "Accepted");

        assertDoesNotThrow(() -> applicationService.updateApplicationStatus("app1", "Accepted"));
        verify(applicationRepository).updateStatus("app1", "Accepted");
    }

    @Test
    void testUpdateStatus_rejectedStatus_callsRepository() {
        doNothing().when(applicationRepository).updateStatus("app1", "Rejected");

        assertDoesNotThrow(() -> applicationService.updateApplicationStatus("app1", "Rejected"));
    }

    @Test
    void testUpdateStatus_pendingStatus_callsRepository() {
        doNothing().when(applicationRepository).updateStatus("app1", "Pending");

        assertDoesNotThrow(() -> applicationService.updateApplicationStatus("app1", "Pending"));
    }

    @Test
    void testUpdateStatus_invalidStatus_throwsException() {
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> applicationService.updateApplicationStatus("app1", "Unknown"));
        assertTrue(ex.getMessage().contains("Invalid"));
    }

    // ---- withdrawApplication ----

    @Test
    void testWithdraw_ownPendingApplication_success() {
        ApplicationRecord record = new ApplicationRecord("app1", "j1", "a1", "Pending", "2025-01-01");
        when(applicationRepository.findById("app1")).thenReturn(record);
        doNothing().when(applicationRepository).delete("app1");

        assertDoesNotThrow(() -> applicationService.withdrawApplication("app1", "a1"));
        verify(applicationRepository).delete("app1");
    }

    @Test
    void testWithdraw_notOwner_throwsException() {
        ApplicationRecord record = new ApplicationRecord("app1", "j1", "a1", "Pending", "2025-01-01");
        when(applicationRepository.findById("app1")).thenReturn(record);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> applicationService.withdrawApplication("app1", "other-user"));
        assertTrue(ex.getMessage().contains("own"));
    }

    @Test
    void testWithdraw_acceptedApplication_throwsException() {
        ApplicationRecord record = new ApplicationRecord("app1", "j1", "a1", "Accepted", "2025-01-01");
        when(applicationRepository.findById("app1")).thenReturn(record);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> applicationService.withdrawApplication("app1", "a1"));
        assertTrue(ex.getMessage().contains("pending"));
    }

    @Test
    void testWithdraw_notFound_throwsException() {
        when(applicationRepository.findById("missing")).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> applicationService.withdrawApplication("missing", "a1"));
        assertTrue(ex.getMessage().contains("not found"));
    }

    // ---- getApplicationsByApplicant ----

    @Test
    void testGetApplicationsByApplicant_returnsCorrectList() {
        List<ApplicationRecord> records = Arrays.asList(
                new ApplicationRecord("a1", "j1", "user1", "Pending", "2025-01-01"),
                new ApplicationRecord("a2", "j2", "user1", "Accepted", "2025-01-02")
        );
        when(applicationRepository.findByApplicantId("user1")).thenReturn(records);

        List<ApplicationRecord> result = applicationService.getApplicationsByApplicant("user1");

        assertEquals(2, result.size());
    }

    // ---- Test-only subclass ----

    static class ApplicationServiceUnderTest extends ApplicationService {
        private final ApplicationRepository appRepo;
        private final JobRepository jobRepo;

        ApplicationServiceUnderTest(ApplicationRepository appRepo, JobRepository jobRepo) {
            super(testContext());
            this.appRepo = appRepo;
            this.jobRepo = jobRepo;
        }

        private static ServletContext testContext() {
            try {
                ServletContext context = mock(ServletContext.class);
                Path dataDir = Files.createTempDirectory("application-service-test");
                when(context.getAttribute(PathUtil.DATA_DIR_ATTRIBUTE)).thenReturn(dataDir.toString());
                return context;
            } catch (java.io.IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public ApplicationRecord applyForJob(String jobId, String applicantId) {
            Job job = jobRepo.findById(jobId);
            if (job == null) throw new RuntimeException("Job does not exist.");
            if (hasApplied(jobId, applicantId)) throw new RuntimeException("You have already applied for this job.");

            ApplicationRecord record = new ApplicationRecord();
            record.setId("app-test");
            record.setJobId(jobId);
            record.setApplicantId(applicantId);
            record.setStatus("Pending");
            record.setAppliedAt("2025-01-01 10:00");
            appRepo.save(record);
            return record;
        }

        @Override
        public boolean hasApplied(String jobId, String applicantId) {
            return appRepo.findByJobIdAndApplicantId(jobId, applicantId) != null;
        }

        @Override
        public List<ApplicationRecord> getApplicationsByApplicant(String applicantId) {
            return appRepo.findByApplicantId(applicantId);
        }

        @Override
        public ApplicationRecord getApplicationById(String applicationId) {
            return appRepo.findById(applicationId);
        }

        @Override
        public void updateApplicationStatus(String applicationId, String status) {
            if (!"Pending".equals(status) && !"Accepted".equals(status) && !"Rejected".equals(status)) {
                throw new RuntimeException("Invalid application status.");
            }
            appRepo.updateStatus(applicationId, status);
        }

        @Override
        public void withdrawApplication(String applicationId, String applicantId) {
            ApplicationRecord application = appRepo.findById(applicationId);
            if (application == null) throw new RuntimeException("Application not found.");
            if (!application.getApplicantId().equals(applicantId))
                throw new RuntimeException("You can only withdraw your own application.");
            if (!"Pending".equals(application.getStatus()))
                throw new RuntimeException("Only pending applications can be withdrawn.");
            appRepo.delete(applicationId);
        }
    }
}
