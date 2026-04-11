package com.bupt.tarecruitment.servlet;

import com.bupt.tarecruitment.model.Applicant;
import com.bupt.tarecruitment.service.AuthService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RegisterServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUtf8(request, response);
        if (getCurrentUser(request) != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        request.setAttribute("authMode", "register");
        forwardView(request, response, "login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUtf8(request, response);
        if (getCurrentUser(request) != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        AuthService authService = new AuthService(getServletContext());
        try {
            Applicant applicant = authService.registerApplicant(
                    fullName, email, username, password, confirmPassword
            );

            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }

            HttpSession session = request.getSession(true);
            session.setAttribute("currentUser", applicant);
            response.sendRedirect(request.getContextPath() + "/applicant/profile?msg=registered");
        } catch (IllegalArgumentException ex) {
            request.setAttribute("authMode", "register");
            request.setAttribute("errorMessage", ex.getMessage());
            request.setAttribute("registerFullName", fullName);
            request.setAttribute("registerEmail", email);
            request.setAttribute("registerUsername", username);
            forwardView(request, response, "login.jsp");
        }
    }
}
