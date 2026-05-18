package com.bupt.tarecruitment.servlet;

import com.bupt.tarecruitment.model.Notification;
import com.bupt.tarecruitment.model.User;
import com.bupt.tarecruitment.repository.NotificationRepository;
import com.bupt.tarecruitment.service.ApplicationService;
import com.bupt.tarecruitment.service.JobService;
import com.bupt.tarecruitment.service.WorkloadService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class DashboardServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUtf8(request, response);
        if (!requireLogin(request, response)) {
            return;
        }

        User currentUser = getCurrentUser(request);
        JobService jobService = new JobService(getServletContext());
        ApplicationService applicationService = new ApplicationService(getServletContext());
        WorkloadService workloadService = new WorkloadService(getServletContext());
        NotificationRepository notificationRepository = new NotificationRepository(getServletContext());

        List<Notification> notifications = notificationRepository.findByUserId(currentUser.getId());
        int unreadCount = notificationRepository.countUnreadByUserId(currentUser.getId());
        
        request.setAttribute("notifications", notifications);
        request.setAttribute("unreadCount", unreadCount);

        if ("APPLICANT".equalsIgnoreCase(currentUser.getRole())) {
            request.setAttribute("openJobCount", jobService.getOpenJobs().size());
            request.setAttribute("myApplicationCount",
                    applicationService.getApplicationsByApplicant(currentUser.getId()).size());
        } else if ("MO".equalsIgnoreCase(currentUser.getRole())) {
            request.setAttribute("myJobCount", jobService.getJobsByMo(currentUser.getId()).size());
        } else if ("ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            request.setAttribute("summaryCount", workloadService.getApplicantWorkloadSummaries().size());
        }

        forwardView(request, response, "dashboard.jsp");
    }
}
