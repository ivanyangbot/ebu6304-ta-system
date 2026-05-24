package com.bupt.tarecruitment.servlet;

import com.bupt.tarecruitment.JsonTestData;
import com.bupt.tarecruitment.TestFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.nio.file.Path;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Integration-style tests for {@link AdminAiQueryStreamServlet} validation paths.
 *
 * <p>The tests exercise CSRF and blank-question handling without invoking the
 * external AI service used by successful streaming requests.</p>
 */
class AdminAiQueryStreamServletTest {
    @TempDir
    Path dataDir;

    private AdminAiQueryStreamServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;

    /**
     * Creates an authenticated admin request context backed by temporary JSON data.
     *
     * @throws Exception if servlet initialisation fails
     */
    @BeforeEach
    void setUp() throws Exception {
        JsonTestData.writeEmptyCoreFiles(dataDir);
        JsonTestData.writeUsers(dataDir, List.of(TestFixtures.admin("admin-1", "admin")));
        ServletContext context = ServletTestSupport.servletContext(dataDir);
        servlet = new AdminAiQueryStreamServlet();
        ServletTestSupport.init(servlet, context);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        when(request.getContextPath()).thenReturn("");
        when(request.getSession(false)).thenReturn(session);
        when(request.getSession(true)).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(TestFixtures.admin("admin-1", "admin"));
    }

    /**
     * Verifies invalid CSRF tokens are rejected before processing the question.
     *
     * @throws Exception if the servlet request fails
     */
    @Test
    void doPost_invalidCsrf_forwardsError() throws Exception {
        RequestDispatcher dispatcher = ServletTestSupport.stubForward(request, "error.jsp");
        when(session.getAttribute("csrfToken")).thenReturn("expected-token");
        when(request.getParameter("_csrf")).thenReturn("wrong-token");

        servlet.doPost(request, response);

        verify(request).setAttribute("errorMessage", "Invalid CSRF token.");
        verify(request).setAttribute("backUrl", "/admin/ai-query");
        verify(dispatcher).forward(request, response);
    }

    /**
     * Verifies blank questions return a validation error without calling AI APIs.
     *
     * @throws Exception if the servlet request fails
     */
    @Test
    void doPost_emptyQuestion_setsValidationErrorWithoutExternalAiCall() throws Exception {
        RequestDispatcher dispatcher = ServletTestSupport.stubForward(request, "admin-ai-query.jsp");
        when(session.getAttribute("csrfToken")).thenReturn("expected-token");
        when(request.getParameter("_csrf")).thenReturn("expected-token");
        when(request.getParameter("question")).thenReturn("  ");

        servlet.doPost(request, response);

        verify(session).setAttribute(anyString(), anyString());
        verify(request).setAttribute("question", "  ");
        verify(request).setAttribute("errorMessage", "Please enter a question.");
        verify(dispatcher).forward(request, response);
    }
}
