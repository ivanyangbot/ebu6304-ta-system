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
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Integration-style tests for {@link AdminAiQueryServlet} page access.
 *
 * <p>The tests verify authentication, administrator authorization, CSRF token
 * generation, recruitment data snapshot creation, and JSP forwarding.</p>
 */
class AdminAiQueryServletTest {
    @TempDir
    Path dataDir;

    private AdminAiQueryServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;

    /**
     * Seeds recruitment data and initialises the admin AI query servlet.
     *
     * @throws Exception if servlet initialisation fails
     */
    @BeforeEach
    void setUp() throws Exception {
        JsonTestData.writeEmptyCoreFiles(dataDir);
        JsonTestData.writeUsers(dataDir, List.of(TestFixtures.admin("admin-1", "admin")));
        JsonTestData.writeJobs(dataDir, List.of(TestFixtures.job("job-1", "mo-1", "Lab TA", "Open")));
        JsonTestData.writeApplications(dataDir, List.of(TestFixtures.application("application-1", "job-1", "applicant-1", "Pending")));
        ServletContext context = ServletTestSupport.servletContext(dataDir);
        servlet = new AdminAiQueryServlet();
        ServletTestSupport.init(servlet, context);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        when(request.getContextPath()).thenReturn("");
    }

    /**
     * Verifies anonymous users are redirected to the login page.
     *
     * @throws Exception if the servlet request fails
     */
    @Test
    void doGet_anonymousUser_redirectsLogin() throws Exception {
        servlet.doGet(request, response);

        verify(response).sendRedirect("/login");
    }

    /**
     * Verifies non-admin users receive the shared permission error page.
     *
     * @throws Exception if the servlet request fails
     */
    @Test
    void doGet_nonAdmin_forwardsPermissionError() throws Exception {
        RequestDispatcher dispatcher = ServletTestSupport.stubForward(request, "error.jsp");
        HttpSession session = ServletTestSupport.sessionWithUser(TestFixtures.applicant("applicant-1", "alice"));
        when(request.getSession(false)).thenReturn(session);

        servlet.doGet(request, response);

        verify(request).setAttribute("errorMessage", "You do not have permission to access this page.");
        verify(request).setAttribute("backUrl", "/dashboard");
        verify(dispatcher).forward(request, response);
    }

    /**
     * Verifies admin users receive a CSRF token and data snapshot before rendering.
     *
     * @throws Exception if the servlet request fails
     */
    @Test
    void doGet_adminGeneratesCsrfSnapshotAndForwardsPage() throws Exception {
        RequestDispatcher dispatcher = ServletTestSupport.stubForward(request, "admin-ai-query.jsp");
        HttpSession session = mock(HttpSession.class);
        when(request.getSession(false)).thenReturn(session);
        when(request.getSession(true)).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(TestFixtures.admin("admin-1", "admin"));

        servlet.doGet(request, response);

        verify(session).setAttribute(anyString(), anyString());
        verify(request).setAttribute(eq("dataSnapshot"), contains("TA Recruitment System Data Snapshot"));
        verify(dispatcher).forward(request, response);
    }
}
