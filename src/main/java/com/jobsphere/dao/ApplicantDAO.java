package com.jobsphere.dao;

import com.jobsphere.model.Applicant;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApplicantDAO {
    //the applicant its skills is array of strings
//we will use singleton here too to jsut dealing with just one object from it
    private static ApplicantDAO instance;

    private ApplicantDAO() {
    }

    public static ApplicantDAO getInstance() {
        if (instance == null) {
            instance = new ApplicantDAO();
        }
        return instance;
    }

    // Get applicant profile by its user ID
    public Applicant getProfile(int userId) throws SQLException {
        String sql = "SELECT * FROM applicants WHERE user_id = ?";
        Applicant applicant = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                applicant = new Applicant();
                applicant.setId(rs.getInt("user_id"));
                applicant.setPhone(rs.getString("phone"));
                applicant.setResumeUrl(rs.getString("resume_url"));
                applicant.setSkills(rs.getString("skills")); // JSON stored as string
                applicant.setExperienceYears(rs.getInt("experience_years"));
            }

        }
        return applicant;
    }

    // Update applicant profile
    //this update function will update all the applicant information with the passed comming values
    //and it returned true or false indecate that update happened successfully or no
    public boolean updateProfile(Applicant applicant) throws SQLException {
        String sql = "UPDATE applicants SET phone = ?, resume_url = ?, skills = ?, experience_years = ? WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, applicant.getPhone());
            stmt.setString(2, applicant.getResumeUrl());
            stmt.setString(3, applicant.getSkills());
            stmt.setInt(4, applicant.getExperienceYears());
            stmt.setInt(5, applicant.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        }
    }

    // Insert a new applicant profile
    public void insertApplicantProfile(Connection conn, Applicant applicant) throws SQLException {
        String sql = "INSERT INTO applicants (user_id, phone, resume_url, skills, experience_years) " +
                "VALUES (?, ?, ?, ?::json, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, applicant.getId());
            stmt.setString(2, applicant.getPhone());
            stmt.setString(3, applicant.getResumeUrl());
            stmt.setString(4, applicant.getSkills()); // skills as JSON string
            stmt.setInt(5, applicant.getExperienceYears());

            stmt.executeUpdate();

        }
    }


    public List<Integer> getDistinctExperienceYears() {
    List<Integer> years = new ArrayList<>();
    String sql = "SELECT DISTINCT experience_years FROM applicants ORDER BY experience_years";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            years.add(rs.getInt("experience_years"));
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return years;
}


public List<String> getDistinctSkills() {
  List<String> skills = new ArrayList<>();
  //cause skills stored in json column to ca nsave multiple items in the same single column we need to use this function to flatten it 
  String sql = "SELECT DISTINCT jsonb_array_elements_text(skills) AS skill FROM applicants ORDER BY skill";

  try (Connection conn = DBConnection.getConnection();
       PreparedStatement stmt = conn.prepareStatement(sql);
       ResultSet rs = stmt.executeQuery()) {

      while (rs.next()) {
          skills.add(rs.getString("skill"));
      }

  } catch (SQLException e) {
      e.printStackTrace();
  }

  return skills;
}

}
