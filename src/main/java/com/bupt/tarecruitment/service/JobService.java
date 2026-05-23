package com.bupt.tarecruitment.service;

import com.bupt.tarecruitment.model.Job;
import com.bupt.tarecruitment.repository.JobRepository;
import com.bupt.tarecruitment.util.IdUtil;

import javax.servlet.ServletContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Business-logic service for managing TA job postings.
 *
 * <p>This service provides operations for creating, retrieving, and filtering
 * {@link Job} objects. It delegates persistence to
 * {@link JobRepository} and handles the generation of unique IDs via
 * {@link IdUtil}.</p>
 *
 * <p>Typical call flow for a Module Organiser posting a new job:</p>
 * <ol>
 *   <li>MO submits the job-creation form ({@link com.bupt.tarecruitment.servlet.JobCreateServlet})</li>
 *   <li>Servlet calls {@link #createJob(String, String, String, List, int, String)}</li>
 *   <li>Service creates the {@link Job}, assigns an ID, and saves it via the repository</li>
 * </ol>
 *
 * @author  Group 71
 * @version 1.0
 * @see     JobRepository
 * @see     com.bupt.tarecruitment.servlet.JobCreateServlet
 * @see     com.bupt.tarecruitment.servlet.JobListServlet
 */
public class JobService {

    private final JobRepository jobRepository;

    /**
     * Creates a {@code JobService} backed by the JSON job store.
     *
     * @param servletContext the servlet context used to resolve the data file path
     */
    public JobService(ServletContext servletContext) {
        this.jobRepository = new JobRepository(servletContext);
    }

    /**
     * Returns all jobs in the system regardless of status.
     *
     * @return list of all {@link Job} objects; never {@code null}
     */
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    /**
     * Returns only jobs whose status is {@code "Open"}.
     *
     * @return list of open {@link Job} objects; never {@code null}
     */
    public List<Job> getOpenJobs() {
        return jobRepository.findAllOpen();
    }

    /**
     * Looks up a single job by its unique identifier.
     *
     * @param id the job ID to search for
     * @return the matching {@link Job}, or {@code null} if not found
     */
    public Job getJobById(String id) {
        return jobRepository.findById(id);
    }

    /**
     * Returns all jobs posted by a specific Module Organiser.
     *
     * @param moId the user ID of the Module Organiser
     * @return list of {@link Job} objects posted by the given MO; never {@code null}
     */
    public List<Job> getJobsByMo(String moId) {
        return jobRepository.findByMoId(moId);
    }

    /**
     * Updates an existing open job posting.
     *
     * <p>Only jobs whose status is {@code "Open"} can be edited. Completed jobs
     * are intentionally locked to preserve already reviewed recruitment records.</p>
     *
     * @param jobId          ID of the job to update
     * @param title          updated short title of the position
     * @param moduleName     updated academic module or activity name
     * @param description    updated detailed description of duties
     * @param requiredSkills updated required skill strings
     * @param hours          updated estimated workload hours
     * @throws RuntimeException if the job is missing, completed, or contains invalid required fields
     */
    public void updateJob(String jobId, String title, String moduleName, String description, List<String> requiredSkills, int hours) {
        Job job = jobRepository.findById(jobId);
        if (job == null) {
            throw new RuntimeException("Job not found.");
        }
        if (!"Open".equalsIgnoreCase(job.getStatus())) {
            throw new RuntimeException("Only open jobs can be edited.");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new RuntimeException("Job title cannot be empty.");
        }
        if (moduleName == null || moduleName.trim().isEmpty()) {
            throw new RuntimeException("Module name cannot be empty.");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new RuntimeException("Description cannot be empty.");
        }
        if (hours <= 0) {
            throw new RuntimeException("Hours must be a positive integer.");
        }
        job.setTitle(title);
        job.setModuleName(moduleName);
        job.setDescription(description);
        job.setRequiredSkills(requiredSkills);
        job.setHours(hours);
        jobRepository.update(job);
    }

    /**
     * Creates and persists a new job posting.
     *
     * <p>A unique job ID is generated automatically and the initial status is set
     * to {@code "Open"}.</p>
     *
     * @param title          short title of the position
     * @param moduleName     name of the related academic module
     * @param description    detailed description of duties
     * @param requiredSkills list of required skill strings
     * @param hours          estimated weekly hours for the position
     * @param moId           user ID of the Module Organiser creating this posting
     * @return the newly created and persisted {@link Job}
     */
    /**
     * Creates and persists a new job posting with an optional deadline.
     *
     * @param title          short title of the position
     * @param moduleName     name of the related academic module
     * @param description    detailed description of duties
     * @param requiredSkills list of required skill strings
     * @param hours          estimated weekly hours for the position
     * @param moId           user ID of the Module Organiser creating this posting
     * @param deadline       optional last date for applications; {@code null} means no deadline
     * @return the newly created and persisted {@link Job}
     */
    public Job createJob(String title, String moduleName, String description, List<String> requiredSkills, int hours,
                         String moId, LocalDate deadline) {
        Job job = new Job();
        job.setId(IdUtil.generateId("job"));
        job.setTitle(title);
        job.setModuleName(moduleName);
        job.setDescription(description);
        job.setRequiredSkills(requiredSkills);
        job.setHours(hours);
        job.setPostedByMoId(moId);
        job.setStatus("Open");
        job.setDeadline(deadline);
        jobRepository.save(job);
        return job;
    }

    public Job createJob(String title, String moduleName, String description, List<String> requiredSkills, int hours,
                         String moId) {
        return createJob(title, moduleName, description, requiredSkills, hours, moId, null);
    }

    /**
     * Returns jobs whose deadline falls within the next {@code days} days
     * (inclusive). Jobs without a deadline are excluded.
     *
     * @param days number of days to look ahead (e.g. 7 for one week)
     * @return list of {@link Job} objects expiring soon; never {@code null}
     */
    public List<Job> getJobsExpiringSoon(int days) {
        LocalDate cutoff = LocalDate.now().plusDays(days);
        return jobRepository.findAll().stream()
                .filter(j -> j.getDeadline() != null)
                .filter(j -> "Open".equals(j.getStatus()))
                .filter(j -> !j.getDeadline().isAfter(cutoff))
                .collect(Collectors.toList());
    }

    /**
     * Updates the deadline of a job.
     *
     * @param jobId    ID of the job to update
     * @param deadline new deadline; {@code null} removes it
     */
    public void updateDeadline(String jobId, LocalDate deadline) {
        Job job = jobRepository.findById(jobId);
        if (job == null) throw new RuntimeException("Job not found.");
        job.setDeadline(deadline);
        jobRepository.update(job);
    }
}
