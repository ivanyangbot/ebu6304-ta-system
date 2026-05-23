package com.bupt.tarecruitment.service;

import com.bupt.tarecruitment.model.Applicant;
import com.bupt.tarecruitment.model.ApplicationRecord;
import com.bupt.tarecruitment.model.Job;
import com.bupt.tarecruitment.model.WorkloadSummary;
import com.bupt.tarecruitment.repository.ApplicationRepository;
import com.bupt.tarecruitment.repository.JobRepository;
import com.bupt.tarecruitment.repository.UserRepository;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Calculates and summarises the accepted workload for all applicants.
 *
 * <p>This service is used by the administrator to monitor whether any TA
 * applicant is carrying an excessive number of accepted positions. For each
 * applicant in the system it computes:</p>
 * <ul>
 *   <li>The number of accepted applications</li>
 *   <li>The total weekly hours across all accepted positions</li>
 *   <li>A derived status label: {@code "Normal"} or {@code "Overloaded"}</li>
 * </ul>
 *
 * <p>An applicant is considered {@code "Overloaded"} when their total accepted
 * hours exceed {@link #DEFAULT_THRESHOLD}.</p>
 *
 * @author  Group 71
 * @version 1.0
 * @see     WorkloadSummary
 * @see     com.bupt.tarecruitment.servlet.AdminWorkloadServlet
 */
public class WorkloadService {

    /**
     * Default maximum weekly hours before an applicant is flagged as overloaded.
     * Applicants with accepted hours {@code > DEFAULT_THRESHOLD} are labelled
     * {@code "Overloaded"}.
     */
    public static final int DEFAULT_THRESHOLD = 10;

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;

    /**
     * Creates a {@code WorkloadService} backed by the JSON data stores.
     *
     * @param servletContext the servlet context used to resolve data file paths
     */
    public WorkloadService(ServletContext servletContext) {
        this.userRepository = new UserRepository(servletContext);
        this.jobRepository = new JobRepository(servletContext);
        this.applicationRepository = new ApplicationRepository(servletContext);
    }

    /**
     * Returns workload summaries for every applicant registered in the system.
     *
     * <p>The result list has one {@link WorkloadSummary} per applicant, ordered
     * as returned by the user repository (typically insertion order).</p>
     *
     * @return list of {@link WorkloadSummary} objects; never {@code null}
     */
    public List<WorkloadSummary> getApplicantWorkloadSummaries() {
        List<Applicant> applicants = userRepository.findAllApplicants();
        List<ApplicationRecord> applications = applicationRepository.findAll();
        List<Job> jobs = jobRepository.findAll();
        List<WorkloadSummary> summaries = new ArrayList<>();

        for (Applicant applicant : applicants) {
            int acceptedCount = 0;
            int totalHours = 0;

            for (ApplicationRecord application : applications) {
                if (applicant.getId().equals(application.getApplicantId())
                        && "Accepted".equalsIgnoreCase(application.getStatus())) {
                    acceptedCount++;
                    Job relatedJob = findJobById(jobs, application.getJobId());
                    if (relatedJob != null) {
                        totalHours += relatedJob.getHours();
                    }
                }
            }

            String workloadStatus = totalHours > DEFAULT_THRESHOLD ? "Overloaded" : "Normal";
            summaries.add(new WorkloadSummary(applicant, acceptedCount, totalHours, workloadStatus));
        }

        return summaries;
    }

    /**
     * Finds a job in the provided list by its ID.
     *
     * @param jobs  the list of jobs to search through
     * @param jobId the ID to look for
     * @return the matching {@link Job}, or {@code null} if not found
     */
    private Job findJobById(List<Job> jobs, String jobId) {
        for (Job job : jobs) {
            if (job.getId().equals(jobId)) {
                return job;
            }
        }
        return null;
    }
}
