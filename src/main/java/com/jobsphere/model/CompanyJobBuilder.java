package com.jobsphere.model;

public class CompanyJobBuilder extends JobBuilder {
    private String title;
    private String description;
    private String requirements;
    private String careerLevel;
    private String jobType;
    private String workplace;
    private String country;
    private String city;
    private String jobCategory;
    private String responsibilities;
    private String salary;

    public CompanyJobBuilder setMainInfo(String title, String description, String requirements) {
        this.title = title;
        this.description = description;
        this.requirements = requirements;
        return this;
    }
    public CompanyJobBuilder setJobDetails(String careerLevel, String jobType, String workplace) {
        this.careerLevel = careerLevel;
        this.jobType = jobType;
        this.workplace = workplace;
        return this;
    }
    public CompanyJobBuilder setLocation(String country, String city) {
        this.country = country;
        this.city = city;
        return this;
    }

    public CompanyJobBuilder setCategory(String jobCategory) {
        this.jobCategory = jobCategory;
        return this;
    }
    public JobBuilder setResponsibilities(String responsibilities) {
        this.responsibilities = responsibilities;
        return this;
    }

    public JobBuilder setSalary(String salary) {
        this.salary = salary;
        return this;
    }
    public Job build() {
        Job job = new Job();
        job.setTitle(title);
        job.setDescription(description);
        job.setRequirements(requirements);
        job.setCareerLevel(careerLevel);
        job.setJobType(jobType);
        job.setWorkplace(workplace);
        job.setCountry(country);
        job.setCity(city);
        job.setJobCategory(jobCategory);
        job.setResponsibilities(responsibilities);
        job.setSalary(salary);

        // must be changed
        job.setCompanyId(3); //to be checked

        return job;
    }
}
