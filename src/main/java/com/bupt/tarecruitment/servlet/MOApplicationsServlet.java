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

public class MOApplicationsServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUtf8(request, response);
        if (!requireRole(request, response, "MO")) {
            return;
        }

        String jobId = request.getParameter("jobId");
        JobService jobService = new JobService(getServletContext());
        Job job = jobService.getJobById(jobId);
        if (job == null) {
            forwardError(request, response, "The selected job does not exist.", request.getContextPath() + "/mo/jobs");
            return;
        }

        if (!getCurrentUser(request).getId().equals(job.getPostedByMoId())) {
            forwardError(request, response, "You can only view applications for your own jobs.",
                    request.getContextPath() + "/mo/jobs");
            return;
        }

        ApplicationService applicationService = new ApplicationService(getServletContext());
        UserRepository userRepository = new UserRepository(getServletContext());
        MatchService matchService = new MatchService();
        List<ApplicationDisplay> displays = new ArrayList<>();
        List<ApplicationRecord> records = applicationService.getApplicationsByJob(jobId);

        for (ApplicationRecord record : records) {
            Applicant applicant = userRepository.findApplicantById(record.getApplicantId());
            MatchResult matchResult = applicant == null
                    ? new MatchResult()
                    : matchService.calculateMatch(applicant.getSkills(), job.getRequiredSkills());
            displays.add(new ApplicationDisplay(record, job, applicant, matchResult));
        }

        request.setAttribute("job", job);
        request.setAttribute("applicationDisplays", displays);
        forwardView(request, response, "mo-applications.jsp");
    }
}
