package com.bupt.tarecruitment.servlet;

import com.bupt.tarecruitment.service.WorkloadService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminWorkloadServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUtf8(request, response);
        if (!requireRole(request, response, "ADMIN")) {
            return;
        }

        WorkloadService workloadService = new WorkloadService(getServletContext());
        request.setAttribute("workloadSummaries", workloadService.getApplicantWorkloadSummaries());
        request.setAttribute("threshold", WorkloadService.DEFAULT_THRESHOLD);
        forwardView(request, response, "admin-workload.jsp");
    }
}
