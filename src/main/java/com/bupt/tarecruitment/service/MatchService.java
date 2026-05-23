package com.bupt.tarecruitment.service;

import com.bupt.tarecruitment.model.MatchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Provides skill-matching logic between an applicant's declared skills and a
 * job's required skills.
 *
 * <p>The matching algorithm is case-insensitive and normalises each skill string
 * by trimming whitespace and capitalising the first letter before comparison.
 * The resulting {@link MatchResult} contains three components:</p>
 * <ul>
 *   <li>{@code matchedSkills} – skills the applicant has that satisfy the job requirements</li>
 *   <li>{@code missingSkills} – required skills the applicant does not possess</li>
 *   <li>{@code score}         – percentage of required skills covered (0.0 – 100.0, 1 d.p.)</li>
 * </ul>
 *
 * <p>Usage example:</p>
 * <pre>
 *   MatchService service = new MatchService();
 *   MatchResult result = service.calculateMatch(applicant.getSkills(), job.getRequiredSkills());
 * </pre>
 *
 * @author  Group 71
 * @version 1.0
 * @see     MatchResult
 */
public class MatchService {

    /**
     * Calculates the skill match between an applicant and a job.
     *
     * <p>Both skill lists are normalised before comparison:
     * leading/trailing whitespace is removed, the first character is uppercased,
     * and comparison is done case-insensitively.</p>
     *
     * @param applicantSkills list of skills declared by the applicant; {@code null} is treated as empty
     * @param requiredSkills  list of skills required by the job; {@code null} is treated as empty
     * @return a {@link MatchResult} containing matched skills, missing skills, and score
     */
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

    /**
     * Normalises a list of skill strings by trimming whitespace, removing blank entries,
     * and capitalising the first character of each skill.
     *
     * @param skills raw skill list; {@code null} returns an empty list
     * @return normalised, non-null list of skill strings
     */
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

    /**
     * Capitalises the first character of a string while leaving the rest unchanged.
     *
     * @param value the string to capitalise; must not be {@code null}
     * @return the capitalised string
     */
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
