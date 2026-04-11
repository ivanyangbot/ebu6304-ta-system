package com.bupt.tarecruitment.servlet;

import com.bupt.tarecruitment.model.User;
import com.bupt.tarecruitment.service.AuthService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUtf8(request, response);
        if (getCurrentUser(request) != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        forwardView(request, response, "login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUtf8(request, response);

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        AuthService authService = new AuthService(getServletContext());
        User user = authService.login(username, password);

        if (user == null) {
            request.setAttribute("authMode", "login");
            request.setAttribute("errorMessage", "Invalid username or password.");
            request.setAttribute("username", username);
            forwardView(request, response, "login.jsp");
            return;
        }

        HttpSession oldSession = request.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }

        HttpSession session = request.getSession(true);
        session.setAttribute("currentUser", user);
        response.sendRedirect(request.getContextPath() + "/dashboard");
    }
}
