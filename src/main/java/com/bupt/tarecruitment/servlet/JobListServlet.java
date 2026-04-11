package com.bupt.tarecruitment.servlet;

import com.bupt.tarecruitment.model.ApplicationRecord;
import com.bupt.tarecruitment.model.User;
import com.bupt.tarecruitment.service.ApplicationService;
import com.bupt.tarecruitment.service.JobService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobListServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUtf8(request, response);
        if (!requireRole(request, response, "APPLICANT")) {
            return;
        }

        JobService jobService = new JobService(getServletContext());
        ApplicationService applicationService = new ApplicationService(getServletContext());
        User currentUser = getCurrentUser(request);
        List<ApplicationRecord> myApplications = applicationService.getApplicationsByApplicant(currentUser.getId());
        Map<String, Boolean> appliedJobMap = new HashMap<>();

        for (ApplicationRecord application : myApplications) {
            appliedJobMap.put(application.getJobId(), true);
        }

        request.setAttribute("jobs", jobService.getOpenJobs());
        request.setAttribute("appliedJobMap", appliedJobMap);
        forwardView(request, response, "job-list.jsp");
    }
}
