package com.bupt.tarecruitment.servlet;

import com.bupt.tarecruitment.model.ApplicationRecord;
import com.bupt.tarecruitment.model.Job;
import com.bupt.tarecruitment.model.MoActionRequiredJobView;
import com.bupt.tarecruitment.model.MoPendingApplicationView;
import com.bupt.tarecruitment.model.Notification;
import com.bupt.tarecruitment.model.User;
import com.bupt.tarecruitment.model.WorkloadSummary;
import com.bupt.tarecruitment.repository.ApplicationRepository;
import com.bupt.tarecruitment.repository.NotificationRepository;
import com.bupt.tarecruitment.repository.UserRepository;
import com.bupt.tarecruitment.service.ApplicationService;
import com.bupt.tarecruitment.service.JobService;
import com.bupt.tarecruitment.service.WorkloadService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Builds role-specific dashboard data for applicants, MOs, and administrators.
 *
 * <p>The dashboard is intentionally kept as a single Servlet/JSP endpoint, while
 * this servlet supplies the different summary metrics needed by each role.</p>
 *
 * <p>Applicant dashboards receive profile and application counts, MO dashboards
 * receive pending-review summaries, and administrator dashboards receive global
 * user, job, application, acceptance-rate, and workload statistics.</p>
 *
 * @author  Group 71
 * @version 1.0
 * @see     ApplicationService
 * @see     JobService
 * @see     WorkloadService
 * @see     MoPendingApplicationView
 * @see     MoActionRequiredJobView
 */
public class DashboardServlet extends BaseServlet {
    /**
     * Builds the role-specific dashboard model for applicants, MOs, and administrators.
     *
     * <p>The applicant dashboard now receives profile summary data, while the MO
     * and admin dashboards receive recruitment metrics used by the enhanced JSP
     * cards. Keeping this logic in the servlet preserves the existing
     * Servlet/JSP architecture and avoids introducing a new controller layer.</p>
     *
     * @param request  HTTP request from a logged-in user
     * @param response HTTP response used for login redirects or dashboard forwarding
     * @throws ServletException if forwarding to the dashboard JSP fails
     * @throws IOException      if redirecting or forwarding fails
     */
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
            UserRepository userRepository = new UserRepository(getServletContext());
            request.setAttribute("applicantProfile", userRepository.findApplicantById(currentUser.getId()));

        } else if ("MO".equalsIgnoreCase(currentUser.getRole())) {
            UserRepository userRepository = new UserRepository(getServletContext());
            List<Job> moJobs = jobService.getJobsByMo(currentUser.getId());
            request.setAttribute("myJobCount", moJobs.size());

            ApplicationRepository appRepo = new ApplicationRepository(getServletContext());
            List<ApplicationRecord> allApps = appRepo.findAll();

            int pendingCount = 0;
            List<MoPendingApplicationView> recentPendingApplications = new ArrayList<>();
            List<MoActionRequiredJobView> actionRequiredJobs = new ArrayList<>();

            for (Job job : moJobs) {
                if (!"Open".equals(job.getStatus())) {
                    continue;
                }

                int jobPendingCount = 0;
                for (ApplicationRecord app : allApps) {
                    if (!job.getId().equals(app.getJobId()) || !"Pending".equals(app.getStatus())) {
                        continue;
                    }
                    pendingCount++;
                    jobPendingCount++;
                    User applicant = userRepository.findById(app.getApplicantId());
                    recentPendingApplications.add(new MoPendingApplicationView(
                            app.getId(),
                            job.getId(),
                            job.getTitle(),
                            applicant == null ? "Unknown Applicant" : applicant.getFullName(),
                            app.getAppliedAt()
                    ));
                }

                if (jobPendingCount > 0) {
                    actionRequiredJobs.add(new MoActionRequiredJobView(job.getId(), job.getTitle(), jobPendingCount));
                }
            }

            recentPendingApplications.sort(Comparator.comparing(
                    MoPendingApplicationView::getAppliedAt,
                    Comparator.nullsLast(Comparator.reverseOrder())
            ));
            if (recentPendingApplications.size() > 5) {
                recentPendingApplications = new ArrayList<>(recentPendingApplications.subList(0, 5));
            }

            actionRequiredJobs.sort(Comparator
                    .comparingInt(MoActionRequiredJobView::getPendingCount).reversed()
                    .thenComparing(MoActionRequiredJobView::getJobTitle));

            request.setAttribute("pendingApplicationCount", pendingCount);
            request.setAttribute("jobsNeedingAction", actionRequiredJobs.size());
            request.setAttribute("recentPendingApplications", recentPendingApplications);
            request.setAttribute("actionRequiredJobs", actionRequiredJobs);

        } else if ("ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            UserRepository userRepository = new UserRepository(getServletContext());
            List<User> allUsers = userRepository.findAll();
            int totalUserCount = allUsers.size();
            int applicantCount = 0;
            int moCount = 0;
            for (User u : allUsers) {
                if ("APPLICANT".equalsIgnoreCase(u.getRole())) applicantCount++;
                else if ("MO".equalsIgnoreCase(u.getRole())) moCount++;
            }

            int openJobCount = jobService.getOpenJobs().size();

            ApplicationRepository appRepo = new ApplicationRepository(getServletContext());
            List<ApplicationRecord> allApps = appRepo.findAll();
            int pendingCount = 0;
            int acceptedCount = 0;
            for (ApplicationRecord app : allApps) {
                if ("Pending".equals(app.getStatus())) pendingCount++;
                else if ("Accepted".equals(app.getStatus())) acceptedCount++;
            }
            int totalAppCount = allApps.size();
            int acceptanceRate = totalAppCount > 0 ? (acceptedCount * 100 / totalAppCount) : 0;

            List<WorkloadSummary> summaries = workloadService.getApplicantWorkloadSummaries();
            int overloadedCount = 0;
            for (WorkloadSummary s : summaries) {
                if ("Overloaded".equals(s.getWorkloadStatus())) overloadedCount++;
            }

            request.setAttribute("totalUserCount", totalUserCount);
            request.setAttribute("applicantCount", applicantCount);
            request.setAttribute("moCount", moCount);
            request.setAttribute("openJobCount", openJobCount);
            request.setAttribute("pendingApplicationCount", pendingCount);
            request.setAttribute("acceptanceRate", acceptanceRate);
            request.setAttribute("overloadedCount", overloadedCount);
            request.setAttribute("summaryCount", summaries.size());
        }

        forwardView(request, response, "dashboard.jsp");
    }
}
