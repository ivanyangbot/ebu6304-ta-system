package com.bupt.tarecruitment.service;

import com.bupt.tarecruitment.model.Applicant;
import com.bupt.tarecruitment.model.ApplicationPriorityView;
import com.bupt.tarecruitment.model.Job;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ApplicantRankingService}.
 *
 * <p>Verifies composite scoring, result ordering, decision band classification,
 * and null/empty edge cases.</p>
 */
class ApplicantRankingServiceTest {

    private ApplicantRankingService rankingService;

    @BeforeEach
    void setUp() {
        rankingService = new ApplicantRankingService();
    }

    private Job makeJob(List<String> requiredSkills) {
        return new Job("j1", "TA", "Module", "desc", requiredSkills, 5, "mo1", "Open");
    }

    private Applicant makeApplicant(String id, List<String> skills, String intro) {
        Applicant a = new Applicant(id, "user" + id, "pass", "APPLICANT", "Name " + id, id + "@test.com", skills);
        a.setSelfIntroduction(intro);
        return a;
    }

    // ---- Null guards ----

    @Test
    void testRankApplicants_nullJob_returnsEmptyList() {
        List<ApplicationPriorityView> result = rankingService.rankApplicants(
                null, Collections.singletonList(makeApplicant("1", Collections.emptyList(), "")));
        assertTrue(result.isEmpty());
    }

    @Test
    void testRankApplicants_nullApplicants_returnsEmptyList() {
        List<ApplicationPriorityView> result = rankingService.rankApplicants(makeJob(Collections.emptyList()), null);
        assertTrue(result.isEmpty());
    }

    @Test
    void testRankApplicants_emptyApplicants_returnsEmptyList() {
        List<ApplicationPriorityView> result = rankingService.rankApplicants(
                makeJob(Collections.emptyList()), Collections.emptyList());
        assertTrue(result.isEmpty());
    }

    // ---- Correct number of results ----

    @Test
    void testRankApplicants_threeApplicants_returnsThreeResults() {
        Job job = makeJob(Arrays.asList("Java", "Python"));
        List<Applicant> applicants = Arrays.asList(
                makeApplicant("1", Arrays.asList("Java", "Python"), "Long intro text here"),
                makeApplicant("2", Arrays.asList("Java"), ""),
                makeApplicant("3", Collections.emptyList(), ""));

        List<ApplicationPriorityView> result = rankingService.rankApplicants(job, applicants);

        assertEquals(3, result.size());
    }

    // ---- Results sorted descending by score ----

    @Test
    void testRankApplicants_sortedDescendingByScore() {
        Job job = makeJob(Arrays.asList("Java", "Python"));
        Applicant strong = makeApplicant("1", Arrays.asList("Java", "Python"),
                "This is a detailed introduction for the position");
        Applicant weak = makeApplicant("2", Collections.emptyList(), "");

        List<ApplicationPriorityView> result = rankingService.rankApplicants(job, Arrays.asList(weak, strong));

        // Strong candidate must appear first
        assertEquals("1", result.get(0).getApplicantId());
    }

    // ---- Decision band ----

    @Test
    void testRankApplicants_highScoreApplicant_decisionBandHigh() {
        Job job = makeJob(Arrays.asList("Java", "Python"));
        // Full skill match + non-empty introduction → high composite score
        Applicant top = makeApplicant("1", Arrays.asList("Java", "Python"),
                "Experienced TA with strong Java and Python background willing to support all labs.");

        List<ApplicationPriorityView> result = rankingService.rankApplicants(job, List.of(top));

        assertEquals("High", result.get(0).getDecisionBand());
    }

    @Test
    void testRankApplicants_noSkillsNoIntro_decisionBandLow() {
        Job job = makeJob(Arrays.asList("Java", "Python"));
        Applicant weak = makeApplicant("2", Collections.emptyList(), "");

        List<ApplicationPriorityView> result = rankingService.rankApplicants(job, List.of(weak));

        assertEquals("Low", result.get(0).getDecisionBand());
    }

    // ---- Score between 0 and 100 ----

    @Test
    void testRankApplicants_scoresBoundedCorrectly() {
        Job job = makeJob(Arrays.asList("Java"));
        Applicant a = makeApplicant("1", Arrays.asList("Java"), "intro");

        List<ApplicationPriorityView> result = rankingService.rankApplicants(job, List.of(a));

        double score = result.get(0).getPriorityScore();
        assertTrue(score >= 0.0 && score <= 100.0,
                "Score should be in [0, 100] but was: " + score);
    }

    // ---- Job ID is propagated ----

    @Test
    void testRankApplicants_jobIdPropagatedToView() {
        Job job = makeJob(Collections.emptyList());
        Applicant a = makeApplicant("1", Collections.emptyList(), "");

        List<ApplicationPriorityView> result = rankingService.rankApplicants(job, List.of(a));

        assertEquals("j1", result.get(0).getJobId());
    }
}
