package com.bupt.tarecruitment.servlet;

import com.bupt.tarecruitment.service.JobService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JobCreateServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUtf8(request, response);
        if (!requireRole(request, response, "MO")) {
            return;
        }
        forwardView(request, response, "create-job.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUtf8(request, response);
        if (!requireRole(request, response, "MO")) {
            return;
        }

        String title = request.getParameter("title");
        String moduleName = request.getParameter("moduleName");
        String description = request.getParameter("description");
        String requiredSkillsText = request.getParameter("requiredSkills");
        String hoursText = request.getParameter("hours");

        if (isBlank(title) || isBlank(moduleName) || isBlank(description) || isBlank(hoursText)) {
            request.setAttribute("errorMessage", "Please complete all required fields.");
            forwardView(request, response, "create-job.jsp");
            return;
        }

        int hours;
        try {
            hours = Integer.parseInt(hoursText);
            if (hours <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Hours must be a positive integer.");
            forwardView(request, response, "create-job.jsp");
            return;
        }

        JobService jobService = new JobService(getServletContext());
        jobService.createJob(title.trim(), moduleName.trim(), description.trim(), parseSkills(requiredSkillsText), hours,
                getCurrentUser(request).getId());
        response.sendRedirect(request.getContextPath() + "/mo/jobs?msg=created");
    }

    private List<String> parseSkills(String skillsText) {
        List<String> skills = new ArrayList<>();
        if (skillsText == null || skillsText.trim().isEmpty()) {
            return skills;
        }

        String[] parts = skillsText.split(",");
        for (String part : parts) {
            String value = part.trim();
            if (!value.isEmpty()) {
                skills.add(value);
            }
        }
        return skills;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
