package com.bupt.tarecruitment.listener;

import com.bupt.tarecruitment.util.ConfigLoader;
import com.bupt.tarecruitment.util.PathUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Servlet context listener that initialises the application's data directory
 * on startup.
 *
 * <p>This listener is registered in {@code web.xml} and fires when the servlet
 * container starts the web application. Its sole responsibility is to delegate to
 * {@link PathUtil#initializeDataDirectory(javax.servlet.ServletContext)} so that
 * the {@code WEB-INF/data/} directory and seed JSON files are ready before any
 * servlet handles a request.</p>
 *
 * <p>Failing to initialise the data directory would cause a
 * {@link RuntimeException} on the first repository access.</p>
 *
 * @author  Group 71
 * @version 1.0
 * @see     PathUtil#initializeDataDirectory(javax.servlet.ServletContext)
 */
public class DataBootstrapListener implements ServletContextListener {

    /**
     * Called by the servlet container when the web application is starting up.
     * Triggers data directory initialisation.
     *
     * @param sce the servlet context event containing the application's {@link javax.servlet.ServletContext}
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Load local.properties first so API keys are available to all servlets
        ConfigLoader.load(sce.getServletContext());
        PathUtil.initializeDataDirectory(sce.getServletContext());
    }
}
