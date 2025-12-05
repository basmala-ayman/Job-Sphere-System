package com.jobsphere.model;
import com.jobsphere.dao.JobDAO;

import java.util.List;
public interface JobSearchStrategy {
    List<Job> search(JobDAO dao);
}
