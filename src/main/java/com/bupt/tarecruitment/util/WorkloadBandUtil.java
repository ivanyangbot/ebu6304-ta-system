package com.bupt.tarecruitment.util;

/**
 * Utility class that classifies a total hours value into a workload band.
 *
 * <p>Workload bands are used throughout the system to give a human-readable
 * label to an applicant's projected or actual weekly hours. The thresholds
 * are defined as follows:</p>
 *
 * <table border="1">
 *   <caption>Workload band thresholds</caption>
 *   <tr><th>Band</th><th>Hours range</th></tr>
 *   <tr><td>{@code "Light"}   </td><td>0 – 59 hrs/week</td></tr>
 *   <tr><td>{@code "Normal"}  </td><td>60 – 109 hrs/week</td></tr>
 *   <tr><td>{@code "Heavy"}   </td><td>110 – 159 hrs/week</td></tr>
 *   <tr><td>{@code "Critical"}</td><td>≥ 160 hrs/week</td></tr>
 * </table>
 *
 * <p>This class is non-instantiable (utility class pattern).</p>
 *
 * @author  Group 71
 * @version 1.0
 * @see     com.bupt.tarecruitment.service.LoadProjectionService
 */
public final class WorkloadBandUtil {

    /** Private constructor to prevent instantiation. */
    private WorkloadBandUtil() {
    }

    /**
     * Returns the workload band label for the given total hours.
     *
     * <p>Negative values are treated as zero.</p>
     *
     * @param totalHours total accepted weekly hours (may be negative, treated as 0)
     * @return one of {@code "Light"}, {@code "Normal"}, {@code "Heavy"}, or {@code "Critical"}
     */
    public static String bandOf(int totalHours) {
        int safeHours = Math.max(totalHours, 0);
        if (safeHours >= 160) {
            return "Critical";
        }
        if (safeHours >= 110) {
            return "Heavy";
        }
        if (safeHours >= 60) {
            return "Normal";
        }
        return "Light";
    }

    /**
     * Checks whether an applicant can accept additional hours without exceeding the
     * system-wide hard cap of 180 total hours per week.
     *
     * <p>Both parameters are clamped to zero if negative.</p>
     *
     * @param totalHours    the applicant's current accepted weekly hours
     * @param incomingHours the hours associated with the new position being considered
     * @return {@code true} if the projected total is strictly less than 180
     */
    public static boolean canAcceptMore(int totalHours, int incomingHours) {
        return Math.max(totalHours, 0) + Math.max(incomingHours, 0) < 180;
    }
}
