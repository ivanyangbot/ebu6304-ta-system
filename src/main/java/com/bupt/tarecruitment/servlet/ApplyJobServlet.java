package com.bupt.tarecruitment.servlet;

import com.bupt.tarecruitment.model.Job;
import com.bupt.tarecruitment.service.ApplicationService;
import com.bupt.tarecruitment.service.JobService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ApplyJobServlet extends BaseServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUtf8(request, response);
        if (!requireRole(request, response, "APPLICANT")) {
            return;
        }

        String jobId = request.getParameter("id");
        JobService jobService = new JobService(getServletContext());
        Job job = jobService.getJobById(jobId);
        if (job == null) {
            forwardError(request, response, "The selected job does not exist.", request.getContextPath() + "/jobs");
            return;
        }

        ApplicationService applicationService = new ApplicationService(getServletContext());
        try {
            applicationService.applyForJob(jobId, getCurrentUser(request).getId());
            response.sendRedirect(request.getContextPath() + "/jobs/detail?id=" + jobId + "&msg=applied");
        } catch (RuntimeException e) {
            response.sendRedirect(request.getContextPath() + "/jobs/detail?id=" + jobId + "&msg=duplicate");
        }
    }
}
