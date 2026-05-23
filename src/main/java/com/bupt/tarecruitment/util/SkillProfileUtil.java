package com.bupt.tarecruitment.util;

import com.bupt.tarecruitment.model.Applicant;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Utility class providing skill normalisation and applicant profile completeness estimation.
 *
 * <p>This class is used by ranking and shortlisting services to ensure consistent
 * skill comparison and to derive a completeness score for applicant profiles.</p>
 *
 * <p>This class is non-instantiable (utility class pattern).</p>
 *
 * @author  Group 71
 * @version 1.0
 * @see     com.bupt.tarecruitment.service.ApplicantRankingService
 * @see     com.bupt.tarecruitment.service.ShortlistPlannerService
 */
public final class SkillProfileUtil {

    /** Private constructor to prevent instantiation. */
    private SkillProfileUtil() {
    }

    /**
     * Normalises a list of skill strings into a deduplicated, lowercase set.
     *
     * <p>Each skill is trimmed and converted to lower-case. Blank entries and
     * {@code null} elements are silently skipped. Insertion order is preserved
     * via {@link LinkedHashSet}.</p>
     *
     * @param skills the raw skill list; {@code null} returns an empty set
     * @return a normalised, non-null {@link Set} of lowercase skill strings
     */
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

    /**
     * Estimates the completeness of an applicant's profile as a score between 0.0 and 100.0.
     *
     * <p>The score is composed of three components:</p>
     * <ul>
     *   <li><strong>45 points</strong> – applicant has at least one declared skill</li>
     *   <li><strong>35 points</strong> – self-introduction is at least 30 characters long</li>
     *   <li><strong>20 points</strong> – email address contains an {@code @} character</li>
     * </ul>
     *
     * @param applicant the applicant to evaluate; {@code null} returns 0.0
     * @return profile completeness score in the range [0.0, 100.0]
     */
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
