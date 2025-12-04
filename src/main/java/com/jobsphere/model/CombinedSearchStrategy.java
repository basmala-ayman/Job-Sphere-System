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
        List<Job> filtered = dao.filterJobs(country, jobType);
        return filtered.stream()
                .filter(j -> j.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                .toList();
    }
}
