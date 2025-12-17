package com.jobsphere.model;

public class Applicant extends User {
    private String phone;         // phone number
    private String resumeUrl;     // URL to resume
    private String skills;        // store it like that JSON string like '["Java","SQL"]'
    private int experienceYears;  // integer years of experience

    // Constructor
    public Applicant() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getResumeUrl() {
        return resumeUrl;
    }

    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }
}
