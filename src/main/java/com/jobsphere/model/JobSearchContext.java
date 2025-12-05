package com.jobsphere.model;
import com.jobsphere.dao.JobDAO;

import java.util.List;

public class JobSearchContext {
    private JobSearchStrategy strategy;

    public void setStrategy(JobSearchStrategy strategy) {
        this.strategy = strategy;
    }

    public List<Job> execute(JobDAO dao) {
        return strategy.search(dao);
    }
}
