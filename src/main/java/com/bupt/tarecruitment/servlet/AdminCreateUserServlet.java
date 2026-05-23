package com.bupt.tarecruitment.servlet;

import com.bupt.tarecruitment.model.Admin;
import com.bupt.tarecruitment.model.Applicant;
import com.bupt.tarecruitment.model.MO;
import com.bupt.tarecruitment.repository.UserRepository;
import com.bupt.tarecruitment.service.ActivityLogService;
import com.bupt.tarecruitment.util.IdUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class AdminCreateUserServlet extends BaseServlet {
    private UserRepository userRepository;

    @Override
    public void init() throws ServletException {
        super.init();
        userRepository = new UserRepository(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUtf8(request, response);
        if (!requireRole(request, response, "ADMIN")) {
            return;
        }

        generateCsrfToken(request);
        forwardView(request, response, "admin-create-user.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUtf8(request, response);
        if (!requireRole(request, response, "ADMIN")) {
            return;
        }

        if (!validateCsrfToken(request)) {
            request.setAttribute("errorMessage", "Invalid CSRF token.");
            forwardView(request, response, "admin-create-user.jsp");
            return;
        }

        String role = request.getParameter("role");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            validateInput(fullName, email, username, password);

            if (userRepository.findByUsername(username.trim()) != null) {
                throw new IllegalArgumentException("Username is already taken.");
            }
            if (userRepository.findByEmail(email.trim()) != null) {
                throw new IllegalArgumentException("Email is already registered.");
            }

            ActivityLogService activityLogService = new ActivityLogService(getServletContext());
            if ("APPLICANT".equalsIgnoreCase(role)) {
                Applicant applicant = new Applicant(
                        IdUtil.generateId("applicant"),
                        username.trim(),
                        password.trim(),
                        "APPLICANT",
                        fullName.trim(),
                        email.trim(),
                        new ArrayList<>(),
                        ""
                );
                userRepository.createApplicant(applicant);
                activityLogService.logCreateUser(getCurrentUser(request), fullName.trim(), "APPLICANT", applicant.getId());
            } else if ("MO".equalsIgnoreCase(role)) {
                MO mo = new MO(
                        IdUtil.generateId("mo"),
                        username.trim(),
                        password.trim(),
                        "MO",
                        fullName.trim(),
                        email.trim()
                );
                userRepository.createMO(mo);
                activityLogService.logCreateUser(getCurrentUser(request), fullName.trim(), "MO", mo.getId());
            } else if ("ADMIN".equalsIgnoreCase(role)) {
                Admin admin = new Admin(
                        IdUtil.generateId("admin"),
                        username.trim(),
                        password.trim(),
                        "ADMIN",
                        fullName.trim(),
                        email.trim()
                );
                userRepository.createAdmin(admin);
                activityLogService.logCreateUser(getCurrentUser(request), fullName.trim(), "ADMIN", admin.getId());
            }

            response.sendRedirect(request.getContextPath() + "/admin/users?msg=created");
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("role", role);
            request.setAttribute("fullName", fullName);
            request.setAttribute("email", email);
            request.setAttribute("username", username);
            forwardView(request, response, "admin-create-user.jsp");
        }
    }

    private void validateInput(String fullName, String email, String username, String password) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be empty.");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters.");
        }
    }
}
