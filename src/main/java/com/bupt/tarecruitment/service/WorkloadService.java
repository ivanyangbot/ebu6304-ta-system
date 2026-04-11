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

public class WorkloadService {
    public static final int DEFAULT_THRESHOLD = 10;

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;

    public WorkloadService(ServletContext servletContext) {
        this.userRepository = new UserRepository(servletContext);
        this.jobRepository = new JobRepository(servletContext);
        this.applicationRepository = new ApplicationRepository(servletContext);
    }

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

    private Job findJobById(List<Job> jobs, String jobId) {
        for (Job job : jobs) {
            if (job.getId().equals(jobId)) {
                return job;
            }
        }
        return null;
    }
}
