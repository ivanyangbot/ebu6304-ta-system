package com.bupt.tarecruitment.service;

import com.bupt.tarecruitment.model.Applicant;
import com.bupt.tarecruitment.model.CandidateFitSnapshot;
import com.bupt.tarecruitment.model.Job;
import com.bupt.tarecruitment.util.SkillProfileUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * Builds a ranked shortlist of candidates for a job posting.
 *
 * <p>Unlike {@link ApplicantRankingService}, which produces a lightweight
 * priority view, this service generates richer {@link CandidateFitSnapshot}
 * objects that include skill gap analysis and a recommended next step.</p>
 *
 * <p>The fit score is computed as:</p>
 * <pre>
 *   fitScore = (coverageScore × 0.85) + statementBonus
 * </pre>
 * where {@code coverageScore} is the percentage of required skills covered and
 * {@code statementBonus} is up to 15 points based on the length of the
 * applicant's self-introduction.</p>
 *
 * <p>Next-step recommendations:</p>
 * <ul>
 *   <li>{@code "Advance"} – fit score ≥ 85 and no skill gaps</li>
 *   <li>{@code "Discuss"} – fit score ≥ 65</li>
 *   <li>{@code "Hold"}    – otherwise</li>
 * </ul>
 *
 * <p>Results are sorted in descending order of fit score, with alphabetical
 * order by full name as a tiebreaker.</p>
 *
 * @author  Group 71
 * @version 1.0
 * @see     CandidateFitSnapshot
 * @see     SkillProfileUtil
 */
public class ShortlistPlannerService {

    /**
     * Creates a ranked shortlist of candidates for a given job.
     *
     * @param job        the job posting; returns empty list if {@code null}
     * @param applicants the pool of applicants to evaluate; returns empty list if {@code null}
     * @return sorted list of {@link CandidateFitSnapshot} objects (highest fit score first)
     */
    public List<CandidateFitSnapshot> createShortlist(Job job, List<Applicant> applicants) {
        List<CandidateFitSnapshot> shortlist = new ArrayList<>();
        if (job == null || applicants == null) {
            return shortlist;
        }

        Set<String> requiredSkills = SkillProfileUtil.normalize(job.getRequiredSkills());
        for (Applicant applicant : applicants) {
            if (applicant == null) {
                continue;
            }

            Set<String> applicantSkills = SkillProfileUtil.normalize(applicant.getSkills());
            List<String> alignedSkills = new ArrayList<>();
            List<String> gapSkills = new ArrayList<>();

            for (String requiredSkill : requiredSkills) {
                if (applicantSkills.contains(requiredSkill)) {
                    alignedSkills.add(requiredSkill);
                } else {
                    gapSkills.add(requiredSkill);
                }
            }

            double coverageScore = requiredSkills.isEmpty()
                    ? 100.0
                    : alignedSkills.size() * 100.0 / requiredSkills.size();
            double statementBonus = calculateStatementBonus(applicant.getSelfIntroduction());
            double fitScore = round(coverageScore * 0.85 + statementBonus);

            shortlist.add(new CandidateFitSnapshot(
                    applicant,
                    fitScore,
                    alignedSkills,
                    gapSkills,
                    decideNextStep(fitScore, gapSkills.size())
            ));
        }

        shortlist.sort(Comparator.comparingDouble(CandidateFitSnapshot::getFitScore).reversed()
                .thenComparing(item -> safeName(item.getApplicant())));
        return shortlist;
    }

    /**
     * Calculates a bonus score based on the length of the self-introduction.
     *
     * <ul>
     *   <li>≥ 240 characters → 15.0 points</li>
     *   <li>≥ 120 characters → 10.0 points</li>
     *   <li>≥  40 characters →  5.0 points</li>
     *   <li>&lt; 40 characters → 0.0 points</li>
     * </ul>
     *
     * @param statement the applicant's self-introduction text; {@code null} returns 0.0
     * @return bonus score (0.0, 5.0, 10.0, or 15.0)
     */
    private double calculateStatementBonus(String statement) {
        if (statement == null) {
            return 0.0;
        }

        int length = statement.trim().length();
        if (length >= 240) {
            return 15.0;
        }
        if (length >= 120) {
            return 10.0;
        }
        if (length >= 40) {
            return 5.0;
        }
        return 0.0;
    }

    /**
     * Determines the next-step recommendation based on the fit score and skill gaps.
     *
     * @param fitScore fit score (0.0 – 100.0+)
     * @param gapCount number of skills the applicant is missing
     * @return {@code "Advance"}, {@code "Discuss"}, or {@code "Hold"}
     */
    private String decideNextStep(double fitScore, int gapCount) {
        if (fitScore >= 85.0 && gapCount == 0) {
            return "Advance";
        }
        if (fitScore >= 65.0) {
            return "Discuss";
        }
        return "Hold";
    }

    /**
     * Rounds a value to one decimal place.
     *
     * @param value the value to round
     * @return value rounded to 1 d.p.
     */
    private double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }

    /**
     * Null-safe helper that returns the applicant's full name or an empty string.
     *
     * @param applicant the applicant (may be {@code null})
     * @return full name string or {@code ""}
     */
    private String safeName(Applicant applicant) {
        if (applicant == null || applicant.getFullName() == null) {
            return "";
        }
        return applicant.getFullName();
    }
}
