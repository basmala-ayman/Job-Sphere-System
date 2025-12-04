package com.jobsphere.model;

import java.time.LocalDateTime;

public class Job {
  private String title;
  private String description;
  private String requirements;
  private String careerLevel;
  private String jobType;
  private String workplace;
  private String country;

  private String city;
  private String jobCategory;

  // just the extra attributes
  private int id;//this will be handled by the db not from builder
  private int companyId; // it is a id reference to the company that post that job
  private String responsibilities;
  private String salary;
  private String status; // active / paused


  // Getters and setters for the new fields
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getCompanyId() {
    return companyId;
  }

  //to be changed (must be taken from db)
  public void setCompanyId(int companyId) {
    this.companyId = companyId;
  }

  public String getResponsibilities() {
    return responsibilities;
  }

  public void setResponsibilities(String responsibilities) {
    this.responsibilities = responsibilities;
  }

  public String getSalary() {
    return salary;
  }

  public void setSalary(String salary) {
    this.salary = salary;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setRequirements(String requirements) {
    this.requirements = requirements;
  }

  public void setCareerLevel(String careerLevel) {
    this.careerLevel = careerLevel;
  }

  public void setJobType(String jobType) {
    this.jobType = jobType;
  }

  public void setWorkplace(String workplace) {
    this.workplace = workplace;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public void setJobCategory(String jobCategory) {
    this.jobCategory = jobCategory;
  }

  public String getTitle() {
    return title;
}

public String getDescription() {
    return description;
}

public String getRequirements() {
    return requirements;
}

public String getCareerLevel() {
    return careerLevel;
}

public String getJobType() {
    return jobType;
}

public String getWorkplace() {
    return workplace;
}

public String getCountry() {
    return country;
}

public String getCity() {
    return city;
}

public String getJobCategory() {
    return jobCategory;
}


}
