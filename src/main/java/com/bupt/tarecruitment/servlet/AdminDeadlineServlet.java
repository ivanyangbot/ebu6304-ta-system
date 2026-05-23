package com.bupt.tarecruitment.servlet;

import com.bupt.tarecruitment.model.Job;
import com.bupt.tarecruitment.service.JobService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet that provides administrators with a view of TA job postings
 * whose application deadlines are approaching within the next 7 days.
 *
 * <p>Mapped to {@code /admin/deadlines}. Only users with the {@code ADMIN}
 * role may access this page.</p>
 *
 * <p>The page helps administrators proactively monitor recruitment pipelines
 * and remind Module Organisers to review applications before deadlines expire.</p>
 *
 * @author  Group 71
 * @version 1.0
 * @see     JobService#getJobsExpiringSoon(int)
 */
public class AdminDeadlineServlet extends BaseServlet {

    /** Number of days ahead to look when identifying expiring jobs. */
    private static final int LOOKAHEAD_DAYS = 7;

    /**
     * Handles HTTP GET requests. Loads all open jobs expiring within
     * {@value #LOOKAHEAD_DAYS} days and all jobs whose deadline has
     * already passed but are still open, then forwards to the view.
     *
     * @param request  the {@link HttpServletRequest} object
     * @param response the {@link HttpServletResponse} object
     * @throws ServletException if the request cannot be handled
     * @throws IOException      if an I/O error occurs during forwarding
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setUtf8(request, response);
        if (!requireRole(request, response, "ADMIN")) {
            return;
        }

        JobService jobService = new JobService(getServletContext());
        List<Job> expiringSoon = jobService.getJobsExpiringSoon(LOOKAHEAD_DAYS);

        // Also gather overdue open jobs (deadline passed but still "Open")
        List<Job> allJobs = jobService.getAllJobs();
        List<Job> overdueJobs = new java.util.ArrayList<>();
        for (Job job : allJobs) {
            if ("Open".equals(job.getStatus()) && job.isDeadlinePassed()) {
                overdueJobs.add(job);
            }
        }

        request.setAttribute("expiringSoon", expiringSoon);
        request.setAttribute("overdueJobs", overdueJobs);
        request.setAttribute("lookaheadDays", LOOKAHEAD_DAYS);
        forwardView(request, response, "admin-deadlines.jsp");
    }
}
