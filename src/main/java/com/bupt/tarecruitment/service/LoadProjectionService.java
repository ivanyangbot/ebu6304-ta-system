package com.bupt.tarecruitment.service;

import com.bupt.tarecruitment.model.Applicant;
import com.bupt.tarecruitment.model.Job;
import com.bupt.tarecruitment.model.LoadProjection;
import com.bupt.tarecruitment.util.WorkloadBandUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Projects the workload impact of accepting one or more job offers for an applicant.
 *
 * <p>This service estimates how an applicant's total accepted hours would change if a
 * given job were accepted, and classifies the result into a workload band using
 * {@link WorkloadBandUtil}. It is used to provide advisory information on the
 * job-detail view and the shortlisting tools.</p>
 *
 * <p>The projection is computed as:</p>
 * <pre>
 *   projectedHours = currentHours + job.getHours()
 * </pre>
 *
 * @author  Group 71
 * @version 1.0
 * @see     LoadProjection
 * @see     WorkloadBandUtil
 */
public class LoadProjectionService {

    /**
     * Projects the workload impact for a single applicant accepting a single job.
     *
     * @param job                        the job being considered; returns {@code null} if {@code null}
     * @param applicant                  the applicant; returns {@code null} if {@code null}
     * @param currentHoursByApplicantId  map of applicant ID → current accepted weekly hours
     * @return a {@link LoadProjection} with the projected hours, band, and summary,
     *         or {@code null} if either {@code job} or {@code applicant} is {@code null}
     */
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

    /**
     * Projects the workload impact for a single applicant across a batch of jobs.
     *
     * <p>Each projection is calculated independently using the applicant's current hours.
     * {@code null} jobs in the list are silently skipped.</p>
     *
     * @param jobs                       list of jobs to project; returns empty list if {@code null}
     * @param applicant                  the applicant; returns empty list if {@code null}
     * @param currentHoursByApplicantId  map of applicant ID → current accepted weekly hours
     * @return list of {@link LoadProjection} objects (one per non-null job)
     */
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

    /**
     * Looks up the current accepted hours for an applicant from the provided map.
     * Returns 0 if the applicant ID is not in the map, or if either parameter is {@code null}.
     *
     * @param applicantId               the applicant's user ID
     * @param currentHoursByApplicantId the lookup map
     * @return current accepted hours (≥ 0)
     */
    private int lookupHours(String applicantId, Map<String, Integer> currentHoursByApplicantId) {
        if (applicantId == null || currentHoursByApplicantId == null) {
            return 0;
        }
        Integer value = currentHoursByApplicantId.get(applicantId);
        return value == null ? 0 : Math.max(value, 0);
    }

    /**
     * Builds a human-readable summary of the workload projection.
     *
     * @param currentHours  current accepted hours
     * @param jobHours      hours for the job being projected
     * @param workloadBand  the derived workload band
     * @return formatted summary string
     */
    private String buildSummary(int currentHours, int jobHours, String workloadBand) {
        int safeJobHours = Math.max(jobHours, 0);
        return "Current hours: " + currentHours
                + ", job hours: " + safeJobHours
                + ", projected band: " + workloadBand;
    }
}
