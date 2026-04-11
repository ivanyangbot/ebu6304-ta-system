package com.bupt.tarecruitment.model;

public class ApplicationDisplay {
    private ApplicationRecord application;
    private Job job;
    private Applicant applicant;
    private MatchResult matchResult;

    public ApplicationDisplay() {
    }

    public ApplicationDisplay(ApplicationRecord application, Job job, Applicant applicant, MatchResult matchResult) {
        this.application = application;
        this.job = job;
        this.applicant = applicant;
        this.matchResult = matchResult;
    }

    public ApplicationRecord getApplication() {
        return application;
    }

    public void setApplication(ApplicationRecord application) {
        this.application = application;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    public MatchResult getMatchResult() {
        return matchResult;
    }

    public void setMatchResult(MatchResult matchResult) {
        this.matchResult = matchResult;
    }
}
