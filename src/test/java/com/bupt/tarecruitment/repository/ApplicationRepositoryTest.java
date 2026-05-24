package com.bupt.tarecruitment.repository;

import com.bupt.tarecruitment.JsonTestData;
import com.bupt.tarecruitment.TestFixtures;
import com.bupt.tarecruitment.model.ApplicationRecord;
import com.bupt.tarecruitment.util.PathUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.servlet.ServletContext;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link ApplicationRepository} using a temporary JSON data file.
 *
 * <p>The tests verify application persistence, lookup, status update, feedback,
 * and deletion behaviour without touching the application's deployed data.</p>
 */
class ApplicationRepositoryTest {
    @TempDir
    Path dataDir;

    private ApplicationRepository repository;

    /**
     * Seeds {@code applications.json} and creates a repository bound to the temp directory.
     */
    @BeforeEach
    void setUp() {
        JsonTestData.writeApplications(dataDir, List.of(
                TestFixtures.application("app-1", "job-1", "applicant-1", "Pending"),
                TestFixtures.application("app-2", "job-1", "applicant-2", "Accepted"),
                TestFixtures.application("app-3", "job-2", "applicant-1", "Rejected")
        ));
        ServletContext context = mock(ServletContext.class);
        when(context.getAttribute(PathUtil.DATA_DIR_ATTRIBUTE)).thenReturn(dataDir.toString());
        repository = new ApplicationRepository(context);
    }

    /**
     * Verifies that all records are loaded from the JSON store.
     */
    @Test
    void findAll_readsApplicationsFromJsonFile() {
        assertEquals(3, repository.findAll().size());
    }

    /**
     * Verifies ID lookup for existing and missing application records.
     */
    @Test
    void findById_returnsMatchingRecordOrNull() {
        assertEquals("job-1", repository.findById("app-1").getJobId());
        assertNull(repository.findById("missing"));
    }

    /**
     * Verifies applicant, job, and duplicate-application query helpers.
     */
    @Test
    void queryMethods_filterByApplicantAndJob() {
        assertEquals(2, repository.findByApplicantId("applicant-1").size());
        assertEquals(2, repository.findByJobId("job-1").size());
        assertNotNull(repository.findByJobIdAndApplicantId("job-1", "applicant-2"));
        assertNull(repository.findByJobIdAndApplicantId("job-2", "applicant-2"));
    }

    /**
     * Verifies that saving a record appends it to the JSON file.
     */
    @Test
    void save_appendsNewApplication() {
        repository.save(TestFixtures.application("app-4", "job-3", "applicant-3", "Pending"));

        assertEquals(4, repository.findAll().size());
        assertEquals("job-3", repository.findById("app-4").getJobId());
    }

    /**
     * Verifies status updates and missing-record failure behaviour.
     */
    @Test
    void updateStatus_changesOnlyTargetApplication() {
        repository.updateStatus("app-1", "Accepted");

        assertEquals("Accepted", repository.findById("app-1").getStatus());
        assertEquals("Accepted", repository.findById("app-2").getStatus());
        assertThrows(RuntimeException.class, () -> repository.updateStatus("missing", "Rejected"));
    }

    /**
     * Verifies that MO feedback is trimmed and persisted with the new status.
     */
    @Test
    void updateStatusWithFeedback_trimsAndPersistsFeedback() {
        repository.updateStatusWithFeedback("app-1", "Rejected", "  Missing Java experience  ");

        ApplicationRecord updated = repository.findById("app-1");
        assertEquals("Rejected", updated.getStatus());
        assertEquals("Missing Java experience", updated.getMoFeedback());
    }

    /**
     * Verifies deletion of existing records and error handling for missing records.
     */
    @Test
    void delete_removesApplicationAndThrowsForMissingRecord() {
        repository.delete("app-3");

        assertEquals(2, repository.findAll().size());
        assertNull(repository.findById("app-3"));
        assertThrows(RuntimeException.class, () -> repository.delete("missing"));
    }
}
