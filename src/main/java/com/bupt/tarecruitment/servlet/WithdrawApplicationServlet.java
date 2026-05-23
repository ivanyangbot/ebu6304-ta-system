package com.bupt.tarecruitment.servlet;

import com.bupt.tarecruitment.model.ApplicationRecord;
import com.bupt.tarecruitment.model.Job;
import com.bupt.tarecruitment.service.ActivityLogService;
import com.bupt.tarecruitment.service.ApplicationService;
import com.bupt.tarecruitment.service.JobService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WithdrawApplicationServlet extends BaseServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUtf8(request, response);
        if (!requireRole(request, response, "APPLICANT")) {
            return;
        }

        if (!validateCsrfToken(request)) {
            forwardError(request, response, "Invalid CSRF token.", request.getContextPath() + "/applicant/applications");
            return;
        }

        String applicationId = request.getParameter("applicationId");
        String jobId = request.getParameter("jobId");
        String applicantId = getCurrentUser(request).getId();

        if (applicationId == null || applicationId.trim().isEmpty()) {
            if (jobId != null && !jobId.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/jobs/detail?id=" + jobId + "&msg=withdrawError&error=Application ID cannot be empty.");
            } else {
                response.sendRedirect(request.getContextPath() + "/applicant/applications?msg=withdrawError&error=Application ID cannot be empty.");
            }
            return;
        }

        ApplicationService applicationService = new ApplicationService(getServletContext());
        JobService jobService = new JobService(getServletContext());

        try {
            ApplicationRecord appRecord = applicationService.getApplicationById(applicationId);
            String jobTitle = "";
            if (appRecord != null) {
                Job job = jobService.getJobById(appRecord.getJobId());
                if (job != null) {
                    jobTitle = job.getTitle();
                }
            }
            applicationService.withdrawApplication(applicationId, applicantId);
            new ActivityLogService(getServletContext()).logWithdrawApplication(
                    getCurrentUser(request), jobTitle, applicationId);
            response.sendRedirect(request.getContextPath() + "/applicant/applications?msg=withdrawn");
        } catch (RuntimeException e) {
            if (jobId != null && !jobId.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/jobs/detail?id=" + jobId + "&msg=withdrawError&error=" + e.getMessage());
            } else {
                response.sendRedirect(request.getContextPath() + "/applicant/applications?msg=withdrawError&error=" + e.getMessage());
            }
        }
    }
}