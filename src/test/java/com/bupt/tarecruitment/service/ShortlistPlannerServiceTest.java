package com.bupt.tarecruitment.service;

import com.bupt.tarecruitment.model.Applicant;
import com.bupt.tarecruitment.model.CandidateFitSnapshot;
import com.bupt.tarecruitment.model.Job;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ShortlistPlannerService}.
 *
 * <p>Verifies fit-score computation, next-step recommendations, skill gap
 * detection, sorting order, and edge cases.</p>
 */
class ShortlistPlannerServiceTest {

    private ShortlistPlannerService service;

    @BeforeEach
    void setUp() {
        service = new ShortlistPlannerService();
    }

    private Job makeJob(List<String> requiredSkills) {
        return new Job("j1", "TA", "Module", "desc", requiredSkills, 5, "mo1", "Open");
    }

    private Applicant makeApplicant(String id, List<String> skills, String intro) {
        Applicant a = new Applicant(id, "u" + id, "pass", "APPLICANT", "Name" + id, id + "@t.com", skills);
        a.setSelfIntroduction(intro);
        return a;
    }

    // ---- Null / empty guards ----

    @Test
    void testCreateShortlist_nullJob_returnsEmptyList() {
        List<CandidateFitSnapshot> result = service.createShortlist(null, List.of(makeApplicant("1", Collections.emptyList(), "")));
        assertTrue(result.isEmpty());
    }

    @Test
    void testCreateShortlist_nullApplicants_returnsEmptyList() {
        List<CandidateFitSnapshot> result = service.createShortlist(makeJob(Collections.emptyList()), null);
        assertTrue(result.isEmpty());
    }

    // ---- Correct count ----

    @Test
    void testCreateShortlist_twoApplicants_returnsTwoSnapshots() {
        Job job = makeJob(Arrays.asList("Java"));
        List<Applicant> applicants = Arrays.asList(
                makeApplicant("1", Arrays.asList("Java"), "Long enough intro text here"),
                makeApplicant("2", Collections.emptyList(), ""));

        List<CandidateFitSnapshot> result = service.createShortlist(job, applicants);

        assertEquals(2, result.size());
    }

    // ---- Sorted descending by fit score ----

    @Test
    void testCreateShortlist_sortedByFitScoreDescending() {
        Job job = makeJob(Arrays.asList("Java", "Python"));
        Applicant strong = makeApplicant("1", Arrays.asList("Java", "Python"),
                "I have extensive experience in both Java and Python from multiple lab sessions.");
        Applicant weak = makeApplicant("2", Collections.emptyList(), "");

        List<CandidateFitSnapshot> result = service.createShortlist(job, Arrays.asList(weak, strong));

        assertEquals("1", result.get(0).getApplicant().getId());
    }

    // ---- Aligned and gap skills are correct ----

    @Test
    void testCreateShortlist_alignedAndGapSkillsCorrect() {
        Job job = makeJob(Arrays.asList("Java", "Python", "SQL"));
        Applicant a = makeApplicant("1", Arrays.asList("Java", "SQL"), "Some intro text here.");

        List<CandidateFitSnapshot> result = service.createShortlist(job, List.of(a));
        CandidateFitSnapshot snapshot = result.get(0);

        assertEquals(2, snapshot.getAlignedSkills().size());
        assertEquals(1, snapshot.getGapSkills().size());
        assertTrue(snapshot.getGapSkills().stream().anyMatch(s -> s.equalsIgnoreCase("python")));
    }

    // ---- Next step: Advance ----

    @Test
    void testCreateShortlist_perfectMatchWithLongIntro_nextStepAdvance() {
        Job job = makeJob(Arrays.asList("Java"));
        String longIntro = "I have been working with Java for over three years in various software projects "
                + "and I am passionate about teaching and mentoring junior students in programming fundamentals.";
        Applicant a = makeApplicant("1", Arrays.asList("Java"), longIntro);

        List<CandidateFitSnapshot> result = service.createShortlist(job, List.of(a));

        assertEquals("Advance", result.get(0).getNextStep());
    }

    // ---- Next step: Hold ----

    @Test
    void testCreateShortlist_noSkillsNoIntro_nextStepHold() {
        Job job = makeJob(Arrays.asList("Java", "Python"));
        Applicant a = makeApplicant("1", Collections.emptyList(), "");

        List<CandidateFitSnapshot> result = service.createShortlist(job, List.of(a));

        assertEquals("Hold", result.get(0).getNextStep());
    }

    // ---- Fit score bounded ----

    @Test
    void testCreateShortlist_fitScoreNeverNegative() {
        Job job = makeJob(Arrays.asList("Java", "Python"));
        Applicant a = makeApplicant("1", Collections.emptyList(), "");

        List<CandidateFitSnapshot> result = service.createShortlist(job, List.of(a));

        assertTrue(result.get(0).getFitScore() >= 0.0);
    }

    // ---- No required skills: full coverage assumed ----

    @Test
    void testCreateShortlist_noRequiredSkills_highFitScore() {
        Job job = makeJob(Collections.emptyList());
        Applicant a = makeApplicant("1", Arrays.asList("Java"), "Some introduction.");

        List<CandidateFitSnapshot> result = service.createShortlist(job, List.of(a));

        assertTrue(result.get(0).getFitScore() >= 85.0,
                "With no required skills coverage should be 100% base");
    }
}
