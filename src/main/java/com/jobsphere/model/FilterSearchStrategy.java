package com.jobsphere.model;

import com.jobsphere.dao.JobDAO;

import java.util.List;

public class FilterSearchStrategy implements JobSearchStrategy {
    private String country;
    private String jobType;

    public FilterSearchStrategy(String country, String jobType) {
        this.country = country;
        this.jobType = jobType;
    }

    @Override
    public List<Job> search(JobDAO dao) {
        return dao.filterJobs(country, jobType);
    }
}
