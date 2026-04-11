package com.bupt.tarecruitment.service;

import com.bupt.tarecruitment.model.MatchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MatchService {
    public MatchResult calculateMatch(List<String> applicantSkills, List<String> requiredSkills) {
        List<String> normalizedApplicantSkills = normalizeSkills(applicantSkills);
        List<String> normalizedRequiredSkills = normalizeSkills(requiredSkills);
        List<String> matchedSkills = new ArrayList<>();
        List<String> missingSkills = new ArrayList<>();

        for (String requiredSkill : normalizedRequiredSkills) {
            boolean matched = false;
            for (String applicantSkill : normalizedApplicantSkills) {
                if (requiredSkill.equalsIgnoreCase(applicantSkill)) {
                    matched = true;
                    matchedSkills.add(requiredSkill);
                    break;
                }
            }
            if (!matched) {
                missingSkills.add(requiredSkill);
            }
        }

        double score = 0;
        if (!normalizedRequiredSkills.isEmpty()) {
            score = (double) matchedSkills.size() / normalizedRequiredSkills.size() * 100.0;
        }
        score = Math.round(score * 10.0) / 10.0;

        return new MatchResult(matchedSkills, missingSkills, score);
    }

    private List<String> normalizeSkills(List<String> skills) {
        List<String> result = new ArrayList<>();
        if (skills == null) {
            return result;
        }

        for (String skill : skills) {
            if (skill != null) {
                String cleaned = skill.trim();
                if (!cleaned.isEmpty()) {
                    result.add(capitalize(cleaned));
                }
            }
        }
        return result;
    }

    private String capitalize(String value) {
        if (value.isEmpty()) {
            return value;
        }
        if (value.length() == 1) {
            return value.toUpperCase(Locale.ENGLISH);
        }
        return value.substring(0, 1).toUpperCase(Locale.ENGLISH) + value.substring(1);
    }
}
