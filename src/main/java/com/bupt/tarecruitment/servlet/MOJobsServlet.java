package com.bupt.tarecruitment.servlet;

import com.bupt.tarecruitment.service.JobService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MOJobsServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUtf8(request, response);
        if (!requireRole(request, response, "MO")) {
            return;
        }

        JobService jobService = new JobService(getServletContext());
        request.setAttribute("jobs", jobService.getJobsByMo(getCurrentUser(request).getId()));
        forwardView(request, response, "mo-jobs.jsp");
    }
}
