package com.jobsphere.model;

public class SearchCompany {
    private String name;
    private String jobTitle;
    private String skills;
    private int experienceYears;
    private String status;
    private String email;
    private String resumeUrl;

    public SearchCompany(String name, String jobTitle, String skills, int experienceYears, String status, String email, String resumeUrl) {
        this.name = name;
        this.jobTitle = jobTitle;
        this.skills = skills;
        this.experienceYears = experienceYears;
        this.status = status;
        this.email = email;
        this.resumeUrl = resumeUrl;
    }

    // Getters
    public String getName() { return name; }
    public String getJobTitle() { return jobTitle; }
    public String getSkills() { return skills; }
    public int getExperienceYears() { return experienceYears; }
    public String getStatus() { return status; }
    public String getEmail() { return email; }
    public String getResumeUrl() { return resumeUrl; }
}