package com.bupt.tarecruitment.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link WorkloadBandUtil}.
 *
 * <p>Verifies band classification thresholds and the {@code canAcceptMore} check.</p>
 */
class WorkloadBandUtilTest {

    // ---- bandOf ----

    @ParameterizedTest(name = "hours={0} → band={1}")
    @CsvSource({
        "0,   Light",
        "1,   Light",
        "59,  Light",
        "60,  Normal",
        "109, Normal",
        "110, Heavy",
        "159, Heavy",
        "160, Critical",
        "200, Critical"
    })
    void testBandOf_variousHours(int hours, String expectedBand) {
        assertEquals(expectedBand, WorkloadBandUtil.bandOf(hours));
    }

    @Test
    void testBandOf_negativeHours_treatedAsZero_returnsLight() {
        assertEquals("Light", WorkloadBandUtil.bandOf(-10));
    }

    // ---- canAcceptMore ----

    @Test
    void testCanAcceptMore_sumLessThan180_returnsTrue() {
        assertTrue(WorkloadBandUtil.canAcceptMore(100, 50));
    }

    @Test
    void testCanAcceptMore_sumEquals180_returnsFalse() {
        assertFalse(WorkloadBandUtil.canAcceptMore(100, 80));
    }

    @Test
    void testCanAcceptMore_sumExceeds180_returnsFalse() {
        assertFalse(WorkloadBandUtil.canAcceptMore(150, 40));
    }

    @Test
    void testCanAcceptMore_bothZero_returnsTrue() {
        assertTrue(WorkloadBandUtil.canAcceptMore(0, 0));
    }

    @Test
    void testCanAcceptMore_negativeValues_treatedAsZero() {
        assertTrue(WorkloadBandUtil.canAcceptMore(-10, -5));
    }
}
