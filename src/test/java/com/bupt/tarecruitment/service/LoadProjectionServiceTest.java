package com.bupt.tarecruitment.service;

import com.bupt.tarecruitment.model.Applicant;
import com.bupt.tarecruitment.model.Job;
import com.bupt.tarecruitment.model.LoadProjection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link LoadProjectionService}.
 *
 * <p>Verifies projected hours calculation, workload band assignment,
 * summary text generation, and edge cases (null inputs, missing map entries).</p>
 */
class LoadProjectionServiceTest {

    private LoadProjectionService service;

    @BeforeEach
    void setUp() {
        service = new LoadProjectionService();
    }

    private Applicant makeApplicant(String id) {
        return new Applicant(id, "u" + id, "pass", "APPLICANT", "Name" + id, id + "@t.com",
                Collections.emptyList());
    }

    private Job makeJob(String id, int hours) {
        return new Job(id, "TA", "Module", "desc", Collections.emptyList(), hours, "mo1", "Open");
    }

    // ---- project ----

    @Test
    void testProject_nullJob_returnsNull() {
        assertNull(service.project(null, makeApplicant("a1"), Collections.emptyMap()));
    }

    @Test
    void testProject_nullApplicant_returnsNull() {
        assertNull(service.project(makeJob("j1", 5), null, Collections.emptyMap()));
    }

    @Test
    void testProject_noCurrentHours_projectedEqualsJobHours() {
        Job job = makeJob("j1", 8);
        Applicant applicant = makeApplicant("a1");
        Map<String, Integer> currentHours = Collections.emptyMap();

        LoadProjection projection = service.project(job, applicant, currentHours);

        assertNotNull(projection);
        assertEquals(8, projection.getProjectedHours());
    }

    @Test
    void testProject_withCurrentHours_projectedIsSum() {
        Job job = makeJob("j1", 6);
        Applicant applicant = makeApplicant("a1");
        Map<String, Integer> currentHours = new HashMap<>();
        currentHours.put("a1", 20);

        LoadProjection projection = service.project(job, applicant, currentHours);

        assertEquals(26, projection.getProjectedHours()); // 20 + 6
    }

    @Test
    void testProject_lightBand_workloadBandIsLight() {
        Job job = makeJob("j1", 5);
        Applicant applicant = makeApplicant("a1");

        LoadProjection projection = service.project(job, applicant, Collections.emptyMap());

        assertEquals("Light", projection.getWorkloadBand());
    }

    @Test
    void testProject_summaryContainsProjectedBand() {
        Job job = makeJob("j1", 5);
        Applicant applicant = makeApplicant("a1");

        LoadProjection projection = service.project(job, applicant, Collections.emptyMap());

        assertTrue(projection.getSummary().contains("Light"),
                "Summary should mention the band name");
    }

    @Test
    void testProject_negativeJobHours_treatedAsZero() {
        Job job = makeJob("j1", -5);
        Applicant applicant = makeApplicant("a1");

        LoadProjection projection = service.project(job, applicant, Collections.emptyMap());

        assertEquals(0, projection.getProjectedHours());
    }

    @Test
    void testProject_nullCurrentHoursMap_treatedAsZero() {
        Job job = makeJob("j1", 7);
        Applicant applicant = makeApplicant("a1");

        LoadProjection projection = service.project(job, applicant, null);

        assertEquals(7, projection.getProjectedHours());
    }

    // ---- projectBatch ----

    @Test
    void testProjectBatch_nullJobs_returnsEmptyList() {
        List<LoadProjection> result = service.projectBatch(null, makeApplicant("a1"), Collections.emptyMap());
        assertTrue(result.isEmpty());
    }

    @Test
    void testProjectBatch_nullApplicant_returnsEmptyList() {
        List<LoadProjection> result = service.projectBatch(List.of(makeJob("j1", 5)), null, Collections.emptyMap());
        assertTrue(result.isEmpty());
    }

    @Test
    void testProjectBatch_twoJobs_returnsTwoProjections() {
        List<Job> jobs = Arrays.asList(makeJob("j1", 5), makeJob("j2", 8));
        Applicant applicant = makeApplicant("a1");

        List<LoadProjection> result = service.projectBatch(jobs, applicant, Collections.emptyMap());

        assertEquals(2, result.size());
    }
}
