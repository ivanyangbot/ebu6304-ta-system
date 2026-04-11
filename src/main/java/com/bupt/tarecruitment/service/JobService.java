package com.bupt.tarecruitment.service;

import com.bupt.tarecruitment.model.Job;
import com.bupt.tarecruitment.repository.JobRepository;
import com.bupt.tarecruitment.util.IdUtil;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;

public class JobService {
    private final JobRepository jobRepository;

    public JobService(ServletContext servletContext) {
        this.jobRepository = new JobRepository(servletContext);
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public List<Job> getOpenJobs() {
        List<Job> allJobs = jobRepository.findAll();
        List<Job> openJobs = new ArrayList<>();
        for (Job job : allJobs) {
            if (!"Closed".equalsIgnoreCase(job.getStatus())) {
                openJobs.add(job);
            }
        }
        return openJobs;
    }

    public Job getJobById(String id) {
        return jobRepository.findById(id);
    }

    public List<Job> getJobsByMo(String moId) {
        return jobRepository.findByMoId(moId);
    }

    public void createJob(String title, String moduleName, String description, List<String> requiredSkills, int hours,
                          String moId) {
        Job job = new Job();
        job.setId(IdUtil.generateId("job"));
        job.setTitle(title);
        job.setModuleName(moduleName);
        job.setDescription(description);
        job.setRequiredSkills(requiredSkills);
        job.setHours(hours);
        job.setPostedByMoId(moId);
        job.setStatus("Open");
        jobRepository.save(job);
    }
}
