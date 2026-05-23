package com.bupt.tarecruitment.servlet;

import com.bupt.tarecruitment.service.DeepSeekAiService;
import com.bupt.tarecruitment.service.RecruitmentDataSnapshotService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handles administrator AI query submissions.
 *
 * <p>URL mapping: {@code /admin/ai-query/ask}. The servlet reads the current
 * recruitment-data snapshot, sends the administrator's question to
 * {@code deepseek-v4-flash}, and forwards the answer back to
 * {@code admin-ai-query.jsp}.</p>
 *
 * @author  Group 71
 * @version 1.0
 * @see     AdminAiQueryServlet
 * @see     DeepSeekAiService
 */
public class AdminAiQueryStreamServlet extends BaseServlet {

    /**
     * Processes an AI query submission and displays the model answer.
     *
     * @param request  HTTP POST request containing {@code question} and {@code _csrf}
     * @param response HTTP response used for redirects or JSP forwarding
     * @throws ServletException if forwarding to the JSP view fails
     * @throws IOException      if redirecting, forwarding, or API communication fails
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setUtf8(request, response);
        if (!requireRole(request, response, "ADMIN")) {
            return;
        }
        if (!validateCsrfToken(request)) {
            forwardError(request, response, "Invalid CSRF token.", request.getContextPath() + "/admin/ai-query");
            return;
        }

        generateCsrfToken(request);

        String question = request.getParameter("question");
        RecruitmentDataSnapshotService snapshotService = new RecruitmentDataSnapshotService(getServletContext());
        String dataSnapshot = snapshotService.buildSnapshot();

        request.setAttribute("question", question);
        request.setAttribute("dataSnapshot", dataSnapshot);

        if (question == null || question.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Please enter a question.");
            forwardView(request, response, "admin-ai-query.jsp");
            return;
        }

        try {
            DeepSeekAiService aiService = new DeepSeekAiService();
            String answer = aiService.ask(question, dataSnapshot);
            request.setAttribute("answer", answer);
        } catch (IllegalArgumentException | IOException ex) {
            request.setAttribute("errorMessage", "AI query failed: " + ex.getMessage());
        }

        forwardView(request, response, "admin-ai-query.jsp");
    }
}
