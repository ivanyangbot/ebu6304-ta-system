package com.bupt.tarecruitment.model;

/**
 * Read-only dashboard view model for a recent pending application owned by an MO.
 *
 * <p>The JSP layer only needs display-safe fields and navigation IDs, so this
 * class avoids exposing full {@link ApplicationRecord}, {@link Job}, or
 * {@link Applicant} objects to the dashboard.</p>
 *
 * <p>Instances are created by
 * {@link com.bupt.tarecruitment.servlet.DashboardServlet} and are not persisted
 * to any JSON data file.</p>
 *
 * @author  Group 71
 * @version 1.0
 * @see     com.bupt.tarecruitment.servlet.DashboardServlet
 */
public class MoPendingApplicationView {
    /** Application record ID. */
    private final String applicationId;

    /** Job ID used by the Review link. */
    private final String jobId;

    /** Title of the job receiving the application. */
    private final String jobTitle;

    /** Display name of the applicant who submitted the application. */
    private final String applicantName;

    /** Formatted submission time copied from {@link ApplicationRecord#getAppliedAt()}. */
    private final String appliedAt;

    /**
     * Creates a compact pending-application summary for the MO dashboard.
     *
     * @param applicationId application record ID
     * @param jobId         related job ID used to open the review page
     * @param jobTitle      related job title
     * @param applicantName applicant display name
     * @param appliedAt     application submission timestamp
     */
    public MoPendingApplicationView(String applicationId, String jobId, String jobTitle,
                                    String applicantName, String appliedAt) {
        this.applicationId = applicationId;
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.applicantName = applicantName;
        this.appliedAt = appliedAt;
    }

    /**
     * Returns the application record ID.
     *
     * @return application ID
     */
    public String getApplicationId() {
        return applicationId;
    }

    /**
     * Returns the related job ID.
     *
     * @return job ID used to open the MO application review page
     */
    public String getJobId() {
        return jobId;
    }

    /**
     * Returns the related job title.
     *
     * @return job title shown on the dashboard
     */
    public String getJobTitle() {
        return jobTitle;
    }

    /**
     * Returns the applicant display name.
     *
     * @return applicant full name, or a fallback value if the user was missing
     */
    public String getApplicantName() {
        return applicantName;
    }

    /**
     * Returns the application submission timestamp.
     *
     * @return formatted timestamp string copied from the application record
     */
    public String getAppliedAt() {
        return appliedAt;
    }
}
