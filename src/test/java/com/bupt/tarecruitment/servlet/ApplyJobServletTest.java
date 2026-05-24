package com.bupt.tarecruitment.servlet;

import com.bupt.tarecruitment.JsonTestData;
import com.bupt.tarecruitment.TestFixtures;
import com.bupt.tarecruitment.model.Applicant;
import com.bupt.tarecruitment.model.Job;
import com.bupt.tarecruitment.repository.ActivityLogRepository;
import com.bupt.tarecruitment.repository.ApplicationRepository;
import com.bupt.tarecruitment.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Integration-style tests for {@link ApplyJobServlet} recruitment workflow behaviour.
 *
 * <p>The tests cover access control and the applicant apply flow, including
 * application, notification, and activity-log persistence.</p>
 */
class ApplyJobServletTest {
    @TempDir
    Path dataDir;

    private ApplyJobServlet servlet;
    private ServletContext context;
    private HttpServletRequest request;
    private HttpServletResponse response;

    /**
     * Seeds a job, applicant, and module organiser in isolated JSON data files.
     *
     * @throws Exception if servlet initialisation fails
     */
    @BeforeEach
    void setUp() throws Exception {
        Applicant applicant = TestFixtures.applicant("applicant-1", "alice");
        Job job = TestFixtures.job("job-1", "mo-1", "Lab TA", "Open");
        JsonTestData.writeEmptyCoreFiles(dataDir);
        JsonTestData.writeUsers(dataDir, List.of(applicant, TestFixtures.mo("mo-1", "bob")));
        JsonTestData.writeJobs(dataDir, List.of(job));
        context = ServletTestSupport.servletContext(dataDir);
        servlet = new ApplyJobServlet();
        ServletTestSupport.init(servlet, context);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        when(request.getContextPath()).thenReturn("");
        when(request.getParameter("id")).thenReturn("job-1");
    }

    /**
     * Verifies anonymous applicants are redirected to login before applying.
     *
     * @throws Exception if the servlet request fails
     */
    @Test
    void doPost_anonymousUser_redirectsLogin() throws Exception {
        servlet.doPost(request, response);

        verify(response).sendRedirect("/login");
    }

    /**
     * Verifies non-applicant users cannot submit job applications.
     *
     * @throws Exception if the servlet request fails
     */
    @Test
    void doPost_nonApplicant_forwardsPermissionError() throws Exception {
        RequestDispatcher dispatcher = ServletTestSupport.stubForward(request, "error.jsp");
        javax.servlet.http.HttpSession session = ServletTestSupport.sessionWithUser(TestFixtures.mo("mo-1", "bob"));
        when(request.getSession(false)).thenReturn(session);

        servlet.doPost(request, response);

        verify(request).setAttribute("errorMessage", "You do not have permission to access this page.");
        verify(dispatcher).forward(request, response);
    }

    /**
     * Verifies a valid application creates all workflow artifacts and redirects.
     *
     * @throws Exception if the servlet request fails
     */
    @Test
    void doPost_applicantApplyingToOpenJobPersistsWorkflowArtifacts() throws Exception {
        javax.servlet.http.HttpSession session = ServletTestSupport.sessionWithUser(TestFixtures.applicant("applicant-1", "alice"));
        when(request.getSession(false)).thenReturn(session);

        servlet.doPost(request, response);

        assertEquals(1, new ApplicationRepository(context).findAll().size());
        assertEquals("applicant-1", new ApplicationRepository(context).findAll().get(0).getApplicantId());
        assertEquals(1, new NotificationRepository(context).findByUserId("mo-1").size());
        assertEquals(1, new ActivityLogRepository(context).findByUserId("applicant-1").size());
        verify(response).sendRedirect("/jobs/detail?id=job-1&msg=applied");
    }
}
