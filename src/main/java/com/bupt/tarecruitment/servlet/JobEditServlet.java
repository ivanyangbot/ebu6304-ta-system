package com.bupt.tarecruitment.servlet;

import com.bupt.tarecruitment.model.Job;
import com.bupt.tarecruitment.model.User;
import com.bupt.tarecruitment.service.ActivityLogService;
import com.bupt.tarecruitment.service.JobService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles the MO job-edit workflow for open TA job postings.
 *
 * <p>The servlet preserves ownership and status checks in both GET and POST
 * handlers so that an MO can only view or update their own open jobs. Completed
 * jobs are locked from editing to avoid changing historical recruitment data.</p>
 *
 * <p>URL mapping: {@code /mo/jobs/edit}. The GET request renders the edit form,
 * while the POST request validates the submitted fields and delegates the
 * persistence update to {@link JobService#updateJob(String, String, String, String, List, int)}.</p>
 *
 * @author  Group 71
 * @version 1.0
 * @see     JobService
 * @see     ActivityLogService
 */
public class JobEditServlet extends BaseServlet {
    /**
     * Renders the edit form for an owned open job.
     *
     * @param request  HTTP request containing the {@code id} query parameter
     * @param response HTTP response used for redirects or JSP forwarding
     * @throws ServletException if the edit JSP cannot be rendered
     * @throws IOException      if redirecting or forwarding fails
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUtf8(request, response);
        if (!requireRole(request, response, "MO")) {
            return;
        }

        String jobId = request.getParameter("id");
        if (jobId == null || jobId.trim().isEmpty()) {
            forwardError(request, response, "Job ID is required.", request.getContextPath() + "/mo/jobs");
            return;
        }

        JobService jobService = new JobService(getServletContext());
        Job job = jobService.getJobById(jobId);
        if (job == null) {
            forwardError(request, response, "Job not found.", request.getContextPath() + "/mo/jobs");
            return;
        }

        User currentUser = getCurrentUser(request);
        if (!job.getPostedByMoId().equals(currentUser.getId())) {
            forwardError(request, response, "You do not have permission to edit this job.", request.getContextPath() + "/mo/jobs");
            return;
        }

        if (!"Open".equalsIgnoreCase(job.getStatus())) {
            forwardError(request, response, "Only open jobs can be edited.", request.getContextPath() + "/mo/jobs");
            return;
        }

        generateCsrfToken(request);
        request.setAttribute("job", job);
        forwardView(request, response, "edit-job.jsp");
    }

    /**
     * Validates and persists edits submitted by the owning MO.
     *
     * <p>The method validates CSRF protection, job ownership, editable status,
     * required text fields, and positive workload hours before saving changes.</p>
     *
     * @param request  HTTP request carrying the submitted job fields
     * @param response HTTP response used for redirects or JSP forwarding
     * @throws ServletException if the edit JSP cannot be rendered after validation failure
     * @throws IOException      if redirecting or forwarding fails
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUtf8(request, response);
        if (!requireRole(request, response, "MO")) {
            return;
        }

        if (!validateCsrfToken(request)) {
            forwardError(request, response, "Invalid request. Please try again.", request.getContextPath() + "/mo/jobs");
            return;
        }

        String jobId = request.getParameter("id");
        if (jobId == null || jobId.trim().isEmpty()) {
            forwardError(request, response, "Job ID is required.", request.getContextPath() + "/mo/jobs");
            return;
        }

        JobService jobService = new JobService(getServletContext());
        Job job = jobService.getJobById(jobId);
        if (job == null) {
            forwardError(request, response, "Job not found.", request.getContextPath() + "/mo/jobs");
            return;
        }

        User currentUser = getCurrentUser(request);
        if (!job.getPostedByMoId().equals(currentUser.getId())) {
            forwardError(request, response, "You do not have permission to edit this job.", request.getContextPath() + "/mo/jobs");
            return;
        }

        if (!"Open".equalsIgnoreCase(job.getStatus())) {
            forwardError(request, response, "Only open jobs can be edited.", request.getContextPath() + "/mo/jobs");
            return;
        }

        String title = request.getParameter("title");
        String moduleName = request.getParameter("moduleName");
        String description = request.getParameter("description");
        String requiredSkillsText = request.getParameter("requiredSkills");
        String hoursText = request.getParameter("hours");

        if (isBlank(title) || isBlank(moduleName) || isBlank(description) || isBlank(hoursText)) {
            request.setAttribute("errorMessage", "Please complete all required fields.");
            request.setAttribute("job", job);
            generateCsrfToken(request);
            forwardView(request, response, "edit-job.jsp");
            return;
        }

        int hours;
        try {
            hours = Integer.parseInt(hoursText);
            if (hours <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Hours must be a positive integer.");
            request.setAttribute("job", job);
            generateCsrfToken(request);
            forwardView(request, response, "edit-job.jsp");
            return;
        }

        jobService.updateJob(jobId, title.trim(), moduleName.trim(), description.trim(),
                parseSkills(requiredSkillsText), hours);
        new ActivityLogService(getServletContext()).logEditJob(currentUser, title.trim(), jobId);
        response.sendRedirect(request.getContextPath() + "/mo/jobs?msg=updated");
    }

    /**
     * Parses a comma-separated skill list from the edit form.
     *
     * @param skillsText comma-separated skill text
     * @return normalized non-empty skill values in their submitted order
     */
    private List<String> parseSkills(String skillsText) {
        List<String> skills = new ArrayList<>();
        if (skillsText == null || skillsText.trim().isEmpty()) {
            return skills;
        }
        String[] parts = skillsText.split(",");
        for (String part : parts) {
            String value = part.trim();
            if (!value.isEmpty()) {
                skills.add(value);
            }
        }
        return skills;
    }

    /**
     * Checks whether a submitted text field is missing or only whitespace.
     *
     * @param value text value to check
     * @return {@code true} if the value is {@code null}, empty, or only whitespace
     */
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
