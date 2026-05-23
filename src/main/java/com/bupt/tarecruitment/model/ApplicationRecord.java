package com.bupt.tarecruitment.model;

/**
 * Represents a single TA job application submitted by an applicant.
 *
 * <p>An {@code ApplicationRecord} links an {@link Applicant} to a {@link Job}
 * and tracks the current review status. Records are persisted in
 * {@code applications.json} via {@link com.bupt.tarecruitment.repository.ApplicationRepository}.</p>
 *
 * <p>Application status lifecycle:</p>
 * <ol>
 *   <li>{@code "Pending"}  – newly submitted, awaiting MO review</li>
 *   <li>{@code "Accepted"} – MO has accepted the applicant</li>
 *   <li>{@code "Rejected"} – MO has rejected the application</li>
 * </ol>
 *
 * <p>When the MO updates the status to {@code "Accepted"} or {@code "Rejected"},
 * they may optionally provide a {@link #moFeedback} explanation so the applicant
 * can understand the decision (supports explainability of AI-assisted ranking).</p>
 *
 * @author  Group 71
 * @version 1.0
 * @see     com.bupt.tarecruitment.service.ApplicationService
 * @see     com.bupt.tarecruitment.repository.ApplicationRepository
 */
public class ApplicationRecord {

    /** Unique identifier for this application record. */
    private String id;

    /** ID of the {@link Job} this application targets. */
    private String jobId;

    /** ID of the {@link Applicant} who submitted this application. */
    private String applicantId;

    /**
     * Current review status.
     * Valid values: {@code "Pending"}, {@code "Accepted"}, {@code "Rejected"}.
     */
    private String status;

    /** ISO-formatted timestamp of when the application was submitted (e.g. {@code "2025-05-01 14:30"}). */
    private String appliedAt;

    /**
     * Optional feedback message left by the Module Organiser when updating the
     * application status. Provides the applicant with an explanation of the
     * decision (e.g. missing skills, workload reason). May be {@code null} or empty.
     */
    private String moFeedback;

    /**
     * Default no-argument constructor. Sets the initial status to {@code "Pending"}.
     */
    public ApplicationRecord() {
        this.status = "Pending";
    }

    /**
     * Full constructor.
     *
     * @param id          unique identifier
     * @param jobId       target job ID
     * @param applicantId applicant user ID
     * @param status      review status string
     * @param appliedAt   formatted timestamp of submission
     */
    public ApplicationRecord(String id, String jobId, String applicantId, String status, String appliedAt) {
        this.id = id;
        this.jobId = jobId;
        this.applicantId = applicantId;
        this.status = status;
        this.appliedAt = appliedAt;
    }

    /**
     * Constructor with feedback.
     *
     * @param id          unique identifier
     * @param jobId       target job ID
     * @param applicantId applicant user ID
     * @param status      review status string
     * @param appliedAt   formatted timestamp of submission
     * @param moFeedback  optional MO feedback message
     */
    public ApplicationRecord(String id, String jobId, String applicantId, String status, String appliedAt, String moFeedback) {
        this.id = id;
        this.jobId = jobId;
        this.applicantId = applicantId;
        this.status = status;
        this.appliedAt = appliedAt;
        this.moFeedback = moFeedback;
    }

    /**
     * Returns the unique identifier of this application record.
     *
     * @return application ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier.
     *
     * @param id application ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the ID of the job this application targets.
     *
     * @return job ID
     */
    public String getJobId() {
        return jobId;
    }

    /**
     * Sets the target job ID.
     *
     * @param jobId job ID
     */
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    /**
     * Returns the ID of the applicant who submitted this application.
     *
     * @return applicant user ID
     */
    public String getApplicantId() {
        return applicantId;
    }

    /**
     * Sets the applicant user ID.
     *
     * @param applicantId applicant user ID
     */
    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    /**
     * Returns the current review status of this application.
     *
     * @return status string ({@code "Pending"}, {@code "Accepted"}, or {@code "Rejected"})
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the review status.
     *
     * @param status new status string
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Returns the formatted timestamp of when this application was submitted.
     *
     * @return timestamp string (e.g. {@code "2025-05-01 14:30"})
     */
    public String getAppliedAt() {
        return appliedAt;
    }

    /**
     * Sets the submission timestamp.
     *
     * @param appliedAt formatted timestamp string
     */
    public void setAppliedAt(String appliedAt) {
        this.appliedAt = appliedAt;
    }

    /**
     * Returns the MO's feedback message for this application decision.
     *
     * @return feedback text, or {@code null} if no feedback was provided
     */
    public String getMoFeedback() {
        return moFeedback;
    }

    /**
     * Sets the MO's feedback message.
     *
     * @param moFeedback the feedback text explaining the decision; may be {@code null}
     */
    public void setMoFeedback(String moFeedback) {
        this.moFeedback = moFeedback;
    }
}
