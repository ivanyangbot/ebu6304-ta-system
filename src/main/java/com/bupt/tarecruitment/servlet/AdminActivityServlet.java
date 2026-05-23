package com.bupt.tarecruitment.servlet;

import com.bupt.tarecruitment.service.ActivityLogService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AdminActivityServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setUtf8(request, response);
        if (!requireRole(request, response, "ADMIN")) {
            return;
        }

        String userFullName = request.getParameter("userFullName");
        String actionType   = request.getParameter("actionType");
        String userRole     = request.getParameter("userRole");
        String fromDateStr  = request.getParameter("fromDate");
        String toDateStr    = request.getParameter("toDate");

        LocalDateTime fromTime = null;
        LocalDateTime toTime   = null;
        try {
            if (fromDateStr != null && !fromDateStr.isEmpty()) {
                fromTime = LocalDate.parse(fromDateStr).atStartOfDay();
            }
            if (toDateStr != null && !toDateStr.isEmpty()) {
                toTime = LocalDate.parse(toDateStr).atTime(LocalTime.MAX);
            }
        } catch (Exception e) {
            // 忽略日期解析错误，视为不筛选
        }

        ActivityLogService activityLogService = new ActivityLogService(getServletContext());
        request.setAttribute("activityLogs",
                activityLogService.getFilteredGlobal(userFullName, actionType, userRole, fromTime, toTime));

        // 将筛选参数回填，方便视图保持表单状态
        request.setAttribute("filterUserFullName", userFullName);
        request.setAttribute("filterActionType", actionType);
        request.setAttribute("filterUserRole", userRole);
        request.setAttribute("filterFromDate", fromDateStr);
        request.setAttribute("filterToDate", toDateStr);

        forwardView(request, response, "admin-activity.jsp");
    }
}
