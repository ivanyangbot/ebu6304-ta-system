package com.bupt.tarecruitment.model;

/**
 * Composite view model used by JSP pages that display application records.
 *
 * <p>The object joins the raw application record with its related job,
 * applicant, match score, and optional decision-support data. Keeping these
 * values together prevents JSP pages from performing repository lookups or
 * business calculations directly.</p>
 *
 * <p>This class is not persisted to JSON. It is assembled per request by
 * servlets such as {@link com.bupt.tarecruitment.servlet.MyApplicationsServlet}
 * and {@link com.bupt.tarecruitment.servlet.MOApplicationsServlet} for display
 * only.</p>
 *
 * @author  Group 71
 * @version 1.0
 * @see     ApplicationRecord
 * @see     MatchResult
 * @see     ApplicationPriorityView
 * @see     CandidateFitSnapshot
 * @see     LoadProjection
 */
public class ApplicationDisplay {
    /** Raw application record being displayed. */
    private ApplicationRecord application;

    /** Job targeted by the application. */
    private Job job;

    /** Applicant who submitted the application. */
    private Applicant applicant;

    /** Skill-match result between the applicant profile and the target job. */
    private MatchResult matchResult;

    /** Optional ranking output used by the MO review table. */
    private ApplicationPriorityView priorityView;

    /** Optional shortlist guidance used by the MO review table. */
    private CandidateFitSnapshot fitSnapshot;

    /** Optional projected workload if the applicant is accepted for the job. */
    private LoadProjection loadProjection;

    /** i18n key suffix describing the current application status for applicants. */
    private String statusDescription;

    /**
     * Creates an empty display model for frameworks and JSP expression access.
     */
    public ApplicationDisplay() {
    }

    /**
     * Creates the base display model shared by applicant and MO application pages.
     *
     * @param application application record being displayed
     * @param job         related job posting
     * @param applicant   related applicant
     * @param matchResult skill match result for the applicant and job
     */
    public ApplicationDisplay(ApplicationRecord application, Job job, Applicant applicant, MatchResult matchResult) {
        this.application = application;
        this.job = job;
        this.applicant = applicant;
        this.matchResult = matchResult;
    }

    /**
     * Returns the raw application record.
     *
     * @return application record, or {@code null} if not assigned
     */
    public ApplicationRecord getApplication() {
        return application;
    }

    /**
     * Sets the raw application record.
     *
     * @param application application record to display
     */
    public void setApplication(ApplicationRecord application) {
        this.application = application;
    }

    /**
     * Returns the job related to the application.
     *
     * @return related job, or {@code null} if the job no longer exists
     */
    public Job getJob() {
        return job;
    }

    /**
     * Sets the job related to the application.
     *
     * @param job related job
     */
    public void setJob(Job job) {
        this.job = job;
    }

    /**
     * Returns the applicant related to the application.
     *
     * @return related applicant, or {@code null} if the applicant no longer exists
     */
    public Applicant getApplicant() {
        return applicant;
    }

    /**
     * Sets the applicant related to the application.
     *
     * @param applicant related applicant
     */
    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    /**
     * Returns the skill-match result for the application row.
     *
     * @return match result, or {@code null} when it has not been calculated
     */
    public MatchResult getMatchResult() {
        return matchResult;
    }

    /**
     * Sets the skill-match result for the application row.
     *
     * @param matchResult calculated match result
     */
    public void setMatchResult(MatchResult matchResult) {
        this.matchResult = matchResult;
    }

    /**
     * Returns the priority ranking result for MO review.
     *
     * @return priority view, or {@code null} outside the MO review page
     */
    public ApplicationPriorityView getPriorityView() {
        return priorityView;
    }

    /**
     * Sets the priority ranking result for MO review.
     *
     * @param priorityView ranking result to display
     */
    public void setPriorityView(ApplicationPriorityView priorityView) {
        this.priorityView = priorityView;
    }

    /**
     * Returns the shortlist fit snapshot for MO review.
     *
     * @return fit snapshot, or {@code null} outside the MO review page
     */
    public CandidateFitSnapshot getFitSnapshot() {
        return fitSnapshot;
    }

    /**
     * Sets the shortlist fit snapshot for MO review.
     *
     * @param fitSnapshot fit snapshot to display
     */
    public void setFitSnapshot(CandidateFitSnapshot fitSnapshot) {
        this.fitSnapshot = fitSnapshot;
    }

    /**
     * Returns the projected workload for accepting the applicant.
     *
     * @return load projection, or {@code null} if projection is unavailable
     */
    public LoadProjection getLoadProjection() {
        return loadProjection;
    }

    /**
     * Sets the projected workload for accepting the applicant.
     *
     * @param loadProjection projected workload to display
     */
    public void setLoadProjection(LoadProjection loadProjection) {
        this.loadProjection = loadProjection;
    }

    /**
     * Returns the i18n key suffix describing the status.
     *
     * @return normalized status description suffix such as {@code "Pending"}
     */
    public String getStatusDescription() {
        return statusDescription;
    }

    /**
     * Sets the i18n key suffix describing the status.
     *
     * @param statusDescription normalized status description suffix
     */
    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }
}
