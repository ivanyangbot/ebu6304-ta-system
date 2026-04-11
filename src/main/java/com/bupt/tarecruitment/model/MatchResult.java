package com.bupt.tarecruitment.model;

import java.util.ArrayList;
import java.util.List;

public class MatchResult {
    private List<String> matchedSkills;
    private List<String> missingSkills;
    private double score;

    public MatchResult() {
        this.matchedSkills = new ArrayList<>();
        this.missingSkills = new ArrayList<>();
    }

    public MatchResult(List<String> matchedSkills, List<String> missingSkills, double score) {
        this.matchedSkills = matchedSkills == null ? new ArrayList<>() : matchedSkills;
        this.missingSkills = missingSkills == null ? new ArrayList<>() : missingSkills;
        this.score = score;
    }

    public List<String> getMatchedSkills() {
        if (matchedSkills == null) {
            matchedSkills = new ArrayList<>();
        }
        return matchedSkills;
    }

    public void setMatchedSkills(List<String> matchedSkills) {
        this.matchedSkills = matchedSkills == null ? new ArrayList<>() : matchedSkills;
    }

    public List<String> getMissingSkills() {
        if (missingSkills == null) {
            missingSkills = new ArrayList<>();
        }
        return missingSkills;
    }

    public void setMissingSkills(List<String> missingSkills) {
        this.missingSkills = missingSkills == null ? new ArrayList<>() : missingSkills;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
