package com.bupt.tarecruitment.servlet;

import com.bupt.tarecruitment.service.RecruitmentDataSnapshotService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Displays the administrator AI data-query page.
 *
 * <p>URL mapping: {@code /admin/ai-query}. Only users with the {@code ADMIN}
 * role can access this servlet. The page lets administrators ask natural-language
 * questions about the JSON-backed recruitment data.</p>
 *
 * @author  Group 71
 * @version 1.0
 * @see     AdminAiQueryStreamServlet
 * @see     com.bupt.tarecruitment.service.DeepSeekAiService
 */
public class AdminAiQueryServlet extends BaseServlet {

    /**
     * Renders the AI query form and optional data snapshot preview.
     *
     * @param request  HTTP request from the logged-in administrator
     * @param response HTTP response used for redirects or JSP forwarding
     * @throws ServletException if forwarding to the JSP view fails
     * @throws IOException      if redirecting or forwarding fails
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setUtf8(request, response);
        if (!requireRole(request, response, "ADMIN")) {
            return;
        }

        generateCsrfToken(request);
        RecruitmentDataSnapshotService snapshotService = new RecruitmentDataSnapshotService(getServletContext());
        request.setAttribute("dataSnapshot", snapshotService.buildSnapshot());
        forwardView(request, response, "admin-ai-query.jsp");
    }
}
