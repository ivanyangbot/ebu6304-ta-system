package com.bupt.tarecruitment.listener;

import com.bupt.tarecruitment.util.PathUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class DataBootstrapListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        PathUtil.initializeDataDirectory(sce.getServletContext());
    }
}
