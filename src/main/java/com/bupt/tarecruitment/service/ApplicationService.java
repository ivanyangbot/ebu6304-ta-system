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

public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;

    public ApplicationService(ServletContext servletContext) {
        this.applicationRepository = new ApplicationRepository(servletContext);
        this.jobRepository = new JobRepository(servletContext);
    }

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

    public boolean hasApplied(String jobId, String applicantId) {
        return applicationRepository.findByJobIdAndApplicantId(jobId, applicantId) != null;
    }

    public List<ApplicationRecord> getApplicationsByApplicant(String applicantId) {
        return applicationRepository.findByApplicantId(applicantId);
    }

    public List<ApplicationRecord> getApplicationsByJob(String jobId) {
        return applicationRepository.findByJobId(jobId);
    }

    public ApplicationRecord getApplicationById(String applicationId) {
        return applicationRepository.findById(applicationId);
    }

    public void updateApplicationStatus(String applicationId, String status) {
        if (!"Pending".equals(status) && !"Accepted".equals(status) && !"Rejected".equals(status)) {
            throw new RuntimeException("Invalid application status.");
        }
        applicationRepository.updateStatus(applicationId, status);
    }
}
