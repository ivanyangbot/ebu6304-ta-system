package com.bupt.tarecruitment.servlet;

import com.bupt.tarecruitment.model.Job;
import com.bupt.tarecruitment.repository.JobRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class MOJobsServlet extends BaseServlet {
    private JobRepository jobRepository;

    @Override
    public void init() throws ServletException {
        super.init();
        jobRepository = new JobRepository(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUtf8(request, response);
        if (!requireRole(request, response, "MO")) {
            return;
        }

        String moId = getCurrentUser(request).getId();
        List<Job> openJobs = jobRepository.findByMoIdAndStatus(moId, "Open");
        List<Job> completedJobs = jobRepository.findByMoIdAndStatus(moId, "Completed");
        
        request.setAttribute("openJobs", openJobs);
        request.setAttribute("completedJobs", completedJobs);
        forwardView(request, response, "mo-jobs.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUtf8(request, response);
        if (!requireRole(request, response, "MO")) {
            return;
        }

        String action = request.getParameter("action");
        if ("complete".equals(action)) {
            handleCompleteJob(request, response);
            return;
        } else if ("reopen".equals(action)) {
            handleReopenJob(request, response);
            return;
        }

        doGet(request, response);
    }

    private void handleCompleteJob(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!validateCsrfToken(request)) {
            request.setAttribute("errorMessage", "Invalid CSRF token.");
            doGet(request, response);
            return;
        }

        String jobId = request.getParameter("jobId");
        String moId = getCurrentUser(request).getId();

        try {
            Job job = jobRepository.findById(jobId);
            if (job == null) {
                throw new RuntimeException("Job not found.");
            }

            if (!moId.equals(job.getPostedByMoId())) {
                throw new RuntimeException("You can only complete your own jobs.");
            }

            if (!"Open".equals(job.getStatus())) {
                throw new RuntimeException("Only open jobs can be marked as completed.");
            }

            jobRepository.updateStatus(jobId, "Completed");
            request.setAttribute("successMessage", "Job marked as completed successfully.");
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
        }

        doGet(request, response);
    }

    private void handleReopenJob(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!validateCsrfToken(request)) {
            request.setAttribute("errorMessage", "Invalid CSRF token.");
            doGet(request, response);
            return;
        }

        String jobId = request.getParameter("jobId");
        String moId = getCurrentUser(request).getId();

        try {
            Job job = jobRepository.findById(jobId);
            if (job == null) {
                throw new RuntimeException("Job not found.");
            }

            if (!moId.equals(job.getPostedByMoId())) {
                throw new RuntimeException("You can only reopen your own jobs.");
            }

            if (!"Completed".equals(job.getStatus())) {
                throw new RuntimeException("Only completed jobs can be reopened.");
            }

            jobRepository.updateStatus(jobId, "Open");
            request.setAttribute("successMessage", "Job reopened successfully.");
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
        }

        doGet(request, response);
    }
}
