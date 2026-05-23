package com.bupt.tarecruitment.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AvailabilityLedgerRepository {
    private final Map<String, Integer> reservedHoursByApplicantId = new HashMap<>();

    public void reserve(String applicantId, int hours) {
        if (applicantId == null || applicantId.trim().isEmpty()) {
            return;
        }
        int safeHours = Math.max(hours, 0);
        reservedHoursByApplicantId.put(applicantId, getReservedHours(applicantId) + safeHours);
    }

    public void release(String applicantId, int hours) {
        if (applicantId == null || applicantId.trim().isEmpty()) {
            return;
        }
        int updated = getReservedHours(applicantId) - Math.max(hours, 0);
        reservedHoursByApplicantId.put(applicantId, Math.max(updated, 0));
    }

    public int getReservedHours(String applicantId) {
        if (applicantId == null) {
            return 0;
        }
        Integer value = reservedHoursByApplicantId.get(applicantId);
        return value == null ? 0 : value;
    }

    public Map<String, Integer> snapshot() {
        return Collections.unmodifiableMap(new HashMap<>(reservedHoursByApplicantId));
    }
}
