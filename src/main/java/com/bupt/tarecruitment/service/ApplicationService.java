package com.bupt.tarecruitment.service;

import com.bupt.tarecruitment.model.ApplicationRecord;
import com.bupt.tarecruitment.model.Job;
import com.bupt.tarecruitment.repository.ApplicationRepository;
import com.bupt.tarecruitment.repository.JobRepository;
import com.bupt.tarecruitment.util.IdUtil;

import javax.servlet.ServletContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Business-logic service for managing TA job applications.
 *
 * <p>This service handles the complete lifecycle of an
 * {@link ApplicationRecord}:</p>
 * <ul>
 *   <li>Submitting a new application ({@link #applyForJob(String, String)})</li>
 *   <li>Checking whether an applicant has already applied ({@link #hasApplied(String, String)})</li>
 *   <li>Retrieving applications by applicant or by job</li>
 *   <li>Updating the review status ({@link #updateApplicationStatus(String, String)})</li>
 *   <li>Withdrawing a pending application ({@link #withdrawApplication(String, String)})</li>
 * </ul>
 *
 * <p>Business rules enforced by this service:</p>
 * <ul>
 *   <li>An applicant may not apply for the same job twice.</li>
 *   <li>Application status must be one of {@code "Pending"}, {@code "Accepted"}, or {@code "Rejected"}.</li>
 *   <li>Only the applicant who submitted an application may withdraw it.</li>
 *   <li>Only applications in {@code "Pending"} status can be withdrawn.</li>
 * </ul>
 *
 * @author  Group 71
 * @version 1.0
 * @see     ApplicationRecord
 * @see     ApplicationRepository
 */
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;

    /**
     * Creates an {@code ApplicationService} backed by the JSON application store.
     *
     * @param servletContext the servlet context used to resolve data file paths
     */
    public ApplicationService(ServletContext servletContext) {
        this.applicationRepository = new ApplicationRepository(servletContext);
        this.jobRepository = new JobRepository(servletContext);
    }

    /**
     * Submits a new application for a given job.
     *
     * <p>This method verifies that the job exists and that the applicant has not
     * already applied before creating and persisting the new record.</p>
     *
     * @param jobId       ID of the job to apply for
     * @param applicantId ID of the applicant submitting the application
     * @return the newly created {@link ApplicationRecord}
     * @throws RuntimeException if the job does not exist or the applicant has already applied
     */
    public ApplicationRecord applyForJob(String jobId, String applicantId) {
        Job job = jobRepository.findById(jobId);
        if (job == null) {
            throw new RuntimeException("Job does not exist.");
        }

        if (hasApplied(jobId, applicantId)) {
            throw new RuntimeException("You have already applied for this job.");
        }

        ApplicationRecord record = new ApplicationRecord();
        record.setId(IdUtil.generateId("app"));
        record.setJobId(jobId);
        record.setApplicantId(applicantId);
        record.setStatus("Pending");
        record.setAppliedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        applicationRepository.save(record);
        return record;
    }

    /**
     * Checks whether an applicant has already submitted an application for a job.
     *
     * @param jobId       the job ID
     * @param applicantId the applicant's user ID
     * @return {@code true} if an application exists for this (job, applicant) pair
     */
    public boolean hasApplied(String jobId, String applicantId) {
        return applicationRepository.findByJobIdAndApplicantId(jobId, applicantId) != null;
    }

    /**
     * Returns all application records submitted by a specific applicant.
     *
     * @param applicantId the applicant's user ID
     * @return list of {@link ApplicationRecord} objects; never {@code null}
     */
    public List<ApplicationRecord> getApplicationsByApplicant(String applicantId) {
        return applicationRepository.findByApplicantId(applicantId);
    }

    /**
     * Returns all application records for a specific job.
     *
     * @param jobId the job ID
     * @return list of {@link ApplicationRecord} objects; never {@code null}
     */
    public List<ApplicationRecord> getApplicationsByJob(String jobId) {
        return applicationRepository.findByJobId(jobId);
    }

    /**
     * Retrieves a single application record by its unique identifier.
     *
     * @param applicationId the application ID
     * @return the matching {@link ApplicationRecord}, or {@code null} if not found
     */
    public ApplicationRecord getApplicationById(String applicationId) {
        return applicationRepository.findById(applicationId);
    }

    /**
     * Updates the review status of an existing application.
     *
     * @param applicationId ID of the application to update
     * @param status        new status string; must be {@code "Pending"}, {@code "Accepted"}, or {@code "Rejected"}
     * @throws RuntimeException if the status value is not one of the accepted values
     */
    public void updateApplicationStatus(String applicationId, String status) {
        if (!"Pending".equals(status) && !"Accepted".equals(status) && !"Rejected".equals(status)) {
            throw new RuntimeException("Invalid application status.");
        }
        applicationRepository.updateStatus(applicationId, status);
    }

    /**
     * Withdraws (deletes) a pending application.
     *
     * <p>Only the applicant who originally submitted the application may withdraw
     * it, and only while the status is still {@code "Pending"}.</p>
     *
     * @param applicationId ID of the application to withdraw
     * @param applicantId   ID of the applicant requesting the withdrawal
     * @throws RuntimeException if the application is not found, the applicant does not own it,
     *                          or the application is not in {@code "Pending"} status
     */
    public void withdrawApplication(String applicationId, String applicantId) {
        ApplicationRecord application = applicationRepository.findById(applicationId);
        if (application == null) {
            throw new RuntimeException("Application not found.");
        }
        if (!application.getApplicantId().equals(applicantId)) {
            throw new RuntimeException("You can only withdraw your own application.");
        }
        if (!"Pending".equals(application.getStatus())) {
            throw new RuntimeException("Only pending applications can be withdrawn.");
        }
        applicationRepository.delete(applicationId);
    }
}
