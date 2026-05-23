package com.bupt.tarecruitment.servlet;

import com.bupt.tarecruitment.model.Applicant;
import com.bupt.tarecruitment.model.ApplicationRecord;
import com.bupt.tarecruitment.model.Job;
import com.bupt.tarecruitment.model.LoadProjection;
import com.bupt.tarecruitment.model.WorkloadSummary;
import com.bupt.tarecruitment.repository.NotificationRepository;
import com.bupt.tarecruitment.repository.UserRepository;
import com.bupt.tarecruitment.service.ApplicationService;
import com.bupt.tarecruitment.service.JobService;
import com.bupt.tarecruitment.service.LoadProjectionService;
import com.bupt.tarecruitment.service.WorkloadService;
import com.bupt.tarecruitment.util.WorkloadBandUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles MO updates to application review status.
 *
 * <p>The servlet verifies that the application belongs to the submitted job,
 * that the job belongs to the current MO, and that accepting the applicant will
 * not exceed the workload limit enforced by {@link WorkloadBandUtil}.</p>
 *
 * <p>URL mapping: {@code /mo/application/update}. This endpoint is reached from
 * the MO application review table.</p>
 *
 * @author  Group 71
 * @version 1.0
 * @see     ApplicationService
 * @see     LoadProjectionService
 * @see     WorkloadService
 */
public class UpdateApplicationStatusServlet extends BaseServlet {
    /**
     * Processes a status update request and sends the applicant notification.
     *
     * @param request  HTTP request containing {@code applicationId}, {@code jobId}, and {@code status}
     * @param response HTTP response used for redirects, errors, or forwarding
     * @throws ServletException if forwarding to an error page fails
     * @throws IOException      if redirecting or forwarding fails
     */
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
        NotificationRepository notificationRepository = new NotificationRepository(getServletContext());
        ApplicationRecord record = applicationService.getApplicationById(applicationId);
        Job job = jobService.getJobById(jobId);

        if (record == null || job == null) {
            forwardError(request, response, "Application or job not found.", request.getContextPath() + "/mo/jobs");
            return;
        }

        if (!jobId.equals(record.getJobId())) {
            forwardError(request, response, "Application does not belong to the selected job.",
                    request.getContextPath() + "/mo/jobs");
            return;
        }

        if (!getCurrentUser(request).getId().equals(job.getPostedByMoId())) {
            forwardError(request, response, "You can only update applications for your own jobs.",
                    request.getContextPath() + "/mo/jobs");
            return;
        }

        try {
            if ("Accepted".equals(status) && !"Accepted".equalsIgnoreCase(record.getStatus())) {
                validateProjectedWorkload(record, job);
            }
            applicationService.updateApplicationStatus(applicationId, status);

            notificationRepository.createApplicationStatusNotification(
                    record.getApplicantId(),
                    job.getTitle(),
                    status,
                    applicationId
            );

            response.sendRedirect(request.getContextPath() + "/mo/applications?jobId=" + jobId + "&msg=updated");
        } catch (RuntimeException e) {
            forwardError(request, response, e.getMessage(), request.getContextPath() + "/mo/applications?jobId=" + jobId);
        }
    }

    /**
     * Blocks acceptance if the applicant's projected workload would exceed the
     * system's configured acceptance ceiling.
     *
     * @param record application record being accepted
     * @param job    job related to the application
     * @throws RuntimeException if the applicant is missing or projected workload is too high
     */
    private void validateProjectedWorkload(ApplicationRecord record, Job job) {
        UserRepository userRepository = new UserRepository(getServletContext());
        Applicant applicant = userRepository.findApplicantById(record.getApplicantId());
        if (applicant == null) {
            throw new RuntimeException("Applicant not found.");
        }

        int currentHours = getCurrentHours(record.getApplicantId());
        if (!WorkloadBandUtil.canAcceptMore(currentHours, job.getHours())) {
            Map<String, Integer> currentHoursByApplicantId = new HashMap<>();
            currentHoursByApplicantId.put(record.getApplicantId(), currentHours);
            LoadProjection projection = new LoadProjectionService().project(job, applicant, currentHoursByApplicantId);
            String summary = projection == null ? "" : " " + projection.getSummary();
            throw new RuntimeException("Cannot accept this applicant because the projected workload is too high." + summary);
        }
    }

    /**
     * Looks up the applicant's currently accepted workload hours.
     *
     * @param applicantId applicant user ID
     * @return current accepted workload hours, or {@code 0} if no summary exists
     */
    private int getCurrentHours(String applicantId) {
        WorkloadService workloadService = new WorkloadService(getServletContext());
        for (WorkloadSummary summary : workloadService.getApplicantWorkloadSummaries()) {
            if (summary.getApplicant() != null && applicantId.equals(summary.getApplicant().getId())) {
                return Math.max(summary.getTotalHours(), 0);
            }
        }
        return 0;
    }
}
