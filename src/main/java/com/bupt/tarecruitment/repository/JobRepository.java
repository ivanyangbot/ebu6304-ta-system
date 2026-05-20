package com.bupt.tarecruitment.repository;

import com.bupt.tarecruitment.model.Job;
import com.bupt.tarecruitment.util.JsonFileUtil;
import com.bupt.tarecruitment.util.PathUtil;
import com.google.gson.reflect.TypeToken;

import javax.servlet.ServletContext;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class JobRepository {
    private final Path filePath;
    private static final Type JOB_LIST_TYPE = new TypeToken<List<Job>>() {
    }.getType();

    public JobRepository(ServletContext servletContext) {
        this.filePath = PathUtil.getDataFilePath(servletContext, "jobs.json");
    }

    public List<Job> findAll() {
        return JsonFileUtil.readList(filePath, JOB_LIST_TYPE);
    }

    public Job findById(String id) {
        List<Job> jobs = findAll();
        for (Job job : jobs) {
            if (job.getId().equals(id)) {
                return job;
            }
        }
        return null;
    }

    public List<Job> findByMoId(String moId) {
        List<Job> jobs = findAll();
        List<Job> result = new ArrayList<>();
        for (Job job : jobs) {
            if (moId.equals(job.getPostedByMoId())) {
                result.add(job);
            }
        }
        return result;
    }

    public void save(Job job) {
        List<Job> jobs = findAll();
        jobs.add(job);
        JsonFileUtil.writeJson(filePath, jobs);
    }

    public void update(Job updatedJob) {
        synchronized (JobRepository.class) {
            List<Job> jobs = findAll();
            for (int i = 0; i < jobs.size(); i++) {
                if (jobs.get(i).getId().equals(updatedJob.getId())) {
                    jobs.set(i, updatedJob);
                    JsonFileUtil.writeJson(filePath, jobs);
                    return;
                }
            }
            throw new RuntimeException("Job not found: " + updatedJob.getId());
        }
    }

    public void updateStatus(String jobId, String newStatus) {
        synchronized (JobRepository.class) {
            List<Job> jobs = findAll();
            for (Job job : jobs) {
                if (job.getId().equals(jobId)) {
                    job.setStatus(newStatus);
                    JsonFileUtil.writeJson(filePath, jobs);
                    return;
                }
            }
            throw new RuntimeException("Job not found: " + jobId);
        }
    }

    public List<Job> findAllOpen() {
        List<Job> jobs = findAll();
        List<Job> result = new ArrayList<>();
        for (Job job : jobs) {
            if ("Open".equals(job.getStatus())) {
                result.add(job);
            }
        }
        return result;
    }

    public List<Job> findByMoIdAndStatus(String moId, String status) {
        List<Job> jobs = findAll();
        List<Job> result = new ArrayList<>();
        for (Job job : jobs) {
            if (moId.equals(job.getPostedByMoId()) && status.equals(job.getStatus())) {
                result.add(job);
            }
        }
        return result;
    }

    public List<Job> findAllByStatus(String status) {
        List<Job> jobs = findAll();
        List<Job> result = new ArrayList<>();
        for (Job job : jobs) {
            if (status.equals(job.getStatus())) {
                result.add(job);
            }
        }
        return result;
    }
}
