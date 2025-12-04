package com.jobsphere.model;

import com.jobsphere.dao.JobDAO;

import java.util.List;

public class CombinedSearchStrategy implements JobSearchStrategy {
    private String keyword;
    private String country;
    private String jobType;

    public CombinedSearchStrategy(String keyword, String country, String jobType) {
        this.keyword = keyword;
        this.country = country;
        this.jobType = jobType;
    }

    @Override
    public List<Job> search(JobDAO dao) {
        List<Job> filteredJobs = dao.filterJobs(country, jobType);
        return filteredJobs.stream()
                .filter(job -> job.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                .toList();
    }
}
