package com.bupt.tarecruitment.servlet;

import com.bupt.tarecruitment.model.ApplicationDisplay;
import com.bupt.tarecruitment.model.ApplicationRecord;
import com.bupt.tarecruitment.model.Job;
import com.bupt.tarecruitment.service.ApplicationService;
import com.bupt.tarecruitment.service.JobService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyApplicationsServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUtf8(request, response);
        if (!requireRole(request, response, "APPLICANT")) {
            return;
        }

        ApplicationService applicationService = new ApplicationService(getServletContext());
        JobService jobService = new JobService(getServletContext());
        List<ApplicationRecord> records = applicationService.getApplicationsByApplicant(getCurrentUser(request).getId());
        List<ApplicationDisplay> displays = new ArrayList<>();

        for (ApplicationRecord record : records) {
            Job job = jobService.getJobById(record.getJobId());
            displays.add(new ApplicationDisplay(record, job, null, null));
        }

        request.setAttribute("applicationDisplays", displays);
        forwardView(request, response, "my-applications.jsp");
    }
}
