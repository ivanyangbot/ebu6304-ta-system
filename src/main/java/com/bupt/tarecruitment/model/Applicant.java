package com.bupt.tarecruitment.model;

import java.util.ArrayList;
import java.util.List;

public class Applicant extends User {
    private List<String> skills;
    private String selfIntroduction;

    public Applicant() {
        this.skills = new ArrayList<>();
        this.selfIntroduction = "";
    }

    public Applicant(String id, String username, String password, String role, String fullName, String email,
                     List<String> skills) {
        this(id, username, password, role, fullName, email, skills, "");
    }

    public Applicant(String id, String username, String password, String role, String fullName, String email,
                     List<String> skills, String selfIntroduction) {
        super(id, username, password, role, fullName, email);
        this.skills = skills == null ? new ArrayList<>() : skills;
        this.selfIntroduction = selfIntroduction == null ? "" : selfIntroduction;
    }

    public List<String> getSkills() {
        if (skills == null) {
            skills = new ArrayList<>();
        }
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills == null ? new ArrayList<>() : skills;
    }

    public String getSelfIntroduction() {
        if (selfIntroduction == null) {
            selfIntroduction = "";
        }
        return selfIntroduction;
    }

    public void setSelfIntroduction(String selfIntroduction) {
        this.selfIntroduction = selfIntroduction == null ? "" : selfIntroduction;
    }
}
