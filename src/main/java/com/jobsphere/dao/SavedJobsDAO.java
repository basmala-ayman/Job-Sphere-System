package com.jobsphere.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.jobsphere.model.Job;

public class SavedJobsDAO {

  private static SavedJobsDAO instance;

  private SavedJobsDAO() {}

  public static SavedJobsDAO getInstance() {
      if (instance == null) {
          instance = new SavedJobsDAO();
      }
      return instance;
  }

  public boolean saveJob(int applicantId, int jobId) {
      String sql = "INSERT INTO saved_jobs (applicant_id, job_id) VALUES (?, ?)";

      try (Connection conn = DBConnection.getConnection();
           PreparedStatement stmt = conn.prepareStatement(sql)) {

          stmt.setInt(1, applicantId);
          stmt.setInt(2, jobId);

          stmt.executeUpdate();
          return true;

      } catch (SQLException e) {
          return false;
      }
  }

//  public List<Job> getSavedJobsByApplicant(int applicantId) {
//
//    List<Job> jobs = new ArrayList<>();
//
//    String sql = """
//        SELECT j.*
//        FROM jobs j
//        JOIN saved_jobs s ON j.id = s.job_id
//        WHERE s.applicant_id = ?
//        ORDER BY s.saved_at DESC
//    """;
//
//    try (Connection conn = DBConnection.getConnection();
//         PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//        stmt.setInt(1, applicantId);
//        ResultSet rs = stmt.executeQuery();
//
//        while (rs.next()) {
//            jobs.add(JobDAO.getInstance().mapRowToJob(rs));
//        }
//
//    } catch (SQLException e) {
//        e.printStackTrace();
//    }
//
//    return jobs;
//}

}
