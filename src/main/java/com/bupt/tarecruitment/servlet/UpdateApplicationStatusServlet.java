package com.bupt.tarecruitment.servlet;

import com.bupt.tarecruitment.model.ApplicationRecord;
import com.bupt.tarecruitment.model.Applicant;
import com.bupt.tarecruitment.model.Job;
import com.bupt.tarecruitment.repository.NotificationRepository;
import com.bupt.tarecruitment.repository.UserRepository;
import com.bupt.tarecruitment.service.ActivityLogService;
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
        String feedback = request.getParameter("moFeedback");  // optional MO feedback

        ApplicationService applicationService = new ApplicationService(getServletContext());
        JobService jobService = new JobService(getServletContext());
        NotificationRepository notificationRepository = new NotificationRepository(getServletContext());
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
            String oldStatus = record.getStatus();

            applicationService.updateApplicationStatus(applicationId, status, feedback);
            
            notificationRepository.createApplicationStatusNotification(
                    record.getApplicantId(),
                    job.getTitle(),
                    status,
                    applicationId
            );

            UserRepository userRepository = new UserRepository(getServletContext());
            Applicant applicant = userRepository.findApplicantById(record.getApplicantId());
            String applicantName = applicant != null ? applicant.getFullName() : record.getApplicantId();
            new ActivityLogService(getServletContext()).logUpdateApplicationStatus(
                    getCurrentUser(request), applicantName, job.getTitle(), applicationId, oldStatus, status);

            response.sendRedirect(request.getContextPath() + "/mo/applications?jobId=" + jobId + "&msg=updated");
        } catch (RuntimeException e) {
            forwardError(request, response, e.getMessage(), request.getContextPath() + "/mo/applications?jobId=" + jobId);
        }
    }
}
