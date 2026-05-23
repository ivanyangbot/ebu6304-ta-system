package com.bupt.tarecruitment.util;

import com.bupt.tarecruitment.model.Applicant;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public final class SkillProfileUtil {
    private SkillProfileUtil() {
    }

    public static Set<String> normalize(List<String> skills) {
        Set<String> normalized = new LinkedHashSet<>();
        if (skills == null) {
            return normalized;
        }

        for (String skill : skills) {
            if (skill == null) {
                continue;
            }
            String cleaned = skill.trim().toLowerCase(Locale.ENGLISH);
            if (!cleaned.isEmpty()) {
                normalized.add(cleaned);
            }
        }
        return normalized;
    }

    public static double estimateProfileCompleteness(Applicant applicant) {
        if (applicant == null) {
            return 0.0;
        }

        double score = 0.0;
        if (!normalize(applicant.getSkills()).isEmpty()) {
            score += 45.0;
        }
        if (applicant.getSelfIntroduction() != null && applicant.getSelfIntroduction().trim().length() >= 30) {
            score += 35.0;
        }
        if (applicant.getEmail() != null && applicant.getEmail().contains("@")) {
            score += 20.0;
        }
        return score;
    }
}
