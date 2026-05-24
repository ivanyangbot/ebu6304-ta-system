package com.bupt.tarecruitment.servlet;

import com.bupt.tarecruitment.JsonTestData;
import com.bupt.tarecruitment.TestFixtures;
import com.bupt.tarecruitment.model.User;
import com.bupt.tarecruitment.repository.UserRepository;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Integration-style tests for {@link RegisterServlet} using temporary JSON data.
 *
 * <p>The tests cover register-page rendering, validation failure handling,
 * applicant persistence, session creation, and post-registration redirection.</p>
 */
class RegisterServletTest {
    @TempDir
    Path dataDir;

    private RegisterServlet servlet;
    private ServletContext context;
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
        context = ServletTestSupport.servletContext(dataDir);
        servlet = new RegisterServlet();
        ServletTestSupport.init(servlet, context);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        when(request.getContextPath()).thenReturn("");
    }

    /**
     * Verifies the registration page reuses the login JSP in register mode.
     *
     * @throws Exception if the servlet request fails
     */
    @Test
    void doGet_anonymousUser_setsRegisterModeAndForwardsLogin() throws Exception {
        RequestDispatcher dispatcher = ServletTestSupport.stubForward(request, "login.jsp");

        servlet.doGet(request, response);

        verify(request).setAttribute("authMode", "register");
        verify(dispatcher).forward(request, response);
    }

    /**
     * Verifies invalid registration input shows an error and preserves form values.
     *
     * @throws Exception if the servlet request fails
     */
    @Test
    void doPost_invalidEmail_setsErrorAndPreservesFormValues() throws Exception {
        RequestDispatcher dispatcher = ServletTestSupport.stubForward(request, "login.jsp");
        stubRegistrationForm("Alice Wang", "invalid-email", "alice", "password123", "password123");

        servlet.doPost(request, response);

        verify(request).setAttribute("authMode", "register");
        verify(request).setAttribute("errorMessage", "Please enter a valid email address.");
        verify(request).setAttribute("registerFullName", "Alice Wang");
        verify(request).setAttribute("registerEmail", "invalid-email");
        verify(request).setAttribute("registerUsername", "alice");
        verify(dispatcher).forward(request, response);
    }

    /**
     * Verifies valid applicant registration persists the user and logs them in.
     *
     * @throws Exception if the servlet request fails
     */
    @Test
    void doPost_validApplicant_persistsUserCreatesSessionAndRedirectsProfile() throws Exception {
        HttpSession newSession = mock(HttpSession.class);
        stubRegistrationForm("Alice Wang", "alice@example.com", "alice", "password123", "password123");
        when(request.getSession(false)).thenReturn(null);
        when(request.getSession(true)).thenReturn(newSession);

        servlet.doPost(request, response);

        User persisted = new UserRepository(context).findByUsername("alice");
        assertNotNull(persisted);
        assertEquals("APPLICANT", persisted.getRole());
        verify(newSession).setAttribute(eq("currentUser"), argThat(user ->
                user instanceof User && persisted.getId().equals(((User) user).getId())));
        verify(response).sendRedirect("/applicant/profile?msg=registered");
    }

    private void stubRegistrationForm(String fullName, String email, String username,
                                      String password, String confirmPassword) {
        when(request.getParameter("fullName")).thenReturn(fullName);
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("username")).thenReturn(username);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("confirmPassword")).thenReturn(confirmPassword);
    }
}
