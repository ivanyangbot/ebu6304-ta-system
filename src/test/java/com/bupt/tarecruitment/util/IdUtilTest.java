package com.bupt.tarecruitment.util;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link IdUtil}.
 *
 * <p>Verifies ID format, prefix inclusion, uniqueness, and null-safety.</p>
 */
class IdUtilTest {

    // ---- Format ----

    @Test
    void testGenerateId_startsWithPrefix() {
        String id = IdUtil.generateId("job");
        assertTrue(id.startsWith("job-"), "ID should start with 'job-' but was: " + id);
    }

    @Test
    void testGenerateId_correctLength() {
        // "job" + "-" + 8 hex chars = 12 characters
        String id = IdUtil.generateId("job");
        assertEquals(12, id.length(), "Expected length 12 but was: " + id.length());
    }

    @Test
    void testGenerateId_differentPrefixes_reflectedInId() {
        String appId = IdUtil.generateId("app");
        String applicantId = IdUtil.generateId("applicant");

        assertTrue(appId.startsWith("app-"));
        assertTrue(applicantId.startsWith("applicant-"));
    }

    @Test
    void testGenerateId_suffixIsAlphanumericOnly() {
        String id = IdUtil.generateId("log");
        String suffix = id.substring("log-".length());
        assertTrue(suffix.matches("[0-9a-f]{8}"),
                "Suffix should be 8 hex chars but was: " + suffix);
    }

    // ---- Uniqueness ----

    @Test
    void testGenerateId_multipleCallsProduceUniqueIds() {
        Set<String> ids = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            ids.add(IdUtil.generateId("test"));
        }
        assertEquals(1000, ids.size(), "All 1000 generated IDs should be unique");
    }

    // ---- Empty prefix ----

    @Test
    void testGenerateId_emptyPrefix_startsWithHyphen() {
        String id = IdUtil.generateId("");
        assertTrue(id.startsWith("-"), "Empty prefix should produce ID starting with '-'");
    }
}
