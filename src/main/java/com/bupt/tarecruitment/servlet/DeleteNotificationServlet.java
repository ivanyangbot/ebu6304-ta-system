package com.bupt.tarecruitment.servlet;

import com.bupt.tarecruitment.model.Notification;
import com.bupt.tarecruitment.repository.NotificationRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteNotificationServlet extends BaseServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setUtf8(request, response);
        if (!requireLogin(request, response)) {
            return;
        }

        String notificationId = request.getParameter("notificationId");
        
        if (notificationId == null || notificationId.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/dashboard?msg=error");
            return;
        }

        NotificationRepository notificationRepository = new NotificationRepository(getServletContext());
        notificationRepository.deleteNotification(notificationId);

        response.sendRedirect(request.getContextPath() + "/dashboard");
    }
}