package com.bupt.tarecruitment.servlet;

import com.bupt.tarecruitment.JsonTestData;
import com.bupt.tarecruitment.TestFixtures;
import com.bupt.tarecruitment.model.Applicant;
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

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Integration-style tests for {@link LoginServlet} using mocked servlet APIs.
 *
 * <p>The tests exercise login page rendering, authenticated redirects,
 * credential validation, and session replacement against temporary JSON users.</p>
 */
class LoginServletTest {
    @TempDir
    Path dataDir;

    private LoginServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;

    /**
     * Creates an isolated servlet instance and empty JSON data files for each test.
     *
     * @throws Exception if servlet initialisation fails
     */
    @BeforeEach
    void setUp() throws Exception {
        JsonTestData.writeEmptyCoreFiles(dataDir);
        ServletContext context = ServletTestSupport.servletContext(dataDir);
        servlet = new LoginServlet();
        ServletTestSupport.init(servlet, context);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        when(request.getContextPath()).thenReturn("");
    }

    /**
     * Verifies anonymous users are forwarded to the login JSP.
     *
     * @throws Exception if the servlet request fails
     */
    @Test
    void doGet_anonymousUser_forwardsLoginPage() throws Exception {
        RequestDispatcher dispatcher = ServletTestSupport.stubForward(request, "login.jsp");

        servlet.doGet(request, response);

        verify(dispatcher).forward(request, response);
    }

    /**
     * Verifies logged-in users are redirected away from the login page.
     *
     * @throws Exception if the servlet request fails
     */
    @Test
    void doGet_loggedInUser_redirectsDashboard() throws Exception {
        HttpSession session = ServletTestSupport.sessionWithUser(TestFixtures.applicant("u-1", "alice"));
        when(request.getSession(false)).thenReturn(session);

        servlet.doGet(request, response);

        verify(response).sendRedirect("/dashboard");
    }

    /**
     * Verifies invalid credentials preserve the username and show a login error.
     *
     * @throws Exception if the servlet request fails
     */
    @Test
    void doPost_invalidCredentials_setsErrorAndForwardsLogin() throws Exception {
        RequestDispatcher dispatcher = ServletTestSupport.stubForward(request, "login.jsp");
        when(request.getParameter("username")).thenReturn("missing");
        when(request.getParameter("password")).thenReturn("wrong");

        servlet.doPost(request, response);

        verify(request).setAttribute("authMode", "login");
        verify(request).setAttribute("errorMessage", "Invalid username or password.");
        verify(request).setAttribute("username", "missing");
        verify(dispatcher).forward(request, response);
    }

    /**
     * Verifies valid credentials create a fresh authenticated session.
     *
     * @throws Exception if the servlet request fails
     */
    @Test
    void doPost_validCredentials_replacesSessionAndRedirectsDashboard() throws Exception {
        Applicant applicant = TestFixtures.applicant("applicant-1", "alice");
        JsonTestData.writeUsers(dataDir, List.of(applicant));
        HttpSession oldSession = mock(HttpSession.class);
        HttpSession newSession = mock(HttpSession.class);
        when(request.getParameter("username")).thenReturn("alice");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getSession(false)).thenReturn(oldSession);
        when(request.getSession(true)).thenReturn(newSession);

        servlet.doPost(request, response);

        verify(oldSession).invalidate();
        verify(newSession).setAttribute(eq("currentUser"), argThat(user ->
                user instanceof Applicant && "applicant-1".equals(((Applicant) user).getId())));
        verify(response).sendRedirect("/dashboard");
    }
}
