package com.bupt.tarecruitment.service;

import com.bupt.tarecruitment.model.Applicant;
import com.bupt.tarecruitment.model.ApplicationPriorityView;
import com.bupt.tarecruitment.model.Job;
import com.bupt.tarecruitment.util.SkillProfileUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * Ranks a list of applicants for a given job posting using a composite scoring model.
 *
 * <p>The ranking algorithm combines two weighted components:</p>
 * <ul>
 *   <li><strong>Skill score (70%)</strong> – percentage of required skills covered by the applicant</li>
 *   <li><strong>Profile completeness score (30%)</strong> – estimated completeness of the applicant's
 *       profile as calculated by {@link SkillProfileUtil#estimateProfileCompleteness(Applicant)}</li>
 * </ul>
 *
 * <p>The composite score is mapped to a decision band:</p>
 * <ul>
 *   <li>{@code "High"}   – score ≥ 85.0</li>
 *   <li>{@code "Medium"} – score ≥ 60.0</li>
 *   <li>{@code "Low"}    – score &lt; 60.0</li>
 * </ul>
 *
 * <p>Results are returned in descending order of priority score.</p>
 *
 * @author  Group 71
 * @version 1.0
 * @see     ApplicationPriorityView
 * @see     SkillProfileUtil
 */
public class ApplicantRankingService {

    /**
     * Ranks the provided applicants for the given job.
     *
     * @param job        the job posting to rank applicants against; returns empty list if {@code null}
     * @param applicants the list of applicants to rank; returns empty list if {@code null}
     * @return list of {@link ApplicationPriorityView} objects sorted in descending order of priority score
     */
    public List<ApplicationPriorityView> rankApplicants(Job job, List<Applicant> applicants) {
        List<ApplicationPriorityView> results = new ArrayList<>();
        if (job == null || applicants == null) {
            return results;
        }

        Set<String> requiredSkills = SkillProfileUtil.normalize(job.getRequiredSkills());
        for (Applicant applicant : applicants) {
            if (applicant == null) {
                continue;
            }

            Set<String> applicantSkills = SkillProfileUtil.normalize(applicant.getSkills());
            int matchedSkills = countMatches(requiredSkills, applicantSkills);
            double skillScore = requiredSkills.isEmpty() ? 100.0 : matchedSkills * 100.0 / requiredSkills.size();
            double profileScore = SkillProfileUtil.estimateProfileCompleteness(applicant);
            double finalScore = round(skillScore * 0.7 + profileScore * 0.3);

            results.add(new ApplicationPriorityView(
                    applicant.getId(),
                    job.getId(),
                    finalScore,
                    classify(finalScore)
            ));
        }

        results.sort(Comparator.comparingDouble(ApplicationPriorityView::getPriorityScore).reversed());
        return results;
    }

    /**
     * Counts how many of the required skills are present in the applicant's skill set.
     *
     * @param requiredSkills  normalised set of required skills
     * @param applicantSkills normalised set of the applicant's skills
     * @return number of matched skills
     */
    private int countMatches(Set<String> requiredSkills, Set<String> applicantSkills) {
        int count = 0;
        for (String requiredSkill : requiredSkills) {
            if (applicantSkills.contains(requiredSkill)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Classifies a composite score into a decision band label.
     *
     * @param finalScore composite priority score (0.0 – 100.0)
     * @return {@code "High"}, {@code "Medium"}, or {@code "Low"}
     */
    private String classify(double finalScore) {
        if (finalScore >= 85.0) {
            return "High";
        }
        if (finalScore >= 60.0) {
            return "Medium";
        }
        return "Low";
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
}
