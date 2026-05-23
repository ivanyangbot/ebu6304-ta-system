package com.bupt.tarecruitment.model;

/**
 * A read-only view of an applicant's current workload.
 *
 * <p>Instances of this class are produced by
 * {@link com.bupt.tarecruitment.service.WorkloadService} and are displayed on
 * the administrator's workload monitoring page. Each summary captures how many
 * accepted TA positions an applicant currently holds, the corresponding total
 * weekly hours, and a simple status label derived from a configurable
 * threshold.</p>
 *
 * <p>Workload status values:</p>
 * <ul>
 *   <li>{@code "Normal"}     – total hours ≤ {@link com.bupt.tarecruitment.service.WorkloadService#DEFAULT_THRESHOLD}</li>
 *   <li>{@code "Overloaded"} – total hours &gt; threshold</li>
 * </ul>
 *
 * @author  Group 71
 * @version 1.0
 * @see     com.bupt.tarecruitment.service.WorkloadService
 */
public class WorkloadSummary {

    /** The applicant whose workload is being summarised. */
    private Applicant applicant;

    /** Number of applications that have been accepted for this applicant. */
    private int acceptedJobsCount;

    /** Sum of weekly hours across all accepted positions. */
    private int totalHours;

    /**
     * Derived workload status.
     * Values: {@code "Normal"} or {@code "Overloaded"}.
     */
    private String workloadStatus;

    /**
     * Default no-argument constructor required for JSON deserialization.
     */
    public WorkloadSummary() {
    }

    /**
     * Full constructor.
     *
     * @param applicant        the applicant being summarised
     * @param acceptedJobsCount number of accepted applications
     * @param totalHours       total weekly hours across accepted positions
     * @param workloadStatus   derived status string ({@code "Normal"} or {@code "Overloaded"})
     */
    public WorkloadSummary(Applicant applicant, int acceptedJobsCount, int totalHours, String workloadStatus) {
        this.applicant = applicant;
        this.acceptedJobsCount = acceptedJobsCount;
        this.totalHours = totalHours;
        this.workloadStatus = workloadStatus;
    }

    /**
     * Returns the applicant whose workload is summarised.
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
     * Returns the number of accepted applications for this applicant.
     *
     * @return count of accepted positions
     */
    public int getAcceptedJobsCount() { return acceptedJobsCount; }

    /**
     * Sets the count of accepted applications.
     *
     * @param acceptedJobsCount accepted application count
     */
    public void setAcceptedJobsCount(int acceptedJobsCount) { this.acceptedJobsCount = acceptedJobsCount; }

    /**
     * Returns the total weekly hours for all accepted positions.
     *
     * @return total hours
     */
    public int getTotalHours() { return totalHours; }

    /**
     * Sets the total weekly hours.
     *
     * @param totalHours total hours
     */
    public void setTotalHours(int totalHours) { this.totalHours = totalHours; }

    /**
     * Returns the derived workload status.
     *
     * @return {@code "Normal"} or {@code "Overloaded"}
     */
    public String getWorkloadStatus() { return workloadStatus; }

    /**
     * Sets the workload status.
     *
     * @param workloadStatus workload status string
     */
    public void setWorkloadStatus(String workloadStatus) { this.workloadStatus = workloadStatus; }
}
