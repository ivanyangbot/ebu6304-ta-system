package com.bupt.tarecruitment.servlet;

import com.bupt.tarecruitment.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public abstract class BaseServlet extends HttpServlet {
    protected User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (User) session.getAttribute("currentUser");
    }

    protected boolean requireLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (getCurrentUser(request) == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        return true;
    }

    protected boolean requireRole(HttpServletRequest request, HttpServletResponse response, String role)
            throws IOException, ServletException {
        if (!requireLogin(request, response)) {
            return false;
        }

        User user = getCurrentUser(request);
        if (user == null || !role.equalsIgnoreCase(user.getRole())) {
            forwardError(request, response, "You do not have permission to access this page.",
                    request.getContextPath() + "/dashboard");
            return false;
        }
        return true;
    }

    protected void forwardView(HttpServletRequest request, HttpServletResponse response, String viewPath)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/" + viewPath).forward(request, response);
    }

    protected void forwardError(HttpServletRequest request, HttpServletResponse response, String message, String backUrl)
            throws ServletException, IOException {
        request.setAttribute("errorMessage", message);
        request.setAttribute("backUrl", backUrl);
        forwardView(request, response, "error.jsp");
    }

    protected void setUtf8(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
    }
}
