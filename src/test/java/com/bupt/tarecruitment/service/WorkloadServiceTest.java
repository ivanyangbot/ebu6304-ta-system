package com.bupt.tarecruitment.service;

import com.bupt.tarecruitment.model.Applicant;
import com.bupt.tarecruitment.model.ApplicationRecord;
import com.bupt.tarecruitment.model.Job;
import com.bupt.tarecruitment.model.WorkloadSummary;
import com.bupt.tarecruitment.repository.ApplicationRepository;
import com.bupt.tarecruitment.repository.JobRepository;
import com.bupt.tarecruitment.repository.UserRepository;
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
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link WorkloadService}.
 *
 * <p>Verifies workload summary computation including total-hours aggregation,
 * "Overloaded" threshold detection, and empty data edge cases.</p>
 */
@ExtendWith(MockitoExtension.class)
class WorkloadServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private ApplicationRepository applicationRepository;
    @Mock private JobRepository jobRepository;

    private WorkloadServiceUnderTest workloadService;

    @BeforeEach
    void setUp() {
        workloadService = new WorkloadServiceUnderTest(userRepository, applicationRepository, jobRepository);
    }

    // ---- Normal workload ----

    @Test
    void testGetSummaries_normalWorkload_statusIsNormal() {
        Applicant applicant = new Applicant("a1", "bob", "pass", "APPLICANT", "Bob", "b@test.com",
                Collections.emptyList());
        Job job = new Job("j1", "TA", "Mod", "desc", Collections.emptyList(), 5, "mo1", "Open");
        ApplicationRecord accepted = new ApplicationRecord("app1", "j1", "a1", "Accepted", "2025-01-01");

        when(userRepository.findAllApplicants()).thenReturn(List.of(applicant));
        when(applicationRepository.findAll()).thenReturn(List.of(accepted));
        when(jobRepository.findAll()).thenReturn(List.of(job));

        List<WorkloadSummary> summaries = workloadService.getApplicantWorkloadSummaries();

        assertEquals(1, summaries.size());
        WorkloadSummary summary = summaries.get(0);
        assertEquals(1, summary.getAcceptedJobsCount());
        assertEquals(5, summary.getTotalHours());
        assertEquals("Normal", summary.getWorkloadStatus());
    }

    // ---- Overloaded detection ----

    @Test
    void testGetSummaries_totalHoursExceedsThreshold_statusIsOverloaded() {
        Applicant applicant = new Applicant("a1", "charlie", "pass", "APPLICANT", "Charlie", "c@test.com",
                Collections.emptyList());
        Job job1 = new Job("j1", "TA1", "Mod1", "d", Collections.emptyList(), 7, "mo1", "Open");
        Job job2 = new Job("j2", "TA2", "Mod2", "d", Collections.emptyList(), 6, "mo1", "Open");
        ApplicationRecord accepted1 = new ApplicationRecord("app1", "j1", "a1", "Accepted", "2025-01-01");
        ApplicationRecord accepted2 = new ApplicationRecord("app2", "j2", "a1", "Accepted", "2025-01-02");

        when(userRepository.findAllApplicants()).thenReturn(List.of(applicant));
        when(applicationRepository.findAll()).thenReturn(Arrays.asList(accepted1, accepted2));
        when(jobRepository.findAll()).thenReturn(Arrays.asList(job1, job2));

        List<WorkloadSummary> summaries = workloadService.getApplicantWorkloadSummaries();

        WorkloadSummary summary = summaries.get(0);
        assertEquals(13, summary.getTotalHours());  // 7+6 = 13 > threshold(10)
        assertEquals("Overloaded", summary.getWorkloadStatus());
    }

    // ---- Rejected applications are not counted ----

    @Test
    void testGetSummaries_rejectedApplicationsNotCounted() {
        Applicant applicant = new Applicant("a1", "dave", "pass", "APPLICANT", "Dave", "d@test.com",
                Collections.emptyList());
        Job job = new Job("j1", "TA", "Mod", "desc", Collections.emptyList(), 8, "mo1", "Open");
        ApplicationRecord rejected = new ApplicationRecord("app1", "j1", "a1", "Rejected", "2025-01-01");

        when(userRepository.findAllApplicants()).thenReturn(List.of(applicant));
        when(applicationRepository.findAll()).thenReturn(List.of(rejected));
        when(jobRepository.findAll()).thenReturn(List.of(job));

        List<WorkloadSummary> summaries = workloadService.getApplicantWorkloadSummaries();

        assertEquals(0, summaries.get(0).getAcceptedJobsCount());
        assertEquals(0, summaries.get(0).getTotalHours());
        assertEquals("Normal", summaries.get(0).getWorkloadStatus());
    }

    // ---- No applicants ----

    @Test
    void testGetSummaries_noApplicants_returnsEmptyList() {
        when(userRepository.findAllApplicants()).thenReturn(Collections.emptyList());
        when(applicationRepository.findAll()).thenReturn(Collections.emptyList());
        when(jobRepository.findAll()).thenReturn(Collections.emptyList());

        List<WorkloadSummary> summaries = workloadService.getApplicantWorkloadSummaries();

        assertTrue(summaries.isEmpty());
    }

    // ---- DEFAULT_THRESHOLD constant ----

    @Test
    void testDefaultThreshold_is10() {
        assertEquals(10, WorkloadService.DEFAULT_THRESHOLD);
    }

    // ---- Test-only subclass ----

    static class WorkloadServiceUnderTest extends WorkloadService {
        private final UserRepository ur;
        private final ApplicationRepository ar;
        private final JobRepository jr;

        WorkloadServiceUnderTest(UserRepository ur, ApplicationRepository ar, JobRepository jr) {
            super(testContext());
            this.ur = ur;
            this.ar = ar;
            this.jr = jr;
        }

        private static ServletContext testContext() {
            try {
                ServletContext context = mock(ServletContext.class);
                Path dataDir = Files.createTempDirectory("workload-service-test");
                when(context.getAttribute(PathUtil.DATA_DIR_ATTRIBUTE)).thenReturn(dataDir.toString());
                return context;
            } catch (java.io.IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public List<WorkloadSummary> getApplicantWorkloadSummaries() {
            List<Applicant> applicants = ur.findAllApplicants();
            List<ApplicationRecord> applications = ar.findAll();
            List<Job> jobs = jr.findAll();
            java.util.List<WorkloadSummary> summaries = new java.util.ArrayList<>();

            for (Applicant applicant : applicants) {
                int acceptedCount = 0;
                int totalHours = 0;
                for (ApplicationRecord application : applications) {
                    if (applicant.getId().equals(application.getApplicantId())
                            && "Accepted".equalsIgnoreCase(application.getStatus())) {
                        acceptedCount++;
                        for (Job job : jobs) {
                            if (job.getId().equals(application.getJobId())) {
                                totalHours += job.getHours();
                                break;
                            }
                        }
                    }
                }
                String status = totalHours > DEFAULT_THRESHOLD ? "Overloaded" : "Normal";
                summaries.add(new WorkloadSummary(applicant, acceptedCount, totalHours, status));
            }
            return summaries;
        }
    }
}
