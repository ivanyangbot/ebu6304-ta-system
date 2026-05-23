package com.bupt.tarecruitment.model;

/**
 * Read-only dashboard view model for an MO job that has pending applications.
 *
 * <p>Instances are created by {@code DashboardServlet} to keep the JSP focused
 * on display concerns instead of counting application records itself.</p>
 *
 * <p>This model is used only for the MO dashboard's "Jobs Needing Action" card
 * and is not persisted to the JSON data store.</p>
 *
 * @author  Group 71
 * @version 1.0
 * @see     com.bupt.tarecruitment.servlet.DashboardServlet
 * @see     ApplicationRecord
 * @see     Job
 */
public class MoActionRequiredJobView {
    /** Job ID used by the dashboard Review link. */
    private final String jobId;

    /** Job title shown in the action-required list. */
    private final String jobTitle;

    /** Number of pending applications currently attached to the job. */
    private final int pendingCount;

    /**
     * Creates a compact action-required job summary.
     *
     * @param jobId        job ID used to navigate to the MO review page
     * @param jobTitle     job title shown on the dashboard
     * @param pendingCount number of pending applications for the job
     */
    public MoActionRequiredJobView(String jobId, String jobTitle, int pendingCount) {
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.pendingCount = pendingCount;
    }

    /**
     * Returns the job ID.
     *
     * @return job ID used to open the MO application review page
     */
    public String getJobId() {
        return jobId;
    }

    /**
     * Returns the job title.
     *
     * @return job title displayed on the dashboard
     */
    public String getJobTitle() {
        return jobTitle;
    }

    /**
     * Returns the number of pending applications for the job.
     *
     * @return pending application count
     */
    public int getPendingCount() {
        return pendingCount;
    }
}
