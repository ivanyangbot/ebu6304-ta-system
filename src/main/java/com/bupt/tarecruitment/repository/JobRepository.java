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

/**
 * Repository for persisting and querying {@link Job} entities stored in a
 * JSON flat file ({@code WEB-INF/data/jobs.json}).
 *
 * <p>All write operations are synchronised on the class monitor to prevent
 * concurrent modification of the backing file.</p>
 *
 * @author  Group 71
 * @version 1.0
 */
public class JobRepository {
    private final Path filePath;
    private static final Type JOB_LIST_TYPE = new TypeToken<List<Job>>() {
    }.getType();

    /**
     * Constructs a new {@code JobRepository} and resolves the data file path
     * from the servlet context.
     *
     * @param servletContext the servlet context used to locate {@code WEB-INF/data/jobs.json}
     */
    public JobRepository(ServletContext servletContext) {
        this.filePath = PathUtil.getDataFilePath(servletContext, "jobs.json");
    }

    /**
     * Returns all jobs stored in the data file.
     *
     * @return mutable list of all {@link Job} objects; empty list if the file is absent or empty
     */
    public List<Job> findAll() {
        return JsonFileUtil.readList(filePath, JOB_LIST_TYPE);
    }

    /**
     * Finds a single job by its unique identifier.
     *
     * @param id the job ID to look up
     * @return the matching {@link Job}, or {@code null} if not found
     */
    public Job findById(String id) {
        List<Job> jobs = findAll();
        for (Job job : jobs) {
            if (job.getId().equals(id)) {
                return job;
            }
        }
        return null;
    }

    /**
     * Returns all jobs posted by a specific Module Organiser.
     *
     * @param moId the user ID of the Module Organiser
     * @return list of {@link Job} objects owned by the given MO; empty list if none
     */
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

    /**
     * Persists a new job by appending it to the data file.
     *
     * @param job the {@link Job} to save; must not be {@code null}
     */
    public void save(Job job) {
        List<Job> jobs = findAll();
        jobs.add(job);
        JsonFileUtil.writeJson(filePath, jobs);
    }

    /**
     * Replaces an existing job record with the supplied updated version.
     * The job is matched by its {@code id} field.
     *
     * @param updatedJob the job carrying the new field values
     * @throws RuntimeException if no job with a matching ID is found
     */
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

    /**
     * Updates the status field of a single job identified by its ID.
     *
     * @param jobId     the ID of the job to update
     * @param newStatus the new status value (e.g. {@code "Open"}, {@code "Completed"})
     * @throws RuntimeException if no job with the given ID is found
     */
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

    /**
     * Returns all jobs whose status is {@code "Open"}.
     *
     * @return list of open {@link Job} objects; empty list if none exist
     */
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

    /**
     * Returns all jobs posted by a specific MO that also match the given status.
     *
     * @param moId   the user ID of the Module Organiser
     * @param status the status to filter by (e.g. {@code "Open"}, {@code "Completed"})
     * @return filtered list of {@link Job} objects; empty list if no match
     */
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

    /**
     * Returns all jobs whose status matches the given value.
     *
     * @param status the status to filter by (e.g. {@code "Open"}, {@code "Completed"})
     * @return filtered list of {@link Job} objects; empty list if no match
     */
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
