package com.bupt.tarecruitment.model;

/**
 * A composite view object that aggregates an application with its related entities.
 *
 * <p>This class is a read-only view model used by servlet layer to pass
 * rich application information to JSP views. It bundles together an
 * {@link ApplicationRecord}, the corresponding {@link Job}, the
 * {@link Applicant}, and the skill {@link MatchResult} so that a single
 * object can be forwarded as a request attribute.</p>
 *
 * <p>Instances are typically built in
 * {@link com.bupt.tarecruitment.servlet.MOApplicationsServlet} or
 * {@link com.bupt.tarecruitment.servlet.MyApplicationsServlet}.</p>
 *
 * @author  Group 71
 * @version 1.0
 * @see     ApplicationRecord
 * @see     Job
 * @see     Applicant
 * @see     MatchResult
 */
public class ApplicationDisplay {

    /** The underlying application record. */
    private ApplicationRecord application;

    /** The job posting this application relates to. */
    private Job job;

    /** The applicant who submitted this application. */
    private Applicant applicant;

    /** The skill match result between the applicant and the job. */
    private MatchResult matchResult;

    /**
     * Default no-argument constructor.
     */
    public ApplicationDisplay() {
    }

    /**
     * Full constructor.
     *
     * @param application  the application record
     * @param job          the related job posting
     * @param applicant    the applicant who applied
     * @param matchResult  the calculated skill match result
     */
    public ApplicationDisplay(ApplicationRecord application, Job job, Applicant applicant, MatchResult matchResult) {
        this.application = application;
        this.job = job;
        this.applicant = applicant;
        this.matchResult = matchResult;
    }

    /**
     * Returns the application record.
     *
     * @return the {@link ApplicationRecord}
     */
    public ApplicationRecord getApplication() { return application; }

    /**
     * Sets the application record.
     *
     * @param application the application record
     */
    public void setApplication(ApplicationRecord application) { this.application = application; }

    /**
     * Returns the related job posting.
     *
     * @return the {@link Job}
     */
    public Job getJob() { return job; }

    /**
     * Sets the related job posting.
     *
     * @param job the job posting
     */
    public void setJob(Job job) { this.job = job; }

    /**
     * Returns the applicant.
     *
     * @return the {@link Applicant}
     */
    public Applicant getApplicant() { return applicant; }

    /**
     * Sets the applicant.
     *
     * @param applicant the applicant
     */
    public void setApplicant(Applicant applicant) { this.applicant = applicant; }

    /**
     * Returns the skill match result.
     *
     * @return the {@link MatchResult}
     */
    public MatchResult getMatchResult() { return matchResult; }

    /**
     * Sets the skill match result.
     *
     * @param matchResult the match result
     */
    public void setMatchResult(MatchResult matchResult) { this.matchResult = matchResult; }
}
