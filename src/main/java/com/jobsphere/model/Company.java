package com.jobsphere.model;

public class Company extends User {
    private int userId;        // corresponds to user_id in DB
    private String website;    // company's website
    private String description; // company description
    private String industry;   // company industry

    // Constructor
    public Company() {
    }

    // Getters and Setters
//    public int getUserId() {
//        return userId;
//    }

//    public void setUserId(int userId) {
//        this.userId = userId;
//    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }
}
