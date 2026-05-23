package com.bupt.tarecruitment.service;

import com.bupt.tarecruitment.model.Applicant;
import com.bupt.tarecruitment.model.ApplicationPriorityView;
import com.bupt.tarecruitment.model.Job;
import com.bupt.tarecruitment.util.SkillProfileUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class ApplicantRankingService {
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

    private int countMatches(Set<String> requiredSkills, Set<String> applicantSkills) {
        int count = 0;
        for (String requiredSkill : requiredSkills) {
            if (applicantSkills.contains(requiredSkill)) {
                count++;
            }
        }
        return count;
    }

    private String classify(double finalScore) {
        if (finalScore >= 85.0) {
            return "High";
        }
        if (finalScore >= 60.0) {
            return "Medium";
        }
        return "Low";
    }

    private double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}
