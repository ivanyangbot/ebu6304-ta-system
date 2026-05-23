package com.bupt.tarecruitment.model;

public class LoadProjection {
    private final Job job;
    private final Applicant applicant;
    private final int projectedHours;
    private final String workloadBand;
    private final String summary;

    public LoadProjection(Job job, Applicant applicant, int projectedHours, String workloadBand, String summary) {
        this.job = job;
        this.applicant = applicant;
        this.projectedHours = projectedHours;
        this.workloadBand = workloadBand == null ? "Normal" : workloadBand;
        this.summary = summary == null ? "" : summary;
    }

    public Job getJob() {
        return job;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public int getProjectedHours() {
        return projectedHours;
    }

    public String getWorkloadBand() {
        return workloadBand;
    }

    public String getSummary() {
        return summary;
    }
}
