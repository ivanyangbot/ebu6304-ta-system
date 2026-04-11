package com.bupt.tarecruitment.servlet;

import com.bupt.tarecruitment.model.Applicant;
import com.bupt.tarecruitment.model.Job;
import com.bupt.tarecruitment.model.MatchResult;
import com.bupt.tarecruitment.repository.UserRepository;
import com.bupt.tarecruitment.service.ApplicationService;
import com.bupt.tarecruitment.service.JobService;
import com.bupt.tarecruitment.service.MatchService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JobDetailServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

        UserRepository userRepository = new UserRepository(getServletContext());
        Applicant applicant = userRepository.findApplicantById(getCurrentUser(request).getId());
        ApplicationService applicationService = new ApplicationService(getServletContext());
        MatchService matchService = new MatchService();
        MatchResult matchResult = matchService.calculateMatch(applicant.getSkills(), job.getRequiredSkills());

        request.setAttribute("job", job);
        request.setAttribute("matchResult", matchResult);
        request.setAttribute("alreadyApplied", applicationService.hasApplied(jobId, applicant.getId()));
        forwardView(request, response, "job-detail.jsp");
    }
}
