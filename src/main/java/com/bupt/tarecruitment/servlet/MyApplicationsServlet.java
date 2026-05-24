package com.bupt.tarecruitment.servlet;

import com.bupt.tarecruitment.model.*;
import com.bupt.tarecruitment.repository.UserRepository;
import com.bupt.tarecruitment.service.ApplicationService;
import com.bupt.tarecruitment.service.JobService;
import com.bupt.tarecruitment.service.MatchService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Renders the applicant's own application history.
 *
 * <p>The servlet enriches each application with its job, match score, missing
 * skills, and a simple status description so that applicants can understand the
 * current review result without opening each job detail page.</p>
 *
 * <p>URL mapping: {@code /applicant/applications}. Only users with the
 * {@code APPLICANT} role can access this servlet.</p>
 *
 * @author  Group 71
 * @version 1.0
 * @see     ApplicationDisplay
 * @see     MatchService
 * @see     ApplicationService
 */
public class MyApplicationsServlet extends BaseServlet {
    /**
     * Builds the application-history view for the logged-in applicant.
     *
     * @param request  HTTP request from the logged-in applicant
     * @param response HTTP response used for redirects or JSP forwarding
     * @throws ServletException if forwarding to the JSP view fails
     * @throws IOException      if redirecting or forwarding fails
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUtf8(request, response);
        if (!requireRole(request, response, "APPLICANT")) {
            return;
        }

        generateCsrfToken(request);

        ApplicationService applicationService = new ApplicationService(getServletContext());
        JobService jobService = new JobService(getServletContext());
        UserRepository userRepository = new UserRepository(getServletContext());
        MatchService matchService = new MatchService();

        User currentUser = getCurrentUser(request);
        Applicant applicant = userRepository.findApplicantById(currentUser.getId());

        List<ApplicationRecord> records = applicationService.getApplicationsByApplicant(currentUser.getId());
        List<ApplicationDisplay> displays = new ArrayList<>();

        for (ApplicationRecord record : records) {
            Job job = jobService.getJobById(record.getJobId());
            MatchResult matchResult = null;
            if (applicant != null && job != null) {
                matchResult = matchService.calculateMatch(applicant.getSkills(), job.getRequiredSkills());
            }
            ApplicationDisplay display = new ApplicationDisplay(record, job, applicant, matchResult);
            display.setStatusDescription(buildStatusDescription(record.getStatus()));
            displays.add(display);
        }

        request.setAttribute("applicationDisplays", displays);
        forwardView(request, response, "my-applications.jsp");
    }

    /**
     * Maps an application status to an i18n key suffix consumed by the JSP.
     *
     * @param status raw application status
     * @return normalized status description key suffix
     */
    private String buildStatusDescription(String status) {
        if ("Accepted".equalsIgnoreCase(status)) {
            return "Accepted";
        }
        if ("Rejected".equalsIgnoreCase(status)) {
            return "Rejected";
        }
        return "Pending";
    }
}
