package com.bupt.tarecruitment.servlet;

import com.bupt.tarecruitment.model.Applicant;
import com.bupt.tarecruitment.repository.UserRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApplicantProfileServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUtf8(request, response);
        if (!requireRole(request, response, "APPLICANT")) {
            return;
        }

        UserRepository userRepository = new UserRepository(getServletContext());
        Applicant applicant = userRepository.findApplicantById(getCurrentUser(request).getId());
        if (applicant == null) {
            forwardError(request, response, "Applicant profile not found.", request.getContextPath() + "/dashboard");
            return;
        }

        request.setAttribute("applicant", applicant);
        request.setAttribute("skillsText", String.join(", ", applicant.getSkills()));
        forwardView(request, response, "applicant-profile.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUtf8(request, response);
        if (!requireRole(request, response, "APPLICANT")) {
            return;
        }

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String skillsText = request.getParameter("skills");
        String selfIntroduction = request.getParameter("selfIntroduction");

        if (fullName == null || fullName.trim().isEmpty() || email == null || email.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Full name and email cannot be empty.");
            doGet(request, response);
            return;
        }

        UserRepository userRepository = new UserRepository(getServletContext());
        Applicant applicant = userRepository.findApplicantById(getCurrentUser(request).getId());
        if (applicant == null) {
            forwardError(request, response, "Applicant profile not found.", request.getContextPath() + "/dashboard");
            return;
        }

        applicant.setFullName(fullName.trim());
        applicant.setEmail(email.trim());
        applicant.setSkills(parseSkills(skillsText));
        applicant.setSelfIntroduction(selfIntroduction == null ? "" : selfIntroduction.trim());
        userRepository.updateApplicant(applicant);
        request.getSession().setAttribute("currentUser", applicant);

        response.sendRedirect(request.getContextPath() + "/applicant/profile?msg=updated");
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
}
