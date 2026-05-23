package com.bupt.tarecruitment.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a TA job posting in the recruitment system.
 *
 * <p>A {@code Job} is created by a Module Organiser (MO) and describes a
 * teaching-assistant position for a specific module. Applicants can browse
 * open jobs and submit applications.</p>
 *
 * <p>Job status lifecycle:</p>
 * <ol>
 *   <li>{@code "Open"}      – accepting new applications</li>
 *   <li>{@code "Completed"} – position filled / closed by the MO</li>
 * </ol>
 *
 * <p>If a {@link #deadline} is set, the system automatically prevents new
 * applications once the current date is past that date.</p>
 *
 * @author  Group 71
 * @version 1.0
 * @see     com.bupt.tarecruitment.service.JobService
 * @see     com.bupt.tarecruitment.repository.JobRepository
 */
public class Job {

    /** Unique identifier for this job (generated via {@link com.bupt.tarecruitment.util.IdUtil}). */
    private String id;

    /** Short title of the TA position (e.g. "EBU6304 Lab TA"). */
    private String title;

    /** Name of the academic module this position belongs to. */
    private String moduleName;

    /** Detailed description of duties and responsibilities. */
    private String description;

    /** List of skill names required for this position (e.g. "Java", "Python"). */
    private List<String> requiredSkills;

    /** Estimated weekly working hours for this position. */
    private int hours;

    /** User ID of the Module Organiser who created this posting. */
    private String postedByMoId;

    /**
     * Current status of the job.
     * Valid values: {@code "Open"}, {@code "Completed"}.
     */
    private String status;

    /**
     * Optional application deadline.
     * If set, applicants cannot submit new applications after this date.
     * {@code null} means no deadline enforced.
     */
    private LocalDate deadline;

    /**
     * Default no-argument constructor. Initialises {@code requiredSkills} to an
     * empty list and {@code status} to {@code "Open"}.
     */
    public Job() {
        this.requiredSkills = new ArrayList<>();
        this.status = "Open";
    }

    /**
     * Returns whether the application deadline has passed.
     * Returns {@code false} if no deadline is set.
     *
     * @return {@code true} if today is after the deadline, {@code false} otherwise
     */
    public boolean isDeadlinePassed() {
        if (deadline == null) return false;
        return LocalDate.now().isAfter(deadline);
    }

    /**
     * Returns whether this job is currently accepting applications.
     * A job accepts applications only when its status is {@code "Open"}
     * and the deadline (if set) has not yet passed.
     *
     * @return {@code true} if applications are currently accepted
     */
    public boolean isAcceptingApplications() {
        return "Open".equals(status) && !isDeadlinePassed();
    }

    /**
     * Returns the number of days remaining until the deadline.
     * Returns -1 if no deadline is set, or a negative number if already past.
     *
     * @return days until deadline, or -1 if no deadline
     */
    public long getDaysUntilDeadline() {
        if (deadline == null) return -1;
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), deadline);
    }

    /**
     * Full constructor.
     *
     * @param id             unique identifier
     * @param title          position title
     * @param moduleName     name of the related module
     * @param description    job description
     * @param requiredSkills list of required skill strings; {@code null} becomes empty list
     * @param hours          estimated weekly hours
     * @param postedByMoId   ID of the MO who posted this job
     * @param status         current status string
     */
    public Job(String id, String title, String moduleName, String description, List<String> requiredSkills, int hours,
               String postedByMoId, String status) {
        this.id = id;
        this.title = title;
        this.moduleName = moduleName;
        this.description = description;
        this.requiredSkills = requiredSkills == null ? new ArrayList<>() : requiredSkills;
        this.hours = hours;
        this.postedByMoId = postedByMoId;
        this.status = status;
    }

    /**
     * Returns the unique identifier of this job.
     *
     * @return job ID string
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier of this job.
     *
     * @param id job ID string
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the title of this job posting.
     *
     * @return position title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of this job posting.
     *
     * @param title new title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the name of the module this position belongs to.
     *
     * @return module name
     */
    public String getModuleName() {
        return moduleName;
    }

    /**
     * Sets the module name.
     *
     * @param moduleName new module name
     */
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    /**
     * Returns the detailed description of this job.
     *
     * @return description text
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the detailed description of this job.
     *
     * @param description new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the list of required skills for this job.
     * Lazily initialises the list if it was somehow set to {@code null}.
     *
     * @return non-null list of required skill strings
     */
    public List<String> getRequiredSkills() {
        if (requiredSkills == null) {
            requiredSkills = new ArrayList<>();
        }
        return requiredSkills;
    }

    /**
     * Replaces the list of required skills.
     *
     * @param requiredSkills new skill list; {@code null} becomes empty list
     */
    public void setRequiredSkills(List<String> requiredSkills) {
        this.requiredSkills = requiredSkills == null ? new ArrayList<>() : requiredSkills;
    }

    /**
     * Returns the estimated weekly hours for this position.
     *
     * @return hours per week
     */
    public int getHours() {
        return hours;
    }

    /**
     * Sets the estimated weekly hours.
     *
     * @param hours hours per week
     */
    public void setHours(int hours) {
        this.hours = hours;
    }

    /**
     * Returns the ID of the Module Organiser who posted this job.
     *
     * @return MO user ID
     */
    public String getPostedByMoId() {
        return postedByMoId;
    }

    /**
     * Sets the ID of the Module Organiser who posted this job.
     *
     * @param postedByMoId MO user ID
     */
    public void setPostedByMoId(String postedByMoId) {
        this.postedByMoId = postedByMoId;
    }

    /**
     * Returns the current status of this job ({@code "Open"} or {@code "Completed"}).
     *
     * @return status string
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the current status of this job.
     *
     * @param status new status string (e.g. {@code "Open"}, {@code "Completed"})
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Returns the application deadline for this job.
     *
     * @return the deadline as a {@link LocalDate}, or {@code null} if not set
     */
    public LocalDate getDeadline() {
        return deadline;
    }

    /**
     * Sets the application deadline.
     *
     * @param deadline the last date on which applications are accepted;
     *                 {@code null} removes the deadline constraint
     */
    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }
}
