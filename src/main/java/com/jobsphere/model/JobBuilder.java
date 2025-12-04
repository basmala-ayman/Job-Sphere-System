package com.jobsphere.model;

public abstract class JobBuilder {
    public abstract JobBuilder setMainInfo(String title, String description, String requirements);
    public abstract JobBuilder setJobDetails(String careerLevel, String jobType, String workplace);
    public abstract JobBuilder setLocation(String country, String city);
    public abstract JobBuilder setCategory(String jobCategory);
    public abstract JobBuilder setResponsibilities(String responsibilities);
    public abstract JobBuilder setSalary(String text);
    public abstract Job build();
}
