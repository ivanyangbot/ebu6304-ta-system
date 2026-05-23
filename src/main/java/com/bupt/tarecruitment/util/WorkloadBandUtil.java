package com.bupt.tarecruitment.util;

public final class WorkloadBandUtil {
    private WorkloadBandUtil() {
    }

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

    public static boolean canAcceptMore(int totalHours, int incomingHours) {
        return Math.max(totalHours, 0) + Math.max(incomingHours, 0) < 180;
    }
}
