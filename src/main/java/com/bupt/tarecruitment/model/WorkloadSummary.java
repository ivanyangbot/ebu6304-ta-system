package com.bupt.tarecruitment.model;

public class WorkloadSummary {
    private Applicant applicant;
    private int acceptedJobsCount;
    private int totalHours;
    private String workloadStatus;

    public WorkloadSummary() {
    }

    public WorkloadSummary(Applicant applicant, int acceptedJobsCount, int totalHours, String workloadStatus) {
        this.applicant = applicant;
        this.acceptedJobsCount = acceptedJobsCount;
        this.totalHours = totalHours;
        this.workloadStatus = workloadStatus;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    public int getAcceptedJobsCount() {
        return acceptedJobsCount;
    }

    public void setAcceptedJobsCount(int acceptedJobsCount) {
        this.acceptedJobsCount = acceptedJobsCount;
    }

    public int getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(int totalHours) {
        this.totalHours = totalHours;
    }

    public String getWorkloadStatus() {
        return workloadStatus;
    }

    public void setWorkloadStatus(String workloadStatus) {
        this.workloadStatus = workloadStatus;
    }
}
