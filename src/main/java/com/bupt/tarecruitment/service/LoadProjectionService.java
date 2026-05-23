package com.bupt.tarecruitment.service;

import com.bupt.tarecruitment.model.Applicant;
import com.bupt.tarecruitment.model.Job;
import com.bupt.tarecruitment.model.LoadProjection;
import com.bupt.tarecruitment.util.WorkloadBandUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoadProjectionService {
    public LoadProjection project(Job job, Applicant applicant, Map<String, Integer> currentHoursByApplicantId) {
        if (job == null || applicant == null) {
            return null;
        }

        int currentHours = lookupHours(applicant.getId(), currentHoursByApplicantId);
        int projectedHours = currentHours + Math.max(job.getHours(), 0);
        String workloadBand = WorkloadBandUtil.bandOf(projectedHours);
        String summary = buildSummary(currentHours, job.getHours(), workloadBand);

        return new LoadProjection(job, applicant, projectedHours, workloadBand, summary);
    }

    public List<LoadProjection> projectBatch(List<Job> jobs, Applicant applicant,
                                             Map<String, Integer> currentHoursByApplicantId) {
        List<LoadProjection> projections = new ArrayList<>();
        if (jobs == null || applicant == null) {
            return projections;
        }

        for (Job job : jobs) {
            LoadProjection projection = project(job, applicant, currentHoursByApplicantId);
            if (projection != null) {
                projections.add(projection);
            }
        }
        return projections;
    }

    private int lookupHours(String applicantId, Map<String, Integer> currentHoursByApplicantId) {
        if (applicantId == null || currentHoursByApplicantId == null) {
            return 0;
        }
        Integer value = currentHoursByApplicantId.get(applicantId);
        return value == null ? 0 : Math.max(value, 0);
    }

    private String buildSummary(int currentHours, int jobHours, String workloadBand) {
        int safeJobHours = Math.max(jobHours, 0);
        return "Current hours: " + currentHours
                + ", job hours: " + safeJobHours
                + ", projected band: " + workloadBand;
    }
}
