package com.bupt.tarecruitment.service;

import com.bupt.tarecruitment.model.MatchResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link MatchService}.
 *
 * <p>Verifies skill-matching logic including score calculation,
 * matched/missing skill detection, case-insensitivity, and edge cases.</p>
 */
class MatchServiceTest {

    private MatchService matchService;

    @BeforeEach
    void setUp() {
        matchService = new MatchService();
    }

    // ---- Perfect match ----

    @Test
    void testPerfectMatch_allSkillsPresent_score100() {
        List<String> applicantSkills = Arrays.asList("Java", "Python", "SQL");
        List<String> requiredSkills  = Arrays.asList("Java", "Python", "SQL");

        MatchResult result = matchService.calculateMatch(applicantSkills, requiredSkills);

        assertEquals(100.0, result.getScore(), 0.01);
        assertEquals(3, result.getMatchedSkills().size());
        assertTrue(result.getMissingSkills().isEmpty());
    }

    // ---- No match ----

    @Test
    void testNoMatch_noSkillsShared_score0() {
        List<String> applicantSkills = Arrays.asList("C++", "Rust");
        List<String> requiredSkills  = Arrays.asList("Java", "Python");

        MatchResult result = matchService.calculateMatch(applicantSkills, requiredSkills);

        assertEquals(0.0, result.getScore(), 0.01);
        assertTrue(result.getMatchedSkills().isEmpty());
        assertEquals(2, result.getMissingSkills().size());
    }

    // ---- Partial match ----

    @Test
    void testPartialMatch_someSkillsMissing_correctScore() {
        List<String> applicantSkills = Arrays.asList("Java", "SQL");
        List<String> requiredSkills  = Arrays.asList("Java", "Python", "SQL", "Git");

        MatchResult result = matchService.calculateMatch(applicantSkills, requiredSkills);

        // 2 matched out of 4 required = 50.0
        assertEquals(50.0, result.getScore(), 0.01);
        assertEquals(2, result.getMatchedSkills().size());
        assertEquals(2, result.getMissingSkills().size());
    }

    // ---- Case insensitivity ----

    @Test
    void testCaseInsensitiveMatch_differentCase_stillMatches() {
        List<String> applicantSkills = Arrays.asList("java", "PYTHON");
        List<String> requiredSkills  = Arrays.asList("Java", "Python");

        MatchResult result = matchService.calculateMatch(applicantSkills, requiredSkills);

        assertEquals(100.0, result.getScore(), 0.01);
        assertEquals(2, result.getMatchedSkills().size());
    }

    // ---- Whitespace trimming ----

    @Test
    void testSkillsWithWhitespace_trimmed_matchesCorrectly() {
        List<String> applicantSkills = Arrays.asList("  Java  ", "Python ");
        List<String> requiredSkills  = Arrays.asList("Java", "Python");

        MatchResult result = matchService.calculateMatch(applicantSkills, requiredSkills);

        assertEquals(100.0, result.getScore(), 0.01);
    }

    // ---- Empty required skills ----

    @Test
    void testEmptyRequiredSkills_scoreIs0() {
        List<String> applicantSkills = Arrays.asList("Java", "Python");
        List<String> requiredSkills  = Collections.emptyList();

        MatchResult result = matchService.calculateMatch(applicantSkills, requiredSkills);

        assertEquals(0.0, result.getScore(), 0.01);
        assertTrue(result.getMatchedSkills().isEmpty());
        assertTrue(result.getMissingSkills().isEmpty());
    }

    // ---- Null inputs ----

    @Test
    void testNullApplicantSkills_allRequiredAreMissing() {
        List<String> requiredSkills = Arrays.asList("Java", "Python");

        MatchResult result = matchService.calculateMatch(null, requiredSkills);

        assertEquals(0.0, result.getScore(), 0.01);
        assertEquals(2, result.getMissingSkills().size());
    }

    @Test
    void testNullRequiredSkills_scoreIs0() {
        List<String> applicantSkills = Arrays.asList("Java", "Python");

        MatchResult result = matchService.calculateMatch(applicantSkills, null);

        assertEquals(0.0, result.getScore(), 0.01);
    }

    // ---- Missing skills list is correct ----

    @Test
    void testMissingSkills_correctlyIdentified() {
        List<String> applicantSkills = Arrays.asList("Java");
        List<String> requiredSkills  = Arrays.asList("Java", "Python", "Git");

        MatchResult result = matchService.calculateMatch(applicantSkills, requiredSkills);

        assertTrue(result.getMissingSkills().contains("Python"));
        assertTrue(result.getMissingSkills().contains("Git"));
        assertFalse(result.getMissingSkills().contains("Java"));
    }

    // ---- Score rounding ----

    @Test
    void testScoreRoundedToOneDecimalPlace() {
        // 1 matched out of 3 required = 33.333...  → rounds to 33.3
        List<String> applicantSkills = Arrays.asList("Java");
        List<String> requiredSkills  = Arrays.asList("Java", "Python", "SQL");

        MatchResult result = matchService.calculateMatch(applicantSkills, requiredSkills);

        assertEquals(33.3, result.getScore(), 0.01);
    }
}
