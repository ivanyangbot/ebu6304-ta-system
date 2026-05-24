package com.bupt.tarecruitment.servlet;

import com.bupt.tarecruitment.model.User;
import com.bupt.tarecruitment.util.PathUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.file.Path;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Shared Mockito helpers for servlet integration-style tests.
 *
 * <p>The helpers initialise real servlet instances with mocked servlet APIs while
 * routing repository access to temporary JSON data directories.</p>
 */
final class ServletTestSupport {
    private ServletTestSupport() {
    }

    /**
     * Creates a servlet context whose data directory points at a temporary path.
     *
     * @param dataDir temporary data directory
     * @return mocked servlet context
     */
    static ServletContext servletContext(Path dataDir) {
        ServletContext context = mock(ServletContext.class);
        when(context.getAttribute(PathUtil.DATA_DIR_ATTRIBUTE)).thenReturn(dataDir.toString());
        return context;
    }

    /**
     * Initialises a servlet with a mocked config and supplied servlet context.
     *
     * @param servlet servlet under test
     * @param context mocked servlet context
     * @throws ServletException if servlet initialisation fails
     */
    static void init(HttpServlet servlet, ServletContext context) throws ServletException {
        ServletConfig config = mock(ServletConfig.class);
        when(config.getServletContext()).thenReturn(context);
        servlet.init(config);
    }

    /**
     * Creates a mocked session containing a logged-in current user.
     *
     * @param user user stored under {@code currentUser}
     * @return mocked HTTP session
     */
    static HttpSession sessionWithUser(User user) {
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("currentUser")).thenReturn(user);
        return session;
    }

    /**
     * Stubs forwarding to a JSP under {@code /WEB-INF/views/}.
     *
     * @param request mocked HTTP request
     * @param viewPath JSP file name relative to {@code /WEB-INF/views/}
     * @return mocked request dispatcher for verification
     */
    static RequestDispatcher stubForward(HttpServletRequest request, String viewPath) {
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/WEB-INF/views/" + viewPath)).thenReturn(dispatcher);
        return dispatcher;
    }
}
