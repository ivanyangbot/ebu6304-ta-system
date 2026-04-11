package com.bupt.tarecruitment.model;

import java.util.ArrayList;
import java.util.List;

public class Job {
    private String id;
    private String title;
    private String moduleName;
    private String description;
    private List<String> requiredSkills;
    private int hours;
    private String postedByMoId;
    private String status;

    public Job() {
        this.requiredSkills = new ArrayList<>();
        this.status = "Open";
    }

    public Job(String id, String title, String moduleName, String description, List<String> requiredSkills, int hours,
               String postedByMoId, String status) {
        this.id = id;
        this.title = title;
        this.moduleName = moduleName;
        this.description = description;
        this.requiredSkills = requiredSkills == null ? new ArrayList<>() : requiredSkills;
        this.hours = hours;
        this.postedByMoId = postedByMoId;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getRequiredSkills() {
        if (requiredSkills == null) {
            requiredSkills = new ArrayList<>();
        }
        return requiredSkills;
    }

    public void setRequiredSkills(List<String> requiredSkills) {
        this.requiredSkills = requiredSkills == null ? new ArrayList<>() : requiredSkills;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public String getPostedByMoId() {
        return postedByMoId;
    }

    public void setPostedByMoId(String postedByMoId) {
        this.postedByMoId = postedByMoId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
