package com.bupt.tarecruitment.servlet;

import com.bupt.tarecruitment.model.ApplicationRecord;
import com.bupt.tarecruitment.model.Job;
import com.bupt.tarecruitment.service.ApplicationService;
import com.bupt.tarecruitment.service.JobService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UpdateApplicationStatusServlet extends BaseServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUtf8(request, response);
        if (!requireRole(request, response, "MO")) {
            return;
        }

        String applicationId = request.getParameter("applicationId");
        String jobId = request.getParameter("jobId");
        String status = request.getParameter("status");

        ApplicationService applicationService = new ApplicationService(getServletContext());
        JobService jobService = new JobService(getServletContext());
        ApplicationRecord record = applicationService.getApplicationById(applicationId);
        Job job = jobService.getJobById(jobId);

        if (record == null || job == null) {
            forwardError(request, response, "Application or job not found.", request.getContextPath() + "/mo/jobs");
            return;
        }

        if (!getCurrentUser(request).getId().equals(job.getPostedByMoId())) {
            forwardError(request, response, "You can only update applications for your own jobs.",
                    request.getContextPath() + "/mo/jobs");
            return;
        }

        try {
            applicationService.updateApplicationStatus(applicationId, status);
            response.sendRedirect(request.getContextPath() + "/mo/applications?jobId=" + jobId + "&msg=updated");
        } catch (RuntimeException e) {
            forwardError(request, response, e.getMessage(), request.getContextPath() + "/mo/applications?jobId=" + jobId);
        }
    }
}
