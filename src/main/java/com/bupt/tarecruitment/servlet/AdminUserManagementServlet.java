package com.bupt.tarecruitment.servlet;

import com.bupt.tarecruitment.model.Admin;
import com.bupt.tarecruitment.model.Applicant;
import com.bupt.tarecruitment.model.MO;
import com.bupt.tarecruitment.model.User;
import com.bupt.tarecruitment.repository.UserRepository;

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
        String searchName = request.getParameter("searchName");
        loadUsers(request, searchName);
        request.setAttribute("searchName", searchName);
        forwardView(request, response, "admin-users.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUtf8(request, response);
        if (!requireRole(request, response, "ADMIN")) {
            return;
        }

        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            if (!validateCsrfToken(request)) {
                request.setAttribute("errorMessage", "Invalid CSRF token.");
                loadUsers(request, null);
                forwardView(request, response, "admin-users.jsp");
                return;
            }
            handleDelete(request, response);
            return;
        } else if ("resetPassword".equals(action)) {
            if (!validateCsrfToken(request)) {
                request.setAttribute("errorMessage", "Invalid CSRF token.");
                loadUsers(request, null);
                forwardView(request, response, "admin-users.jsp");
                return;
            }
            handleResetPassword(request, response);
            return;
        }

        loadUsers(request, null);
        forwardView(request, response, "admin-users.jsp");
    }

    private void loadUsers(HttpServletRequest request, String searchName) {
        List<User> allUsers = userRepository.searchByFullName(searchName);
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

        loadUsers(request, null);
        forwardView(request, response, "admin-users.jsp");
    }

    private void handleResetPassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("userId");
        String newPassword = request.getParameter("newPassword");

        try {
            if (userId == null || userId.trim().isEmpty()) {
                throw new IllegalArgumentException("User ID cannot be empty.");
            }
            if (newPassword == null || newPassword.trim().isEmpty()) {
                throw new IllegalArgumentException("New password cannot be empty.");
            }
            if (newPassword.length() < 6) {
                throw new IllegalArgumentException("Password must be at least 6 characters.");
            }

            User user = userRepository.findById(userId);
            if (user == null) {
                throw new RuntimeException("User not found.");
            }

            userRepository.updatePassword(userId, newPassword.trim());
            request.setAttribute("successMessage", "Password reset successfully for user: " + user.getUsername());
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
        }

        loadUsers(request, null);
        forwardView(request, response, "admin-users.jsp");
    }
}