package com.jobsphere.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ApplicationFullInfo {

    private int applicationId;
    private StringProperty status;
    private StringProperty resumeUrl;
    private StringProperty appliedAt;

    private int userId;
    private StringProperty userName;
    private StringProperty email;

    private StringProperty phone;
    private StringProperty skills;
    private int experience;

    private int jobId;
    private StringProperty jobTitle;

    public ApplicationFullInfo(int applicationId, String status, String resumeUrl, String appliedAt,
                               int userId, String userName, String email,
                               String phone, String skills, int experience,
                               int jobId, String jobTitle) {

        this.applicationId = applicationId;
        this.status = new SimpleStringProperty(status);
        this.resumeUrl = new SimpleStringProperty(resumeUrl);
        this.appliedAt = new SimpleStringProperty(appliedAt);

        this.userId = userId;
        this.userName = new SimpleStringProperty(userName);
        this.email = new SimpleStringProperty(email);

        this.phone = new SimpleStringProperty(phone);
        this.skills = new SimpleStringProperty(skills);
        this.experience = experience;

        this.jobId = jobId;
        this.jobTitle = new SimpleStringProperty(jobTitle);
    }

    // JavaFX properties
    public StringProperty statusProperty() { return status; }
    public StringProperty resumeUrlProperty() { return resumeUrl; }
    public StringProperty appliedAtProperty() { return appliedAt; }
    public StringProperty userNameProperty() { return userName; }
    public StringProperty emailProperty() { return email; }
    public StringProperty phoneProperty() { return phone; }
    public StringProperty skillsProperty() { return skills; }
    public StringProperty jobTitleProperty() { return jobTitle; }

    // Getters and setters for non-JavaFX fields or for convenience
    public int getApplicationId() { return applicationId; }
    public String getStatus() { return status.get(); }
    public void setStatus(String newStatus) { status.set(newStatus); }
    public int getUserId() { return userId; }
    public int getJobId() { return jobId; }
    public int getExperience() { return experience; }
}
