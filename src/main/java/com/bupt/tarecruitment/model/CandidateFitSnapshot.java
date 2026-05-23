package com.bupt.tarecruitment.model;

import java.util.ArrayList;
import java.util.List;

public class CandidateFitSnapshot {
    private final Applicant applicant;
    private final double fitScore;
    private final List<String> alignedSkills;
    private final List<String> gapSkills;
    private final String nextStep;

    public CandidateFitSnapshot(Applicant applicant, double fitScore, List<String> alignedSkills,
                                List<String> gapSkills, String nextStep) {
        this.applicant = applicant;
        this.fitScore = fitScore;
        this.alignedSkills = alignedSkills == null ? new ArrayList<>() : new ArrayList<>(alignedSkills);
        this.gapSkills = gapSkills == null ? new ArrayList<>() : new ArrayList<>(gapSkills);
        this.nextStep = nextStep == null ? "Review" : nextStep;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public double getFitScore() {
        return fitScore;
    }

    public List<String> getAlignedSkills() {
        return new ArrayList<>(alignedSkills);
    }

    public List<String> getGapSkills() {
        return new ArrayList<>(gapSkills);
    }

    public String getNextStep() {
        return nextStep;
    }
}
