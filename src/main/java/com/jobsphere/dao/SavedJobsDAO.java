package com.jobsphere.dao;

import java.sql.*;

public class SavedJobsDAO {

    // Save a job and it return t if the record is uniqre and do not existed before and the vice versa for returning false 
    //and it takes applicant id and the job id as inputs 
    public boolean saveJob(int applicantId, int jobId) {
        String sql = "INSERT INTO saved_jobs (applicant_id, job_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, applicantId);
            stmt.setInt(2, jobId);

            stmt.executeUpdate();
            return true;//it means saved successfully 

        } catch (SQLException e) {
            // duplicate (already saved) will throw error
            return false;
        }
    }

}
