package com.bupt.tarecruitment.servlet;

import com.bupt.tarecruitment.model.Admin;
import com.bupt.tarecruitment.model.Applicant;
import com.bupt.tarecruitment.model.MO;
import com.bupt.tarecruitment.model.User;
import com.bupt.tarecruitment.repository.UserRepository;
import com.bupt.tarecruitment.util.IdUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminUserManagementServlet extends BaseServlet {
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
        loadUsers(request);
        forwardView(request, response, "admin-users.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUtf8(request, response);
        if (!requireRole(request, response, "ADMIN")) {
            return;
        }

        String action = request.getParameter("action");
        if ("create".equals(action)) {
            handleCreate(request, response);
            return;
        } else if ("delete".equals(action)) {
            if (!validateCsrfToken(request)) {
                request.setAttribute("errorMessage", "Invalid CSRF token.");
                loadUsers(request);
                forwardView(request, response, "admin-users.jsp");
                return;
            }
            handleDelete(request, response);
            return;
        }

        loadUsers(request);
        forwardView(request, response, "admin-users.jsp");
    }

    private void loadUsers(HttpServletRequest request) {
        List<User> allUsers = userRepository.findAll();
        List<Applicant> applicants = new ArrayList<>();
        List<MO> mos = new ArrayList<>();
        List<Admin> admins = new ArrayList<>();

        for (User user : allUsers) {
            if (user instanceof Applicant) {
                applicants.add((Applicant) user);
            } else if (user instanceof MO) {
                mos.add((MO) user);
            } else if (user instanceof Admin) {
                admins.add((Admin) user);
            }
        }

        request.setAttribute("applicants", applicants);
        request.setAttribute("mos", mos);
        request.setAttribute("admins", admins);
    }

    private void handleCreate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String role = request.getParameter("role");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            validateInput(fullName, email, username, password);

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
            }

            request.setAttribute("successMessage", "User created successfully.");
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }

        loadUsers(request);
        forwardView(request, response, "admin-users.jsp");
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("userId");

        try {
            User user = userRepository.findById(userId);
            if (user == null) {
                throw new RuntimeException("User not found.");
            }
            userRepository.deleteUserById(userId);
            request.setAttribute("successMessage", "User deleted successfully.");
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
        }

        loadUsers(request);
        forwardView(request, response, "admin-users.jsp");
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