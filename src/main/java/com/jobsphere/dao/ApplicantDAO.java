package com.jobsphere.dao;

import com.jobsphere.model.Applicant;

import java.sql.*;

public class ApplicantDAO {
    //the applicant its skills is array of strings

    // Get applicant profile by its user ID
    public Applicant getProfile(int userId) {
        String sql = "SELECT * FROM applicant_profiles WHERE user_id = ?";
        Applicant applicant = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                applicant = new Applicant();
                applicant.setUserId(rs.getInt("user_id"));
                applicant.setPhone(rs.getString("phone"));
                applicant.setResumeUrl(rs.getString("resume_url"));
                applicant.setSkills(rs.getString("skills")); // JSON stored as string
                applicant.setExperienceYears(rs.getInt("experience_years"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return applicant;
    }

    // Update applicant profile
    //this update function will update all the applicant information with the passed comming values
    //and it returned true or false indecate that update happened successfully or no
    public boolean updateProfile(Applicant applicant) {
        String sql = "UPDATE applicant_profiles SET phone = ?, resume_url = ?, skills = ?, experience_years = ? WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, applicant.getPhone());
            stmt.setString(2, applicant.getResumeUrl());
            stmt.setString(3, applicant.getSkills());
            stmt.setInt(4, applicant.getExperienceYears());
            stmt.setInt(5, applicant.getUserId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Insert a new applicant profile
    public void insertApplicantProfile(Applicant applicant) {
        String sql = "INSERT INTO applicant_profiles (user_id, phone, resume_url, skills, experience_years) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, applicant.getId());
            stmt.setString(2, applicant.getPhone());
            stmt.setString(3, applicant.getResumeUrl());
            stmt.setString(4, applicant.getSkills()); // skills as JSON string
            stmt.setInt(5, applicant.getExperienceYears());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
