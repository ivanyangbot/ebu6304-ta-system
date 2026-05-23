package com.bupt.tarecruitment.service;

import com.bupt.tarecruitment.model.Applicant;
import com.bupt.tarecruitment.model.CandidateFitSnapshot;
import com.bupt.tarecruitment.model.Job;
import com.bupt.tarecruitment.util.SkillProfileUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class ShortlistPlannerService {
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

    private String decideNextStep(double fitScore, int gapCount) {
        if (fitScore >= 85.0 && gapCount == 0) {
            return "Advance";
        }
        if (fitScore >= 65.0) {
            return "Discuss";
        }
        return "Hold";
    }

    private double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }

    private String safeName(Applicant applicant) {
        if (applicant == null || applicant.getFullName() == null) {
            return "";
        }
        return applicant.getFullName();
    }
}
