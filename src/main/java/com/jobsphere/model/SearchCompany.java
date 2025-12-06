package com.jobsphere.model;

public class SearchCompany {
    private String jobTitle; //from jobs table
    private String status; //from applications table
    private String name; //from users table
    private String skills;
    private int expYears;
    private String email; //from users table
    private String resumeUrl;

    // Constructor
    public SearchCompany(String name, String jobTitle, String skills,int expYears, String status, String email, String resumeUrl) {
        this.jobTitle = jobTitle;
        this.status = status;
        this.name = name;
        this.skills = skills;
        this.expYears = expYears;
        this.email= email;
        this.resumeUrl=resumeUrl;
    }

    // Getters
    public String getJobTitle() { return jobTitle; }
    public String getStatus() { return status; }
    public String getName() { return name; }
    public String getSkills() { return skills; }
    public int getExpYears() {return expYears; }
    public String getEmail() { return email; }
    public String getResumeUrl() { return resumeUrl; }
}



