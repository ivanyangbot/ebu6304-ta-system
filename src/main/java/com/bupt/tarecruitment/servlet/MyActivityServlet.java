package com.bupt.tarecruitment.servlet;

import com.bupt.tarecruitment.model.User;
import com.bupt.tarecruitment.service.ActivityLogService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyActivityServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setUtf8(request, response);
        if (!requireLogin(request, response)) {
            return;
        }

        User currentUser = getCurrentUser(request);
        ActivityLogService activityLogService = new ActivityLogService(getServletContext());

        String filterType = request.getParameter("actionType");
        request.setAttribute("activityLogs",
                activityLogService.getAllByUserAndType(currentUser.getId(), filterType));
        request.setAttribute("filterType", filterType);

        forwardView(request, response, "my-activity.jsp");
    }
}
