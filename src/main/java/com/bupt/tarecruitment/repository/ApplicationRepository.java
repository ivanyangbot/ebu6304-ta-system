package com.bupt.tarecruitment.repository;

import com.bupt.tarecruitment.model.ApplicationRecord;
import com.bupt.tarecruitment.util.JsonFileUtil;
import com.bupt.tarecruitment.util.PathUtil;
import com.google.gson.reflect.TypeToken;

import javax.servlet.ServletContext;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ApplicationRepository {
    private final Path filePath;
    private static final Type APPLICATION_LIST_TYPE = new TypeToken<List<ApplicationRecord>>() {
    }.getType();

    public ApplicationRepository(ServletContext servletContext) {
        this.filePath = PathUtil.getDataFilePath(servletContext, "applications.json");
    }

    public List<ApplicationRecord> findAll() {
        return JsonFileUtil.readList(filePath, APPLICATION_LIST_TYPE);
    }

    public ApplicationRecord findById(String id) {
        List<ApplicationRecord> applications = findAll();
        for (ApplicationRecord application : applications) {
            if (application.getId().equals(id)) {
                return application;
            }
        }
        return null;
    }

    public List<ApplicationRecord> findByApplicantId(String applicantId) {
        List<ApplicationRecord> applications = findAll();
        List<ApplicationRecord> result = new ArrayList<>();
        for (ApplicationRecord application : applications) {
            if (applicantId.equals(application.getApplicantId())) {
                result.add(application);
            }
        }
        return result;
    }

    public List<ApplicationRecord> findByJobId(String jobId) {
        List<ApplicationRecord> applications = findAll();
        List<ApplicationRecord> result = new ArrayList<>();
        for (ApplicationRecord application : applications) {
            if (jobId.equals(application.getJobId())) {
                result.add(application);
            }
        }
        return result;
    }

    public ApplicationRecord findByJobIdAndApplicantId(String jobId, String applicantId) {
        List<ApplicationRecord> applications = findAll();
        for (ApplicationRecord application : applications) {
            if (jobId.equals(application.getJobId()) && applicantId.equals(application.getApplicantId())) {
                return application;
            }
        }
        return null;
    }

    public void save(ApplicationRecord record) {
        List<ApplicationRecord> applications = findAll();
        applications.add(record);
        JsonFileUtil.writeJson(filePath, applications);
    }

    public void updateStatus(String applicationId, String status) {
        List<ApplicationRecord> applications = findAll();
        for (ApplicationRecord application : applications) {
            if (application.getId().equals(applicationId)) {
                application.setStatus(status);
                JsonFileUtil.writeJson(filePath, applications);
                return;
            }
        }
        throw new RuntimeException("Application not found: " + applicationId);
    }
}
