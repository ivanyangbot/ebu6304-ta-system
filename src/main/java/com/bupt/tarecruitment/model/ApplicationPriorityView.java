package com.bupt.tarecruitment.model;

public class ApplicationPriorityView {
    private final String applicantId;
    private final String jobId;
    private final double priorityScore;
    private final String decisionBand;

    public ApplicationPriorityView(String applicantId, String jobId, double priorityScore, String decisionBand) {
        this.applicantId = applicantId;
        this.jobId = jobId;
        this.priorityScore = priorityScore;
        this.decisionBand = decisionBand == null ? "Review" : decisionBand;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public String getJobId() {
        return jobId;
    }

    public double getPriorityScore() {
        return priorityScore;
    }

    public String getDecisionBand() {
        return decisionBand;
    }
}
