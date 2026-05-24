package com.bupt.tarecruitment.service;

import com.bupt.tarecruitment.model.SkillRecommendation;
import com.bupt.tarecruitment.model.SkillRecommendation.ResourceLink;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link AiRecommendationService}.
 *
 * <p>These tests exercise the service's pure-Java logic: constructor validation,
 * sanitisation, URL validation, static fallback catalogue, and input guards.
 * Tests that would require a live Volcano Engine API call are intentionally
 * excluded – the service's internal fallback mechanism is tested instead by
 * supplying an invalid API key, which causes the HTTP call to fail gracefully
 * and fall back to the built-in static resource catalogue.</p>
 *
 * @author  Group 71
 * @version 1.0
 * @see     AiRecommendationService
 */
class AiRecommendationServiceTest {

    private AiRecommendationService service;

    @BeforeEach
    void setUp() {
        // A syntactically valid but functionally invalid key triggers fallback
        service = new AiRecommendationService("test-api-key-placeholder");
    }

    // =========================================================================
    // Constructor validation
    // =========================================================================

    @Test
    void constructor_blankApiKey_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new AiRecommendationService(""),
                "Blank API key should be rejected");
    }

    @Test
    void constructor_nullApiKey_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new AiRecommendationService(null),
                "Null API key should be rejected");
    }

    @Test
    void constructor_validApiKey_doesNotThrow() {
        assertDoesNotThrow(() -> new AiRecommendationService("some-valid-key"));
    }

    // =========================================================================
    // recommend() – empty / null input guard
    // =========================================================================

    @Test
    void recommend_nullMissingSkills_returnsEmptyList() {
        List<SkillRecommendation> result =
                service.recommend(null, "Lab TA", "EBU6304");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void recommend_emptyMissingSkills_returnsEmptyList() {
        List<SkillRecommendation> result =
                service.recommend(Collections.emptyList(), "Lab TA", "EBU6304");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // =========================================================================
    // recommend() – static fallback (invalid key → network failure → fallback)
    // =========================================================================

    @Test
    void recommend_invalidApiKey_fallbackUsed_returnsOneRecPerMissingSkill() {
        AiRecommendationService svc = new AiRecommendationService("invalid-key-xyz");
        List<String> missing = Arrays.asList("Java", "Python");

        List<SkillRecommendation> recs = svc.recommend(missing, "Lab TA", "EBU6304");

        // Must return exactly one recommendation per missing skill
        assertEquals(2, recs.size());
    }

    @Test
    void recommend_fallback_knownSkill_returnsHttpsLinks() {
        AiRecommendationService svc = new AiRecommendationService("invalid-key");
        List<SkillRecommendation> recs =
                svc.recommend(List.of("Java"), "Module TA", "CS101");

        SkillRecommendation rec = recs.get(0);
        assertNotNull(rec.getResourceLinks());
        assertFalse(rec.getResourceLinks().isEmpty(),
                "Static fallback should provide at least one resource link for Java");

        for (ResourceLink link : rec.getResourceLinks()) {
            assertTrue(link.getUrl().startsWith("https://"),
                    "Resource URLs must use HTTPS: " + link.getUrl());
        }
    }

    @Test
    void recommend_fallback_unknownSkill_returnsGenericLinks() {
        AiRecommendationService svc = new AiRecommendationService("invalid-key");
        List<SkillRecommendation> recs =
                svc.recommend(List.of("Quantum Entanglement"), "Lab TA", "PHYS999");

        SkillRecommendation rec = recs.get(0);
        assertFalse(rec.getResourceLinks().isEmpty(),
                "Generic fallback links should be returned for unknown skills");
    }

    @Test
    void recommend_fallback_multipleSkills_eachHasReasonAndLinks() {
        AiRecommendationService svc = new AiRecommendationService("invalid-key");
        List<String> missing = Arrays.asList("Java", "SQL", "Docker");

        List<SkillRecommendation> recs = svc.recommend(missing, "Dev TA", "EBU4201");

        assertEquals(3, recs.size());
        for (SkillRecommendation rec : recs) {
            assertNotNull(rec.getSkill(), "Skill name must not be null");
            assertNotNull(rec.getReason(), "Reason must not be null");
            assertFalse(rec.getResourceLinks().isEmpty(), "Resource links must not be empty");
        }
    }

    // =========================================================================
    // sanitise()
    // =========================================================================

    @Test
    void sanitise_nullInput_returnsEmptyString() {
        assertEquals("", service.sanitise(null));
    }

    @Test
    void sanitise_emptyString_returnsEmptyString() {
        assertEquals("", service.sanitise(""));
    }

    @Test
    void sanitise_htmlTags_stripped() {
        String result = service.sanitise("<b>Important</b> skill <script>alert(1)</script>");
        assertFalse(result.contains("<b>"), "HTML bold tags should be stripped");
        assertFalse(result.contains("<script>"), "Script tags should be stripped");
        assertTrue(result.contains("Important"), "Text content should be preserved");
    }

    @Test
    void sanitise_longString_truncatedAtLimit() {
        String longStr = "A".repeat(700);
        String result = service.sanitise(longStr);

        assertTrue(result.length() <= 603,
                "Sanitised string should be capped at 600 chars + ellipsis");
        assertTrue(result.endsWith("\u2026"), "Truncated string should end with ellipsis");
    }

    @Test
    void sanitise_controlCharacters_removed() {
        String withCtrl = "Hello\u0007World\u0001Test";
        String result = service.sanitise(withCtrl);
        assertFalse(result.contains("\u0007"), "Control char BEL should be removed");
        assertFalse(result.contains("\u0001"), "Control char SOH should be removed");
        assertTrue(result.contains("Hello"), "Normal text should be preserved");
        assertTrue(result.contains("World"), "Normal text should be preserved");
    }

    @Test
    void sanitise_normalText_preservedExactly() {
        String normal = "Learn Java using official tutorials and practice exercises.";
        assertEquals(normal, service.sanitise(normal));
    }

    @Test
    void sanitise_multipleBlankLines_collapsed() {
        String input = "Step 1\n\n\n\nStep 2";
        String result = service.sanitise(input);
        assertFalse(result.contains("\n\n\n"),
                "Three or more consecutive newlines should be collapsed");
    }

    // =========================================================================
    // SkillRecommendation model tests
    // =========================================================================

    @Test
    void skillRecommendation_defaultConstructor_emptyResourceLinks() {
        SkillRecommendation rec = new SkillRecommendation();
        assertNotNull(rec.getResourceLinks());
        assertTrue(rec.getResourceLinks().isEmpty());
        assertEquals(0, rec.getEstimatedHours());
    }

    @Test
    void skillRecommendation_setEstimatedHours_negativeClampedToZero() {
        SkillRecommendation rec = new SkillRecommendation();
        rec.setEstimatedHours(-10);
        assertEquals(0, rec.getEstimatedHours(), "Negative hours should be clamped to 0");
    }

    @Test
    void skillRecommendation_nullResourceLinks_convertedToEmptyList() {
        SkillRecommendation rec = new SkillRecommendation();
        rec.setResourceLinks(null);
        assertNotNull(rec.getResourceLinks());
        assertTrue(rec.getResourceLinks().isEmpty());
    }

    @Test
    void skillRecommendation_fullConstructor_fieldsSetCorrectly() {
        List<ResourceLink> links = List.of(new ResourceLink("Oracle", "https://docs.oracle.com"));
        SkillRecommendation rec = new SkillRecommendation("Java", "Core language for TA role",
                "1. Learn basics | 2. Practice", links, 30);

        assertEquals("Java", rec.getSkill());
        assertEquals("Core language for TA role", rec.getReason());
        assertEquals("1. Learn basics | 2. Practice", rec.getLearningPath());
        assertEquals(1, rec.getResourceLinks().size());
        assertEquals(30, rec.getEstimatedHours());
    }

    // =========================================================================
    // ResourceLink nested class
    // =========================================================================

    @Test
    void resourceLink_constructorAndGetters_correct() {
        ResourceLink link = new ResourceLink("Oracle Java Tutorials",
                "https://docs.oracle.com/javase/tutorial/");

        assertEquals("Oracle Java Tutorials", link.getLabel());
        assertEquals("https://docs.oracle.com/javase/tutorial/", link.getUrl());
    }

    @Test
    void resourceLink_defaultConstructorAndSetters_updateValues() {
        ResourceLink link = new ResourceLink();
        link.setLabel("New Label");
        link.setUrl("https://example.com");

        assertEquals("New Label", link.getLabel());
        assertEquals("https://example.com", link.getUrl());
    }

    // =========================================================================
    // Static fallback catalogue – spot-check specific skills
    // =========================================================================

    @Test
    void fallback_python_hasAtLeastTwoLinks() {
        AiRecommendationService svc = new AiRecommendationService("invalid-key");
        List<SkillRecommendation> recs = svc.recommend(List.of("Python"), "TA", "Module");

        assertTrue(recs.get(0).getResourceLinks().size() >= 2,
                "Python fallback should have at least 2 resources");
    }

    @Test
    void fallback_git_hasLinks() {
        AiRecommendationService svc = new AiRecommendationService("invalid-key");
        List<SkillRecommendation> recs = svc.recommend(List.of("Git"), "TA", "Module");

        assertFalse(recs.get(0).getResourceLinks().isEmpty(),
                "Git fallback should provide learning resources");
    }

    @Test
    void fallback_allLinksAreHttps() {
        AiRecommendationService svc = new AiRecommendationService("invalid-key");
        List<String> skills = Arrays.asList("Java", "Python", "SQL", "Git", "Docker",
                "Machine Learning", "JavaScript", "Linux");

        for (String skill : skills) {
            List<SkillRecommendation> recs = svc.recommend(List.of(skill), "TA", "CS");
            for (ResourceLink link : recs.get(0).getResourceLinks()) {
                assertTrue(link.getUrl().startsWith("https://"),
                        "All static resource URLs must use HTTPS — failed for skill: " + skill
                                + ", URL: " + link.getUrl());
            }
        }
    }
}
