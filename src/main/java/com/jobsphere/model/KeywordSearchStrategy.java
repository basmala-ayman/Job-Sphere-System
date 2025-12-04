package com.jobsphere.model;

import com.jobsphere.dao.JobDAO;

import java.util.List;

public class KeywordSearchStrategy implements JobSearchStrategy {
    private String keyword;

    public KeywordSearchStrategy(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public List<Job> search(JobDAO dao) {
        return dao.searchJobs(keyword);
    }
}
