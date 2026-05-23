package com.bupt.tarecruitment.model;

/**
 * An immutable projection of an applicant's workload if a specific job is accepted.
 *
 * <p>A {@code LoadProjection} is produced by
 * {@link com.bupt.tarecruitment.service.LoadProjectionService} and shows how an
 * applicant's total accepted hours would change if they were accepted for a
 * given job. This information is displayed in the job-detail view to help both
 * applicants and MOs make informed decisions.</p>
 *
 * <p>Workload band values:</p>
 * <ul>
 *   <li>{@code "Normal"}     – projected hours within the threshold</li>
 *   <li>{@code "Near Limit"} – approaching the threshold</li>
 *   <li>{@code "Overloaded"} – projected hours exceed the threshold</li>
 * </ul>
 *
 * @author  Group 71
 * @version 1.0
 * @see     com.bupt.tarecruitment.service.LoadProjectionService
 * @see     com.bupt.tarecruitment.util.WorkloadBandUtil
 */
public class LoadProjection {

    /** The job being considered for acceptance. */
    private final Job job;

    /** The applicant for whom the projection is calculated. */
    private final Applicant applicant;

    /** Projected total weekly hours if this job is accepted. */
    private final int projectedHours;

    /**
     * Workload band label derived from the projected hours.
     * Defaults to {@code "Normal"} if {@code null} is provided.
     */
    private final String workloadBand;

    /**
     * Human-readable summary describing the projected workload impact.
     * Defaults to an empty string if {@code null} is provided.
     */
    private final String summary;

    /**
     * Constructs an immutable {@code LoadProjection}.
     *
     * @param job            the job being considered
     * @param applicant      the applicant
     * @param projectedHours projected total weekly hours after acceptance
     * @param workloadBand   workload band label; {@code null} defaults to {@code "Normal"}
     * @param summary        human-readable impact summary; {@code null} defaults to empty string
     */
    public LoadProjection(Job job, Applicant applicant, int projectedHours, String workloadBand, String summary) {
        this.job = job;
        this.applicant = applicant;
        this.projectedHours = projectedHours;
        this.workloadBand = workloadBand == null ? "Normal" : workloadBand;
        this.summary = summary == null ? "" : summary;
    }

    /**
     * Returns the job being evaluated.
     *
     * @return the {@link Job}
     */
    public Job getJob() { return job; }

    /**
     * Returns the applicant for whom the projection was calculated.
     *
     * @return the {@link Applicant}
     */
    public Applicant getApplicant() { return applicant; }

    /**
     * Returns the projected total weekly hours if this job is accepted.
     *
     * @return projected hours
     */
    public int getProjectedHours() { return projectedHours; }

    /**
     * Returns the workload band label.
     *
     * @return workload band string (e.g. {@code "Normal"}, {@code "Overloaded"})
     */
    public String getWorkloadBand() { return workloadBand; }

    /**
     * Returns the human-readable workload impact summary.
     *
     * @return summary string
     */
    public String getSummary() { return summary; }
}
