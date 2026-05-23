package com.bupt.tarecruitment.util;

import com.bupt.tarecruitment.model.Applicant;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link SkillProfileUtil}.
 *
 * <p>Verifies skill normalisation (deduplication, lower-casing, trimming)
 * and profile completeness scoring logic.</p>
 */
class SkillProfileUtilTest {

    // ---- normalize ----

    @Test
    void testNormalize_lowercasesSkills() {
        Set<String> result = SkillProfileUtil.normalize(Arrays.asList("Java", "PYTHON", "sql"));
        assertTrue(result.contains("java"));
        assertTrue(result.contains("python"));
        assertTrue(result.contains("sql"));
    }

    @Test
    void testNormalize_trimsWhitespace() {
        Set<String> result = SkillProfileUtil.normalize(Arrays.asList("  java  ", " python "));
        assertTrue(result.contains("java"));
        assertTrue(result.contains("python"));
    }

    @Test
    void testNormalize_deduplicatesSkills() {
        Set<String> result = SkillProfileUtil.normalize(Arrays.asList("Java", "java", "JAVA"));
        assertEquals(1, result.size());
    }

    @Test
    void testNormalize_removesBlankEntries() {
        Set<String> result = SkillProfileUtil.normalize(Arrays.asList("Java", "", "  ", null));
        assertEquals(1, result.size());
    }

    @Test
    void testNormalize_nullInput_returnsEmptySet() {
        Set<String> result = SkillProfileUtil.normalize(null);
        assertTrue(result.isEmpty());
    }

    @Test
    void testNormalize_emptyList_returnsEmptySet() {
        Set<String> result = SkillProfileUtil.normalize(Collections.emptyList());
        assertTrue(result.isEmpty());
    }

    // ---- estimateProfileCompleteness ----

    @Test
    void testEstimateProfileCompleteness_nullApplicant_returns0() {
        assertEquals(0.0, SkillProfileUtil.estimateProfileCompleteness(null), 0.01);
    }

    @Test
    void testEstimateProfileCompleteness_emptyProfile_returns0() {
        Applicant a = new Applicant("1", "u", "p", "APPLICANT", "Name", "no-at-symbol",
                Collections.emptyList(), "");
        assertEquals(0.0, SkillProfileUtil.estimateProfileCompleteness(a), 0.01);
    }

    @Test
    void testEstimateProfileCompleteness_skillsOnly_returns45() {
        Applicant a = new Applicant("1", "u", "p", "APPLICANT", "Name", "no-at",
                Arrays.asList("Java"), "");
        // skills → 45 pts; no email @; no intro ≥ 30 chars
        assertEquals(45.0, SkillProfileUtil.estimateProfileCompleteness(a), 0.01);
    }

    @Test
    void testEstimateProfileCompleteness_skillsPlusEmail_returns65() {
        Applicant a = new Applicant("1", "u", "p", "APPLICANT", "Name", "u@test.com",
                Arrays.asList("Java"), "");
        // skills(45) + email(20) = 65
        assertEquals(65.0, SkillProfileUtil.estimateProfileCompleteness(a), 0.01);
    }

    @Test
    void testEstimateProfileCompleteness_fullProfile_returns100() {
        Applicant a = new Applicant("1", "u", "p", "APPLICANT", "Name", "u@test.com",
                Arrays.asList("Java"),
                "I am a dedicated student with experience in software engineering and Java programming.");
        // skills(45) + intro≥30chars(35) + email(20) = 100
        assertEquals(100.0, SkillProfileUtil.estimateProfileCompleteness(a), 0.01);
    }

    @Test
    void testEstimateProfileCompleteness_introTooShort_introNotCounted() {
        Applicant a = new Applicant("1", "u", "p", "APPLICANT", "Name", "u@test.com",
                Arrays.asList("Java"), "Short.");
        // "Short." is 6 chars, less than 30 → intro NOT counted
        // skills(45) + email(20) = 65
        assertEquals(65.0, SkillProfileUtil.estimateProfileCompleteness(a), 0.01);
    }
}
