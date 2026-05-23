package com.bupt.tarecruitment.model;

/**
 * An immutable view of an application's computed priority for shortlisting.
 *
 * <p>Instances are produced by
 * {@link com.bupt.tarecruitment.service.ApplicantRankingService} or
 * {@link com.bupt.tarecruitment.service.ShortlistPlannerService} and are used
 * to rank candidates for a given job posting. The {@link #priorityScore} is a
 * composite value derived from the skill-match score, workload, and other
 * heuristics. The {@link #decisionBand} maps the score to a human-readable
 * recommendation tier.</p>
 *
 * <p>Decision band values (indicative):</p>
 * <ul>
 *   <li>{@code "Strong Accept"} – high priority, recommended for acceptance</li>
 *   <li>{@code "Accept"}        – above-average match</li>
 *   <li>{@code "Review"}        – borderline, requires manual review</li>
 *   <li>{@code "Reject"}        – low priority</li>
 * </ul>
 *
 * @author  Group 71
 * @version 1.0
 * @see     com.bupt.tarecruitment.service.ApplicantRankingService
 * @see     com.bupt.tarecruitment.service.ShortlistPlannerService
 */
public class ApplicationPriorityView {

    /** ID of the applicant being ranked. */
    private final String applicantId;

    /** ID of the job the applicant applied for. */
    private final String jobId;

    /**
     * Composite priority score used for ranking.
     * Higher values indicate a stronger candidate fit.
     */
    private final double priorityScore;

    /**
     * Human-readable decision recommendation derived from the priority score.
     * Defaults to {@code "Review"} if not specified.
     */
    private final String decisionBand;

    /**
     * Constructs an immutable priority view.
     *
     * @param applicantId   ID of the applicant
     * @param jobId         ID of the job
     * @param priorityScore composite ranking score
     * @param decisionBand  decision recommendation string; {@code null} defaults to {@code "Review"}
     */
    public ApplicationPriorityView(String applicantId, String jobId, double priorityScore, String decisionBand) {
        this.applicantId = applicantId;
        this.jobId = jobId;
        this.priorityScore = priorityScore;
        this.decisionBand = decisionBand == null ? "Review" : decisionBand;
    }

    /**
     * Returns the applicant ID.
     *
     * @return applicant user ID
     */
    public String getApplicantId() { return applicantId; }

    /**
     * Returns the job ID.
     *
     * @return job ID
     */
    public String getJobId() { return jobId; }

    /**
     * Returns the composite priority score.
     *
     * @return priority score
     */
    public double getPriorityScore() { return priorityScore; }

    /**
     * Returns the decision band recommendation.
     *
     * @return decision band string (e.g. {@code "Accept"}, {@code "Review"})
     */
    public String getDecisionBand() { return decisionBand; }
}
