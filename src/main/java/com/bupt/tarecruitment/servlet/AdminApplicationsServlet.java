package com.bupt.tarecruitment.servlet;

import com.bupt.tarecruitment.model.ApplicationDisplay;
import com.bupt.tarecruitment.model.ApplicationRecord;
import com.bupt.tarecruitment.model.Applicant;
import com.bupt.tarecruitment.model.Job;
import com.bupt.tarecruitment.repository.ApplicationRepository;
import com.bupt.tarecruitment.repository.JobRepository;
import com.bupt.tarecruitment.repository.UserRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet that provides administrators with a consolidated view of all
 * {@link com.bupt.tarecruitment.model.ApplicationRecord ApplicationRecord}s
 * across every job posting in the system.
 *
 * <p>Mapped to {@code /admin/applications} (see {@code web.xml}). Access is
 * restricted to users with the {@code ADMIN} role; any other authenticated
 * user is redirected to the login page by
 * {@link BaseServlet#requireRole(HttpServletRequest, HttpServletResponse, String)}.</p>
 *
 * <p>The servlet fetches every application from
 * {@link com.bupt.tarecruitment.repository.ApplicationRepository}, enriches
 * each entry with its associated {@link com.bupt.tarecruitment.model.Job} and
 * {@link com.bupt.tarecruitment.model.Applicant}, then exposes the combined
 * data as a list of {@link com.bupt.tarecruitment.model.ApplicationDisplay}
 * objects under the request attribute {@code "applications"} before forwarding
 * to {@code admin-applications.jsp}.</p>
 *
 * @author  Group 71
 * @version 1.0
 * @see     com.bupt.tarecruitment.model.ApplicationDisplay
 * @see     com.bupt.tarecruitment.repository.ApplicationRepository
 */
public class AdminApplicationsServlet extends BaseServlet {

    /**
     * Handles HTTP GET requests. Loads all applications system-wide,
     * resolves the associated job and applicant for each entry, and
     * forwards the enriched list to the admin applications view.
     *
     * @param request  the {@link HttpServletRequest} object
     * @param response the {@link HttpServletResponse} object
     * @throws ServletException if the request cannot be handled
     * @throws IOException      if an I/O error occurs during forwarding
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUtf8(request, response);
        if (!requireRole(request, response, "ADMIN")) {
            return;
        }

        ApplicationRepository applicationRepository = new ApplicationRepository(getServletContext());
        JobRepository jobRepository = new JobRepository(getServletContext());
        UserRepository userRepository = new UserRepository(getServletContext());

        List<ApplicationRecord> allApplications = applicationRepository.findAll();
        List<ApplicationDisplay> applicationDisplays = new ArrayList<>();

        for (ApplicationRecord application : allApplications) {
            Job job = jobRepository.findById(application.getJobId());
            Applicant applicant = userRepository.findApplicantById(application.getApplicantId());
            applicationDisplays.add(new ApplicationDisplay(application, job, applicant, null));
        }

        request.setAttribute("applications", applicationDisplays);
        forwardView(request, response, "admin-applications.jsp");
    }
}
