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

public class AdminApplicationsServlet extends BaseServlet {
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
